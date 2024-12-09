package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dto.property

import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyDataTransferObjectParams

data class PropertyDataTransferObject private constructor(
    override val id: Long,
    override val name: String?,
    override val overview: String?,
    override val isFeatured: Boolean?,
    override val lowestPricePerNightInEuros: Double?,
    override val rating: Double?,
    override val overallRating: Double?,
    override val city: String?,
    override val country: String?,
    override val imageUrl: String?
) : PropertyDataTransferObjectParams {

    companion object {

        fun newInstance(
            id: Long,
            name: String?,
            overview: String?,
            isFeatured: Boolean?,
            lowestPricePerNightInEuros: Double?,
            rating: Double?,
            overallRating: Double?,
            city: String?,
            country: String?,
            imageUrl: String?
        ): PropertyDataTransferObject {
            return PropertyDataTransferObject(
                id = id,
                name = name,
                overview = overview,
                isFeatured = isFeatured,
                lowestPricePerNightInEuros = lowestPricePerNightInEuros,
                rating = rating,
                overallRating = overallRating,
                city = city,
                country = country,
                imageUrl = imageUrl,
            )
        }

    }

}
