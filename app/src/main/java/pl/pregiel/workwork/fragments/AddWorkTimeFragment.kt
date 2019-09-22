package pl.pregiel.workwork.fragments

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_addworktime.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.Settings
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.database.services.WorkTimeService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.data.pojo.WorkTime
import pl.pregiel.workwork.exceptions.EmptyFieldException
import pl.pregiel.workwork.exceptions.ShowToastException
import pl.pregiel.workwork.utils.ControlSetup
import pl.pregiel.workwork.utils.ErrorToasts
import pl.pregiel.workwork.utils.Utils
import kotlin.math.roundToInt


class AddWorkTimeFragment : FormFragment(), TaggedFragment {

    override fun fragmentTag() = "ADD_WORKTIME"

    private lateinit var workService: WorkService
    private lateinit var workTimeService: WorkTimeService

    private lateinit var work: Work

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments
        workService = WorkService(context!!)
        workTimeService = WorkTimeService(context!!)

        work = workService.getById(arguments!!.getInt("work_id"))!!
        activity?.title = String.format(getString(R.string.title_addWorkTime), work.title)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addworktime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ControlSetup.setupDatePicker(context!!, editText_addWorkTime_date)
        ControlSetup.setupTimeAmountPicker(context!!, editText_addWorkTime_amount, work.timeAmount)
        ControlSetup.setupCustomRadioButtonGroup(radioButton_addWorkTime_rangeTime, radioButton_addWorkTime_amountTime)
        ControlSetup.setupSalaryEditText(editText_addWorkTime_salary, Settings.DEFAULT_HOURLY_WAGE.toString())
        ControlSetup.setupCurrencySpinner(context!!, spinner_addWorkTime_currency, work.currency)

        if (work.timeMode == 1) {
            ControlSetup.setupTimePickerWithDelay(context!!, editText_addWorkTime_from, work.timeToNow)
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_to)
        } else {
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_from, work.timeFrom)
            ControlSetup.setupTimePicker(context!!, editText_addWorkTime_to, work.timeTo)
        }

        button_addWorkTime_add.setOnClickListener {
            try {
                val workTime = WorkTime()
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

                workTime.salary = (salaryString.toDouble() * 100).roundToInt()
                workTime.salaryMode = if (radioButton_addWorkTime_salaryPerHour.isChecked) 0 else 1
                workTime.info = editText_addWorkTime_info.text.toString()
                workTime.workId = work.id

                workTimeService.create(workTime)

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
