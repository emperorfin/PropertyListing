package emperorfin.android.propertylisting.ui.model.networkstat

import emperorfin.android.propertylisting.domain.model.networkstat.NetworkStatModel
import javax.inject.Inject


class NetworkStatUiModelMapper @Inject constructor() {

    fun transform(networkStat: NetworkStatModel): NetworkStatUiModel {

        val requestMethod: String = networkStat.requestMethod
        val duration: Long = networkStat.duration

        return NetworkStatUiModel.newInstance(
            requestMethod = requestMethod,
            duration = duration
        )
    }

}