package emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity.Companion.COLUMN_INFO_ID
import emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.property.PropertyEntity.Companion.TABLE_NAME
import emperorfin.android.propertylisting.domain.uilayer.event.output.property.PropertyEntityParams


// The Room ORM won't be able to instantiate this class if it's one and only constructor is made
// private.
@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [COLUMN_INFO_ID]
)
data class PropertyEntity(
    @ColumnInfo(name = COLUMN_INFO_ID)
    override val id: Long,
    @ColumnInfo(name = COLUMN_INFO_NAME)
    override val name: String,
    @ColumnInfo(name = COLUMN_INFO_OVERVIEW)
    override val overview: String,
    @ColumnInfo(name = COLUMN_INFO_IS_FEATURED)
    override val isFeatured: Boolean?,
    @ColumnInfo(name = COLUMN_INFO_LOWEST_PRICE_PER_NIGHT_IN_EUROS)
    override val lowestPricePerNightInEuros: Double,
    @ColumnInfo(name = COLUMN_INFO_RATING)
    override val rating: Double,
    @ColumnInfo(name = COLUMN_INFO_OVERALL_RATING)
    override val overallRating: Double,
    @ColumnInfo(name = COLUMN_INFO_CITY)
    override val city: String,
    @ColumnInfo(name = COLUMN_INFO_COUNTRY)
    override val country: String,
    @ColumnInfo(name = COLUMN_INFO_IMAGE_URL)
    override val imageUrl: String,
) : PropertyEntityParams {

    companion object {

        const val TABLE_NAME = "table_properties"

        const val COLUMN_INFO_ID = "id"
        const val COLUMN_INFO_NAME = "name"
        const val COLUMN_INFO_OVERVIEW = "overview"
        const val COLUMN_INFO_IS_FEATURED = "is_featured"
        const val COLUMN_INFO_LOWEST_PRICE_PER_NIGHT_IN_EUROS = "lowest_price_per_night_in_euros"
        const val COLUMN_INFO_RATING = "rating"
        const val COLUMN_INFO_OVERALL_RATING = "overall_rating"
        const val COLUMN_INFO_CITY = "city"
        const val COLUMN_INFO_COUNTRY = "country"
        const val COLUMN_INFO_IMAGE_URL = "image_url"

        fun newInstance(
            id: Long,
            name: String,
            overview: String,
            isFeatured: Boolean?,
            lowestPricePerNightInEuros: Double,
            rating: Double,
            overallRating: Double,
            city: String,
            country: String,
            imageUrl: String,
        ): PropertyEntity {
            return PropertyEntity(
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
