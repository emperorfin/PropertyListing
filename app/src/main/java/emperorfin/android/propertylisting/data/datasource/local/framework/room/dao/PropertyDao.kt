package emperorfin.android.propertylisting.data.datasource.local.framework.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity.Companion.COLUMN_INFO_CURRENCY_SYMBOL_BASE
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity.Companion.COLUMN_INFO_REQUEST_METHOD
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity.Companion.TABLE_NAME as TABLE_NAME_NETWORK_STAT
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity.Companion.TABLE_NAME as TABLE_NAME_PROPERTY
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity.Companion.COLUMN_INFO_ID as COLUMN_INFO_ID_PROPERTY
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity.Companion.COLUMN_INFO_NAME
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity.Companion.COLUMN_INFO_OVERVIEW
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity.Companion.TABLE_NAME as TABLE_NAME_CURRENCY_RATE
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity.Companion.COLUMN_INFO_ID as COLUMN_INFO_ID_CURRENCY_RATE
import emperorfin.android.propertylisting.domain.datalayer.dao.IPropertyDao


@Dao
interface PropertyDao : IPropertyDao {

    @Query("SELECT COUNT(*) FROM $TABLE_NAME_PROPERTY")
    override suspend fun countAllProperties(): Int

    @Query("SELECT * FROM $TABLE_NAME_PROPERTY ORDER BY $COLUMN_INFO_NAME ASC")
    override suspend fun getAllProperties(): List<PropertyEntity>

    // Seems more readable when a (long) query string is in a single line rather than breaking it
    // down into multiple lines.
    @Query("SELECT * FROM $TABLE_NAME_PROPERTY WHERE $COLUMN_INFO_NAME LIKE '%' || :query || '%' OR $COLUMN_INFO_OVERVIEW LIKE '%' || :query || '%' ORDER BY $COLUMN_INFO_NAME ASC")
    override suspend fun getProperties(query: String): List<PropertyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertProperty(property: PropertyEntity): Long

    @Query("DELETE FROM $TABLE_NAME_PROPERTY WHERE $COLUMN_INFO_ID_PROPERTY = :id")
    override suspend fun deleteProperty(id: Long): Int

    @Query("SELECT COUNT(*) FROM $TABLE_NAME_CURRENCY_RATE")
    override suspend fun countAllCurrencyRates(): Int

    @Query("SELECT COUNT(*) FROM $TABLE_NAME_CURRENCY_RATE WHERE $COLUMN_INFO_CURRENCY_SYMBOL_BASE = :currencySymbolBase")
    override suspend fun countCurrencyRates(currencySymbolBase: String): Int

    // Seems more readable when a (long) query string is in a single line rather than breaking it
    // down into multiple lines.
    @Query("SELECT * FROM $TABLE_NAME_CURRENCY_RATE WHERE $COLUMN_INFO_CURRENCY_SYMBOL_BASE = :currencySymbolBase ORDER BY $COLUMN_INFO_ID_CURRENCY_RATE ASC")
    override suspend fun getCurrencyRates(currencySymbolBase: String): List<CurrencyRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertCurrencyRates(currencyRates: List<CurrencyRateEntity>): List<Long>

    @Query("DELETE FROM $TABLE_NAME_CURRENCY_RATE WHERE $COLUMN_INFO_CURRENCY_SYMBOL_BASE = :currencySymbolBase")
    override suspend fun deleteCurrencyRates(currencySymbolBase: String): Int

    @Query("SELECT COUNT(*) FROM $TABLE_NAME_NETWORK_STAT")
    override suspend fun countAllNetworkStats(): Int

    @Query("SELECT * FROM $TABLE_NAME_NETWORK_STAT ORDER BY $COLUMN_INFO_REQUEST_METHOD ASC")
    override suspend fun getAllNetworkStats(): List<NetworkStatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertNetworkStat(networkStat: NetworkStatEntity): Long

    @Query("DELETE FROM $TABLE_NAME_NETWORK_STAT WHERE $COLUMN_INFO_REQUEST_METHOD = :requestMethod")
    override suspend fun deleteNetworkStat(requestMethod: String): Int

    // This must be overridden else the Room ORM won't allow project to compile.
    override suspend fun getProperties(): Any =
        throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    // This must be overridden else the Room ORM won't allow project to compile.
    override suspend fun getCurrencyRates(): Any =
        throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)

    // This must be overridden else the Room ORM won't allow project to compile.
    override suspend fun getNetworkStats(requestMethod: String, duration: String): Any =
        throw IllegalStateException(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
    
}