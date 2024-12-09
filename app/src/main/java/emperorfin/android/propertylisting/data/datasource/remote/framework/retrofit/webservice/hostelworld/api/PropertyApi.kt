package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.api

import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.currencyrates.CurrencyRatesResponse
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.PropertiesResponse
import emperorfin.android.propertylisting.domain.datalayer.dao.IPropertyDao
import retrofit2.Response
import retrofit2.http.GET


interface PropertyApi : IPropertyDao {

    @GET("a1517b9da90dd6877385a65f324ffbc3/raw/058e83aa802907cb0032a15ca9054da563138541/properties.json")
    override suspend fun getProperties(): Response<PropertiesResponse>

    @GET("16e87e40ca7b9650aa8e1b936f23e14e/raw/15efd901b57c2b66bf0201ee7aa9aa2edc6df779/rates.json")
    override suspend fun getCurrencyRates(): Response<CurrencyRatesResponse>

}