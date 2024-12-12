package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dtosource.test

import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dtosource.PropertyRemoteDataSource
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.api.FakePropertyApi
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_BASE_EUR
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AFN
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModelMapper
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModelMapper
import emperorfin.android.propertylisting.domain.model.property.PropertyModelMapper
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


class PropertyRemoteDataSourceTest {

    private companion object {

        const val ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED: String =
            "An operation is not implemented: Not yet implemented"

        const val IS_CURRENCY_RATE_EXCEPTION_TRUE: Boolean = true
        const val IS_GET_CURRENCY_RATES_FAILED_TRUE: Boolean = true

        val PARAMS_NONE_CURRENCY_RATE: None_CurrencyRate = None_CurrencyRate()
        val PARAMS_CURRENCY_RATE: CurrencyRateParams = CurrencyRateParams()
        val PARAMS_BAD_CURRENCY_RATE: CurrencyRateBadParams = CurrencyRateBadParams()

    }

    private lateinit var propertyApi: FakePropertyApi

    // Class under test
    private lateinit var propertyRemoteDataSource: PropertyRemoteDataSource

    @JvmField
    @Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Before
    fun createLocalDataSource() {

        propertyApi = FakePropertyApi()

        propertyRemoteDataSource = PropertyRemoteDataSource(
            propertyDao = propertyApi,
            ioDispatcher = Dispatchers.Unconfined,
            mainDispatcher = Dispatchers.Unconfined,
            propertyModelMapper = PropertyModelMapper(),
            currencyRateModelMapper = CurrencyRateModelMapper(),
            networkStatModelMapper = NetworkStatModelMapper(),
        )
    }

    @Test
    fun getCurrencyRates_CurrencyRatesListNotEmpty() = runTest {

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
            propertyRemoteDataSource.getCurrencyRates(params = params) as ResultData.Success

        assertThat(currencyRatesModelResultData.data, IsEqual(currencyRatesModel))
    }

    @Test
    fun getCurrencyRates_GetCurrencyRatesRemoteError() = runTest {

        propertyApi = propertyApi
            .copy(isGetRemoteCurrencyRatesFailed = IS_GET_CURRENCY_RATES_FAILED_TRUE)

        propertyRemoteDataSource = propertyRemoteDataSource
            .copy(propertyDao = propertyApi)

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error =
            propertyRemoteDataSource.getCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(GetCurrencyRateRemoteError::class.java))
    }

    @Test
    fun getCurrencyRates_ExceptionThrown() = runTest {

        propertyApi = propertyApi.copy(isException = IS_CURRENCY_RATE_EXCEPTION_TRUE)

        propertyRemoteDataSource = propertyRemoteDataSource
            .copy(propertyDao = propertyApi)

        val params = PARAMS_CURRENCY_RATE

        val errorResultData: ResultData.Error =
            propertyRemoteDataSource.getCurrencyRates(params = params) as ResultData.Error

        assertThat(errorResultData.failure, IsInstanceOf(CurrencyRateRemoteError::class.java))
    }

    @Test
    fun getCurrencyRates_IllegalArgumentExceptionThrown() = runTest {

        val params = PARAMS_NONE_CURRENCY_RATE

        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED))

        propertyRemoteDataSource.getCurrencyRates(params = params)
    }

    @Test
    fun getCurrencyRates_NotImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_NOT_YET_IMPLEMENTED))

        propertyRemoteDataSource.getCurrencyRates(params = params)
    }

    @Test
    fun countAllCurrencyRates_NotYetImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRemoteDataSource.countAllCurrencyRates(params = params)
    }

    @Test
    fun countCurrencyRates_NotYetImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRemoteDataSource.countCurrencyRates(params = params)
    }

    @Test
    fun saveCurrencyRates_NotYetImplementedErrorThrown() = runTest {

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRemoteDataSource.saveCurrencyRates(currencyRatesModel = emptyList())
    }

    @Test
    fun deleteCurrencyRates_NotYetImplementedErrorThrown() = runTest {

        val params = PARAMS_BAD_CURRENCY_RATE

        expectedException.expect(NotImplementedError::class.java)
        expectedException.expectMessage(equalTo(ERROR_MESSAGE_TODO_NOT_YET_IMPLEMENTED))

        propertyRemoteDataSource.deleteCurrencyRates(params = params)
    }

}

private class CurrencyRateBadParams : Params_CurrencyRate