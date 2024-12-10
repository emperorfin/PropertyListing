package emperorfin.android.propertylisting.domain.datalayer.dao

import androidx.room.Room
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyEntityParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.CurrencyRateEntityParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatEntityParams
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.currencyrates.CurrencyRatesResponse
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.networkstat.NetworkStatsResponse
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.PropertiesResponse
import retrofit2.Retrofit

/**
 * __NOTE__
 *
 * Classes (e.g. [PropertyEntity]) from other layers such as the __*data*__ layer isn't supposed to be
 * a dependency in or referenced from the __*domain*__ layer. The reason [PropertyEntity] instead of
 * [PropertyEntityParams] (which is from the __*domain*__ layer) is referenced here is because the [Room]
 * ORM doesn't support return type override. This also applies to [CurrencyRateEntity] instead of
 * [CurrencyRateEntityParams] and [NetworkStatEntity] instead of [NetworkStatEntityParams].
 *
 * Because [Retrofit] supports return type override, [PropertiesResponse], [CurrencyRatesResponse]
 * and [NetworkStatsResponse] weren't referenced in this __*domain*__ layer.
 */
interface IPropertyDao {

    suspend fun countAllProperties(): Int

    suspend fun getAllProperties(): List<PropertyEntity>

    suspend fun getProperties(query: String): List<PropertyEntity>

//    suspend fun getProperties(): Response<PropertiesResponse>
    suspend fun getProperties(): Any

    suspend fun insertProperty(property: PropertyEntity): Long

    suspend fun deleteProperty(id: Long): Int

    suspend fun countAllCurrencyRates(): Int

    suspend fun countCurrencyRates(currencySymbolBase: String): Int
    
    suspend fun getCurrencyRates(currencySymbolBase: String): List<CurrencyRateEntity>

//    suspend fun getCurrencyRates(): Response<CurrencyRatesResponse>
    suspend fun getCurrencyRates(): Any

    suspend fun insertCurrencyRates(currencyRates: List<CurrencyRateEntity>): List<Long>

    suspend fun deleteCurrencyRates(currencySymbolBase: String): Int

    suspend fun countAllNetworkStats(): Int

    suspend fun getAllNetworkStats(): List<NetworkStatEntity>

//    suspend fun getNetworkStats(): Response<NetworkStatsResponse>
    suspend fun getNetworkStats(requestMethod: String, duration: String): Any

    suspend fun insertNetworkStat(networkStat: NetworkStatEntity): Long

    suspend fun deleteNetworkStat(requestMethod: String): Int
    
}