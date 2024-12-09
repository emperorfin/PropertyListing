package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dtosource

import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED
import emperorfin.android.propertylisting.data.constant.StringConstants.ERROR_MESSAGE_NOT_YET_IMPLEMENTED
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dto.currencyrate.CurrencyRateDataTransferObject
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dto.property.PropertyDataTransferObject
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dto.property.PropertyDataTransferObjectMapper
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.currencyrates.CurrencyRatesResponse
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.PropertiesResponse
import emperorfin.android.propertylisting.di.IoDispatcher
import emperorfin.android.propertylisting.di.MainDispatcher
import emperorfin.android.propertylisting.di.RemotePropertyDao
import emperorfin.android.propertylisting.domain.datalayer.dao.IPropertyDao
import emperorfin.android.propertylisting.domain.datalayer.datasource.PropertyDataSource
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.GetCurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure.CurrencyRateRemoteError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.GetPropertyRemoteError
import emperorfin.android.propertylisting.domain.exception.PropertyFailure.PropertyRemoteError
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModelMapper
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModelMapper
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.PropertyParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData.Success
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData.Error
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.None as None_CurrencyRate
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.None as None_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params as Params_Property
import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.Params as Params_CurrencyRate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

data class PropertyRemoteDataSource @Inject constructor(
    @RemotePropertyDao private val propertyDao: IPropertyDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val propertyModelMapper: PropertyModelMapper,
    private val currencyRateModelMapper: CurrencyRateModelMapper
) : PropertyDataSource {

    override suspend fun getProperties(params: Params_Property): ResultData<List<PropertyModel>> = withContext(ioDispatcher) {
        when(params){
            is None_Property -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is PropertyParams -> {

                return@withContext try {

                    val response: Response<PropertiesResponse> = propertyDao.getProperties() as Response<PropertiesResponse>

                    withContext(mainDispatcher){
                        if (response.isSuccessful){

                            response.body()?.let {

                                val propertiesModel: List<PropertyModel> =
                                    buildPropertyModelList(response = it)

                                return@withContext Success(propertiesModel)
                            }
                        }

                        return@withContext Error(failure = GetPropertyRemoteError())
                    }

                } catch (e: Exception){
                    return@withContext Error(failure = PropertyRemoteError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    override suspend fun getCurrencyRates(params: Params_CurrencyRate): ResultData<List<CurrencyRateModel>> = withContext(ioDispatcher) {
        when(params){
            is None_CurrencyRate -> {
                throw IllegalArgumentException(ERROR_MESSAGE_INAPPROPRIATE_ARGUMENT_PASSED)
            }
            is CurrencyRateParams -> {

                return@withContext try {

                    val response: Response<CurrencyRatesResponse> =
                        propertyDao.getCurrencyRates() as Response<CurrencyRatesResponse>

                    withContext(mainDispatcher){
                        if (response.isSuccessful){

                            val responseBody: CurrencyRatesResponse? = response.body()

                            responseBody?.let {
                                val currencyRatesModel: List<CurrencyRateModel> =
                                    buildCurrencyRateModelList(base = it.base, exchangesRates = it.rates)

                                return@withContext Success(currencyRatesModel)
                            }
                        }

                        return@withContext Error(failure = GetCurrencyRateRemoteError())
                    }

                } catch (e: Exception){
                    return@withContext Error(failure = CurrencyRateRemoteError(cause = e))
                }
            }
            else -> throw NotImplementedError(ERROR_MESSAGE_NOT_YET_IMPLEMENTED)
        }
    }

    private fun buildPropertyModelList(response: PropertiesResponse): List<PropertyModel> {

        val propertiesResponse = response.properties

        val propertiesDto = mutableListOf<PropertyDataTransferObject>()

        propertiesResponse.forEach { property ->

            val propertyDto = PropertyDataTransferObject.newInstance(
                id = property.id,
                name = property.name,
                overview = property.overview,
                isFeatured = property.isFeatured,
                lowestPricePerNightInEuros = property.lowestPricePerNight?.value?.toDouble(),
                rating = property.starRating,
                overallRating = property.overallRating?.overall,
                city = response.location?.city?.name,
                country = response.location?.city?.country,
            )

            propertiesDto.add(propertyDto)

        }

        return propertiesDto.map {
            propertyModelMapper.transform(it)
        }

    }

    private fun buildCurrencyRateModelList(
        base: String?, exchangesRates: Map<String, Number>
    ): List<CurrencyRateModel> {
        val currencyRatesDto = mutableListOf<CurrencyRateDataTransferObject>()

        exchangesRates.forEach {

            val currencySymbolOther: String = it.key
            val rate: Double = it.value.toDouble()

            val currencyRate = CurrencyRateDataTransferObject.newInstance(
                currencySymbolBase = base,
                currencySymbolOther = currencySymbolOther,
                rate = rate
            )

            currencyRatesDto.add(currencyRate)
        }

        return currencyRatesDto.map {
            currencyRateModelMapper.transform(it)
        }
    }


    // The following overridden functions throw exceptions since there's no corresponding webservice
    // API endpoints.

    override suspend fun countAllProperties(params: Params_Property): ResultData<Int> =
        TODO("Not yet implemented")

    override suspend fun saveProperty(property: PropertyModel): ResultData<Long> =
        TODO("Not yet implemented")

    override suspend fun deleteProperty(params: Params_Property): ResultData<Int> =
        TODO("Not yet implemented")

    override suspend fun countAllCurrencyRates(params: Params_CurrencyRate): ResultData<Int> =
        TODO("Not yet implemented")

    override suspend fun countCurrencyRates(params: Params_CurrencyRate): ResultData<Int> =
        TODO("Not yet implemented")

    override suspend fun saveCurrencyRates(currencyRatesModel: List<CurrencyRateModel>): ResultData<List<Long>> =
        TODO("Not yet implemented")

    override suspend fun deleteCurrencyRates(params: Params_CurrencyRate): ResultData<Int> =
        TODO("Not yet implemented")
}
