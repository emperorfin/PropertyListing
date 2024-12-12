package emperorfin.android.propertylisting.data.datasource.local.framework.room.entitysource.test

import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.local.framework.room.dao.FakePropertyDao
import emperorfin.android.propertylisting.data.datasource.local.framework.room.dao.FakePropertyDao.Companion.NUM_OF_CURRENCY_RATES_150
import emperorfin.android.propertylisting.data.datasource.local.framework.room.dao.FakePropertyDao.Companion.TABLE_ROW_IDS_TWO
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntityMapper
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntityMapper
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntityMapper
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entitysource.PropertyLocalDataSource
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_BASE_USD
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_ALL
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_ALL
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_ALL
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_NAME_EUR
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.NonExistentCurrencyRateDataLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateListNotAvailableLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.InsertCurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.DeleteCurrencyRateLocalError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModelMapper
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModelMapper
import emperorfin.android.propertylisting.domain.model.property.PropertyModelMapper
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.None as None_CurrencyRate
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate


internal class PropertyLocalDataSourceTest {

    private companion object {

        const val NUM_OF_CURRENCY_RATES_MINUS_1: Int = -1
        const val NUM_OF_CURRENCY_RATES_0: Int = 0
        const val NUM_OF_CURRENCY_RATES_DELETED_1: Int = 1

        const val IS_CURRENCY_RATE_EXCEPTION_TRUE: Boolean = true
        const val IS_CURRENCY_RATES_LIST_EMPTY_TRUE: Boolean = true

        val PARAMS_NONE_CURRENCY_RATE: None_CurrencyRate = None_CurrencyRate()
        val PARAMS_CURRENCY_RATE: CurrencyRateParams = CurrencyRateParams(currencySymbolBase = CURRENCY_NAME_EUR)
        val PARAMS_BAD_CURRENCY_RATE: CurrencyRateBadParams = CurrencyRateBadParams()

    }

    private lateinit var propertyDao: FakePropertyDao

    // Class under test
    private lateinit var propertyLocalDataSource: PropertyLocalDataSource

    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Before
    fun createLocalDataSource() {

        propertyDao = FakePropertyDao()

        propertyLocalDataSource = PropertyLocalDataSource(
            propertyDao = propertyDao,
            ioDispatcher = Dispatchers.Unconfined,
            propertyEntityMapper = PropertyEntityMapper(),
            propertyModelMapper = PropertyModelMapper(),
            currencyRateEntityMapper = CurrencyRateEntityMapper(),
            currencyRateModelMapper = CurrencyRateModelMapper(),
            networkStatEntityMapper = NetworkStatEntityMapper(),
            networkStatModelMapper = NetworkStatModelMapper(),
        )
    }

    @Test
    fun countAllCurrencyRates_CurrencyRatesMoreThanZero() = runTest {
        val noOfCurrencyRatesExpected: Int = NUM_OF_CURRENCY_RATES_150

        val params = PARAMS_NONE_CURRENCY_RATE

        val numOfAllCurrencyRatesResultData: ResultData.Success<Int> =
            propertyLocalDataSource
                .countAllCurrencyRates(params = params) as ResultData.Success

        assertThat(numOfAllCurrencyRatesResultData.data, IsEqual(noOfCurrencyRatesExpected))
    }

    @Test
    fun countAllCurrencyRates_NonExistentCurrencyRateDataError() = runTest {

        propertyDao = propertyDao.copy(noOfCurrencyRates = NUM_OF_CURRENCY_RATES_0)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_NONE_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countAllCurrencyRates(params = params) as ResultData.Error

        assertThat(
            errorResultData.failure,
            IsInstanceOf(NonExistentCurrencyRateDataLocalError::class.java)
        )
    }

