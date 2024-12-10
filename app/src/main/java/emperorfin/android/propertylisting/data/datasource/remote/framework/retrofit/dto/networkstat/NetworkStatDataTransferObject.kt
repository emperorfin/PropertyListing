package emperorfin.android.propertylisting.data.datasource.remote.framework.retrofit.dto.networkstat

import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatDataTransferObjectParams


data class NetworkStatDataTransferObject private constructor(
    override val requestMethod: String?,
    override val duration: Long?
) : NetworkStatDataTransferObjectParams {

    companion object {

        fun newInstance(requestMethod: String?, duration: Long?): NetworkStatDataTransferObject {
            return NetworkStatDataTransferObject(
                requestMethod = requestMethod,
                duration = duration,
            )
        }

    }

}
