package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.api

import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.currencyrates.CurrencyRatesResponse
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AFN
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_NAME_EUR
import emperorfin.android.propertylisting.domain.datalayer.dao.IFakePropertyDao
import okhttp3.ResponseBody
import retrofit2.Response


internal data class FakePropertyApi(
    private val noOfCurrencyRates: Int = NUM_OF_CURRENCY_RATES_150,
    private val noOfCurrencyRatesDeleted: Int = NUM_OF_CURRENCY_RATES_150,
    private val tableRowIds: List<Long> = TABLE_ROW_IDS_TWO,
    private val currencyRatesEntity: List<CurrencyRateEntity> = CURRENCY_RATES_ENTITY,
    private val isException: Boolean = false,
    private val isCountException: Boolean = false,
    private val isEmptyList: Boolean = false,
    private val isGetRemoteCurrencyRatesFailed: Boolean = false
) : IFakePropertyDao {

    companion object {

        const val NUM_OF_CURRENCY_RATES_150: Int = 150

        private val CURRENCY_RATES_MAP: Map<String, Double> = mapOf(
            CURRENCY_SYMBOL_OTHER_AED to RATE_AED,
            CURRENCY_SYMBOL_OTHER_AFN to RATE_AFN
        )

        val TABLE_ROW_IDS_TWO: List<Long> = listOf(1L, 2L)

        val CURRENCY_RATES_ENTITY: List<CurrencyRateEntity> =
            DataGeneratorUtil.getCurrencyRateEntityList()

        fun getSuccessfulCurrencyRates(): Response<CurrencyRatesResponse> {
            val responseWrapper = CurrencyRatesResponse(
                success = true,
                timestamp = 1710499564,
                historical = true,
                base = CURRENCY_NAME_EUR,
                date = "2024-03-15",
                rates = CURRENCY_RATES_MAP
            )

            val response: Response<CurrencyRatesResponse> = Response.success(responseWrapper)

            return response
        }

        fun getFailedCurrencyRates(): Response<CurrencyRatesResponse> {
            val responseBody: ResponseBody = ResponseBody.create(null, "Error encountered.")

            val response: Response<CurrencyRatesResponse> = Response.error(404, responseBody)

            return response
        }
    }

    override suspend fun countAllProperties(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProperties(): List<PropertyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getProperties(query: String): List<PropertyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getProperties(): Any {
        TODO("Not yet implemented")
    }

    override suspend fun insertProperty(property: PropertyEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProperty(id: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun countAllCurrencyRates(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun countCurrencyRates(currencySymbolBase: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencyRates(currencySymbolBase: String): List<CurrencyRateEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencyRates(): Any {
        if (isException) throw Exception()

        if (isGetRemoteCurrencyRatesFailed) return getFailedCurrencyRates()

        return getSuccessfulCurrencyRates()
    }

    override suspend fun insertCurrencyRates(currencyRates: List<CurrencyRateEntity>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrencyRates(currencySymbolBase: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun countAllNetworkStats(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAllNetworkStats(): List<NetworkStatEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getNetworkStats(requestMethod: String, duration: String): Any {
        TODO("Not yet implemented")
    }

    override suspend fun insertNetworkStat(networkStat: NetworkStatEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNetworkStat(requestMethod: String): Int {
        TODO("Not yet implemented")
    }
}