    @Test
    fun countAllCurrencyRates_GeneralError() = runTest {

        propertyDao = propertyDao.copy(noOfCurrencyRates = NUM_OF_CURRENCY_RATES_MINUS_1)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_NONE_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countAllCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countAllCurrencyRates_ExceptionThrown() = runTest {

        propertyDao = propertyDao.copy(isCountException = IS_CURRENCY_RATE_EXCEPTION_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_NONE_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countAllCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countAllCurrencyRates_IllegalArgumentExceptionThrown() = runTest {

        val params = CurrencyRateParams()

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyLocalDataSource.countAllCurrencyRates(params = params)
    }

    @Test
    fun countAllCurrencyRates_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyLocalDataSource.countAllCurrencyRates(params = params)
    }

    @Test
    fun countCurrencyRates_CurrencyRatesMoreThanZero() = runTest {
        val noOfCurrencyRatesExpected: Int = NUM_OF_CURRENCY_RATES_150

        val params = PARAMS_CURRENCY_RATE

        val numOfCurrencyRatesResultData: ResultData.Success<Int> =
            propertyLocalDataSource
                .countCurrencyRates(params = params) as ResultData.Success

        assertThat(numOfCurrencyRatesResultData.data, IsEqual(noOfCurrencyRatesExpected))
    }

    @Test
    fun countCurrencyRates_NonExistentCurrencyRateDataError() = runTest {
        propertyDao = propertyDao.copy(noOfCurrencyRates = NUM_OF_CURRENCY_RATES_0)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countCurrencyRates(params = params) as ResultData.Error

        assertThat(
            errorResultData.failure,
            IsInstanceOf(NonExistentCurrencyRateDataLocalError::class.java)
        )
    }

    @Test
    fun countCurrencyRates_GeneralError() = runTest {

        propertyDao = propertyDao.copy(noOfCurrencyRates = NUM_OF_CURRENCY_RATES_MINUS_1)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countCurrencyRates_ExceptionThrown() = runTest {

        propertyDao = propertyDao.copy(isCountException = IS_CURRENCY_RATE_EXCEPTION_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countCurrencyRates_ExceptionThrownWhenBaseCurrencySymbolParamsIsNull() = runTest {

        val params = CurrencyRateParams() // When currencySymbolBase is null.

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .countCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countCurrencyRates_IllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyLocalDataSource.countCurrencyRates(params = params)
    }

    @Test
    fun countCurrencyRates_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyLocalDataSource.countCurrencyRates(params = params)
    }

    @Test
    fun getCurrencyRates_CurrencyRatesListNotEmpty() = runTest {

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRatesModel = listOf(currencyRateModel1, currencyRateModel2)

        val params = PARAMS_CURRENCY_RATE

        val currencyRatesModelResultData: ResultData.Success<List<CurrencyRateModel>> =
            propertyLocalDataSource
                .getCurrencyRates(params = params) as ResultData.Success

        assertThat(currencyRatesModelResultData.data, IsEqual(currencyRatesModel))
    }

    @Test
    fun getCurrencyRates_CurrencyRateListNotAvailableLocalError() = runTest {

        propertyDao = propertyDao.copy(isEmptyList = IS_CURRENCY_RATES_LIST_EMPTY_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .getCurrencyRates(params = params) as ResultData.Error

        assertThat(
            errorResultData.failure,
            IsInstanceOf(CurrencyRateListNotAvailableLocalError::class.java)
        )
    }

    @Test
    fun getCurrencyRates_ExceptionThrown() = runTest {

        propertyDao = propertyDao.copy(isException = IS_CURRENCY_RATE_EXCEPTION_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .getCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun getCurrencyRates_IllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyLocalDataSource.getCurrencyRates(params = params)
    }

    @Test
    fun getCurrencyRates_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyLocalDataSource.getCurrencyRates(params = params)
    }

    @Test
    fun saveCurrencyRates_CurrencyRatesListNotEmpty() = runTest {
        val tableRowIdsExpected: List<Long> = TABLE_ROW_IDS_TWO

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRatesModel: List<CurrencyRateModel> =
            listOf(currencyRateModel1, currencyRateModel2)

        val tableRowIdsResultData: ResultData.Success<List<Long>> =
            propertyLocalDataSource
                .saveCurrencyRates(currencyRatesModel = currencyRatesModel) as ResultData.Success

        assertThat(tableRowIdsResultData.data, IsEqual(tableRowIdsExpected))
    }

    @Test
    fun saveCurrencyRates_CurrencyRatesListIsEmpty() = runTest {

        propertyDao = propertyDao.copy(isEmptyList = IS_CURRENCY_RATES_LIST_EMPTY_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val errorMessageExpected: Int = R.string.error_cant_save_empty_currency_rate_list

        val currencyRatesModel = emptyList<CurrencyRateModel>()

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .saveCurrencyRates(currencyRatesModel = currencyRatesModel) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as InsertCurrencyRateLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(InsertCurrencyRateLocalError::class.java))
    }

    @Test
    fun saveCurrencyRates_AllCurrencyRatesNotSavedError() = runTest {

        propertyDao = propertyDao.copy(tableRowIds = TABLE_ROW_IDS_TWO)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRateModel3 = CurrencyRateModel.newInstance(
            id = ID_ALL,
            rate = RATE_ALL,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_USD,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_ALL,
        )

        val currencyRatesModel: List<CurrencyRateModel> =
            listOf(currencyRateModel1, currencyRateModel2, currencyRateModel3)

        val errorMessageExpected: Int = R.string.error_all_currency_rates_not_saved

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .saveCurrencyRates(currencyRatesModel = currencyRatesModel) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as InsertCurrencyRateLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(InsertCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_CurrencyRatesDeletedSuccessfully() = runTest {
        val numOfCurrencyRatesDeletedExpected: Int = NUM_OF_CURRENCY_RATES_150

        val params = PARAMS_CURRENCY_RATE

        val numOfCurrencyRatesDeletedResultData: ResultData.Success<Int> =
            propertyLocalDataSource
                .deleteCurrencyRates(params = params) as ResultData.Success

        assertThat(
            numOfCurrencyRatesDeletedResultData.data,
            IsEqual(numOfCurrencyRatesDeletedExpected)
        )
    }

    @Test
    fun deleteCurrencyRates_ExceptionThrownWhileCountingCurrencyRates() = runTest {

        propertyDao = propertyDao.copy(isCountException = IS_CURRENCY_RATE_EXCEPTION_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .deleteCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(DeleteCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_ErrorDeletingCurrencyRates() = runTest {

        propertyDao =
            propertyDao.copy(noOfCurrencyRatesDeleted = NUM_OF_CURRENCY_RATES_DELETED_1)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorMessageExpected: Int = R.string.error_deleting_currency_rates

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .deleteCurrencyRates(params = params) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as DeleteCurrencyRateLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(DeleteCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_ExceptionThrown() = runTest {

        propertyDao = propertyDao.copy(isException = IS_CURRENCY_RATE_EXCEPTION_TRUE)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            propertyDao = propertyDao
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyLocalDataSource
            .deleteCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(DeleteCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_IllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyLocalDataSource.deleteCurrencyRates(params = params)
    }

    @Test
    fun deleteCurrencyRates_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyLocalDataSource.deleteCurrencyRates(params = params)
    }
    
}

private class CurrencyRateBadParams : Params_CurrencyRate