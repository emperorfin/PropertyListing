package emperorfin.android.propertylisting.domain.uilayer.event.input.property

import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyModelParams

data class PropertyParams(
    override val id: Long = 0L,
    override val name: String? = null,
    override val overview: String? = null,
    override val isFeatured: Boolean? = null,
    override val lowestPricePerNightInEuros: Double? = null,
    override val rating: Double? = null,
    override val overallRating: Double? = null,
    override val city: String? = null,
    override val country: String? = null,
    override val imageUrl: String? = null,
) : PropertyModelParams
