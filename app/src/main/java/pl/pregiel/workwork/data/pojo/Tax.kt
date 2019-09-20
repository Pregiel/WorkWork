package pl.pregiel.workwork.data.pojo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import pl.pregiel.workwork.utils.roundTo2DecimalPoint

@JsonRootName("Tax")
data class Tax(
        @set:JsonProperty("PensionInsuranceTax")
        var pensionInsuranceTax: PensionInsuranceTax = PensionInsuranceTax(),

        @set:JsonProperty("DisabilityInsuranceTax")
        var disabilityInsuranceTax: DisabilityInsuranceTax = DisabilityInsuranceTax(),

        @set:JsonProperty("SicknessInsuranceTax")
        var sicknessInsuranceTax: Double = 0.0,

        @set:JsonProperty("SocialInsuranceLimit")
        var socialInsuranceLimit: Double = 0.0,

        @set:JsonProperty("AccidentIsuranceTax")
        var accidentInsuranceTax: Double = 0.0,

        @set:JsonProperty("GettingIncomeCost")
        var gettingIncomeCost: GettingIncomeCost = GettingIncomeCost(),

        @set:JsonProperty("TaxFree")
        var taxFree: Double = 0.0,

        @set:JsonProperty("HealthInsuranceTax")
        var healthInsuranceTax: Double = 0.0,

        @set:JsonProperty("HealthInsuranceEffectiveTax")
        var healthInsuranceEffectiveTax: Double = 0.0,

        @set:JsonProperty("FirstTaxLevel")
        var firstTaxLevel: Double = 0.0,

        @set:JsonProperty("SecondLevel")
        var secondLevel: Double = 0.0,

        @set:JsonProperty("SecondTaxLevel")
        var secondTaxLevel: Double = 0.0
) {
    fun getPensionLimitForEmployee(): Double =
            roundTo2DecimalPoint(socialInsuranceLimit * pensionInsuranceTax.forEmployee)

    fun getPensionLimitForEmployer(): Double =
            roundTo2DecimalPoint(socialInsuranceLimit * pensionInsuranceTax.forEmployer)

    fun getDisabilityLimitForEmployee(): Double =
            roundTo2DecimalPoint(socialInsuranceLimit * disabilityInsuranceTax.forEmployee)

    fun getDisabilityLimitForEmployer(): Double =
            roundTo2DecimalPoint(socialInsuranceLimit * disabilityInsuranceTax.forEmployer)
}

@JsonRootName("PensionInsuranceTax")
data class PensionInsuranceTax(
        @set:JsonProperty("ForEmployee")
        var forEmployee: Double = 0.0,

        @set:JsonProperty("ForEmployer")
        var forEmployer: Double = 0.0
)


@JsonRootName("DisabilityInsuranceTax")
data class DisabilityInsuranceTax(
        @set:JsonProperty("ForEmployee")
        var forEmployee: Double = 0.0,

        @set:JsonProperty("ForEmployer")
        var forEmployer: Double = 0.0
)

@JsonRootName("GettingIncomeCost")
data class GettingIncomeCost(
        @set:JsonProperty("Resident")
        var resident: Double = 0.0,

        @set:JsonProperty("Non-resident")
        var nonResident: Double = 0.0
)