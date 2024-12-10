package emperorfin.android.propertylisting.data.repository

import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.di.IoDispatcher
import emperorfin.android.propertylisting.di.PropertyLocalDataSourceAnnotation
import emperorfin.android.propertylisting.di.PropertyRemoteDataSourceAnnotation
import emperorfin.android.propertylisting.domain.datalayer.datasource.PropertyDataSource
import emperorfin.android.propertylisting.domain.datalayer.repository.IPropertyRepository
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateLocalError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRepositoryError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.NetworkStatListNotAvailableRepositoryError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.NetworkStatRemoteError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.GetNetworkStatRepositoryError
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure.NetworkStatLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.GetPropertyRepositoryError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.PropertyListNotAvailableRepositoryError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.PropertyLocalError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.PropertyRemoteError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat.NetworkStatParams
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.PropertyParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData.Success
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData.Error
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.Params as Params_NetworkStat
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params as Params_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject


data class PropertyRepository @Inject constructor(
    @PropertyLocalDataSourceAnnotation private val propertyLocalDataSource: PropertyDataSource,
    @PropertyRemoteDataSourceAnnotation private val propertyRemoteDataSource: PropertyDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IPropertyRepository {

    private var cachedProperties: ConcurrentMap<String, PropertyModel>? = null
    private var cachedCurrencyRates: ConcurrentMap<String, List<CurrencyRateModel>>? = null
    private var cachedNetworkStats: ConcurrentMap<String, NetworkStatModel>? = null

    override suspend fun countAllProperties(
        params: Params_Property, countRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {
        if (countRemotely) {
            return@withContext propertyRemoteDataSource.countAllProperties(params = params)
        } else {
            return@withContext propertyLocalDataSource.countAllProperties(params = params)
        }
    }

    override suspend fun getProperties(
        params: Params_Property, forceUpdate: Boolean
    ): ResultData<List<PropertyModel>> {
        return withContext(ioDispatcher) {
            // Respond immediately with cache if available and not dirty
            if (!forceUpdate) {
                cachedProperties?.let { properties ->
                    return@withContext Success(properties.values.sortedBy { it.name })
                }
            }

            val newProperties: ResultData<List<PropertyModel>> =
                fetchPropertiesFromRemoteOrLocal(params = params, forceUpdate = forceUpdate)

            // Refresh the cache with the new properties
            (newProperties as? Success)?.let { refreshPropertyCache(it.data) }

            cachedProperties?.values?.let { properties ->
                return@withContext Success(properties.sortedBy { it.name })
            }

            (newProperties as? Success)?.let {
                if (it.data.isNotEmpty()) {
                    return@withContext Success(it.data)
                } else {
                    return@withContext Error(
                        failure = PropertyListNotAvailableRepositoryError()
                    )
                }
            }

            return@withContext newProperties as Error
        }
    }

    override suspend fun saveProperty(
        property: PropertyModel, saveRemotely: Boolean
    ): ResultData<Long> = withContext(ioDispatcher) {

        if (saveRemotely) {
            return@withContext propertyRemoteDataSource.saveProperty(property = property)
        } else {
            return@withContext propertyLocalDataSource.saveProperty(property = property)
        }

    }

    override suspend fun deleteProperty(
        params: Params_Property, deleteRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {

        if (deleteRemotely) {
            return@withContext propertyRemoteDataSource.deleteProperty(params = params)
        } else {
            return@withContext propertyLocalDataSource.deleteProperty(params = params)
        }
    }

    private suspend fun fetchPropertiesFromRemoteOrLocal(
        params: Params_Property, forceUpdate: Boolean
    ): ResultData<List<PropertyModel>> {

        var isRemoteException = false

        // Remote first
        if (forceUpdate) {
            when (val propertiesRemote = propertyRemoteDataSource.getProperties(params = params)) {
                is Error -> {
                    if (propertiesRemote.failure is PropertyRemoteError)
                        isRemoteException = true
                }
                is Success -> {
                    refreshPropertyLocalDataSource(properties = propertiesRemote.data)

                    return propertiesRemote
                }
                else -> {}
            }
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            if (isRemoteException)
                return Error(
                    GetPropertyRepositoryError(
                        message = R.string.exception_occurred_remote
                    )
                )

            return Error(
                GetPropertyRepositoryError(
                    message = R.string.error_cant_force_refresh_properties_remote_data_source_unavailable
                )
            )
        }

        // Local if remote fails
        val propertiesLocal = propertyLocalDataSource.getProperties(params = params)

        if (propertiesLocal is Success) return propertiesLocal

        if ((propertiesLocal as Error).failure is PropertyLocalError)
            return Error(
                GetPropertyRepositoryError(
                    R.string.exception_occurred_local
                )
            )

        return Error(
            GetPropertyRepositoryError(
                R.string.error_fetching_from_remote_and_local
            )
        )
    }

    private fun refreshPropertyCache(properties: List<PropertyModel>) {
        cachedProperties?.clear()

        properties.sortedBy { it.name }.forEach {
            cachePropertyAndPerform(it) {}
        }
    }

    private suspend fun refreshPropertyLocalDataSource(properties: List<PropertyModel>) {

        return // TODO: REMOVE THIS LINE TO REFRESH LOCAL DATA SOURCE

        properties.forEach {
            val params = PropertyParams(id = it.id)

            propertyLocalDataSource.deleteProperty(params = params)

            propertyLocalDataSource.saveProperty(property = it)
        }
    }

    private fun cacheProperty(property: PropertyModel): PropertyModel {

        val cachedProperty = PropertyModel.newInstance(
            id = property.id,
            name = property.name,
            overview = property.overview,
            isFeatured = property.isFeatured,
            lowestPricePerNightInEuros = property.lowestPricePerNightInEuros,
            rating = property.rating,
            overallRating = property.overallRating,
            city = property.city,
            country = property.country,
            imageUrl = property.imageUrl,
        )

        // Create if it doesn't exist.
        if (cachedProperties == null) {
            cachedProperties = ConcurrentHashMap()
        }

        cachedProperties?.put(cachedProperty.id.toString(), cachedProperty)

        return cachedProperty
    }

    private inline fun cachePropertyAndPerform(property: PropertyModel, perform: (PropertyModel) -> Unit) {

        val cachedProperty = cacheProperty(property)

        perform(cachedProperty)
    }

    override suspend fun countAllCurrencyRates(
        params: Params_CurrencyRate, countRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {
        if (countRemotely) {
            return@withContext propertyRemoteDataSource.countAllCurrencyRates(params = params)
        } else {
            return@withContext propertyLocalDataSource.countAllCurrencyRates(params = params)
        }
    }

    override suspend fun countCurrencyRates(
        params: Params_CurrencyRate, countRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {

        if (countRemotely) {
            return@withContext propertyRemoteDataSource.countCurrencyRates(params = params)
        } else {
            return@withContext propertyLocalDataSource.countCurrencyRates(params = params)
        }
    }

    override suspend fun getCurrencyRates(
        params: Params_CurrencyRate, forceUpdate: Boolean
    ): ResultData<List<CurrencyRateModel>> = withContext(ioDispatcher) {

        if (!forceUpdate) {
            cachedCurrencyRates?.let {
                val cachedCurrencyRates: MutableCollection<List<CurrencyRateModel>> = it.values

                if (cachedCurrencyRates.isNotEmpty()) {
                    return@withContext Success(cachedCurrencyRates.first())
                }
            }
        }

        val newCurrencyRates: ResultData<List<CurrencyRateModel>> =
            fetchCurrencyRatesFromRemoteOrLocal(params = params, forceUpdate = forceUpdate)

        (newCurrencyRates as? Success)?.let { refreshCurrencyRateCache(it.data) }

        cachedCurrencyRates?.values?.let {
            val currencyRates: MutableCollection<List<CurrencyRateModel>> = it

            if (currencyRates.isNotEmpty()) {
                return@withContext Success(currencyRates.first())
            }
        }

        (newCurrencyRates as? Success)?.let {
            return@withContext it
        }

        return@withContext newCurrencyRates as Error
    }

    override suspend fun saveCurrencyRates(
        currencyRatesModel: List<CurrencyRateModel>, saveRemotely: Boolean
    ): ResultData<List<Long>> = withContext(ioDispatcher) {

        if (saveRemotely) {
            return@withContext propertyRemoteDataSource.saveCurrencyRates(currencyRatesModel = currencyRatesModel)
        } else {
            return@withContext propertyLocalDataSource.saveCurrencyRates(currencyRatesModel = currencyRatesModel)
        }

    }

    override suspend fun deleteCurrencyRates(
        params: Params_CurrencyRate, deleteRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {

        if (deleteRemotely) {
            return@withContext propertyRemoteDataSource.deleteCurrencyRates(params = params)
        } else {
            return@withContext propertyLocalDataSource.deleteCurrencyRates(params = params)
        }
    }

    private suspend fun fetchCurrencyRatesFromRemoteOrLocal(
        params: Params_CurrencyRate, forceUpdate: Boolean
    ): ResultData<List<CurrencyRateModel>> {
        var isRemoteException = false

        if (forceUpdate) {
            when (val currencyRatesRemote = propertyRemoteDataSource.getCurrencyRates(params = params)) {
                is Error -> {
                    if (currencyRatesRemote.failure is CurrencyRateRemoteError)
                        isRemoteException = true
                }
                is Success -> {
                    refreshCurrencyRateLocalDataSource(params = params, currencyRates = currencyRatesRemote.data)

                    return currencyRatesRemote
                }
                else -> {}
            }
        }

        if (forceUpdate) {
            if (isRemoteException)
                return Error(
                    GetCurrencyRateRepositoryError(
                        message = R.string.exception_occurred_remote
                    )
                )

            return Error(
                GetCurrencyRateRemoteError(
                    message = R.string.error_cant_force_refresh_currency_rates_remote_data_source_unavailable
                )
            )
        }

        val currencyRatesLocal = propertyLocalDataSource.getCurrencyRates(params = params)

        if (currencyRatesLocal is Success) return currencyRatesLocal

        if ((currencyRatesLocal as Error).failure is CurrencyRateLocalError)
            return Error(
                GetCurrencyRateRepositoryError(
                    R.string.exception_occurred_local
                )
            )

        return Error(
            GetCurrencyRateRepositoryError(
                R.string.error_fetching_from_remote_and_local
            )
        )
    }

    private fun refreshCurrencyRateCache(currencyRates: List<CurrencyRateModel>) {
        cachedCurrencyRates?.clear()

        currencyRates.sortedBy { it.currencySymbolOther }.apply {
            cacheCurrencyRateAndPerform(currencyRates = this) {}
        }
    }

    private suspend fun refreshCurrencyRateLocalDataSource(
        params: Params_CurrencyRate, currencyRates: List<CurrencyRateModel>
    ) {
        propertyLocalDataSource.deleteCurrencyRates(params = params)

        propertyLocalDataSource.saveCurrencyRates(currencyRatesModel = currencyRates)
    }

    private fun cacheCurrencyRates(currencyRates: List<CurrencyRateModel>): List<CurrencyRateModel> {

        val cachedCurrencyRatesNew = mutableListOf<CurrencyRateModel>()

        currencyRates.forEach {
            val currencyRate = CurrencyRateModel.newInstance(
                currencySymbolBase = it.currencySymbolBase,
                currencySymbolOther = it.currencySymbolOther,
                rate = it.rate,
                id = it.id,
            )

            cachedCurrencyRatesNew.add(currencyRate)
        }

        if (cachedCurrencyRates == null) {
            cachedCurrencyRates = ConcurrentHashMap()
        }
        cachedCurrencyRates?.put(cachedCurrencyRatesNew.first().currencySymbolBase, cachedCurrencyRatesNew)

        return cachedCurrencyRatesNew
    }

    private inline fun cacheCurrencyRateAndPerform(currencyRates: List<CurrencyRateModel>, perform: (List<CurrencyRateModel>) -> Unit) {
        val cachedCurrencyRates = cacheCurrencyRates(currencyRates = currencyRates)

        perform(cachedCurrencyRates)
    }

    override suspend fun countAllNetworkStats(
        params: Params_NetworkStat, countRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {
        if (countRemotely) {
            return@withContext propertyRemoteDataSource.countAllNetworkStats(params = params)
        } else {
            return@withContext propertyLocalDataSource.countAllNetworkStats(params = params)
        }
    }

    override suspend fun getNetworkStats(
        params: Params_NetworkStat, forceUpdate: Boolean
    ): ResultData<List<NetworkStatModel>> {
        return withContext(ioDispatcher) {
            if (!forceUpdate) {
                cachedNetworkStats?.let { networkStats ->
                    return@withContext Success(networkStats.values.sortedBy { it.requestMethod })
                }
            }

            val newNetworkStats: ResultData<List<NetworkStatModel>> =
                fetchNetworkStatsFromRemoteOrLocal(params = params, forceUpdate = forceUpdate)

            (newNetworkStats as? Success)?.let { refreshNetworkStatCache(it.data) }

            cachedNetworkStats?.values?.let { networkStats ->
                return@withContext Success(networkStats.sortedBy { it.requestMethod })
            }

            (newNetworkStats as? Success)?.let {
                if (it.data.isNotEmpty()) {
                    return@withContext Success(it.data)
                } else {
                    return@withContext Error(
                        failure = NetworkStatListNotAvailableRepositoryError()
                    )
                }
            }

            return@withContext newNetworkStats as Error
        }
    }

    override suspend fun saveNetworkStat(
        networkStat: NetworkStatModel, saveRemotely: Boolean
    ): ResultData<Long> = withContext(ioDispatcher) {

        if (saveRemotely) {
            return@withContext propertyRemoteDataSource.saveNetworkStat(networkStat = networkStat)
        } else {
            return@withContext propertyLocalDataSource.saveNetworkStat(networkStat = networkStat)
        }

    }

    override suspend fun deleteNetworkStat(
        params: Params_NetworkStat, deleteRemotely: Boolean
    ): ResultData<Int> = withContext(ioDispatcher) {

        if (deleteRemotely) {
            return@withContext propertyRemoteDataSource.deleteNetworkStat(params = params)
        } else {
            return@withContext propertyLocalDataSource.deleteNetworkStat(params = params)
        }
    }

    private suspend fun fetchNetworkStatsFromRemoteOrLocal(
        params: Params_NetworkStat, forceUpdate: Boolean
    ): ResultData<List<NetworkStatModel>> {

        var isRemoteException = false

        if (forceUpdate) {
            when (val networkStatsRemote = propertyRemoteDataSource.getNetworkStats(params = params)) {
                is Error -> {
                    if (networkStatsRemote.failure is NetworkStatRemoteError)
                        isRemoteException = true
                }
                is Success -> {
                    refreshNetworkStatLocalDataSource(networkStats = networkStatsRemote.data)

                    return networkStatsRemote
                }
                else -> {}
            }
        }

        if (forceUpdate) {
            if (isRemoteException)
                return Error(
                    GetNetworkStatRepositoryError(
                        message = R.string.exception_occurred_remote
                    )
                )

            return Error(
                GetNetworkStatRepositoryError(
                    message = R.string.error_cant_force_refresh_network_stats_remote_data_source_unavailable
                )
            )
        }

        val networkStatsLocal = propertyLocalDataSource.getNetworkStats(params = params)

        if (networkStatsLocal is Success) return networkStatsLocal

        if ((networkStatsLocal as Error).failure is NetworkStatLocalError)
            return Error(
                GetNetworkStatRepositoryError(
                    R.string.exception_occurred_local
                )
            )

        return Error(
            GetNetworkStatRepositoryError(
                R.string.error_fetching_from_remote_and_local
            )
        )
    }

    private fun refreshNetworkStatCache(networkStats: List<NetworkStatModel>) {
        cachedNetworkStats?.clear()

        networkStats.sortedBy { it.requestMethod }.forEach {
            cacheNetworkStatAndPerform(it) {}
        }
    }

    private suspend fun refreshNetworkStatLocalDataSource(networkStats: List<NetworkStatModel>) {

        return // TODO: REMOVE THIS LINE TO REFRESH LOCAL DATA SOURCE

        networkStats.forEach {
            val params = NetworkStatParams(requestMethod = it.requestMethod, duration = it.duration)

            propertyLocalDataSource.deleteNetworkStat(params = params)

            propertyLocalDataSource.saveNetworkStat(networkStat = it)
        }
    }

    private fun cacheNetworkStat(networkStat: NetworkStatModel): NetworkStatModel {

        val cachedNetworkStat = NetworkStatModel.newInstance(
            requestMethod = networkStat.requestMethod,
            duration = networkStat.duration,
        )

        if (cachedNetworkStats == null) {
            cachedNetworkStats = ConcurrentHashMap()
        }

        cachedNetworkStats?.put(cachedNetworkStat.requestMethod, cachedNetworkStat)

        return cachedNetworkStat
    }

    private inline fun cacheNetworkStatAndPerform(networkStat: NetworkStatModel, perform: (NetworkStatModel) -> Unit) {

        val cachedNetworkStat = cacheNetworkStat(networkStat)

        perform(cachedNetworkStat)
    }

}
