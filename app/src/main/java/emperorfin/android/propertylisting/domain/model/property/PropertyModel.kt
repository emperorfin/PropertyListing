package emperorfin.android.propertylisting.domain.model.property

import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyModelParams

data class PropertyModel private constructor(
    override val id: Long,
    override val name: String,
    override val overview: String,
    override val isFeatured: Boolean?,
    override val lowestPricePerNightInEuros: Double,
    override val rating: Double,
    override val overallRating: Double,
    override val city: String,
    override val country: String
) : PropertyModelParams {

    companion object {

        fun newInstance(
            id: Long,
            name: String,
            overview: String,
            isFeatured: Boolean?,
            lowestPricePerNightInEuros: Double,
            rating: Double,
            overallRating: Double,
            city: String,
            country: String
        ): PropertyModel {
            return PropertyModel(
                id = id,
                name = name,
                overview = overview,
                isFeatured = isFeatured,
                lowestPricePerNightInEuros = lowestPricePerNightInEuros,
                rating = rating,
                overallRating = overallRating,
                city = city,
                country = country,
            )
        }

    }

}
