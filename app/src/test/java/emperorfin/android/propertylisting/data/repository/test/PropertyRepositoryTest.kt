package emperorfin.android.propertylisting.data.repository.test

import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entitysource.fake.FakePropertyLocalDataSource
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dtosource.fake.FakePropertyRemoteDataSource
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_BASE_EUR
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AFN
import emperorfin.android.propertylisting.data.repository.PropertyRepository
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.NonExistentCurrencyRateDataLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRepositoryError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.InsertCurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.DeleteCurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.None as None_CurrencyRate
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate
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


internal class PropertyRepositoryTest {

    private companion object {

        const val NUM_OF_CURRENCY_RATES_MINUS_1: Int = -1
        const val NUM_OF_CURRENCY_RATES_0: Int = 0
        const val NUM_OF_CURRENCY_RATES_150: Int = 150

        const val IS_CURRENCY_RATE_FORCE_UPDATE_FALSE: Boolean = false
        const val IS_CURRENCY_RATE_FORCE_UPDATE_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE: Boolean = false
        const val IS_CURRENCY_RATE_COUNT_REMOTELY_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_GET_CURRENCY_RATES_RESPONSE_UNSUCCESSFUL_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_SAVE_REMOTELY_FALSE: Boolean = false
        const val IS_CURRENCY_RATE_SAVE_REMOTELY_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE: Boolean = false
        const val IS_CURRENCY_RATE_DELETE_REMOTELY_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_EXCEPTION_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_SAVE_CURRENCY_RATES_ERROR_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_DELETE_CURRENCY_RATES_ERROR_WHILE_DELETING_ALL_RATE_TRUE: Boolean = true
        const val IS_CURRENCY_RATE_DELETE_CURRENCY_RATES_ERROR_DURING_RATES_COUNT_TRUE: Boolean = true

        const val ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED: String =
            "An operation is not implemented: Not yet implemented"

        val PARAMS_NONE_CURRENCY_RATE: None_CurrencyRate = None_CurrencyRate()
        val PARAMS_CURRENCY_RATE: CurrencyRateParams = 
            CurrencyRateParams(currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR)
        val PARAMS_BAD_CURRENCY_RATE: CurrencyRateBadParams = CurrencyRateBadParams()

    }

    private lateinit var propertyLocalDataSource: FakePropertyLocalDataSource
    private lateinit var propertyRemoteDataSource: FakePropertyRemoteDataSource

    // Class under test
    private lateinit var propertyRepository: PropertyRepository

    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Before
    fun createRepository() {

        propertyLocalDataSource = FakePropertyLocalDataSource()
        propertyRemoteDataSource = FakePropertyRemoteDataSource()

        propertyRepository = PropertyRepository(
            propertyLocalDataSource = propertyLocalDataSource,
            propertyRemoteDataSource = propertyRemoteDataSource,
            ioDispatcher = Dispatchers.Unconfined
        )
    }

    @Test
    fun countAllCurrencyRates_LocalDataSourceCurrencyRatesMoreThanZero() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            countAllCurrencyRatesResultData = ResultData.Success(NUM_OF_CURRENCY_RATES_150)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val noOfCurrencyRatesExpected: Int = NUM_OF_CURRENCY_RATES_150

        val params = PARAMS_NONE_CURRENCY_RATE

        val numOfAllCurrencyRatesResultData: ResultData.Success<Int> = propertyRepository
            .countAllCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Success

