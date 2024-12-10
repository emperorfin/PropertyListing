package emperorfin.android.propertylisting.ui.screen.propertydetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.palette.graphics.Palette
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.palette.BitmapPalette
import emperorfin.android.propertylisting.R
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_SYMBOL_EUR
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_SYMBOL_GBP
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_SYMBOL_USD
import emperorfin.android.propertylisting.domain.constant.StringConstants.EMPTY
import emperorfin.android.propertylisting.domain.constant.StringConstants.QUESTION_MARK
import emperorfin.android.propertylisting.domain.uilayer.event.input.currencyrate.CurrencyRateParams
import emperorfin.android.propertylisting.ui.component.AppBar
import emperorfin.android.propertylisting.ui.component.ContentLoader
import emperorfin.android.propertylisting.ui.component.EmptyContent
import emperorfin.android.propertylisting.ui.component.LoadingIndicator
import emperorfin.android.propertylisting.ui.component.NetworkImage
import emperorfin.android.propertylisting.ui.component.PropertyDetailsScreenMenu
import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel
import emperorfin.android.propertylisting.ui.navigation.NavigationActions
import emperorfin.android.propertylisting.ui.screen.propertydetails.stateholder.PropertyDetailsUiState
import emperorfin.android.propertylisting.ui.screen.propertydetails.stateholder.PropertyDetailsViewModel
import emperorfin.android.propertylisting.ui.theme.Background


@Composable
fun PropertyDetailsScreen(
    modifier: Modifier = Modifier,
    navigationActions: NavigationActions?,
    property: PropertyUiModel?,
    viewModel: PropertyDetailsViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    var currencyPrice by remember {
        mutableStateOf(
            property?.lowestPricePerNightInEurosWithCurrencySymbol ?:
            "$CURRENCY_SYMBOL_EUR$QUESTION_MARK"
        )
    }

    val uiState by viewModel.uiState.collectAsState()

    val propertyNew = uiState.property

    LaunchedEffect(key1 = property) {

        viewModel.setPropertyOld(propertyOld = property)

        viewModel.loadCurrencyRates(
            params = CurrencyRateParams(),
            isRefresh = false
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {

            var propertyName = EMPTY

            if (propertyNew != null) {
                propertyName = propertyNew.name.ifEmpty {
                    propertyNew.overview
                }
            }

            AppBar(
                title = propertyName,
                onBackPress = {
                    navigationActions?.navigateBack()
                },
                actions = {
                    PropertyDetailsScreenMenu(
                        onEurosPriceClick = {
                            currencyPrice =
                                propertyNew?.lowestPricePerNightInEurosWithCurrencySymbol ?:
                                "$CURRENCY_SYMBOL_EUR$QUESTION_MARK"
                        },
                        onDollarsPriceClick = {
                            currencyPrice =
                                propertyNew?.lowestPricePerNightInDollarsWithCurrencySymbol ?:
                                "$CURRENCY_SYMBOL_USD$QUESTION_MARK"
                        },
                        onPoundsPriceClick = {
                            currencyPrice =
                                propertyNew?.lowestPricePerNightInPoundsWithCurrencySymbol ?:
                                "$CURRENCY_SYMBOL_GBP$QUESTION_MARK"
                        },
                    )
                }
            )
        },
        modifier = modifier.fillMaxSize(),
        containerColor = Background
    ) { paddingValues ->

        Content(
            modifier = Modifier.padding(paddingValues),
            property = propertyNew,
            viewModel = viewModel,
            uiState = uiState,
            currencyPrice = currencyPrice
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
    property: PropertyUiModel?,
    viewModel: PropertyDetailsViewModel,
    uiState: PropertyDetailsUiState,
    currencyPrice: String
) {

    val isLoading = uiState.isLoading
    val errorMessage = uiState.errorMessage

    ContentLoader(
        loading = isLoading,
        empty = property == null && !isLoading,
        emptyContent = {
            EmptyContent(
                errorLabel = errorMessage ?: R.string.content_description_error_message,
                onRetry = {
                    viewModel.loadCurrencyRates(
                        params = CurrencyRateParams(),
                        isRefresh = false
                    )
                }
            )
        },
        loadingIndicator = {
            LoadingIndicator(modifier = modifier)
        }
    ) {

        property?.let {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(Background)
                    .fillMaxSize(),
            ) {

                Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_58)))

                Header(property = it, currencyPrice = currencyPrice)

                Description(it)

                Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_24)))
            }

        }

    }
}

@Composable
private fun Header(
    property: PropertyUiModel,
    currencyPrice: String
) {

    val txtOverallRating: String = stringResource(id = R.string.txt_overall_rating)

    Column {
        var palette by remember { mutableStateOf<Palette?>(null) }
        NetworkImage(
            modifier = Modifier
                .height(height = dimensionResource(id = R.dimen.property_details_screen_network_image_height_380)),
            networkUrl = property.imageUrl,
            circularReveal = CircularReveal(duration = integerResource(id = R.integer.property_details_screen_network_image_circular_reveal_duration_300)),
            shimmerParams = null,
            bitmapPalette = BitmapPalette {
                palette = it
            }
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_25)))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.property_details_screen_text_padding_horizontal_8)),
            text = property.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = integerResource(id = R.integer.property_details_screen_text_max_lines_2),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_6)))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.property_details_screen_text_padding_horizontal_8)),
            text = currencyPrice,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = integerResource(id = R.integer.property_details_screen_text_max_lines_2),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_6)))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.property_details_screen_text_padding_horizontal_8)),
            text = property.city,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = integerResource(id = R.integer.property_details_screen_text_max_lines_1),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_6)))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.property_details_screen_text_padding_horizontal_8)),
            text = "$txtOverallRating: ${property.overallRating}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = integerResource(id = R.integer.property_details_screen_text_max_lines_1),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_8)))

    }
}

@Composable
private fun Description(
    property: PropertyUiModel
) {

    Column {
        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_23)))

        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            maxLines = integerResource(id = R.integer.property_details_screen_text_max_lines_1),
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.property_details_screen_text_padding_horizontal_15))
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_12)))

        Text(
            text = property.overview,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.property_details_screen_text_padding_horizontal_15))
        )

        Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.property_details_screen_spacer_height_15)))
    }
}