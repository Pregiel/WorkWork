package pl.pregiel.workwork.calculation

import pl.pregiel.workwork.data.pojo.Tax
import pl.pregiel.workwork.utils.round
import pl.pregiel.workwork.utils.roundTo2DecimalPoint
import kotlin.math.abs

class CalculationFromNetto(
        private val tax: Tax,
        override var netto: Double,
        private val workLocation: Boolean,
        private val old26: Boolean
) : Calculation() {
    override fun calculate() {
        val totalSocialInsuranceTaxes = tax.pensionInsuranceTax.forEmployee +
                tax.disabilityInsuranceTax.forEmployee + tax.sicknessInsuranceTax

        brutto = roundTo2DecimalPoint(
                (netto - if (old26) tax.taxFree + tax.firstTaxLevel * if (workLocation) tax.gettingIncomeCost.resident else tax.gettingIncomeCost.nonResident else 0.0) /
                        ((1 - totalSocialInsuranceTaxes - tax.healthInsuranceTax * (1 - totalSocialInsuranceTaxes)) -
                                if (old26) (tax.firstTaxLevel * (1 - totalSocialInsuranceTaxes)) - (tax.healthInsuranceEffectiveTax * (1 - totalSocialInsuranceTaxes)) else 0.0)
        )

        var counter = 100
        val calculationFromBrutto = CalculationFromBrutto(tax, brutto, workLocation, old26)

        do  {
            calculationFromBrutto.brutto = brutto
            calculationFromBrutto.calculate()

            brutto = roundTo2DecimalPoint(brutto + netto - calculationFromBrutto.netto)

            --counter
            if (counter < 1)
                break
        } while (!netto.equals(calculationFromBrutto.netto))

        pensionInsurance = calculationFromBrutto.pensionInsurance
        disabilityInsurance = calculationFromBrutto.disabilityInsurance
        sicknessInsurance = calculationFromBrutto.sicknessInsurance
        healthInsurance = calculationFromBrutto.healthInsurance
        gettingIncomeAdvance = calculationFromBrutto.gettingIncomeAdvance
    }
}