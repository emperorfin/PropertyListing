package emperorfin.android.propertylisting.domain.datalayer.repository

import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.Params as Params_NetworkStat
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params as Params_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate


interface IPropertyRepository {

    suspend fun countAllProperties(params: Params_Property, countRemotely: Boolean = false): ResultData<Int>

    suspend fun getProperties(params: Params_Property, forceUpdate: Boolean = false): ResultData<List<PropertyModel>>

    suspend fun saveProperty(property: PropertyModel, saveRemotely: Boolean = false): ResultData<Long>

    suspend fun deleteProperty(params: Params_Property, deleteRemotely: Boolean = false): ResultData<Int>

    suspend fun countAllCurrencyRates(params: Params_CurrencyRate, countRemotely: Boolean = false): ResultData<Int>

    suspend fun countCurrencyRates(params: Params_CurrencyRate, countRemotely: Boolean = false): ResultData<Int>

    suspend fun getCurrencyRates(params: Params_CurrencyRate, forceUpdate: Boolean = false): ResultData<List<CurrencyRateModel>>

    suspend fun saveCurrencyRates(currencyRatesModel: List<CurrencyRateModel>, saveRemotely: Boolean = false): ResultData<List<Long>>

    suspend fun deleteCurrencyRates(params: Params_CurrencyRate, deleteRemotely: Boolean = false): ResultData<Int>

    suspend fun countAllNetworkStats(params: Params_NetworkStat, countRemotely: Boolean = false): ResultData<Int>

    suspend fun getNetworkStats(params: Params_NetworkStat, forceUpdate: Boolean = false): ResultData<List<NetworkStatModel>>

    suspend fun saveNetworkStat(networkStat: NetworkStatModel, saveRemotely: Boolean = false): ResultData<Long>

    suspend fun deleteNetworkStat(params: Params_NetworkStat, deleteRemotely: Boolean = false): ResultData<Int>
    
}