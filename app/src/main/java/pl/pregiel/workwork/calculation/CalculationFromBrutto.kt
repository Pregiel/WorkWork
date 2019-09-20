package pl.pregiel.workwork.calculation

import pl.pregiel.workwork.data.pojo.Tax
import pl.pregiel.workwork.utils.round
import pl.pregiel.workwork.utils.roundTo2DecimalPoint


class CalculationFromBrutto(
        private val tax: Tax,
        override var brutto: Double,
        private val workLocation: Boolean,
        private val old26: Boolean
) : Calculation() {

    override fun calculate() {
        pensionInsurance = roundTo2DecimalPoint(brutto * tax.pensionInsuranceTax.forEmployee)
        if (pensionInsurance > tax.getPensionLimitForEmployee())
            pensionInsurance = tax.getPensionLimitForEmployee()

        disabilityInsurance = roundTo2DecimalPoint(brutto * tax.disabilityInsuranceTax.forEmployee)
        if (disabilityInsurance > tax.getDisabilityLimitForEmployee())
            disabilityInsurance = tax.getDisabilityLimitForEmployee()

        sicknessInsurance = roundTo2DecimalPoint(brutto * tax.sicknessInsuranceTax)
        val socialInsurances = pensionInsurance + disabilityInsurance + sicknessInsurance

        val healthInsuranceBase = roundTo2DecimalPoint(brutto - socialInsurances)

        val salaryWithoutGettingIncomeCost = if (workLocation)
            round(healthInsuranceBase - tax.gettingIncomeCost.resident)
        else
            round(healthInsuranceBase - tax.gettingIncomeCost.nonResident)

        healthInsurance = roundTo2DecimalPoint(healthInsuranceBase * tax.healthInsuranceTax)
        var taxBase = roundTo2DecimalPoint(
                salaryWithoutGettingIncomeCost * tax.firstTaxLevel - tax.taxFree
        )
        if (taxBase < 0.0) taxBase = 0.0
        if (healthInsurance > taxBase)
            healthInsurance = taxBase

        gettingIncomeAdvance = if (old26)
            round(
                    taxBase - roundTo2DecimalPoint(healthInsuranceBase * tax.healthInsuranceEffectiveTax)
            )
        else
            0.0
        if (gettingIncomeAdvance < 0.0)
            gettingIncomeAdvance = 0.0

        netto = roundTo2DecimalPoint(
                brutto - socialInsurances - healthInsurance - gettingIncomeAdvance
        )
    }
}

