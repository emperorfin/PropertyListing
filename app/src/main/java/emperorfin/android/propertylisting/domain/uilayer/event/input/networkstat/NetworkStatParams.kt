package emperorfin.android.propertylisting.domain.uilayer.event.input.networkstat

import emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat.NetworkStatModelParams


data class NetworkStatParams(
    override val requestMethod: String? = null,
    override val duration: Long? = null
) : NetworkStatModelParams
