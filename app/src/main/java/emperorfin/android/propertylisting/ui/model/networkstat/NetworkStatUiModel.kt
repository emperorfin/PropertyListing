package emperorfin.android.propertylisting.ui.model.networkstat

import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatUiModelParams


data class NetworkStatUiModel private constructor(
    override val requestMethod: String,
    override val duration: Long
) : NetworkStatUiModelParams {

    companion object {

        fun newInstance(requestMethod: String, duration: Long): NetworkStatUiModel {
            return NetworkStatUiModel(
                requestMethod = requestMethod,
                duration = duration,
            )
        }

    }

}
