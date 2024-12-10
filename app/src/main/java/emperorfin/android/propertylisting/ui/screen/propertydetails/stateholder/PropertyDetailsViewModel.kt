package emperorfin.android.propertylisting.ui.screen.propertydetails.stateholder

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.di.DefaultDispatcher
import emperorfin.android.propertylisting.di.IoDispatcher
import emperorfin.android.propertylisting.domain.datalayer.repository.IPropertyRepository
import emperorfin.android.propertylisting.domain.exception.CurrencyRateFailure
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
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
data class PropertyDetailsViewModel @Inject constructor(
    val application: Application,
    @IoDispatcher private val coroutineDispatcherIo: CoroutineDispatcher,
    @DefaultDispatcher private val coroutineDispatcherDefault: CoroutineDispatcher,
    private val propertyUiModelMapper: PropertyUiModelMapper,
    private val propertyRepository: IPropertyRepository,
) : ViewModel() {

    companion object {

        private const val NUM_OF_CURRENCY_RATES_MINUS_1: Int = -1
        private const val NUM_OF_CURRENCY_RATES_0: Int = 0
    }

    private val _isLoading = MutableStateFlow(false)

    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _messageSnackBar: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val messageSnackBar: StateFlow<Int?> = _messageSnackBar

    private val _property: MutableStateFlow<ResultData<PropertyUiModel>> =
        MutableStateFlow(ResultData.Loading)
    private val property: StateFlow<ResultData<PropertyUiModel>> = _property

    private lateinit var propertyOld: PropertyUiModel

    val uiState: StateFlow<PropertyDetailsUiState> = combine(
        _isLoading, _errorMessage, property, messageSnackBar,
    ) { isLoading, errorMessage, property, messageSnackBar ->
        when (property) {
            ResultData.Loading -> {
                PropertyDetailsUiState(isLoading = true)
            }

            is ResultData.Error -> {
                PropertyDetailsUiState(
                    errorMessage = (property.failure as CurrencyRateFailure).message,
                    messageSnackBar = messageSnackBar
                )
            }

            is ResultData.Success<PropertyUiModel> -> {

                PropertyDetailsUiState(
                    property = property.data,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    messageSnackBar = messageSnackBar
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PropertyDetailsUiState(isLoading = true)
        )

    fun snackBarMessageShown() {
        _messageSnackBar.value = null
    }

    fun setPropertyOld(propertyOld: PropertyUiModel?) {
        this.propertyOld = propertyOld!!
    }

    private fun getCurrencyRatesViaRepository(
        params: CurrencyRateParams,
        isRefresh: Boolean,
    ) = viewModelScope.launch(context = coroutineDispatcherIo) {

        _property.value = ResultData.Loading

        val currencyRatesResultData: ResultData<List<CurrencyRateModel>> =
            propertyRepository.getCurrencyRates(params = params, forceUpdate = isRefresh)

        if (currencyRatesResultData.succeeded) {
            val currencyRatesModel = (currencyRatesResultData as ResultData.Success).data

            val property = propertyUiModelMapper.transform(
                property = propertyOld,
                currencyRates = currencyRatesModel
            )

            _property.value = ResultData.Success(data = property)
        } else {
            val error: ResultData.Error = (currencyRatesResultData as ResultData.Error)
            _property.value = error
        }

    }

    fun loadCurrencyRates(
        params: CurrencyRateParams,
        isRefresh: Boolean
    ) {
        viewModelScope.launch {

            var currencyRatesCount by Delegates.notNull<Int>()

            val currencyRatesCountDataResultEvent =
                propertyRepository.countCurrencyRates(params = params)

            currencyRatesCount = if (currencyRatesCountDataResultEvent.succeeded)
                (currencyRatesCountDataResultEvent as ResultData.Success).data
            else
                NUM_OF_CURRENCY_RATES_MINUS_1

            if (currencyRatesCount > NUM_OF_CURRENCY_RATES_0 || isRefresh) {

                if (hasInternetConnection(application)) {

                    getCurrencyRatesViaRepository(
                        params = params,
                        isRefresh = true
                    )
                } else {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            application,
                            R.string.message_no_internet_loading_cached_currency_rates,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    getCurrencyRatesViaRepository(
                        params = params,
                        isRefresh = false
                    )
                }
            } else {

                if (hasInternetConnection(application)) {
                    getCurrencyRatesViaRepository(
                        params = params,
                        isRefresh = true
                    )
                } else {

                    _messageSnackBar.value = R.string.message_no_internet_connectivity

                    _property.value = ResultData.Error(
                        failure = CurrencyRateFailure.CurrencyRateRemoteError(
                            message = R.string.message_no_internet_connectivity
                        )
                    )
                }
            }

        }
    }

}
