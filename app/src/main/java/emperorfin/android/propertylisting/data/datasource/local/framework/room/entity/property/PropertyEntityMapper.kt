package emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property

import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import javax.inject.Inject

class PropertyEntityMapper @Inject constructor() {

    fun transform(property: PropertyModel): PropertyEntity {

        val id: Long = property.id
        val name: String = property.name
        val overview: String = property.overview
        val isFeatured: Boolean? = property.isFeatured
        val lowestPricePerNightInEuros: Double = property.lowestPricePerNightInEuros
        val rating: Double = property.rating
        val overallRating: Double = property.overallRating
        val city: String = property.city
        val country: String = property.country

        return PropertyEntity.newInstance(
            id = id,
            name = name,
            overview = overview,
            isFeatured = isFeatured,
            lowestPricePerNightInEuros = lowestPricePerNightInEuros,
            rating = rating,
            overallRating = overallRating,
            city = city,
            country = country
        )
    }

}