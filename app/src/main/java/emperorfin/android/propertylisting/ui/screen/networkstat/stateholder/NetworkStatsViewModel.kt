package emperorfin.android.propertylisting.ui.screen.networkstat.stateholder

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.di.DefaultDispatcher
import emperorfin.android.propertylisting.di.IoDispatcher
import emperorfin.android.propertylisting.domain.datalayer.repository.IPropertyRepository
import emperorfin.android.propertylisting.domain.exception.NetworkStatFailure
import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat.None
import emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat.NetworkStatParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.ResultData
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.Params
import emperorfin.android.propertylisting.domain.uilayer.event.output.succeeded
import emperorfin.android.propertylisting.ui.model.networkstat.NetworkStatUiModelMapper
import emperorfin.android.propertylisting.ui.model.networkstat.NetworkStatUiModel
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
data class NetworkStatsViewModel @Inject constructor(
    val application: Application,
    @IoDispatcher private val coroutineDispatcherIo: CoroutineDispatcher,
    @DefaultDispatcher private val coroutineDispatcherDefault: CoroutineDispatcher,
    private val networkStatUiModelMapper: NetworkStatUiModelMapper,
    private val propertyRepository: IPropertyRepository,
) : ViewModel() {

    companion object {
        private const val NUM_OF_NETWORK_STATS_MINUS_1: Int = -1
        private const val NUM_OF_NETWORK_STATS_0: Int = 0
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    val errorMessage: StateFlow<Int?> = _errorMessage

    private val _messageSnackBar: MutableStateFlow<Int?> = MutableStateFlow(null)
    val messageSnackBar: StateFlow<Int?> = _messageSnackBar

    private val _networkStats: MutableStateFlow<ResultData<List<NetworkStatUiModel>>> =  MutableStateFlow(
        ResultData.Loading)
    val networkStats: StateFlow<ResultData<List<NetworkStatUiModel>>> = _networkStats

    init {

        loadNetworkStats(
            params = NetworkStatParams(requestMethod = "load", duration = 1000L),
            isRefresh = false
        )
    }

    val uiState: StateFlow<NetworkStatsUiState> = combine(
        isLoading, errorMessage, networkStats, messageSnackBar
    ) { isLoading, errorMessage, networkStats, messageSnackBar ->

        when (networkStats) {

            ResultData.Loading -> {
                NetworkStatsUiState(isLoading = true)
            }
            is ResultData.Error -> {
                NetworkStatsUiState(
                    errorMessage = (networkStats.failure as NetworkStatFailure).message,
                    messageSnackBar = messageSnackBar
                )
            }
            is ResultData.Success<List<NetworkStatUiModel>> -> {

                val _networkStats: List<NetworkStatUiModel> = networkStats.data

                val networkStatsSorted = _networkStats.sortedBy { it.requestMethod }

                NetworkStatsUiState(
                    networkStats = networkStatsSorted,
                    isLoading = isLoading,
                    errorMessage = null,
                    messageSnackBar = messageSnackBar
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = NetworkStatsUiState(isLoading = true)
        )

    fun snackBarMessageShown() {
        _messageSnackBar.value = null
    }

    private fun getNetworkStatsViaRepository(
        params: Params,
        isRefresh: Boolean = false,
    ) = viewModelScope.launch(context = coroutineDispatcherIo) {

        _networkStats.value = ResultData.Loading

        val networkStatsResultData: ResultData<List<NetworkStatModel>> =
            propertyRepository.getNetworkStats(params = params, forceUpdate = isRefresh)

        if (networkStatsResultData.succeeded) {

            val networkStatsModel = (networkStatsResultData as ResultData.Success).data

            val networkStatsUiModel: List<NetworkStatUiModel> = networkStatsModel.map {
                networkStatUiModelMapper.transform(it)
            }

            _networkStats.value = ResultData.Success(data = networkStatsUiModel)

        } else {
            val error: ResultData.Error = (networkStatsResultData as ResultData.Error)
            _networkStats.value = error

        }

    }

    private fun searchNetworkStats(params: NetworkStatParams, isRefresh: Boolean = true) {
        getNetworkStatsViaRepository(params = params, isRefresh = isRefresh)
    }

    private fun getAllNetworkStats(params: None = None(), isRefresh: Boolean = false) {
        getNetworkStatsViaRepository(params = params, isRefresh = isRefresh)
    }


    fun loadNetworkStats(params: Params, isRefresh: Boolean) {
        viewModelScope.launch {

            var networkStatsCount by Delegates.notNull<Int>()

            val networkStatsCountDataResultEvent = propertyRepository.countAllNetworkStats(params = None())

            networkStatsCount = if (networkStatsCountDataResultEvent.succeeded)
                (networkStatsCountDataResultEvent as ResultData.Success).data
            else
                NUM_OF_NETWORK_STATS_MINUS_1

            if (networkStatsCount > NUM_OF_NETWORK_STATS_0 || isRefresh){

                if (hasInternetConnection(application)){

                    searchNetworkStats(
                        params = params as NetworkStatParams,
                        isRefresh = true,
                    )
                } else {

                    withContext(Dispatchers.Main){
                        Toast.makeText(
                            application,
                            R.string.message_no_internet_loading_cached_network_stats,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    getAllNetworkStats(
                        params = None(),
                        isRefresh = false
                    )
                }
            } else {

                if (hasInternetConnection(application)){
                    searchNetworkStats(
                        params = params as NetworkStatParams,
                        isRefresh = true
                    )
                } else {

                    _messageSnackBar.value = R.string.message_no_internet_connectivity

                    _networkStats.value = ResultData.Error(
                        failure = NetworkStatFailure.NetworkStatRemoteError(
                            message = R.string.message_no_internet_connectivity
                        )
                    )
                }
            }

        }
    }

}
