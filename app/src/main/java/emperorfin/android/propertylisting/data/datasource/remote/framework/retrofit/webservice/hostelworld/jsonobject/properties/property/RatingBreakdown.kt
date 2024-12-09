package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.jsonobject.properties.property

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RatingBreakdown(
    val ratingsCount: Int?,
    val security: Int?,
    val location: Int?,
    val staff: Int?,
    @SerializedName("fun")
    val _fun: Int?, // The compiler doesn't allow a parameter with the name "fun".
    val clean: Int?,
    val facilities: Int?,
    val value: Int?,
    val average: Int?,
) : Serializable
