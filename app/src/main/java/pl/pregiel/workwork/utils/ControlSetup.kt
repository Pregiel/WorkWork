package pl.pregiel.workwork.utils


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.fragments.dialogFragments.HourAndMinutePickerDialogFragment
import pl.pregiel.workwork.listeners.TextListener
import java.util.*

object ControlSetup {

    fun setupDatePicker(context: Context, editText: EditText, dateString: String? = null) {
        var date = dateString ?: ""

        if (!date.matches(context.getString(R.string.match_date).toRegex())) {
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            date = String.format(context.getString(R.string.format_date),
                    currentDay, currentMonth + 1, currentYear)
        }
        editText.setText(date)

        editText.setOnClickListener {
            val timeSplit = editText.text.toString().split("\\.".toRegex())
            var initialDay: Int
            var initialMonth: Int
            var initialYear: Int
            try {
                initialDay = Integer.valueOf(timeSplit[0])
                initialMonth = Integer.valueOf(timeSplit[1]) - 1
                initialYear = Integer.valueOf(timeSplit[2])
            } catch (e: NumberFormatException) {
                val calendar = Calendar.getInstance()
                initialDay = calendar.get(Calendar.DAY_OF_MONTH)
                initialMonth = calendar.get(Calendar.MONTH)
                initialYear = calendar.get(Calendar.YEAR)
            }

            val datePickerDialog = DatePickerDialog(context,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        editText.setText(String.format(view.context.getString(R.string.format_date),
                                dayOfMonth, month + 1, year))
                    }, initialYear, initialMonth, initialDay)
            datePickerDialog.show()
        }
    }

    fun setupTimePicker(context: Context, editText: EditText) {
        setupTimePickerWithDelay(context, editText, 0)
    }

    fun setupTimePicker(context: Context, editText: EditText, defaultMinute: Int) {
        val defaultTime = Utils.timeMinutesToString(defaultMinute)
        setupTimePicker(context, editText, defaultTime)
    }

    fun setupTimePicker(context: Context, editText: EditText, defaultHour: Int, defaultMinute: Int) {
        val defaultTime = context.getString(R.string.format_time, defaultHour, defaultMinute)
        setupTimePicker(context, editText, defaultTime)
    }

    fun setupTimePicker(context: Context, editText: EditText, defaultTime: String) {
        if (!defaultTime.matches(context.getString(R.string.match_time).toRegex())) {
            setupTimePicker(context, editText, 0, 0)
            return
        }
        editText.setText(defaultTime)

        editText.setOnClickListener {
            val timeSplit = editText.text.toString().split(":")
            var initialHour: Int
            var initialMinute: Int
            try {
                initialHour = Integer.valueOf(timeSplit[0])
                initialMinute = Integer.valueOf(timeSplit[1])
            } catch (e: NumberFormatException) {
                val calendar = Calendar.getInstance()
                initialHour = calendar.get(Calendar.HOUR_OF_DAY)
                initialMinute = calendar.get(Calendar.MINUTE)
            }

            val timePickerDialog = TimePickerDialog(context,
                    TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                        editText.setText(String.format(view.context.getString(R.string.format_time), hour, minute))
                    },
                    initialHour,
                    initialMinute,
                    true)
            timePickerDialog.show()
        }
    }

    fun setupTimePickerWithDelay(context: Context, editText: EditText, modifier: Int = 0) {
        var hourModifier = modifier / 60
        val minuteModifier = modifier % 60

        val calendar = Calendar.getInstance()

        val currentMinute = (calendar.get(Calendar.MINUTE) + minuteModifier) % 60
        hourModifier += (calendar.get(Calendar.MINUTE) + minuteModifier) / 60

        var currentHour = (calendar.get(Calendar.HOUR_OF_DAY) + hourModifier) % 24
        while (currentHour < 0) {
            currentHour += 24
        }

        setupTimePicker(context, editText, currentHour, currentMinute)
    }

    fun setupTimeAmountPicker(context: Context, editText: EditText, defaultHour: Int, defaultMinute: Int) {
        val defaultTime = String.format(context.getString(R.string.format_timeWithSuffixes), defaultHour, defaultMinute)
        setupTimeAmountPicker(context, editText, defaultTime)
    }

    fun setupTimeAmountPicker(context: Context, editText: EditText, defaultMinute: Int) {
        val defaultTime = Utils.timeMinutesToStringWithSuffixes(defaultMinute)
        setupTimeAmountPicker(context, editText, defaultTime)
    }

    fun setupTimeAmountPicker(context: Context, editText: EditText, defaultTimeWithSuffixes: String) {
        if (!defaultTimeWithSuffixes.matches(context.getString(R.string.match_timeWithSuffixes).toRegex())) {
            setupTimeAmountPicker(context, editText, 1, 0)
            return
        }
        editText.setText(defaultTimeWithSuffixes)

        editText.setOnClickListener {
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val hourAndMinutePickerDialogFragment = HourAndMinutePickerDialogFragment()
            val values = editText.text.toString().split("h ")

            val hour = values[0].replace("\\D".toRegex(), "").toIntOrNull() ?: 1
            val minute = values[1].replace("\\D".toRegex(), "").toIntOrNull() ?: 0

            hourAndMinutePickerDialogFragment.setHourAndMinute(hour, minute)
            hourAndMinutePickerDialogFragment.onOkClickListener = View.OnClickListener {
                editText.setText(context.getString(R.string.format_timeWithSuffixes,
                        hourAndMinutePickerDialogFragment.hour, hourAndMinutePickerDialogFragment.minute))
                hourAndMinutePickerDialogFragment.dismiss()
            }
            hourAndMinutePickerDialogFragment.show(fragmentManager, HourAndMinutePickerDialogFragment.TAG)
        }
    }

    fun setupCustomRadioButtonGroup(vararg radioButtons: RadioButton) {
        setupCustomRadioButtonGroup(true, *radioButtons)
    }

    fun setupCustomRadioButtonGroup(setDisabled: Boolean, vararg radioButtons: RadioButton) {
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                for (radioButton in radioButtons) {
                    if (buttonView.id == radioButton.id) {
                        if (setDisabled) {
                            AndroidUtils.setEnabledControl(true,
                                    radioButton.parent as ViewGroup,
                                    radioButton.id)
                        }
                    } else {
                        radioButton.isChecked = false
                        if (setDisabled) {
                            AndroidUtils.setEnabledControl(false, radioButton.parent as ViewGroup, radioButton.id)
                        }
                    }
                }
            }
        }

        for (radioButton in radioButtons) {
            radioButton.setOnCheckedChangeListener(checkedChangeListener)

            if (radioButton !== radioButtons[0]) {
                if (setDisabled) {
                    AndroidUtils.setEnabledControl(false, radioButton.parent as ViewGroup, radioButton.id)
                }
            }
        }

    }

    fun setupSalaryEditText(editText: EditText, defaultValue: Double) {
        setupSalaryEditText(editText, Utils.formatDoubleToString(defaultValue))
    }

    fun setupSalaryEditText(editText: EditText, defaultValue: String) {
        editText.addTextChangedListener(object : TextListener() {
            override fun onTextChanged(before: String, old: String, new: String, after: String) {
                val oldText = before + old + after
                val newText = before + new + after

                startUpdates()
                if (newText.matches("^[0-9]*[,.]?[0-9]{0,2}$".toRegex())) {
                    editText.setText(newText.replace("\\.".toRegex(), ","))
                    editText.setSelection(before.length + new.length)
                } else {
                    editText.setText(oldText)
                    editText.setSelection(before.length + old.length)
                }
                endUpdates()
            }
        })
        editText.setText(defaultValue)
    }

    fun setupCurrencySpinner(context: Context, spinner: Spinner, selectedItem: Int) {
        spinner.setSelection(selectedItem)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var lastSelected = spinner.selectedItemPosition
            var ignore = true

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (!ignore) {
                    CustomAlert.buildAlert(context,
                            R.string.global_changeCurrency,
                            R.string.alert_changeCurrency,
                            R.string.global_yes,
                            {
                                lastSelected = spinner.selectedItemPosition
                            },
                            R.string.global_cancel,
                            {
                                if (!ignore) {
                                    ignore = true
                                    spinner.setSelection(lastSelected)
                                }
                            }
                    ).show()
                } else {
                    ignore = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
