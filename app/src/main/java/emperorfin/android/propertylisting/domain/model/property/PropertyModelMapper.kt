package emperorfin.android.propertylisting.domain.model.property

import emperorfin.android.propertylisting.domain.constant.DoubleConstants.MINUS_0_0
import emperorfin.android.propertylisting.domain.constant.StringConstants.EMPTY
import emperorfin.android.propertylisting.domain.constant.StringConstants.PROTOCOL_HTTP
import emperorfin.android.propertylisting.domain.constant.StringConstants.PROTOCOL_HTTPS
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyDataTransferObjectParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyEntityParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyUiModelParams
import java.math.RoundingMode
import java.net.URLEncoder
import javax.inject.Inject

class PropertyModelMapper @Inject constructor() {

    companion object {
        private const val DECIMAL_PLACES_1: Int = 1
    }

    fun transform(property: PropertyDataTransferObjectParams): PropertyModel {

        val id: Long = property.id
        val name: String = URLEncoder.encode(property.name) ?: EMPTY
        val overview: String = URLEncoder.encode(property.overview) ?: EMPTY
        val isFeatured: Boolean? = property.isFeatured
        val lowestPricePerNightInEuros: Double = property.lowestPricePerNightInEuros ?: MINUS_0_0
        val rating: Double = property.rating ?: MINUS_0_0
        val overallRating: Double = property.overallRating ?: MINUS_0_0
        val city: String = property.city ?: EMPTY
        val country: String = property.country ?: EMPTY
        var imageUrl: String = property.imageUrl ?: EMPTY

        var ratingRoundedUp = MINUS_0_0

        if (rating > MINUS_0_0) {
            ratingRoundedUp = rating.toBigDecimal().setScale(DECIMAL_PLACES_1, RoundingMode.UP).toDouble()
        }

        if (imageUrl.isNotEmpty()) {
            if (!imageUrl.startsWith(PROTOCOL_HTTP) && !imageUrl.startsWith(PROTOCOL_HTTPS)) {
                imageUrl = "$PROTOCOL_HTTPS$imageUrl"
            }
        }

        return PropertyModel.newInstance(
            id = id,
            name = name,
            overview = overview,
            isFeatured = isFeatured,
            lowestPricePerNightInEuros = lowestPricePerNightInEuros,
            rating = ratingRoundedUp,
            overallRating = overallRating,
            city = city,
            country = country,
            imageUrl = imageUrl,
        )
    }

    fun transform(property: PropertyEntityParams): PropertyModel {

        val id: Long = property.id
        val name: String = property.name!!
        val overview: String = property.overview!!
        val isFeatured: Boolean? = property.isFeatured
        val lowestPricePerNightInEuros: Double = property.lowestPricePerNightInEuros!!
        val rating: Double = property.rating!!
        val overallRating: Double = property.overallRating!!
        val city: String = property.city!!
        val country: String = property.country!!
        val imageUrl: String = property.imageUrl!!

        return PropertyModel.newInstance(
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

    fun transform(property: PropertyUiModelParams): PropertyModel {

        val id: Long = property.id
        val name: String = property.name!!
        val overview: String = property.overview!!
        val isFeatured: Boolean? = property.isFeatured
        val lowestPricePerNightInEuros: Double = property.lowestPricePerNightInEuros!!
        val rating: Double = property.rating!!
        val overallRating: Double = property.overallRating!!
        val city: String = property.city!!
        val country: String = property.country!!
        val imageUrl: String = property.imageUrl!!

        return PropertyModel.newInstance(
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