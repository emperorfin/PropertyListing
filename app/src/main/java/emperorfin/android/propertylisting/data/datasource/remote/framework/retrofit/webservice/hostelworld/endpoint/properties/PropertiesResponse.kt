package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties

import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.propertiesresponse.FilterData
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.propertiesresponse.Location
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.endpoint.properties.propertiesresponse.Pagination
import emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.webservice.hostelworld.jsonobject.properties.property.Property


data class PropertiesResponse(
    val properties: List<Property>,
    val location: Location?,
    val locationEn: Location?,
    val filterData: FilterData?,
    val sortOrder: Any?,
    val pagination: Pagination?,
)
