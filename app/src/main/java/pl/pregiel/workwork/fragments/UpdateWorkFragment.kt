package pl.pregiel.workwork.fragments

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_addwork.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.ValidationConst
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.exceptions.EmptyFieldException
import pl.pregiel.workwork.exceptions.ShowToastException
import pl.pregiel.workwork.exceptions.TooShortFieldException
import pl.pregiel.workwork.utils.ControlSetup
import pl.pregiel.workwork.utils.ErrorToasts
import pl.pregiel.workwork.utils.Utils
import kotlin.math.round


class UpdateWorkFragment : FormFragment(), TaggedFragment {

    override fun fragmentTag() = "UPDATE_WORK"

    private lateinit var workService: WorkService
    private lateinit var work: Work

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workService = WorkService(context!!)

        work = workService.getById(arguments!!.getInt("work_id"))!!
        activity!!.title = getString(R.string.title_updateWork, "tak")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addwork, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText_addWork_title.setText(work.title)

        ControlSetup.setupTimePicker(context!!, editText_addWork_from, work.timeFrom)
        ControlSetup.setupTimePicker(context!!, editText_addWork_to, work.timeTo)
        ControlSetup.setupTimeAmountPicker(context!!, editText_addWork_toNow, work.timeToNow)
        ControlSetup.setupTimeAmountPicker(context!!, editText_addWork_amount, work.timeAmount)
        ControlSetup.setupCustomRadioButtonGroup(false, radioButton_addWork_rangeTime, radioButton_addWork_amountTime, radioButton_addWork_toNow)

        when {
            work.timeMode == 0 -> radioButton_addWork_rangeTime.isChecked = true
            work.timeMode == 1 -> radioButton_addWork_amountTime.isChecked = true
            else -> radioButton_addWork_toNow.isChecked = true
        }

        ControlSetup.setupSalaryEditText(editText_addWork_salary, work.salary.toDouble() / 100)

        when {
            work.salaryMode == 0 -> radioButton_addWork_salaryPerHour.isChecked = true
            else -> radioButton_addWork_salaryForAll.isChecked = true
        }

        ControlSetup.setupCurrencySpinner(context!!, spinner_addWork_currency, work.currency)

        editText_addWork_info.setText(work.info)

        button_addWork_add.setText(R.string.global_update)
        button_addWork_add.setOnClickListener {
            try {
                if (editText_addWork_title.text.toString().length < ValidationConst.TITLE_MINIMUM_LENGTH) {
                    throw TooShortFieldException(getString(R.string.global_title), ValidationConst.TITLE_MINIMUM_LENGTH)
                }
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

                work.salary = round(java.lang.Float.valueOf(salaryString) * 100).toInt()
                work.salaryMode = if (radioButton_addWork_salaryPerHour.isChecked) 0 else 1
                work.currency = spinner_addWork_currency.selectedItemPosition
                work.info = editText_addWork_info.text.toString()

                workService.update(work)

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
