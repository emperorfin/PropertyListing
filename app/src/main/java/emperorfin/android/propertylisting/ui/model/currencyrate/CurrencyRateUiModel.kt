package emperorfin.android.propertylisting.ui.model.currencyrate

import emperorfin.android.propertylisting.domain.uilayer.event.output.currencyrate.CurrencyRateUiModelParams

data class CurrencyRateUiModel private constructor(
    override val id: String,
    override val currencySymbolBase: String,
    override val currencySymbolOther: String,
    override val rate: Double
) : CurrencyRateUiModelParams {

    companion object {

        fun newInstance(
            id: String,
            currencySymbolBase: String,
            currencySymbolOther: String,
            rate: Double
        ): CurrencyRateUiModel {
            return CurrencyRateUiModel(
                id = id,
                currencySymbolBase = currencySymbolBase,
                currencySymbolOther = currencySymbolOther,
                rate = rate
            )
        }

    }

}