package emperorfin.android.propertylisting.data.datasource.local.framework.room.entitysource

import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.currencyrate.CurrencyRateEntityMapper
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat.NetworkStatEntityMapper
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntityMapper
import emperorfin.android.propertylisting.di.IoDispatcher
import emperorfin.android.propertylisting.di.LocalPropertyDao
import emperorfin.android.propertylisting.domain.datalayer.dao.IPropertyDao
import emperorfin.android.propertylisting.domain.datalayer.datasource.PropertyDataSource
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.NonExistentCurrencyRateDataLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateListNotAvailableLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.InsertCurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.DeleteCurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.NonExistentNetworkStatDataLocalError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.NetworkStatLocalError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.NetworkStatListNotAvailableLocalError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.InsertNetworkStatLocalError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.DeleteNetworkStatLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.PropertyListNotAvailableLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.NonExistentPropertyDataLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.PropertyLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.InsertPropertyLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.DeletePropertyLocalError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModelMapper
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModelMapper
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModelMapper
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat.NetworkStatParams
import emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat.None as None_NetworkStat
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.PropertyParams
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.None as None_Property
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.None as None_CurrencyRate
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData.Success
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData.Error
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.Params as Params_NetworkStat
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params as Params_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


data class PropertyLocalDataSource @Inject constructor(
    @LocalPropertyDao private val propertyDao: IPropertyDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val propertyEntityMapper: PropertyEntityMapper,
    private val propertyModelMapper: PropertyModelMapper,
    private val currencyRateEntityMapper: CurrencyRateEntityMapper,
    private val currencyRateModelMapper: CurrencyRateModelMapper,
    private val networkStatEntityMapper: NetworkStatEntityMapper,
    private val networkStatModelMapper: NetworkStatModelMapper
) : PropertyDataSource {

    private companion object {
        const val NUM_OF_PROPERTIES_0: Int = 0
        const val NUM_OF_PROPERTY_ROWS_DELETED_1: Int = 1
        const val PROPERTY_TABLE_ROW_ID_1: Long = 1L

        const val NUM_OF_NETWORK_STATS_0: Int = 0
        const val NUM_OF_NETWORK_STAT_ROWS_DELETED_1: Int = 1
        const val NETWORK_STAT_TABLE_ROW_ID_1: Long = 1L
        
        const val NUM_OF_CURRENCY_RATES_0: Int = 0
    }

    override suspend fun countAllProperties(params: Params_Property): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None_Property -> {
                return@withContext try {

                    val numOfProperties: Int = propertyDao.countAllProperties()

                    if (numOfProperties > NUM_OF_PROPERTIES_0) {
                        return@withContext Success(data = numOfProperties)
                    } else if (numOfProperties == NUM_OF_PROPERTIES_0) {
                        return@withContext Error(failure = NonExistentPropertyDataLocalError())
                    }

                    return@withContext Error(failure = PropertyLocalError())

                } catch (e: Exception){
                    return@withContext Error(failure = PropertyLocalError(cause = e))
                }
            }
            is PropertyParams -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }


    }

    override suspend fun getProperties(params: Params_Property): ResultData<List<PropertyModel>> = withContext(ioDispatcher) {

        when(params){
            is None_Property -> {
                return@withContext try {
                    val entityProperties: List<PropertyEntity> = propertyDao.getAllProperties()

                    if (entityProperties.isEmpty())
                        return@withContext Error(failure = PropertyListNotAvailableLocalError())

                    val modelProperties = entityProperties.map {
                        propertyModelMapper.transform(it)
                    }

                    return@withContext Success(modelProperties)

                } catch (e: Exception){
                    return@withContext Error(failure = PropertyLocalError(cause = e))
                }
            }
            is PropertyParams -> {
                return@withContext try {

                    val propertyName: String? = params.name
                    val propertyOverview: String? = params.overview

                    var searchQuery = ""

                    if (propertyName != null) {
                        searchQuery = propertyName
                    } else if (propertyOverview != null) {
                        searchQuery = propertyOverview
                    }

                    val entityProperties: List<PropertyEntity> = if (searchQuery.isNotEmpty()) {
                        propertyDao.getProperties(searchQuery)
                    } else {
                        propertyDao.getAllProperties()
                    }

                    if (entityProperties.isEmpty())
                        return@withContext Error(failure = PropertyListNotAvailableLocalError())

                    val modelProperties = entityProperties.map {
                        propertyModelMapper.transform(it)
                    }

                    return@withContext Success(modelProperties)

                } catch (e: Exception){
                    return@withContext Error(failure = PropertyLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun saveProperty(property: PropertyModel): ResultData<Long> = withContext(ioDispatcher) {

        val entityProperty = propertyEntityMapper.transform(property)
        
        val tableRowId: Long = propertyDao.insertProperty(entityProperty)

        if (tableRowId < PROPERTY_TABLE_ROW_ID_1)
            return@withContext Error(
                InsertPropertyLocalError(message = R.string.error_local_insert_property)
            )

        return@withContext Success(tableRowId)
    }

    override suspend fun deleteProperty(params: Params_Property): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None_Property -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is PropertyParams -> {
                return@withContext try {

                    val numOfPropertiesDeleted: Int = propertyDao.deleteProperty(params.id)

                    if (numOfPropertiesDeleted < NUM_OF_PROPERTY_ROWS_DELETED_1) {
                        return@withContext Error(failure = DeletePropertyLocalError(R.string.error_local_delete_property))
                    }

                    return@withContext Success(numOfPropertiesDeleted)

                } catch (e: Exception){
                    return@withContext Error(failure = DeletePropertyLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun countAllCurrencyRates(params: Params_CurrencyRate): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None_CurrencyRate -> {
                return@withContext try {

                    val numOfAllCurrencyRates: Int = propertyDao.countAllCurrencyRates()

                    if (numOfAllCurrencyRates > NUM_OF_CURRENCY_RATES_0) {
                        return@withContext Success(data = numOfAllCurrencyRates)
                    } else if (numOfAllCurrencyRates == NUM_OF_CURRENCY_RATES_0) {
                        return@withContext Error(failure = NonExistentCurrencyRateDataLocalError())
                    }

                    return@withContext Error(failure = CurrencyRateLocalError())

                } catch (e: Exception){
                    return@withContext Error(failure = CurrencyRateLocalError(cause = e))
                }
            }
            is CurrencyRateParams -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }


    }

    override suspend fun countCurrencyRates(params: Params_CurrencyRate): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None_CurrencyRate -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is CurrencyRateParams -> {
                return@withContext try {

                    val numOfCurrencyRates: Int = propertyDao.countCurrencyRates(params.currencySymbolBase!!)

                    if (numOfCurrencyRates > NUM_OF_CURRENCY_RATES_0) {
                        return@withContext Success(data = numOfCurrencyRates)
                    } else if (numOfCurrencyRates == NUM_OF_CURRENCY_RATES_0) {
                        return@withContext Error(failure = NonExistentCurrencyRateDataLocalError())
                    }

                    return@withContext Error(failure = CurrencyRateLocalError())

                } catch (e: Exception){
                    return@withContext Error(failure = CurrencyRateLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun getCurrencyRates(
        params: Params_CurrencyRate
    ): ResultData<List<CurrencyRateModel>> = withContext(ioDispatcher) {

        when(params){
            is None_CurrencyRate -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is CurrencyRateParams -> {
                return@withContext try {
                    val currencyRatesEntity: List<CurrencyRateEntity> =
                        propertyDao.getCurrencyRates(params.currencySymbolBase!!)

                    if (currencyRatesEntity.isEmpty())
                        return@withContext Error(failure = CurrencyRateListNotAvailableLocalError())

                    val currencyRatesModel = currencyRatesEntity.map {
                        currencyRateModelMapper.transform(it)
                    }

                    return@withContext Success(currencyRatesModel)

                } catch (e: Exception){
                    return@withContext Error(failure = CurrencyRateLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun saveCurrencyRates(
        currencyRatesModel: List<CurrencyRateModel>
    ): ResultData<List<Long>> = withContext(ioDispatcher){

        if (currencyRatesModel.isEmpty())
            return@withContext Error(
                failure = InsertCurrencyRateLocalError(message = R.string.error_cant_save_empty_currency_rate_list)
            )

        val currencyRatesEntity = currencyRatesModel.map {
            currencyRateEntityMapper.transform(it)
        }

        val tableRowIds: List<Long> = propertyDao.insertCurrencyRates(currencyRatesEntity)

        if (tableRowIds.size != currencyRatesEntity.size)
            return@withContext Error(
                InsertCurrencyRateLocalError(message = R.string.error_all_currency_rates_not_saved)
            )

        return@withContext Success(tableRowIds)
    }

    override suspend fun deleteCurrencyRates(params: Params_CurrencyRate): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None_CurrencyRate -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is CurrencyRateParams -> {
                return@withContext try {

                    val numOfCurrencyRatesResultData: ResultData<Int> = countCurrencyRates(params)

                    val numOfCurrencyRates: Int = if(numOfCurrencyRatesResultData is Error &&
                        numOfCurrencyRatesResultData.failure is CurrencyRateLocalError){
                        return@withContext Error(failure = DeleteCurrencyRateLocalError())
                    } else if(numOfCurrencyRatesResultData is Error &&
                        numOfCurrencyRatesResultData.failure is NonExistentCurrencyRateDataLocalError) {
                        NUM_OF_CURRENCY_RATES_0
                    } else {
                        (numOfCurrencyRatesResultData as Success).data
                    }

                    val numOfCurrencyRatesDeleted: Int = propertyDao.deleteCurrencyRates(params.currencySymbolBase!!)

                    if (numOfCurrencyRatesDeleted > NUM_OF_CURRENCY_RATES_0 && numOfCurrencyRatesDeleted != numOfCurrencyRates) {
                        return@withContext Error(failure = DeleteCurrencyRateLocalError(R.string.error_deleting_currency_rates))
                    }

                    return@withContext Success(numOfCurrencyRatesDeleted)

                } catch (e: Exception){
                    return@withContext Error(failure = DeleteCurrencyRateLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun countAllNetworkStats(params: Params_NetworkStat): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is Params_NetworkStat -> {
                return@withContext try {

                    val numOfNetworkStats: Int = propertyDao.countAllNetworkStats()

                    if (numOfNetworkStats > NUM_OF_NETWORK_STATS_0) {
                        return@withContext Success(data = numOfNetworkStats)
                    } else if (numOfNetworkStats == NUM_OF_NETWORK_STATS_0) {
                        return@withContext Error(failure = NonExistentNetworkStatDataLocalError())
                    }

                    return@withContext Error(failure = NetworkStatLocalError())

                } catch (e: Exception){
                    return@withContext Error(failure = NetworkStatLocalError(cause = e))
                }
            }
            is NetworkStatParams -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }

    }

    override suspend fun getNetworkStats(params: Params_NetworkStat): ResultData<List<NetworkStatModel>> = withContext(ioDispatcher) {

        when(params){
            is None_NetworkStat -> {
                return@withContext try {
                    val entityNetworkStats: List<NetworkStatEntity> = propertyDao.getAllNetworkStats()

                    if (entityNetworkStats.isEmpty())
                        return@withContext Error(failure = NetworkStatListNotAvailableLocalError())

                    val modelNetworkStats = entityNetworkStats.map {
                        networkStatModelMapper.transform(it)
                    }

                    return@withContext Success(modelNetworkStats)

                } catch (e: Exception){
                    return@withContext Error(failure = NetworkStatLocalError(cause = e))
                }
            }
            is NetworkStatParams -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun saveNetworkStat(networkStat: NetworkStatModel): ResultData<Long> = withContext(ioDispatcher) {

        val entityNetworkStat = networkStatEntityMapper.transform(networkStat)

        val tableRowId: Long = propertyDao.insertNetworkStat(entityNetworkStat)

        if (tableRowId < NETWORK_STAT_TABLE_ROW_ID_1)
            return@withContext Error(
                InsertNetworkStatLocalError(message = R.string.error_local_insert_network_stat)
            )

        return@withContext Success(tableRowId)
    }

    override suspend fun deleteNetworkStat(params: Params_NetworkStat): ResultData<Int> = withContext(ioDispatcher) {
        when(params){
            is None_NetworkStat -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is NetworkStatParams -> {
                return@withContext try {

                    val numOfNetworkStatsDeleted: Int = propertyDao.deleteNetworkStat(params.requestMethod!!)

                    if (numOfNetworkStatsDeleted < NUM_OF_NETWORK_STAT_ROWS_DELETED_1) {
                        return@withContext Error(failure = DeleteNetworkStatLocalError(R.string.error_local_delete_network_stat))
                    }

                    return@withContext Success(numOfNetworkStatsDeleted)

                } catch (e: Exception){
                    return@withContext Error(failure = DeleteNetworkStatLocalError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }
}
