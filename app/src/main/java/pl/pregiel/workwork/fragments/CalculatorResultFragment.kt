package pl.pregiel.workwork.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_calculator_result.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.calculation.Calculation
import pl.pregiel.workwork.calculation.CalculationFromBrutto
import pl.pregiel.workwork.calculation.CalculationFromNetto
import pl.pregiel.workwork.utils.XmlParser
import pl.pregiel.workwork.utils.roundTo2DecimalPoint
import java.io.InputStream

class CalculatorResultFragment : Fragment(), TaggedFragment {

    companion object {
        const val FRAGMENT_TAG = "CALCULATOR_RESULT"
    }

    override fun fragmentTag(): String = FRAGMENT_TAG

    private lateinit var calculation : Calculation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val salary = arguments!!.getDouble(CalculatorInputFragment.SALARY, 0.0)
        val salaryType = arguments!!.getInt(CalculatorInputFragment.SALARY_TYPE, 0)
        val taxYear = arguments!!.getInt(CalculatorInputFragment.TAX_YEAR, 0)
        val workLocation = arguments!!.getBoolean(CalculatorInputFragment.WORK_LOCATION, true)
        val old26 = arguments!!.getBoolean(CalculatorInputFragment.OLD26, true)

        val stream: InputStream = when (taxYear) {
            0 -> resources.openRawResource(R.raw.tax2019)
            else -> resources.openRawResource(R.raw.tax2019)
        }
        val tax = XmlParser().parse(stream)

        calculation = when (salaryType) {
            0 -> CalculationFromBrutto(tax, roundTo2DecimalPoint(salary), workLocation, old26)
            else -> CalculationFromNetto(tax, roundTo2DecimalPoint(salary), workLocation, old26)
        }
        calculation.calculate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calculator_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showOutput(calculation)
        initPieChart(calculation)
    }

    private fun showOutput(calculation: Calculation) {
        textView_calcResult_brutto.text = getString(R.string.global_moneyWithCurrency, calculation.brutto)
        textView_calcResult_netto.text = getString(R.string.global_moneyWithCurrency, calculation.netto)
        textView_calcResult_pensionInsurance.text =
                getString(R.string.global_moneyWithCurrency, calculation.pensionInsurance)
        textView_calcResult_disabilityInsurance.text =
                getString(R.string.global_moneyWithCurrency, calculation.disabilityInsurance)
        textView_calcResult_sicknessInsurance.text =
                getString(R.string.global_moneyWithCurrency, calculation.sicknessInsurance)
        textView_calcResult_healthInsurance.text =
                getString(R.string.global_moneyWithCurrency, calculation.healthInsurance)
        textView_calcResult_pitAdvance.text =
                getString(R.string.global_moneyWithCurrency, calculation.gettingIncomeAdvance)
    }

    private fun initPieChart(calculation: Calculation) {
        val pie = pieChart_calcResult_pieChart
        pie.setDrawEntryLabels(false)
        pie.setUsePercentValues(true)
        pie.description.isEnabled = false
        pie.legend.isEnabled = false

        pie.offsetLeftAndRight(5)
        pie.offsetTopAndBottom(20)

        setPieChartData(pie, calculation)
    }

    private fun setPieChartData(pie: PieChart, calculation: Calculation) {
        val entries = ArrayList<PieEntry>()

        entries.add(PieEntry(calculation.netto.toFloat(), getString(R.string.calcResult_nettoSalary)))
        entries.add(PieEntry(calculation.pensionInsurance.toFloat(), getString(R.string.calcResult_pensionInsurance)))
        entries.add(PieEntry(calculation.disabilityInsurance.toFloat(), getString(R.string.calcResult_disabilityInsurance)))
        entries.add(PieEntry(calculation.sicknessInsurance.toFloat(), getString(R.string.calcResult_sicknessInsurance)))
        entries.add(PieEntry(calculation.healthInsurance.toFloat(), getString(R.string.calcResult_healthInsurance)))
        entries.add(PieEntry(calculation.gettingIncomeAdvance.toFloat(), getString(R.string.calcResult_gettingIncomeAdvance)))

        val dataSet = PieDataSet(entries, getString(R.string.calcResult_bruttoSalary))

        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter(pie))
        pieData.setValueTextColor(Color.DKGRAY)
        pieData.setValueTextSize(11f)
        pie.data = pieData

        pie.highlightValues(null)
        pie.invalidate()

        colorSquares(colors)
    }

    private fun colorSquares(colors: ArrayList<Int>) {
        view_calcResult_nettoSquare.setBackgroundColor(colors[0])
        view_calcResult_pensionSquare.setBackgroundColor(colors[1])
        view_calcResult_disabilitySquare.setBackgroundColor(colors[2])
        view_calcResult_sicknessSquare.setBackgroundColor(colors[3])
        view_calcResult_healthSquare.setBackgroundColor(colors[4])
        view_calcResult_gettingIncomeSquare.setBackgroundColor(colors[5])
    }

}
