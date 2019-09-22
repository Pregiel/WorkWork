package pl.pregiel.workwork.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_calculator_input.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.utils.FragmentOpener

class CalculatorInputFragment : Fragment(), TaggedFragment {

    companion object {
        const val SALARY = "SALARY"
        const val SALARY_TYPE = "SALARY_TYPE"
        const val TAX_YEAR = "TAX_YEAR"
        const val WORK_LOCATION = "WORK_LOCATION"
        const val OLD26 = "26OLD"
        const val FRAGMENT_TAG = "CALCULATOR_INPUT"
    }

    override fun fragmentTag(): String = FRAGMENT_TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle(R.string.title_calculator)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calculator_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_input_calculate.setOnClickListener { calculate(context!!) }
    }

    private fun calculate(context: Context) {
        if (editText_input_salary.text.isNullOrEmpty()) {
            Toast.makeText(context, getString(R.string.input_enterSalary), Toast.LENGTH_SHORT).show()
            editText_input_salary.requestFocus()
            return
        }

        val salary = editText_input_salary.text.toString().toDouble()
        if (salary == 0.0) {
            Toast.makeText(context, getString(R.string.input_salaryZero), Toast.LENGTH_SHORT).show()
            editText_input_salary.requestFocus()
            return
        }

        val bundle = Bundle()
        bundle.putDouble(SALARY, salary)
        bundle.putInt(SALARY_TYPE, spinner_input_salaryType.selectedItemPosition)
        bundle.putInt(TAX_YEAR, spinner_input_taxYear.selectedItemPosition)
        bundle.putBoolean(WORK_LOCATION, checkBox_input_workLocation.isChecked)
        bundle.putBoolean(OLD26, checkBox_input_26old.isChecked)

        FragmentOpener.openFragment(context as FragmentActivity, CalculatorResultFragment(), FragmentOpener.OpenMode.ADD, bundle)

    }


}
