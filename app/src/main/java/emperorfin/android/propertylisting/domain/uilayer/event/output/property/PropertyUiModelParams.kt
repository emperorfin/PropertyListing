package emperorfin.android.propertylisting.domain.uilayer.event.output.property

interface PropertyUiModelParams : PropertyModelParams {

    val lowestPricePerNightInDollars: Double?
    val lowestPricePerNightInPounds: Double?
    val lowestPricePerNightInEurosWithCurrencySymbol: String?
    val lowestPricePerNightInDollarsWithCurrencySymbol: String?
    val lowestPricePerNightInPoundsWithCurrencySymbol: String?

}