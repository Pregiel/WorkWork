package pl.pregiel.workwork;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.util.Calendar;

import pl.pregiel.workwork.fragments.dialogFragments.HourAndMinutePickerDialogFragment;
import pl.pregiel.workwork.listeners.TextListener;

public class ControlSetup {

    public static void setupDatePicker(final Context context, @NonNull final EditText editText) {
        setupDatePicker(context, editText, null);
    }

    public static void setupDatePicker(final Context context, @NonNull final EditText editText, @Nullable String date) {
        if (date == null) {
            date = "";
        }

        if (!date.matches(context.getString(R.string.match_date))) {
            final Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);

            date = String.format(context.getString(R.string.format_date),
                    currentDay, (currentMonth + 1), currentYear);
        }
        editText.setText(date);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timeSplit = editText.getText().toString().split("\\.");
                int initialDay, initialMonth, initialYear;
                try {
                    initialDay = Integer.valueOf(timeSplit[0]);
                    initialMonth = Integer.valueOf(timeSplit[1]) -1;
                    initialYear = Integer.valueOf(timeSplit[2]);
                } catch (NumberFormatException e) {
                    final Calendar calendar = Calendar.getInstance();
                    initialDay = calendar.get(Calendar.DAY_OF_MONTH);
                    initialMonth = calendar.get(Calendar.MONTH);
                    initialYear = calendar.get(Calendar.YEAR);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                editText.setText(String.format(context.getString(R.string.format_date),
                                        dayOfMonth, (month + 1), year));
                            }
                        }, initialYear, initialMonth, initialDay);
                datePickerDialog.show();
            }
        });
    }

    public static void setupTimePicker(final Context context, @NonNull final EditText editText) {
        setupTimePickerWithDelay(context, editText, 0);
    }

    public static void setupTimePicker(final Context context, @NonNull final EditText editText, int defaultMinute) {
        String defaultTime = Utils.timeMinutesToString(context, defaultMinute);
        setupTimePicker(context, editText, defaultTime);
    }

    public static void setupTimePicker(final Context context, @NonNull final EditText editText, int defaultHour, int defaultMinute) {
        String defaultTime = String.format(context.getString(R.string.format_time), defaultHour, defaultMinute);
        setupTimePicker(context, editText, defaultTime);
    }

    public static void setupTimePicker(final Context context, @NonNull final EditText editText, String defaultTime) {
        if (!defaultTime.matches(context.getString(R.string.match_time))) {
            setupTimePicker(context, editText, 0, 0);
            return;
        }
        editText.setText(defaultTime);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timeSplit = editText.getText().toString().split(":");
                int initialHour, initialMinute;
                try {
                    initialHour = Integer.valueOf(timeSplit[0]);
                    initialMinute = Integer.valueOf(timeSplit[1]);
                } catch (NumberFormatException e) {
                    final Calendar calendar = Calendar.getInstance();
                    initialHour = calendar.get(Calendar.HOUR_OF_DAY);
                    initialMinute = calendar.get(Calendar.MINUTE);
                }

                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                editText.setText(String.format(context.getString(R.string.format_time), hour, minute));
                            }
                        }, initialHour, initialMinute, true);
                timePickerDialog.show();
            }
        });
    }

    public static void setupTimePickerWithDelay(final Context context, @NonNull final EditText editText, int modifier) {
        int hourModifier = modifier / 60;
        int minuteModifier = modifier % 60;

        final Calendar calendar = Calendar.getInstance();

        int currentMinute = (calendar.get(Calendar.MINUTE) + minuteModifier) % 60;
        hourModifier += (calendar.get(Calendar.MINUTE) + minuteModifier) / 60;

        int currentHour = (calendar.get(Calendar.HOUR_OF_DAY) + hourModifier) % 24;
        while (currentHour < 0) {
            currentHour += 24;
        }

        setupTimePicker(context, editText, currentHour, currentMinute);
    }

    public static void setupTimeAmountPicker(final Context context, @NonNull final EditText editText, final int defaultHour, final int defaultMinute) {
        String defaultTime = String.format(context.getString(R.string.format_timeWithSuffixes), defaultHour, defaultMinute);
        setupTimeAmountPicker(context, editText, defaultTime);
    }

    public static void setupTimeAmountPicker(final Context context, @NonNull final EditText editText, final int defaultMinute) {
        String defaultTime = Utils.timeMinutesToStringWithSuffixes(context, defaultMinute);
        setupTimeAmountPicker(context, editText, defaultTime);
    }

    public static void setupTimeAmountPicker(final Context context, @NonNull final EditText editText, final String defaultTimeWithSuffixes) {
        if (!defaultTimeWithSuffixes.matches(context.getString(R.string.match_timeWithSuffixes))) {
            setupTimeAmountPicker(context, editText, 1, 0);
            return;
        }
        editText.setText(defaultTimeWithSuffixes);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                final HourAndMinutePickerDialogFragment hourAndMinutePickerDialogFragment = new HourAndMinutePickerDialogFragment();
                String[] values = editText.getText().toString().split("h ");

                int hour, minute;

                try {
                    hour = Integer.valueOf(values[0].replaceAll("\\D", ""));
                    minute = Integer.valueOf(values[1].replaceAll("\\D", ""));
                } catch (NumberFormatException e) {
                    hour = 1;
                    minute = 0;
                }

                hourAndMinutePickerDialogFragment.setHourAndMinute(hour, minute);
                hourAndMinutePickerDialogFragment.setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(String.format(context.getString(R.string.format_timeWithSuffixes),
                                hourAndMinutePickerDialogFragment.getHour(), hourAndMinutePickerDialogFragment.getMinute()));
                        hourAndMinutePickerDialogFragment.dismiss();
                    }
                });
                hourAndMinutePickerDialogFragment.show(fragmentManager, HourAndMinutePickerDialogFragment.TAG);
            }
        });
    }

    public static void setupCustomRadioButtonGroup(final RadioButton... radioButtons) {
        setupCustomRadioButtonGroup(true, radioButtons);
    }

    public static void setupCustomRadioButtonGroup(final boolean setDisabled, final RadioButton... radioButtons) {
        if (radioButtons.length < 1)
            return;

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (RadioButton radioButton : radioButtons) {
                        if (buttonView.getId() == radioButton.getId()) {
                            if (setDisabled) {
                                Utils.setEnabledControl(true,
                                        (ViewGroup) radioButton.getParent(),
                                        radioButton.getId());
                            }
                        } else {
                            radioButton.setChecked(false);
                            if (setDisabled) {
                                Utils.setEnabledControl(false,
                                        (ViewGroup) radioButton.getParent(),
                                        radioButton.getId());
                            }
                        }
                    }
                }
            }
        };

        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnCheckedChangeListener(checkedChangeListener);

            if (radioButton != radioButtons[0]) {

                if (setDisabled) {
                    Utils.setEnabledControl(false, (ViewGroup) radioButton.getParent(), radioButton.getId());
                }
            }
        }

    }

    public static void setupSalaryEditText(@NonNull final EditText editText, double defaultValue) {
        setupSalaryEditText(editText, Utils.formatDoubleToString(defaultValue));
    }

    public static void setupSalaryEditText(@NonNull final EditText editText, String defaultValue) {
        editText.addTextChangedListener(new TextListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                String oldText = before + old + after;
                String newText = before + aNew + after;

                startUpdates();
                if (newText.matches("^[0-9]*[,.]?[0-9]{0,2}$")) {
                    editText.setText(newText.replaceAll("\\.", ","));
                    editText.setSelection(before.length() + aNew.length());
                } else {
                    editText.setText(oldText);
                    editText.setSelection(before.length() + old.length());
                }
                endUpdates();
            }
        });
        editText.setText(defaultValue);
    }
}
