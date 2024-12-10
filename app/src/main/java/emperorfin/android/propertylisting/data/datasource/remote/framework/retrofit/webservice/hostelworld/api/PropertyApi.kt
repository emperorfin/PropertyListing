package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.api

import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.currencyrates.CurrencyRatesResponse
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.networkstat.NetworkStatsResponse
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.PropertiesResponse
import emperorfin.android.propertylisting.domain.datalayer.dao.IPropertyDao
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface PropertyApi : IPropertyDao {

    @GET("a1517b9da90dd6877385a65f324ffbc3/raw/058e83aa802907cb0032a15ca9054da563138541/properties.json")
    override suspend fun getProperties(): Response<PropertiesResponse>

    @GET("16e87e40ca7b9650aa8e1b936f23e14e/raw/15efd901b57c2b66bf0201ee7aa9aa2edc6df779/rates.json")
    override suspend fun getCurrencyRates(): Response<CurrencyRatesResponse>

    @GET("6bed011203c6c8217f0d55f74ddcc5c5/raw/ce8f55cfd963aeef70f2ac9f88f34cefd19fca30/stats?&")
    override suspend fun getNetworkStats(
        @Query("action") requestMethod: String,
        @Query("duration") duration: String
    ): Response<NetworkStatsResponse>

}