package emperorfin.android.propertylisting.ui.navigation

import androidx.navigation.NavHostController
import emperorfin.android.propertylisting.ui.navigation.Destinations.ROUTE_PROPERTIES
import emperorfin.android.propertylisting.ui.navigation.Destinations.ROUTE_PROPERTY_DETAILS
import emperorfin.android.propertylisting.ui.navigation.Screens.SCREEN_PROPERTIES
import emperorfin.android.propertylisting.ui.navigation.Screens.SCREEN_PROPERTY_DETAILS


/**
 * Screens used in [Destinations]
 */
private object Screens {
    const val SCREEN_PROPERTIES: String = "properties"
    const val SCREEN_PROPERTY_DETAILS: String =
        "property-details?${ScreenArgs.SCREEN_PROPERTY_DETAILS}={${ScreenArgs.SCREEN_PROPERTY_DETAILS}}"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val ROUTE_PROPERTIES: String = SCREEN_PROPERTIES
    const val ROUTE_PROPERTY_DETAILS: String = SCREEN_PROPERTY_DETAILS
}

object ScreenArgs {
    const val SCREEN_PROPERTY_DETAILS: String = "propertydetails"
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {

    fun navigateToPropertiesScreen() = navController.navigate(ROUTE_PROPERTIES)

    fun navigateToPropertyDetailsScreen(route: String = ROUTE_PROPERTY_DETAILS) =
        navController.navigate(route)

    fun navigateBack() = navController.navigateUp()

}