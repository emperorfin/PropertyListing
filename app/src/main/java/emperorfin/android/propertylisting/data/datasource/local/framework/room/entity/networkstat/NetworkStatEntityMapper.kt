package emperorfin.android.propertylisting.data.datasource.local.framework.room.entity.networkstat

import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import javax.inject.Inject


class NetworkStatEntityMapper @Inject constructor() {

    fun transform(networkStat: NetworkStatModel): NetworkStatEntity {

        val requestMethod: String = networkStat.requestMethod
        val duration: Long = networkStat.duration

        return NetworkStatEntity.newInstance(
            requestMethod = requestMethod,
            duration = duration
        )
    }

}