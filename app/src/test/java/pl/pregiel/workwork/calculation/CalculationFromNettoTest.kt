package pl.pregiel.workwork.calculation

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import pl.pregiel.workwork.data.pojo.DisabilityInsuranceTax
import pl.pregiel.workwork.data.pojo.GettingIncomeCost
import pl.pregiel.workwork.data.pojo.PensionInsuranceTax
import pl.pregiel.workwork.data.pojo.Tax

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("calculation")
class CalculationFromNettoTest {
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
    fun calculateFromNetto_residentAnd26Old() {
        val calculationFromBrutto = CalculationFromNetto(
                tax = tax,
                netto = 3000.0,
                workLocation = true,
                old26 = true
        )
        calculationFromBrutto.calculate()

        assertAll(
                { assertEquals(3000.0, calculationFromBrutto.netto, delta) },
                { assertEquals(410.91, calculationFromBrutto.pensionInsurance, delta) },
                { assertEquals(63.15, calculationFromBrutto.disabilityInsurance, delta) },
                { assertEquals(103.15, calculationFromBrutto.sicknessInsurance, delta) },
                { assertEquals(326.97, calculationFromBrutto.healthInsurance, delta) },
                { assertEquals(306.00, calculationFromBrutto.gettingIncomeAdvance, delta) },
                { assertEquals(4210.18, calculationFromBrutto.brutto, delta) }
        )
    }

    @Test
    fun calculateFromNetto_nonResidentAnd26Old() {
        val calculationFromBrutto = CalculationFromNetto(
                tax = tax,
                netto = 3000.0,
                workLocation = false,
                old26 = true
        )
        calculationFromBrutto.calculate()

        assertAll(
                { assertEquals(3000.0, calculationFromBrutto.netto, delta) },
                { assertEquals(410.17, calculationFromBrutto.pensionInsurance, delta) },
                { assertEquals(63.04, calculationFromBrutto.disabilityInsurance, delta) },
                { assertEquals(102.96, calculationFromBrutto.sicknessInsurance, delta) },
                { assertEquals(326.37, calculationFromBrutto.healthInsurance, delta) },
                { assertEquals(300.00, calculationFromBrutto.gettingIncomeAdvance, delta) },
                { assertEquals(4202.54, calculationFromBrutto.brutto, delta) }
        )
    }

    @Test
    fun calculateFromNetto_residentAndNot26Old() {
        val calculationFromBrutto = CalculationFromNetto(
                tax = tax,
                netto = 3000.0,
                workLocation = true,
                old26 = false
        )
        calculationFromBrutto.calculate()

        assertAll(
                { assertEquals(3000.0, calculationFromBrutto.netto, delta) },
                { assertEquals(372.88, calculationFromBrutto.pensionInsurance, delta) },
                { assertEquals(57.31, calculationFromBrutto.disabilityInsurance, delta) },
                { assertEquals(93.60, calculationFromBrutto.sicknessInsurance, delta) },
                { assertEquals(296.70, calculationFromBrutto.healthInsurance, delta) },
                { assertEquals(0.0, calculationFromBrutto.gettingIncomeAdvance, delta) },
                { assertEquals(3820.49, calculationFromBrutto.brutto, delta) }
        )
    }

    @Test
    fun calculateFromNetto_nonResidentAndNot26Old() {
        val calculationFromBrutto = CalculationFromNetto(
                tax = tax,
                netto = 3000.0,
                workLocation = false,
                old26 = false
        )
        calculationFromBrutto.calculate()

        assertAll(
                { assertEquals(3000.0, calculationFromBrutto.netto, delta) },
                { assertEquals(372.88, calculationFromBrutto.pensionInsurance, delta) },
                { assertEquals(57.31, calculationFromBrutto.disabilityInsurance, delta) },
                { assertEquals(93.60, calculationFromBrutto.sicknessInsurance, delta) },
                { assertEquals(296.70, calculationFromBrutto.healthInsurance, delta) },
                { assertEquals(0.0, calculationFromBrutto.gettingIncomeAdvance, delta) },
                { assertEquals(3820.49, calculationFromBrutto.brutto, delta) }
        )
    }

}