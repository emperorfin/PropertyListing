package emperorfin.android.propertylisting.ui.screen.propertydetails.stateholder

import emperorfin.android.propertylisting.ui.model.property.PropertyUiModel


data class PropertyDetailsUiState(
    val property: PropertyUiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: Int? = null,
    val messageSnackBar: Int? = null,
)
