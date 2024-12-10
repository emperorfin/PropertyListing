package emperorfin.android.propertylisting.ui.screen.networkstat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat.NetworkStatParams
import emperorfin.android.propertylisting.ui.component.AppBar
import emperorfin.android.propertylisting.ui.component.ContentLoader
import emperorfin.android.propertylisting.ui.component.EmptyContent
import emperorfin.android.propertylisting.ui.component.LoadingIndicator
import emperorfin.android.propertylisting.ui.model.networkstat.NetworkStatUiModel
import emperorfin.android.propertylisting.ui.navigation.NavigationActions
import emperorfin.android.propertylisting.ui.screen.networkstat.stateholder.NetworkStatsUiState
import emperorfin.android.propertylisting.ui.screen.networkstat.stateholder.NetworkStatsViewModel


@Composable
fun NetworkStatsScreen(
    modifier: Modifier = Modifier,
    navigationActions: NavigationActions?,
    viewModel: NetworkStatsViewModel = hiltViewModel(),
) {

    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            AppBar(
                title = stringResource(R.string.screen_title_network_stats),
                onBackPress = {
                    navigationActions?.navigateBack()
                }
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->

        Content(
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel,
            uiState = uiState
        )

        // Check for SnackBar messages to display on the screen
        uiState.messageSnackBar?.let { message ->
            val snackBarText = stringResource(message)

            LaunchedEffect(snackBarHostState, viewModel, message, snackBarText) {
                snackBarHostState.showSnackbar(message = snackBarText)
                viewModel.snackBarMessageShown()
            }
        }

    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewModel: NetworkStatsViewModel,
    uiState: NetworkStatsUiState
) {

    val networkStats = uiState.networkStats
    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage

    // This is just for simulating the value/content state.
    val networkStatsSample = listOf(
        NetworkStatUiModel.newInstance("POST", 100L),
        NetworkStatUiModel.newInstance("GET", 120L),
    )

    Column {
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.network_stats_screen_spacer_height_58)))
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.network_stats_screen_spacer_height_58)))

        ContentLoader(
            loading = isLoading,
            empty = networkStats.isEmpty() && !isLoading,
//            empty = networkStatsSample.isEmpty() && !isLoading, // This is just for simulating the value/content state.
            emptyContent = {
                EmptyContent(
                    errorLabel = errorMessage ?: R.string.content_description_error_message,
                    onRetry = {

                        viewModel.loadNetworkStats(
                            params = NetworkStatParams(requestMethod = "load", duration = 1000L),
                            isRefresh = false
                        )
                    }
                )
            },
            loadingIndicator = {
                LoadingIndicator(modifier = modifier)
            }
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                networkStats.let {
                    for (networkStat in it) {
                        Text(
                            text = "Action: ${networkStat.requestMethod} | Duration: ${networkStat.duration}ms",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // This is just for simulating the value/content state.
//                networkStatsSample.let {
//                    for (networkStat in it) {
//                        Text(
//                            text = "Action: ${networkStat.requestMethod} | Duration: ${networkStat.duration}ms",
//                            style = MaterialTheme.typography.bodyMedium,
//                            textAlign = TextAlign.Center,
//                            overflow = TextOverflow.Ellipsis,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }

            }

        }

    }
}