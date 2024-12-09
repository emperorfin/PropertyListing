package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.jsonobject.properties.property

import com.google.gson.annotations.SerializedName
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.jsonobject.properties.property.facilities.Facility
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.jsonobject.properties.property.images.Image
import java.io.Serializable

data class Property(
    val id: Long,
    val isPromoted: Boolean?,
    val hbid: Int?,
    val name: String?,
//    val starRating: Int?,
    val starRating: Double?,
    val overallRating: OverallRating?,
    val ratingBreakdown: RatingBreakdown?,
    val latitude: Double?,
    val longitude: Double?,
    val isFeatured: Boolean?,
    val type: String?,
    val address1: String?,
    val address2: String?,
    val freeCancellationAvailable: Boolean?,
    val freeCancellationAvailableUntil: String?,
    val district: Any?,
    val districts: List<Any>?,
    val freeCancellation: FreeCancellation?,
    val lowestPricePerNight: LowestPricePerNight?,
    val lowestPrivatePricePerNight: LowestPricePerNight?,
    val lowestDormPricePerNight: LowestPricePerNight?,
    val lowestAveragePricePerNight: LowestAveragePricePerNight?,
    val lowestAverageDormPricePerNight: LowestAveragePricePerNight?,
    val lowestAveragePrivatePricePerNight: LowestAveragePricePerNight?,
    val isNew: Boolean?,
    val overview: String?,
    val isElevate: Boolean?,
    @SerializedName("hostelworldRecommends")
    val hostelWorldRecommends: Boolean?,
    val distance: Distance?,
    val position: Int?,
    val hwExtra: Any?,
    val fabSort: FabSort?,
    val promotions: List<Promotion>?,
    val rateRuleViolations: List<Any>?,
    val originalLowestAveragePricePerNight: LowestPricePerNight?,
    val originalLowestAverageDormPricePerNight: LowestPricePerNight?,
    val originalLowestAveragePrivatePricePerNight: LowestPricePerNight?,
    val fenceDiscount: Int?,
    val veryPopular: Boolean?,
    val images: List<Image>?,
    val imagesGallery: List<Image>?,
    val facilities: List<Facility>?,
) : Serializable
