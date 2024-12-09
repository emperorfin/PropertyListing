package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.currencyrates

data class CurrencyRatesResponse(
    val success: Boolean?,
    val timestamp: Long?,
    val historical: Boolean?,
    val base: String?,
    val date: String?,
    val rates: Map<String, Double>,
)
