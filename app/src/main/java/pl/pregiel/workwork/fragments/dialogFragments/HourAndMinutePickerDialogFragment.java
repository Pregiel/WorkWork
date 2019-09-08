package pl.pregiel.workwork.fragments.dialogFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

import pl.pregiel.workwork.R;


public class HourAndMinutePickerDialogFragment extends DialogFragment {
    public static String TAG = "HOURANDMINUTE_PICKER";

    private View.OnClickListener onOkClickListener, onCancelClickListener;
    private int hour = 8, minute = 0;

    public HourAndMinutePickerDialogFragment() {
        this.onCancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_hourminutepicker, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView hourTextView = view.findViewById(R.id.textView_hourMinutePicker_hour);
        final NumberPicker hourNumberPicker = view.findViewById(R.id.numberPicker_hourAndMinutePicker_hour);
        hourNumberPicker.setMaxValue(23);
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setValue(hour);
        hourNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(Locale.getDefault(),"%02d", i);
            }
        });
        hourNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hourTextView.setText(String.format(getString(R.string.format_hourWithSuffix), newVal));
                hour = newVal;
            }
        });
        hourTextView.setText(String.format(getString(R.string.format_hourWithSuffix), hourNumberPicker.getValue()));

        final TextView minuteTextView = view.findViewById(R.id.textView_hourMinutePicker_minute);
        final NumberPicker minuteNumberPicker = view.findViewById(R.id.numberPicker_hourAndMinutePicker_minute);
        minuteNumberPicker.setMaxValue(59);
        minuteNumberPicker.setMinValue(0);
        minuteNumberPicker.setValue(minute);
        minuteNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format(Locale.getDefault(),"%02d", i);
            }
        });
        minuteNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minuteTextView.setText(String.format(getString(R.string.format_minuteWithSuffix), newVal));
                minute = newVal;
            }
        });
        minuteTextView.setText(String.format(getString(R.string.format_minuteWithSuffix), minuteNumberPicker.getValue()));
        if (onOkClickListener == null) {
            onOkClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            };
        }

        final Button okButton = view.findViewById(R.id.button_hourMinutePicker_ok);
        okButton.setOnClickListener(onOkClickListener);

        if (onCancelClickListener == null) {
            onCancelClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            };
        }

        final Button cancelButton = view.findViewById(R.id.button_hourMinutePicker_cancel);
        cancelButton.setOnClickListener(onCancelClickListener);
    }

    public void setHourAndMinute(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setOnOkClickListener(View.OnClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public void setOnCancelClickListener(View.OnClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
}
