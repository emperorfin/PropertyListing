package emperorfin.android.propertylisting.ui.model.currencyrate

import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import java.math.RoundingMode
import javax.inject.Inject


class CurrencyRateUiModelMapper @Inject constructor() {

    companion object {
        private const val DECIMAL_PLACES_2: Int = 2
    }

    fun transform(currencyRate: CurrencyRateModel): CurrencyRateUiModel {

        val id: String = currencyRate.id
        val currencySymbolBase: String = currencyRate.currencySymbolBase
        val currencySymbolOther: String = currencyRate.currencySymbolOther
        val rate: Double = currencyRate.rate

        val rateRoundedUp = rate.toBigDecimal().setScale(DECIMAL_PLACES_2, RoundingMode.UP).toDouble()

        return CurrencyRateUiModel.newInstance(
            id = id,
            currencySymbolBase = currencySymbolBase,
            currencySymbolOther = currencySymbolOther,
            rate = rateRoundedUp
        )

    }

}