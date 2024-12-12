package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dtosource.fake

import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_BASE_EUR
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AFN
import emperorfin.android.propertylisting.domain.datalayer.datasource.FakePropertyDataSource
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.None as None_CurrencyRate
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params as Params_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.Params as Params_NetworkStat


data class FakePropertyRemoteDataSource(
    private val isGetCurrencyRatesException: Boolean = false,
    private val getCurrencyRatesResultData: ResultData.Success<List<CurrencyRateModel>> =
        ResultData.Success(CURRENCY_RATES_MODEL),
    private val isGetCurrencyRatesResponseUnsuccessful: Boolean = false,
) : FakePropertyDataSource {

    private companion object {

        val CURRENCY_RATES_MODEL: List<CurrencyRateModel> = buildModelCurrencyRates()

        fun buildModelCurrencyRates(): List<CurrencyRateModel> {

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

            return listOf(currencyRateModel1, currencyRateModel2)
        }

    }

    override suspend fun countAllProperties(params: Params_Property): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getProperties(params: Params_Property): ResultData<List<PropertyModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveProperty(property: PropertyModel): ResultData<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProperty(params: Params_Property): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun countAllCurrencyRates(params: Params_CurrencyRate): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun countCurrencyRates(params: Params_CurrencyRate): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencyRates(params: Params_CurrencyRate): ResultData<List<CurrencyRateModel>> {
        when(params){
            is None_CurrencyRate -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is CurrencyRateParams -> {

                if (isGetCurrencyRatesException)
                    return ResultData.Error(failure = CurrencyRateRemoteError())

                if (isGetCurrencyRatesResponseUnsuccessful)
                    return ResultData.Error(failure = GetCurrencyRateRemoteError())

                if (getCurrencyRatesResultData.data.isEmpty())
                    throw IllegalArgumentException("getCurrencyRatesResultData.data must not be empty.")

                return getCurrencyRatesResultData
            }

            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun saveCurrencyRates(currencyRatesModel: List<CurrencyRateModel>): ResultData<List<Long>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrencyRates(params: Params_CurrencyRate): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun countAllNetworkStats(params: Params_NetworkStat): ResultData<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getNetworkStats(params: Params_NetworkStat): ResultData<List<NetworkStatModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNetworkStat(networkStat: NetworkStatModel): ResultData<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNetworkStat(params: Params_NetworkStat): ResultData<Int> {
        TODO("Not yet implemented")
    }
}
