package pl.pregiel.workwork.calculation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.pregiel.workwork.data.pojo.DisabilityInsuranceTax
import pl.pregiel.workwork.data.pojo.GettingIncomeCost
import pl.pregiel.workwork.data.pojo.PensionInsuranceTax
import pl.pregiel.workwork.data.pojo.Tax

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("calculation")
internal class CalculationFromBruttoTest {
    private val delta = 1e-15
    private lateinit var tax: Tax

    @BeforeAll
    internal fun setUp() {
        tax = Tax(
                pensionInsuranceTax = PensionInsuranceTax(
                        forEmployee = 0.0976,
                        forEmployer = 0.0976
                ),
                disabilityInsuranceTax = DisabilityInsuranceTax(
                        forEmployee = 0.015,
                        forEmployer = 0.065
                ),
                sicknessInsuranceTax = 0.0245,
                socialInsuranceLimit = 142950.0,
                accidentInsuranceTax = 0.0167,
                gettingIncomeCost = GettingIncomeCost(
                        resident = 111.25,
                        nonResident = 139.06
                ),
                taxFree = 46.33,
                healthInsuranceTax = 0.09,
                healthInsuranceEffectiveTax = 0.0775,
                firstTaxLevel = 0.18,
                secondLevel = 85528.5,
                secondTaxLevel = 0.32
        )
    }

    @Test
    fun calculateFromBrutto_residentAnd26Old() {
        val calculationFromBrutto = CalculationFromBrutto(
                tax = tax,
                brutto = 2250.0,
                workLocation = true,
                old26 = true
        )
        calculationFromBrutto.calculate()

        assertEquals(2250.0, calculationFromBrutto.brutto, delta)
        assertEquals(219.60, calculationFromBrutto.pensionInsurance, delta)
        assertEquals(33.75, calculationFromBrutto.disabilityInsurance, delta)
        assertEquals(55.13, calculationFromBrutto.sicknessInsurance, delta)
        assertEquals(174.74, calculationFromBrutto.healthInsurance, delta)
        assertEquals(133.0, calculationFromBrutto.gettingIncomeAdvance, delta)
        assertEquals(1633.78, calculationFromBrutto.netto, delta)
    }

    @Test
    fun calculateFromBrutto_nonResidentAnd26Old() {
        val calculationFromBrutto = CalculationFromBrutto(
                tax = tax,
                brutto = 2250.0,
                workLocation = false,
                old26 = true
        )
        calculationFromBrutto.calculate()

        assertEquals(2250.0, calculationFromBrutto.brutto, delta)
        assertEquals(219.60, calculationFromBrutto.pensionInsurance, delta)
        assertEquals(33.75, calculationFromBrutto.disabilityInsurance, delta)
        assertEquals(55.13, calculationFromBrutto.sicknessInsurance, delta)
        assertEquals(174.74, calculationFromBrutto.healthInsurance, delta)
        assertEquals(128.0, calculationFromBrutto.gettingIncomeAdvance, delta)
        assertEquals(1638.78, calculationFromBrutto.netto, delta)
    }

    @Test
    fun calculateFromBrutto_residentAndNot26Old() {
        val calculationFromBrutto = CalculationFromBrutto(
                tax = tax,
                brutto = 2250.0,
                workLocation = true,
                old26 = false
        )
        calculationFromBrutto.calculate()

        assertEquals(2250.0, calculationFromBrutto.brutto, delta)
        assertEquals(219.60, calculationFromBrutto.pensionInsurance, delta)
        assertEquals(33.75, calculationFromBrutto.disabilityInsurance, delta)
        assertEquals(55.13, calculationFromBrutto.sicknessInsurance, delta)
        assertEquals(174.74, calculationFromBrutto.healthInsurance, delta)
        assertEquals(0.0, calculationFromBrutto.gettingIncomeAdvance, delta)
        assertEquals(1766.78, calculationFromBrutto.netto, delta)
    }

    @Test
    fun calculateFromBrutto_nonResidentAndNot26Old() {
        val calculationFromBrutto = CalculationFromBrutto(
                tax = tax,
                brutto = 2250.0,
                workLocation = false,
                old26 = false
        )
        calculationFromBrutto.calculate()

        assertEquals(2250.0, calculationFromBrutto.brutto, delta)
        assertEquals(219.60, calculationFromBrutto.pensionInsurance, delta)
        assertEquals(33.75, calculationFromBrutto.disabilityInsurance, delta)
        assertEquals(55.13, calculationFromBrutto.sicknessInsurance, delta)
        assertEquals(174.74, calculationFromBrutto.healthInsurance, delta)
        assertEquals(0.0, calculationFromBrutto.gettingIncomeAdvance, delta)
        assertEquals(1766.78, calculationFromBrutto.netto, delta)
    }

    @Test
    fun calculateFromBrutto_healthInsuranceLowered() {
        val calculationFromBrutto = CalculationFromBrutto(
                tax = tax,
                brutto = 500.0,
                workLocation = true,
                old26 = true
        )
        calculationFromBrutto.calculate()

        assertEquals(500.0, calculationFromBrutto.brutto, delta)
        assertEquals(48.8, calculationFromBrutto.pensionInsurance, delta)
        assertEquals(7.5, calculationFromBrutto.disabilityInsurance, delta)
        assertEquals(12.25, calculationFromBrutto.sicknessInsurance, delta)
        assertEquals(11.27, calculationFromBrutto.healthInsurance, delta)
        assertEquals(0.0, calculationFromBrutto.gettingIncomeAdvance, delta)
        assertEquals(420.18, calculationFromBrutto.netto, delta)
    }

    @Test
    fun calculateFromBrutto_overSocialLimit() {
        val calculationFromBrutto = CalculationFromBrutto(
                tax = tax,
                brutto = 150000.0,
                workLocation = true,
                old26 = true
        )
        calculationFromBrutto.calculate()

        assertEquals(150000.0, calculationFromBrutto.brutto, delta)
        assertEquals(13951.92, calculationFromBrutto.pensionInsurance, delta)
        assertEquals(2144.25, calculationFromBrutto.disabilityInsurance, delta)
        assertEquals(3675.0, calculationFromBrutto.sicknessInsurance, delta)
        assertEquals(11720.59, calculationFromBrutto.healthInsurance, delta)
        assertEquals(13282.0, calculationFromBrutto.gettingIncomeAdvance, delta)
        assertEquals(105226.24, calculationFromBrutto.netto, delta)
    }

}