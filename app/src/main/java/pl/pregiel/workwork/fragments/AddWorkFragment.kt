package pl.pregiel.workwork.fragments

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_addwork.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.Settings
import pl.pregiel.workwork.ValidationConst
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.exceptions.EmptyFieldException
import pl.pregiel.workwork.exceptions.ShowToastException
import pl.pregiel.workwork.exceptions.TooShortFieldException
import pl.pregiel.workwork.utils.ControlSetup
import pl.pregiel.workwork.utils.ErrorToasts
import pl.pregiel.workwork.utils.Utils
import kotlin.math.roundToInt


class AddWorkFragment : FormFragment(), TaggedFragment {

    override fun fragmentTag() = "ADD_WORK"

    private lateinit var workService: WorkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workService = WorkService(context!!)

        activity?.setTitle(R.string.title_addWork)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addwork, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ControlSetup.setupTimePicker(context!!, editText_addWork_from, 8, 0)
        ControlSetup.setupTimePicker(context!!, editText_addWork_to, 16, 0)
        ControlSetup.setupTimeAmountPicker(context!!, editText_addWork_toNow, 1, 0)
        ControlSetup.setupTimeAmountPicker(context!!, editText_addWork_amount, 8, 0)
        ControlSetup.setupCustomRadioButtonGroup(false, radioButton_addWork_rangeTime,
                radioButton_addWork_toNow, radioButton_addWork_amountTime)
        ControlSetup.setupSalaryEditText(editText_addWork_salary, Settings.DEFAULT_HOURLY_WAGE.toDouble())

        button_addWork_add.setOnClickListener {
            try {
                if (editText_addWork_title.text.toString().length < ValidationConst.TITLE_MINIMUM_LENGTH) {
                    throw TooShortFieldException(getString(R.string.global_title), ValidationConst.TITLE_MINIMUM_LENGTH)
                }

                val work = Work()
                work.title = editText_addWork_title.text.toString()
                work.timeMode = when {
                    radioButton_addWork_rangeTime.isChecked -> 0
                    radioButton_addWork_toNow.isChecked -> 1
                    else -> 2
                }
                work.timeFrom = Utils.timeStringToMinutes(editText_addWork_from.text.toString())
                work.timeTo = Utils.timeStringToMinutes(editText_addWork_to.text.toString())
                work.timeAmount = Utils.timeStringWithSuffixesToMinutes(editText_addWork_amount.text.toString())
                work.timeToNow = -Utils.timeStringWithSuffixesToMinutes(editText_addWork_toNow.text.toString())

                val salaryString = editText_addWork_salary.text.toString().replace(",", ".")
                if (salaryString.isEmpty()) {
                    throw EmptyFieldException(getString(R.string.global_salary))
                }

                work.salary = (salaryString.toDouble() * 100).roundToInt()
                work.salaryMode = if (radioButton_addWork_salaryPerHour.isChecked) 0 else 1
                work.currency = spinner_addWork_currency.selectedItemPosition
                work.info = editText_addWork_info.text.toString()

                workService.create(work)

                if (context != null) {
                    (context as FragmentActivity).onBackPressed()
                }
            } catch (e: ShowToastException) {
                e.showToast(context)
            } catch (e: Exception) {
                ErrorToasts.showUnknownErrorToast(context)
                e.printStackTrace()
            }
        }

        button_addWork_cancel.setOnClickListener {
            if (context != null) {
                (context as FragmentActivity).onBackPressed()
            }
        }
    }
}
