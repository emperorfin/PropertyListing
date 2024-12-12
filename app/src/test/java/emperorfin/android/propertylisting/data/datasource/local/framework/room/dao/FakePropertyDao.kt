package emperorfin.android.propertylisting.data.datasource.local.framework.room.dao

import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_BASE_EUR
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.CURRENCY_SYMBOL_OTHER_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.ID_AFN
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AED
import emperorfin.android.propertylisting.data.datasource.util.DataGeneratorUtil.RATE_AFN
import emperorfin.android.propertylisting.domain.datalayer.dao.IFakePropertyDao


internal data class FakePropertyDao(
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

        val TABLE_ROW_IDS_TWO: List<Long> = listOf(1L, 2L)

        val CURRENCY_RATES_ENTITY: List<CurrencyRateEntity> =
            DataGeneratorUtil.getCurrencyRateEntityList()
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
        if (isCountException) throw Exception()

        return noOfCurrencyRates
    }

    override suspend fun countCurrencyRates(currencySymbolBase: String): Int {
        if (isCountException) throw Exception()

        return noOfCurrencyRates
    }

    override suspend fun getCurrencyRates(currencySymbolBase: String): List<CurrencyRateEntity> {
        if (isException) throw Exception()

        if (isEmptyList) return emptyList()

        val currencyRate1 = CurrencyRateEntity.newInstance(
            id = ID_AED,
            rate = RATE_AED,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AED,
        )

        val currencyRate2 = CurrencyRateEntity.newInstance(
            id = ID_AFN,
            rate = RATE_AFN,
            currencySymbolBase = CURRENCY_SYMBOL_BASE_EUR,
            currencySymbolOther = CURRENCY_SYMBOL_OTHER_AFN,
        )

        return listOf(currencyRate1, currencyRate2)
    }

    override suspend fun getCurrencyRates(): Any =
        throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    override suspend fun insertCurrencyRates(currencyRates: List<CurrencyRateEntity>): List<Long> =
        tableRowIds

    override suspend fun deleteCurrencyRates(currencySymbolBase: String): Int {
        if (isException) throw Exception()

        return noOfCurrencyRatesDeleted
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
