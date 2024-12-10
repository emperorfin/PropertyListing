package emperorfin.android.propertylisting.domain.model.networkstat

import emperorfin.android.propertylisting.domain.constant.LongConstants.MINUS_0L
import emperorfin.android.propertylisting.domain.constant.StringConstants.EMPTY
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatDataTransferObjectParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatEntityParams
import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatUiModelParams
import javax.inject.Inject


class NetworkStatModelMapper @Inject constructor() {

    fun transform(networkStat: NetworkStatDataTransferObjectParams): NetworkStatModel {

        val requestMethod: String = networkStat.requestMethod ?: EMPTY
        val duration: Long = networkStat.duration ?: MINUS_0L

        return NetworkStatModel.newInstance(
            requestMethod = requestMethod,
            duration = duration,
        )
    }

    fun transform(networkStat: NetworkStatEntityParams): NetworkStatModel {

        val requestMethod: String = networkStat.requestMethod!!
        val duration: Long = networkStat.duration!!

        return NetworkStatModel.newInstance(
            requestMethod = requestMethod,
            duration = duration,
        )
    }

    fun transform(networkStat: NetworkStatUiModelParams): NetworkStatModel {

        val requestMethod: String = networkStat.requestMethod!!
        val duration: Long = networkStat.duration!!

        return NetworkStatModel.newInstance(
            requestMethod = requestMethod,
            duration = duration,
        )
    }

}