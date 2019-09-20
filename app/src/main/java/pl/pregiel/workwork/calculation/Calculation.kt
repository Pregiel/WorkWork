package pl.pregiel.workwork.calculation

abstract class Calculation {
    open var brutto : Double = 0.0
    open var netto : Double = 0.0
    var pensionInsurance : Double = 0.0
    var disabilityInsurance : Double = 0.0
    var sicknessInsurance : Double = 0.0
    var healthInsurance : Double = 0.0
    var gettingIncomeAdvance : Double = 0.0

    abstract fun calculate()
}