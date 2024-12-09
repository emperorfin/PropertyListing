package emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate

interface CurrencyRateDataTransferObjectParams : Params {

    val currencySymbolBase: String?
    val currencySymbolOther: String?
    val rate: Double?

}