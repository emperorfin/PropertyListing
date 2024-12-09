package emperorfin.android.propertylisting.ui.screen.properties.stateholder

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.di.DefaultDispatcher
import emperorfin.android.propertylisting.di.IoDispatcher
import emperorfin.android.propertylisting.domain.datalayer.repository.IPropertyRepository
import emperorfin.android.propertylisting.domain.exception.PropertyFailure
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModelMapper
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.None
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.PropertyParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.Params
import emperorfin.android.propertylisting.domain.uilayer.event.output.succeeded
import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel
import emperorfin.android.propertylisting.ui.model.property.PropertyUiModelMapper
import emperorfin.android.propertylisting.ui.util.InternetConnectivityUtil.hasInternetConnection
import emperorfin.android.propertylisting.ui.util.WhileUiSubscribed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.properties.Delegates


@HiltViewModel
data class PropertiesViewModel @Inject constructor(
    val application: Application,
    private val propertyRepository: IPropertyRepository,
    private val propertyModelMapper: PropertyModelMapper,
    private val propertyUiModelMapper: PropertyUiModelMapper,
    @IoDispatcher private val coroutineDispatcherIo: CoroutineDispatcher,
    @DefaultDispatcher private val coroutineDispatcherDefault: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        private const val NUM_OF_PROPERTIES_MINUS_1: Int = -1
        private const val NUM_OF_PROPERTIES_0: Int = 0
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    val errorMessage: StateFlow<Int?> = _errorMessage

    private val _messageSnackBar: MutableStateFlow<Int?> = MutableStateFlow(null)
    val messageSnackBar: StateFlow<Int?> = _messageSnackBar

    private val _properties: MutableStateFlow<ResultData<List<PropertyUiModel>>> =  MutableStateFlow(ResultData.Loading)
    val properties: StateFlow<ResultData<List<PropertyUiModel>>> = _properties

    init {

        loadProperties(
            params = PropertyParams(id = -1L),
            isRefresh = false
        )
    }

    val uiState: StateFlow<PropertiesUiState> = combine(
        isLoading, errorMessage, properties, messageSnackBar
    ) { isLoading, errorMessage, properties, messageSnackBar ->

        when (properties) {

            ResultData.Loading -> {
                PropertiesUiState(isLoading = true)
            }
            is ResultData.Error -> {
                PropertiesUiState(
                    errorMessage = (properties.failure as PropertyFailure).message,
                    messageSnackBar = messageSnackBar
                )
            }
            is ResultData.Success<List<PropertyUiModel>> -> {

                val _properties: List<PropertyUiModel> = properties.data

                val propertiesSorted = _properties.sortedBy { it.name }

                PropertiesUiState(
                    properties = propertiesSorted,
                    isLoading = isLoading,
//                    errorMessage = errorMessage,
                    errorMessage = null,
                    messageSnackBar = messageSnackBar
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PropertiesUiState(isLoading = true)
        )

    fun snackBarMessageShown() {
        _messageSnackBar.value = null
    }

    private fun getPropertiesViaRepository(
        params: Params,
        isRefresh: Boolean = false,
    ) = viewModelScope.launch(context = coroutineDispatcherIo) {

        _properties.value = ResultData.Loading

        val propertiesResultData: ResultData<List<PropertyModel>> =
            propertyRepository.getProperties(params = params, forceUpdate = isRefresh)

        if (propertiesResultData.succeeded) {
            val propertiesEntity = (propertiesResultData as ResultData.Success).data

            val propertiesUiModel: List<PropertyUiModel> = propertiesEntity.map {
                propertyModelMapper.transform(it)
            }.map {
                propertyUiModelMapper.transform(it)
            }

            _properties.value = ResultData.Success(data = propertiesUiModel)

        } else {
            val error: ResultData.Error = (propertiesResultData as ResultData.Error)
            _properties.value = error

        }

    }

    private fun searchProperties(params: PropertyParams, isRefresh: Boolean = true) {
        getPropertiesViaRepository(params = params, isRefresh = isRefresh)
    }

    private fun getAllProperties(params: None = None(), isRefresh: Boolean = false) {
        getPropertiesViaRepository(params = params, isRefresh = isRefresh)
    }


    fun loadProperties(params: Params, isRefresh: Boolean) {
        viewModelScope.launch {

            var propertiesCount by Delegates.notNull<Int>()

            val propertiesCountDataResultEvent = propertyRepository.countAllProperties(params = None())

            propertiesCount = if (propertiesCountDataResultEvent.succeeded)
                (propertiesCountDataResultEvent as ResultData.Success).data
            else
                NUM_OF_PROPERTIES_MINUS_1

            if (propertiesCount > NUM_OF_PROPERTIES_0 || isRefresh){

                if (hasInternetConnection(application)){

                    searchProperties(
                        params = params as PropertyParams,
                        isRefresh = true,
                    )
                } else {

                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            application,
                            R.string.message_no_internet_loading_cached_properties,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    getAllProperties(
                        params = None(),
                        isRefresh = false
                    )
                }
            } else {

                if (hasInternetConnection(application)){
                    searchProperties(
                        params = params as PropertyParams,
                        isRefresh = true
                    )
                } else {

                    _messageSnackBar.value = R.string.message_no_internet_connectivity

                    _properties.value = ResultData.Error(
                        failure = PropertyFailure.PropertyRemoteError(
                            message = R.string.message_no_internet_connectivity
                        )
                    )
                }
            }

        }
    }

}
