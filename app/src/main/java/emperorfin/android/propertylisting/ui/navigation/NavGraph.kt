package emperorfin.android.propertylisting.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.squareup.moshi.Moshi
import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel
import emperorfin.android.propertylisting.ui.navigation.Destinations.ROUTE_PROPERTIES
import emperorfin.android.propertylisting.ui.navigation.Destinations.ROUTE_PROPERTY_DETAILS
import emperorfin.android.propertylisting.ui.screen.properties.PropertiesScreen
import emperorfin.android.propertylisting.ui.screen.propertydetails.PropertyDetailsScreen


@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_PROPERTIES,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    },
) {

    ProvideWindowInsets {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
        ) {

            composable(ROUTE_PROPERTIES) {
                PropertiesScreen(navigationActions = navActions)
            }

            composable(ROUTE_PROPERTY_DETAILS) { backStackEntry ->

                val propertyJson =  backStackEntry.arguments?.getString(ScreenArgs.SCREEN_PROPERTY_DETAILS)
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(PropertyUiModel::class.java).lenient()
                val propertyObject = jsonAdapter.fromJson(propertyJson)

                PropertyDetailsScreen(
                    property = propertyObject,
                    navigationActions = navActions,
                )
            }
        }
    }

}