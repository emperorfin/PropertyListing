package emperorfin.android.propertylisting.domain.uilayer.event.output.property

interface PropertyDataTransferObjectParams : Params {

    val id: Long
    val name: String?
    val overview: String?
    val isFeatured: Boolean?
    val lowestPricePerNightInEuros: Double?
    val rating: Double?
    val overallRating: Double?
    val city: String?
    val country: String?

}