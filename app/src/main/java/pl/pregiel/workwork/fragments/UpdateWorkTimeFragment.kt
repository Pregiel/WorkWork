package pl.pregiel.workwork.fragments

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_addworktime.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.database.services.WorkTimeService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.data.pojo.WorkTime
import pl.pregiel.workwork.exceptions.EmptyFieldException
import pl.pregiel.workwork.exceptions.ShowToastException
import pl.pregiel.workwork.utils.ControlSetup
import pl.pregiel.workwork.utils.ErrorToasts
import pl.pregiel.workwork.utils.Utils
import kotlin.math.round


class UpdateWorkTimeFragment : FormFragment(), TaggedFragment {

    override fun fragmentTag() = "UPDATE_WORKTIME"

    private lateinit var workService: WorkService
    private lateinit var workTimeService: WorkTimeService

    private lateinit var work: Work
    private lateinit var workTime: WorkTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workService = WorkService(context!!)
        workTimeService = WorkTimeService(context!!)

        workTime = workTimeService.getById(arguments!!.getInt("worktime_id"))!!
        work = workService.getById(workTime.workId)!!
        activity?.setTitle(R.string.global_update)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addworktime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ControlSetup.setupDatePicker(context!!, editText_addWorkTime_date, workTime.day)
        ControlSetup.setupCustomRadioButtonGroup(radioButton_addWorkTime_rangeTime, radioButton_addWorkTime_amountTime)

        if (workTime.timeFrom > -1 && workTime.timeTo > -1) {
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_from, workTime.timeFrom)
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_to, workTime.timeTo)
            radioButton_addWorkTime_rangeTime.isChecked = true
        } else {
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_from, work.timeFrom)
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_to, work.timeFrom + workTime.time)
            radioButton_addWorkTime_amountTime.isChecked = true
        }

        ControlSetup.setupTimeAmountPicker(context!!, editText_addWorkTime_amount, workTime.time)
        ControlSetup.setupSalaryEditText(editText_addWorkTime_salary, workTime.salary.toDouble() / 100)
        ControlSetup.setupCurrencySpinner(context!!, spinner_addWorkTime_currency, work.currency)

        if (workTime.salaryMode == 0) {
            radioButton_addWorkTime_salaryPerHour.isChecked = true
        } else {
            radioButton_addWorkTime_salaryForAll.isChecked = true
        }

        editText_addWorkTime_info.setText(workTime.info)

        button_addWorkTime_add.setText(R.string.global_update)
        button_addWorkTime_add.setOnClickListener {
            try {
                workTime.day = editText_addWorkTime_date.text.toString()
                if (radioButton_addWorkTime_rangeTime.isChecked) {
                    val timeFromInMinutes = Utils.timeStringToMinutes(editText_addWorkTime_from.text.toString())
                    val timeToInMinutes = Utils.timeStringToMinutes(editText_addWorkTime_to.text.toString())

                    workTime.timeFrom = timeFromInMinutes
                    workTime.timeTo = timeToInMinutes
                    workTime.time = -1

                    workTime.time = if (timeFromInMinutes < timeToInMinutes)
                        timeToInMinutes - timeFromInMinutes
                    else
                        1440 + timeToInMinutes - timeFromInMinutes
                } else {
                    val timeAmountInMinutes = Utils.timeStringWithSuffixesToMinutes(editText_addWorkTime_amount.text.toString())

                    workTime.timeFrom = -1
                    workTime.timeTo = -1
                    workTime.time = timeAmountInMinutes
                }

                val salaryString = editText_addWorkTime_salary.text.toString().replace(",", ".")
                if (salaryString.isEmpty()) {
                    throw EmptyFieldException(getString(R.string.global_salary))
                }

                workTime.salary = round(java.lang.Float.valueOf(salaryString) * 100).toInt()
                workTime.salaryMode = if (radioButton_addWorkTime_salaryPerHour.isChecked) 0 else 1
                workTime.info = editText_addWorkTime_info.text.toString()
                workTime.workId = work.id

                workTimeService.update(workTime)

                if (work.currency != spinner_addWorkTime_currency.selectedItemPosition) {
                    work.currency = spinner_addWorkTime_currency.selectedItemPosition
                    workService.update(work)
                }

                if (context != null) {
                    (context as FragmentActivity).onBackPressed()
                }
            } catch (e: ShowToastException) {
                e.showToast(context)
            } catch (e: Exception) {
                ErrorToasts.showUnknownErrorToast(context)
            }
        }

        button_addWorkTime_cancel.setOnClickListener {
            if (context != null) {
                (context as FragmentActivity).onBackPressed()
            }
        }
    }
}
