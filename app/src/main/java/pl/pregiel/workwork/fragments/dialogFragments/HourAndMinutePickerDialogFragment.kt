package pl.pregiel.workwork.fragments.dialogFragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import kotlinx.android.synthetic.main.dialogfragment_hourminutepicker.*

import java.util.Locale

import pl.pregiel.workwork.R
import pl.pregiel.workwork.fragments.TaggedFragment


class HourAndMinutePickerDialogFragment : DialogFragment(), TaggedFragment {

    companion object {
        const val TAG = "HOURANDMINUTE_PICKER"
    }

    override fun fragmentTag() = TAG


    var onOkClickListener: View.OnClickListener? = null
    var onCancelClickListener: View.OnClickListener? = null

    var hour = 8
    var minute = 0

    init {
        this.onCancelClickListener = View.OnClickListener { dismiss() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialogfragment_hourminutepicker, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberPicker_hourAndMinutePicker_hour.maxValue = 23
        numberPicker_hourAndMinutePicker_hour.minValue = 0
        numberPicker_hourAndMinutePicker_hour.value = hour
        numberPicker_hourAndMinutePicker_hour.setFormatter { i -> String.format(Locale.getDefault(), "%02d", i) }
        numberPicker_hourAndMinutePicker_hour.setOnValueChangedListener { picker, oldVal, newVal ->
            textView_hourMinutePicker_hour.text = String.format(getString(R.string.format_hourWithSuffix), newVal)
            hour = newVal
        }
        textView_hourMinutePicker_hour.text = String.format(getString(R.string.format_hourWithSuffix), numberPicker_hourAndMinutePicker_hour.value)

        numberPicker_hourAndMinutePicker_minute.maxValue = 59
        numberPicker_hourAndMinutePicker_minute.minValue = 0
        numberPicker_hourAndMinutePicker_minute.value = minute
        numberPicker_hourAndMinutePicker_minute.setFormatter { i -> String.format(Locale.getDefault(), "%02d", i) }
        numberPicker_hourAndMinutePicker_minute.setOnValueChangedListener { _, _, newVal ->
            textView_hourMinutePicker_minute.text = String.format(getString(R.string.format_minuteWithSuffix), newVal)
            minute = newVal
        }
        textView_hourMinutePicker_minute.text = String.format(getString(R.string.format_minuteWithSuffix), numberPicker_hourAndMinutePicker_minute.value)

        button_hourMinutePicker_ok.setOnClickListener(onOkClickListener
                ?: View.OnClickListener { dismiss() })
        button_hourMinutePicker_cancel.setOnClickListener(onCancelClickListener
                ?: View.OnClickListener { dismiss() })
    }

    fun setHourAndMinute(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
    }
}
