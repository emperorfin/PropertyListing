package emperorfin.android.propertylisting.ui.model.property

import emperorfin.android.propertylisting.domain.constant.DoubleConstants.MINUS_0_0
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_NAME_GBP
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_NAME_USD
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_SYMBOL_EUR
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_SYMBOL_GBP
import emperorfin.android.propertylisting.domain.constant.StringConstants.CURRENCY_SYMBOL_USD
import emperorfin.android.propertylisting.domain.model.currencyrate.CurrencyRateModel
import emperorfin.android.propertylisting.domain.model.property.PropertyModel
import java.math.RoundingMode
import javax.inject.Inject

class PropertyUiModelMapper @Inject constructor() {

    companion object {
        private const val DECIMAL_PLACES_2: Int = 2
    }

    fun transform(property: PropertyModel): PropertyUiModel {

        val id: Long = property.id
        val name: String = property.name
        val overview: String = property.overview
        val isFeatured: Boolean? = property.isFeatured
        val lowestPricePerNightInEuros: Double = property.lowestPricePerNightInEuros
        val rating: Double = property.rating
        val overallRating: Double = property.overallRating
        val city: String = property.city
        val country: String = property.country
        val imageUrl: String = property.imageUrl

        val lowestPricePerNightInDollars: Double = MINUS_0_0
        val lowestPricePerNightInPounds: Double = MINUS_0_0
        val lowestPricePerNightInEurosWithCurrencySymbol =
            "$CURRENCY_SYMBOL_EUR${lowestPricePerNightInEuros}"
        val lowestPricePerNightInDollarsWithCurrencySymbol =
            "$CURRENCY_SYMBOL_USD${lowestPricePerNightInDollars}"
        val lowestPricePerNightInPoundsWithCurrencySymbol =
            "$CURRENCY_SYMBOL_GBP${lowestPricePerNightInPounds}"

        return PropertyUiModel.newInstance(
            id = id,
            name = name,
            overview = overview,
            isFeatured = isFeatured,
            lowestPricePerNightInEuros = lowestPricePerNightInEuros,
            lowestPricePerNightInDollars = lowestPricePerNightInDollars,
            lowestPricePerNightInPounds = lowestPricePerNightInPounds,
            lowestPricePerNightInEurosWithCurrencySymbol = lowestPricePerNightInEurosWithCurrencySymbol,
            lowestPricePerNightInDollarsWithCurrencySymbol = lowestPricePerNightInDollarsWithCurrencySymbol,
            lowestPricePerNightInPoundsWithCurrencySymbol = lowestPricePerNightInPoundsWithCurrencySymbol,
            rating = rating,
            overallRating = overallRating,
            city = city,
            country = country,
            imageUrl = imageUrl,
        )
    }

    fun transform(property: PropertyUiModel, currencyRates: List<CurrencyRateModel>): PropertyUiModel {

        val id: Long = property.id
        val name: String = property.name
        val overview: String = property.overview
        val isFeatured: Boolean? = property.isFeatured
        val lowestPricePerNightInEuros: Double = property.lowestPricePerNightInEuros
        val rating: Double = property.rating
        val overallRating: Double = property.overallRating
        val city: String = property.city
        val country: String = property.country
        val imageUrl: String = property.imageUrl

        val currencyRateUsd: CurrencyRateModel? =
            currencyRates.singleOrNull { it.currencySymbolOther == CURRENCY_NAME_USD }
        val currencyRateGbp: CurrencyRateModel? =
            currencyRates.singleOrNull { it.currencySymbolOther == CURRENCY_NAME_GBP }

        val lowestPricePerNightInDollars: Double =
            lowestPricePerNightInEuros * (currencyRateUsd?.rate ?: MINUS_0_0)
        val lowestPricePerNightInPounds: Double =
            lowestPricePerNightInEuros * (currencyRateGbp?.rate ?: MINUS_0_0)

        val lowestPricePerNightInDollarsRoundedUp =
            lowestPricePerNightInDollars.toBigDecimal().setScale(DECIMAL_PLACES_2, RoundingMode.UP).toDouble()
        val lowestPricePerNightInPoundsRoundedUp =
            lowestPricePerNightInPounds.toBigDecimal().setScale(DECIMAL_PLACES_2, RoundingMode.UP).toDouble()

        val lowestPricePerNightInEurosWithCurrencySymbol =
            "$CURRENCY_SYMBOL_EUR${lowestPricePerNightInEuros}"
        val lowestPricePerNightInDollarsWithCurrencySymbol =
            "$CURRENCY_SYMBOL_USD${lowestPricePerNightInDollarsRoundedUp}"
        val lowestPricePerNightInPoundsWithCurrencySymbol =
            "$CURRENCY_SYMBOL_GBP${lowestPricePerNightInPoundsRoundedUp}"

        return PropertyUiModel.newInstance(
            id = id,
            name = name,
            overview = overview,
            isFeatured = isFeatured,
            lowestPricePerNightInEuros = lowestPricePerNightInEuros,
            lowestPricePerNightInDollars = lowestPricePerNightInDollars,
            lowestPricePerNightInPounds = lowestPricePerNightInPounds,
            lowestPricePerNightInEurosWithCurrencySymbol = lowestPricePerNightInEurosWithCurrencySymbol,
            lowestPricePerNightInDollarsWithCurrencySymbol = lowestPricePerNightInDollarsWithCurrencySymbol,
            lowestPricePerNightInPoundsWithCurrencySymbol = lowestPricePerNightInPoundsWithCurrencySymbol,
            rating = rating,
            overallRating = overallRating,
            city = city,
            country = country,
            imageUrl = imageUrl,
        )
    }

}