package emperorfin.android.propertylisting.ui.screen.properties.stateholder

import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel



data class PropertiesUiState(
    val properties: List<PropertyUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: Int? = null,
    val messageSnackBar: Int? = null,
)
