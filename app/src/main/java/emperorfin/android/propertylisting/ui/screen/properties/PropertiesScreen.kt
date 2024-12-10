package emperorfin.android.propertylisting.ui.screen.properties

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.squareup.moshi.Moshi
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.domain.uilayer.event.input.property.PropertyParams
import emperorfin.android.propertylisting.ui.component.AppBar
import emperorfin.android.propertylisting.ui.component.ContentLoader
import emperorfin.android.propertylisting.ui.component.EmptyContent
import emperorfin.android.propertylisting.ui.component.LoadingIndicator
import emperorfin.android.propertylisting.ui.component.PropertiesScreenMenu
import emperorfin.android.propertylisting.ui.component.PropertyListItem
import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel
import emperorfin.android.propertylisting.ui.navigation.Destinations.ROUTE_PROPERTY_DETAILS
import emperorfin.android.propertylisting.ui.navigation.NavigationActions
import emperorfin.android.propertylisting.ui.navigation.ScreenArgs
import emperorfin.android.propertylisting.ui.screen.properties.stateholder.PropertiesUiState
import emperorfin.android.propertylisting.ui.screen.properties.stateholder.PropertiesViewModel
import emperorfin.android.propertylisting.ui.util.InternetConnectivityUtil.hasInternetConnection


@Composable
fun PropertiesScreen(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    navigationActions: NavigationActions?,
    viewModel: PropertiesViewModel = hiltViewModel(),
) {

    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            AppBar(
                title = stringResource(R.string.app_name),
                actions = {
                    PropertiesScreenMenu(
                        onNetworkStat = {
                            navigationActions?.navigateToNetworkStatsScreen()
                        }
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->

        Content(
            context = context,
            modifier = Modifier.padding(paddingValues),
            navigationActions = navigationActions,
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
    navigationActions: NavigationActions?,
    context: Context,
    viewModel: PropertiesViewModel,
    uiState: PropertiesUiState
) {

    val lazyListState = rememberLazyGridState()

    val properties = uiState.properties
    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage

    Column {
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.properties_screen_spacer_height_58)))

        ContentLoader(
            loading = isLoading,
            empty = properties.isEmpty() && !isLoading,
            emptyContent = {
                EmptyContent(
                    errorLabel = errorMessage ?: R.string.content_description_error_message,
                    onRetry = {

                        viewModel.loadProperties(
                            params = PropertyParams(id = -1L),
                            isRefresh = false
                        )
                    }
                )
            },
            loadingIndicator = {
                LoadingIndicator(modifier = modifier)
            }
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(count = integerResource(id = R.integer.properties_screen_lazy_vertical_grid_column_count_2)),
                state = lazyListState,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
            ) {

                itemsIndexed(properties) { _, property ->

                    PropertyListItem(
                        property = property,
                        onClick = {
                            // TODO: Remove this restriction once the property details could be viewed offline.
                            if (!hasInternetConnection(context.applicationContext as Application)){
                                Toast.makeText(context, R.string.message_no_internet_connectivity, Toast.LENGTH_SHORT).show()

                                return@PropertyListItem
                            }

                            val moshi = Moshi.Builder().build()
                            val jsonAdapter = moshi.adapter(PropertyUiModel::class.java).lenient()
                            val propertyJson = jsonAdapter.toJson(it)

                            navigationActions?.navigateToPropertyDetailsScreen(
                                ROUTE_PROPERTY_DETAILS.replace("{${ScreenArgs.SCREEN_PROPERTY_DETAILS}}", propertyJson)
                            )
                        }
                    )
                }

            }

        }

    }
}