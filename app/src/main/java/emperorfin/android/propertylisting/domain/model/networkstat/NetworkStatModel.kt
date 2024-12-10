package emperorfin.android.propertylisting.domain.model.networkstat

import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatModelParams


data class NetworkStatModel private constructor(
    override val requestMethod: String,
    override val duration: Long
) : NetworkStatModelParams {

    companion object {

        fun newInstance(requestMethod: String, duration: Long): NetworkStatModel {
            return NetworkStatModel(
                requestMethod = requestMethod,
                duration = duration,
            )
        }

    }

}
