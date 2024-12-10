package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dto.networkstat

import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import javax.inject.Inject

class NetworkStatDataTransferObjectMapper @Inject constructor() {

    fun transform(networkStat: NetworkStatModel): NetworkStatDataTransferObject {

        val requestMethod: String = networkStat.requestMethod
        val duration: Long = networkStat.duration

        return NetworkStatDataTransferObject.newInstance(
            requestMethod = requestMethod,
            duration = duration
        )
    }

}