package emperorfin.android.propertylisting.ui.model.property

import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyUiModelParams

data class PropertyUiModel private constructor(
    override val id: Long,
    override val name: String,
    override val overview: String,
    override val isFeatured: Boolean?,
    override val lowestPricePerNightInEuros: Double,
    override val rating: Double,
    override val overallRating: Double,
    override val lowestPricePerNightInDollars: Double,
    override val lowestPricePerNightInPounds: Double,
    override val lowestPricePerNightInEurosWithCurrencySymbol: String,
    override val lowestPricePerNightInDollarsWithCurrencySymbol: String,
    override val lowestPricePerNightInPoundsWithCurrencySymbol: String,
    override val city: String,
    override val country: String,
    override val imageUrl: String,
) : PropertyUiModelParams {

    companion object {

        fun newInstance(
            id: Long,
            name: String,
            overview: String,
            isFeatured: Boolean?,
            lowestPricePerNightInEuros: Double,
            rating: Double,
            overallRating: Double,
            lowestPricePerNightInDollars: Double,
            lowestPricePerNightInPounds: Double,
            lowestPricePerNightInEurosWithCurrencySymbol: String,
            lowestPricePerNightInDollarsWithCurrencySymbol: String,
            lowestPricePerNightInPoundsWithCurrencySymbol: String,
            city: String,
            country: String,
            imageUrl: String,
        ): PropertyUiModel {
            return PropertyUiModel(
                id = id,
                name = name,
                overview = overview,
                isFeatured = isFeatured,
                lowestPricePerNightInEuros = lowestPricePerNightInEuros,
                rating = rating,
                overallRating = overallRating,
                lowestPricePerNightInDollars = lowestPricePerNightInDollars,
                lowestPricePerNightInPounds = lowestPricePerNightInPounds,
                lowestPricePerNightInEurosWithCurrencySymbol = lowestPricePerNightInEurosWithCurrencySymbol,
                lowestPricePerNightInDollarsWithCurrencySymbol = lowestPricePerNightInDollarsWithCurrencySymbol,
                lowestPricePerNightInPoundsWithCurrencySymbol = lowestPricePerNightInPoundsWithCurrencySymbol,
                city = city,
                country = country,
                imageUrl = imageUrl,
            )
        }

    }

}