        assertThat(numOfAllCurrencyRatesResultData.data, IsEqual(noOfCurrencyRatesExpected))
    }

    @Test
    fun countAllCurrencyRates_LocalDataSourceNonExistentCurrencyRateDataError() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            countAllCurrencyRatesResultData = ResultData.Success(NUM_OF_CURRENCY_RATES_0)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_NONE_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .countAllCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(
            errorResultData.failure,
            IsInstanceOf(NonExistentCurrencyRateDataLocalError::class.java)
        )
    }

    @Test
    fun countAllCurrencyRates_LocalDataSourceGeneralError() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            countAllCurrencyRatesResultData = ResultData.Success(NUM_OF_CURRENCY_RATES_MINUS_1)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_NONE_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .countAllCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countAllCurrencyRates_LocalDataSourceExceptionThrown() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isCountAllCurrencyRatesException = IS_CURRENCY_RATE_EXCEPTION_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_NONE_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .countAllCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countAllCurrencyRates_LocalDataSourceIllegalArgumentExceptionThrown() = runTest {

        val params = CurrencyRateParams()

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyRepository.countAllCurrencyRates(
            params = params,
            countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
        )
    }

    @Test
    fun countAllCurrencyRates_LocalDataSourceNotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyRepository.countAllCurrencyRates(
            params = params,
            countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
        )
    }

    @Test
    fun countCurrencyRates_LocalDataSourceCurrencyRatesMoreThanZero() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            countCurrencyRatesResultData = ResultData.Success(NUM_OF_CURRENCY_RATES_150)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val noOfCurrencyRatesExpected: Int = NUM_OF_CURRENCY_RATES_150

        val params = PARAMS_CURRENCY_RATE

        val numOfAllCurrencyRatesResultData: ResultData.Success<Int> = propertyRepository
            .countCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Success

        assertThat(numOfAllCurrencyRatesResultData.data, IsEqual(noOfCurrencyRatesExpected))
    }

    @Test
    fun countCurrencyRates_LocalDataSourceNonExistentCurrencyRateDataError() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            countCurrencyRatesResultData = ResultData.Success(NUM_OF_CURRENCY_RATES_0)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .countCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(
            errorResultData.failure,
            IsInstanceOf(NonExistentCurrencyRateDataLocalError::class.java)
        )
    }

    @Test
    fun countCurrencyRates_LocalDataSourceGeneralError() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            countCurrencyRatesResultData = ResultData.Success(NUM_OF_CURRENCY_RATES_MINUS_1)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .countCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countCurrencyRates_LocalDataSourceExceptionThrown() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isCountCurrencyRatesException = IS_CURRENCY_RATE_EXCEPTION_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .countCurrencyRates(
                params = params,
                countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateLocalError::class.java))
    }

    @Test
    fun countCurrencyRates_LocalDataSourceIllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyRepository.countCurrencyRates(
            params = params,
            countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
        )
    }

    @Test
    fun countCurrencyRates_LocalDataSourceNotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyRepository.countCurrencyRates(
            params = params,
            countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_FALSE
        )
    }

    @Test
    fun getCurrencyRates_LocalDataSourceCurrencyRatesListNotEmpty() = runTest {

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRatesModel = listOf(currencyRateModel1, currencyRateModel2)

        val params = PARAMS_CURRENCY_RATE

        val currencyRatesModelResultData: ResultData.Success<List<CurrencyRateModel>> =
            propertyRepository
                .getCurrencyRates(
                    params = params,
                    forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_FALSE
                ) as ResultData.Success

        assertThat(currencyRatesModelResultData.data, IsEqual(currencyRatesModel))
    }

    @Test
    fun getCurrencyRates_LocalDataSourceCurrencyRateListNotAvailableLocalError() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            getCurrencyRatesResultData = ResultData.Success(emptyList())
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val errorMessageExpected: Int = R.string.error_fetching_from_remote_and_local

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .getCurrencyRates(
                params = params,
                forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_FALSE
            ) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as GetCurrencyRateRepositoryError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(
            errorResultData.failure,
            IsInstanceOf(GetCurrencyRateRepositoryError::class.java)
        )
    }

    @Test
    fun getCurrencyRates_LocalDataSourceExceptionThrown() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isGetCurrencyRatesException = IS_CURRENCY_RATE_EXCEPTION_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val errorMessageExpected: Int = R.string.exception_occurred_local

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .getCurrencyRates(
                params = params,
                forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_FALSE
            ) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as GetCurrencyRateRepositoryError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(
            errorResultData.failure,
            IsInstanceOf(GetCurrencyRateRepositoryError::class.java)
        )
    }

    @Test
    fun getCurrencyRates_LocalDataSourceIllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyRepository.getCurrencyRates(
            params = params,
            forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_FALSE
        )
    }

    @Test
    fun getCurrencyRates_LocalDataSourceNotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyRepository.getCurrencyRates(
            params = params,
            forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_FALSE
        )
    }

    @Test
    fun saveCurrencyRates_LocalDataSourceCurrencyRatesListNotEmpty() = runTest {
        val tableRowIdsExpected: List<Long> = listOf(1L, 2L)

        propertyLocalDataSource = propertyLocalDataSource.copy(
            saveCurrencyRatesResultData = ResultData.Success(tableRowIdsExpected)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRatesModel: List<CurrencyRateModel> =
            listOf(currencyRateModel1, currencyRateModel2)

        val tableRowIdsResultData: ResultData.Success<List<Long>> = propertyRepository
            .saveCurrencyRates(
                currencyRatesModel = currencyRatesModel,
                saveRemotely = IS_CURRENCY_RATE_SAVE_REMOTELY_FALSE
            ) as ResultData.Success

        assertThat(tableRowIdsResultData.data, IsEqual(tableRowIdsExpected))
    }

    @Test
    fun saveCurrencyRates_LocalDataSourceCurrencyRatesListIsEmpty() = runTest {

        val errorMessageExpected: Int = R.string.error_cant_save_empty_currency_rate_list

        val currencyRatesModel = emptyList<CurrencyRateModel>()

        val errorResultData: ResultData.Error = propertyRepository
            .saveCurrencyRates(
                currencyRatesModel = currencyRatesModel,
                saveRemotely = IS_CURRENCY_RATE_SAVE_REMOTELY_FALSE
            ) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as InsertCurrencyRateLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(InsertCurrencyRateLocalError::class.java))
    }

    @Test
    fun saveCurrencyRates_LocalDataSourceAllCurrencyRatesNotSavedError() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isSaveCurrencyRatesError = IS_CURRENCY_RATE_SAVE_CURRENCY_RATES_ERROR_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val errorMessageExpected: Int = R.string.error_all_currency_rates_not_saved

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRatesModel: List<CurrencyRateModel> =
            listOf(currencyRateModel1, currencyRateModel2)

        val errorResultData: ResultData.Error = propertyRepository
            .saveCurrencyRates(
                currencyRatesModel = currencyRatesModel,
                saveRemotely = IS_CURRENCY_RATE_SAVE_REMOTELY_FALSE
            ) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as InsertCurrencyRateLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(InsertCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_LocalDataSourceCurrencyRatesDeletedSuccessfully() = runTest {
        val numOfCurrencyRatesDeletedExpected: Int = NUM_OF_CURRENCY_RATES_150

        propertyLocalDataSource = propertyLocalDataSource.copy(
            deleteCurrencyRatesResultData = ResultData.Success(numOfCurrencyRatesDeletedExpected)
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val numOfCurrencyRatesDeletedResultData: ResultData.Success<Int> = propertyRepository
            .deleteCurrencyRates(
                params = params,
                deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE
            ) as ResultData.Success

        assertThat(
            numOfCurrencyRatesDeletedResultData.data,
            IsEqual(numOfCurrencyRatesDeletedExpected)
        )
    }

    @Test
    fun deleteCurrencyRates_LocalDataSourceErrorDeletingCurrencyRates() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isDeleteCurrencyRatesErrorWhileDeletingAllRates = IS_CURRENCY_RATE_DELETE_CURRENCY_RATES_ERROR_WHILE_DELETING_ALL_RATE_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorMessageExpected: Int = R.string.error_deleting_currency_rates

        val errorResultData: ResultData.Error = propertyRepository
            .deleteCurrencyRates(
                params = params,
                deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE
            ) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as DeleteCurrencyRateLocalError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(errorResultData.failure, IsInstanceOf(DeleteCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_LocalDataSourceExceptionThrownWhileCountingCurrencyRates() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isDeleteCurrencyRatesErrorDuringRatesCount = IS_CURRENCY_RATE_DELETE_CURRENCY_RATES_ERROR_DURING_RATES_COUNT_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .deleteCurrencyRates(
                params = params,
                deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(DeleteCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_LocalDataSourceExceptionThrown() = runTest {

        propertyLocalDataSource = propertyLocalDataSource.copy(
            isDeleteCurrencyRatesException = IS_CURRENCY_RATE_EXCEPTION_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyLocalDataSource = propertyLocalDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .deleteCurrencyRates(
                params = params,
                deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(DeleteCurrencyRateLocalError::class.java))
    }

    @Test
    fun deleteCurrencyRates_LocalDataSourceIllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyRepository.deleteCurrencyRates(
            params = params,
            deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE
        )
    }

    @Test
    fun deleteCurrencyRates_LocalDataSourceNotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyRepository.deleteCurrencyRates(
            params = params,
            deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_FALSE
        )
    }

    @Test
    fun getCurrencyRates_RemoteDataSourceCurrencyRatesListNotEmpty() = runTest {

        val currencyRateModel1 = CurrencyRateModel.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRateModel2 = CurrencyRateModel.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        val currencyRatesModel = listOf(currencyRateModel1, currencyRateModel2)

        val params = PARAMS_CURRENCY_RATE

        val currencyRatesModelResultData: ResultData.Success<List<CurrencyRateModel>> =
            propertyRepository.getCurrencyRates(
                params = params,
                forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_TRUE
            ) as ResultData.Success

        assertThat(currencyRatesModelResultData.data, IsEqual(currencyRatesModel))
    }

    @Test
    fun getCurrencyRates_RemoteDataSourceGetCurrencyRatesRemoteError() = runTest {

        propertyRemoteDataSource = propertyRemoteDataSource.copy(
            isGetCurrencyRatesResponseUnsuccessful = IS_CURRENCY_RATE_GET_CURRENCY_RATES_RESPONSE_UNSUCCESSFUL_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyRemoteDataSource = propertyRemoteDataSource
        )

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .getCurrencyRates(
                params = params,
                forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_TRUE
            ) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(GetCurrencyRateRemoteError::class.java))
    }

    @Test
    fun getCurrencyRates_RemoteDataSourceExceptionThrown() = runTest {

        propertyRemoteDataSource = propertyRemoteDataSource.copy(
            isGetCurrencyRatesException = IS_CURRENCY_RATE_EXCEPTION_TRUE
        )

        propertyRepository = propertyRepository.copy(
            propertyRemoteDataSource = propertyRemoteDataSource
        )

        val errorMessageExpected: Int = R.string.exception_occurred_remote

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error = propertyRepository
            .getCurrencyRates(
                params = params,
                forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_TRUE
            ) as ResultData.Error

        val errorMessageActual: Int =
            (errorResultData.failure as GetCurrencyRateRepositoryError).message

        assertThat(errorMessageActual, IsEqual(errorMessageExpected))
        assertThat(
            errorResultData.failure,
            IsInstanceOf(GetCurrencyRateRepositoryError::class.java)
        )
    }

    @Test
    fun getCurrencyRates_RemoteDataSourceIllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyRepository.getCurrencyRates(
            params = params,
            forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_TRUE
        )
    }

    @Test
    fun getCurrencyRates_RemoteDataSourceNotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyRepository.getCurrencyRates(
            params = params,
            forceUpdate = IS_CURRENCY_RATE_FORCE_UPDATE_TRUE
        )
    }

    @Test
    fun countAllCurrencyRates_RemoteDataSourceNotYetImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRepository.countAllCurrencyRates(
            params = params,
            countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_TRUE
        )
    }

    @Test
    fun countCurrencyRates_RemoteDataSourceNotYetImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRepository.countCurrencyRates(
            params = params,
            countRemotely = IS_CURRENCY_RATE_COUNT_REMOTELY_TRUE
        )
    }

    @Test
    fun saveCurrencyRates_RemoteDataSourceNotYetImplementedErrorThrown() = runTest {

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRepository.saveCurrencyRates(
            currencyRatesModel = emptyList(),
            saveRemotely = IS_CURRENCY_RATE_SAVE_REMOTELY_TRUE
        )
    }

    @Test
    fun deleteCurrencyRates_RemoteDataSourceNotYetImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRepository.deleteCurrencyRates(
            params = params,
            deleteRemotely = IS_CURRENCY_RATE_DELETE_REMOTELY_TRUE
        )
    }

}

private class CurrencyRateBadParams : Params_CurrencyRate