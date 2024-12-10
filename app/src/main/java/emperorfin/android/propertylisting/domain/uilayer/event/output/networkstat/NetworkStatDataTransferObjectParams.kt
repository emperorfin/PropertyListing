package emperorfin.android.propertylisting.domain.uilayer.event.output.networkstat

interface NetworkStatDataTransferObjectParams : Params {

    val requestMethod: String?
    val duration: Long?

}