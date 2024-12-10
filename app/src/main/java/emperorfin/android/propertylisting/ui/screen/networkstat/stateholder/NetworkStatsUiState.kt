package emperorfin.android.propertylisting.ui.screen.networkstat.stateholder

import emperorfin.android.propertylisting.ui.model.networkstat.NetworkStatUiModel


data class NetworkStatsUiState(
    val networkStats: List<NetworkStatUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: Int? = null,
    val messageSnackBar: Int? = null,
)
