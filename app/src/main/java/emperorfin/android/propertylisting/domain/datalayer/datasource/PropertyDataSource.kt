package emperorfin.android.propertylisting.domain.datalayer.datasource

import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.Params as Params_NetworkStat
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params as Params_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate


interface PropertyDataSource {

    suspend fun countAllProperties(params: Params_Property): ResultData<Int>

    suspend fun getProperties(params: Params_Property): ResultData<List<PropertyModel>>

    suspend fun saveProperty(property: PropertyModel): ResultData<Long>

    suspend fun deleteProperty(params: Params_Property): ResultData<Int>

    suspend fun countAllCurrencyRates(params: Params_CurrencyRate): ResultData<Int>

    suspend fun countCurrencyRates(params: Params_CurrencyRate): ResultData<Int>

    suspend fun getCurrencyRates(params: Params_CurrencyRate): ResultData<List<CurrencyRateModel>>

    suspend fun saveCurrencyRates(currencyRatesModel: List<CurrencyRateModel>): ResultData<List<Long>>

    suspend fun deleteCurrencyRates(params: Params_CurrencyRate): ResultData<Int>

    suspend fun countAllNetworkStats(params: Params_NetworkStat): ResultData<Int>

    suspend fun getNetworkStats(params: Params_NetworkStat): ResultData<List<NetworkStatModel>>

    suspend fun saveNetworkStat(networkStat: NetworkStatModel): ResultData<Long>

    suspend fun deleteNetworkStat(params: Params_NetworkStat): ResultData<Int>

}