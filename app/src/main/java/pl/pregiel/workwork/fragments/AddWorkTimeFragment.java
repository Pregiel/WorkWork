package pl.pregiel.workwork.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.Settings;
import pl.pregiel.workwork.Utils;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;
import pl.pregiel.workwork.filters.DecimalDigitsInputFilter;
import pl.pregiel.workwork.fragments.dialogFragments.HourAndMinutePickerDialogFragment;


public class AddWorkTimeFragment extends Fragment {
    public static final String TAG = "ADD_WORKTIME";

    private WorkService workService;
    private WorkTimeService workTimeService;
    private Work work;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        workService = new WorkService(getContext());
        workTimeService = new WorkTimeService(getContext());

        if (arguments != null && getActivity() != null) {
            work = workService.getById(arguments.getInt("work_id"));
            getActivity().setTitle(String.format(getString(R.string.title_addWorkTime), work.getTitle()));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addworktime, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText dateEditText = view.findViewById(R.id.editText_addWorkTime_date);
        setupDatePicker(getContext(), dateEditText);

        final EditText timeFromEditText = view.findViewById(R.id.editText_addWorkTime_from);
        setupTimePicker(getContext(), timeFromEditText, -1);

        final EditText timeToEditText = view.findViewById(R.id.editText_addWorkTime_to);
        setupTimePicker(getContext(), timeToEditText);

        final EditText timeAmountEditText = view.findViewById(R.id.editText_addWorkTime_amount);
        setupTimeAmountPicker(getContext(), timeAmountEditText, 8, 0);

        final RadioButton timeRangeRadioButton = view.findViewById(R.id.radioButton_addWorkTime_rangeTime);
        final RadioButton timeAmountRadioButton = view.findViewById(R.id.radioButton_addWorkTime_amountTime);
        setupTimeModeRadioButtons(timeRangeRadioButton, timeAmountRadioButton, R.id.radioButton_addWorkTime_rangeTime);

        final EditText salaryEditText = view.findViewById(R.id.editText_addWorkTime_salary);
        salaryEditText.setText(String.valueOf(Settings.DEFAULT_HOURLY_WAGE));
        salaryEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        final RadioButton salaryPerHourRadioButton = view.findViewById(R.id.radioButton_addWorkTime_salaryPerHour);

        final EditText infoEditText = view.findViewById(R.id.editText_addWorkTime_info);

        final Button addButton = view.findViewById(R.id.button_addWorkTime_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WorkTime workTime = new WorkTime();
                    workTime.setDay(dateEditText.getText().toString());
                    if (timeRangeRadioButton.isChecked()) {
                        int timeFromInMinutes = Utils.timeStringToMinutes(timeFromEditText.getText().toString());
                        int timeToInMinutes = Utils.timeStringToMinutes(timeToEditText.getText().toString());

                        workTime.setTimeFrom(timeFromInMinutes);
                        workTime.setTimeFrom(timeToInMinutes);

                        if (timeFromInMinutes < timeToInMinutes) {
                            workTime.setTime(timeToInMinutes - timeFromInMinutes);
                        } else {
                            workTime.setTime((1440 + timeToInMinutes) - timeFromInMinutes);
                        }
                    } else {
                        int timeAmountInMinutes = Utils.timeStringWithSuffixesToMinutes(timeAmountEditText.getText().toString());

                        workTime.setTimeFrom(-1);
                        workTime.setTimeFrom(-1);
                        workTime.setTime(timeAmountInMinutes);
                    }

                    int salary = Math.round(Float.valueOf(salaryEditText.getText().toString()) * 100);
                    workTime.setSalary(salary);
                    workTime.setSalaryMode(salaryPerHourRadioButton.isChecked() ? 0 : 1);

                    workTime.setNotes(infoEditText.getText().toString());
                    workTime.setWorkId(work.getId());

                    workTimeService.create(workTime);
                } catch (Exception e) {
                    Toast.makeText(getContext(), R.string.error_unknown, Toast.LENGTH_LONG).show();
                }
                if (getContext() != null) {
                    ((FragmentActivity) getContext()).onBackPressed();
                }
            }
        });

        final Button cancelButton = view.findViewById(R.id.button_addWorkTime_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null) {
                    ((FragmentActivity) getContext()).onBackPressed();
                }
            }
        });
    }

    private void setupDatePicker(final Context context, @NonNull final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        editText.setText(String.format(getString(R.string.format_date),
                currentDay, (currentMonth + 1), currentYear));

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timeSplit = editText.getText().toString().split("\\.");
                int initialDay, initialMonth, initialYear;
                try {
                    initialDay = Integer.valueOf(timeSplit[0]);
                    initialMonth = Integer.valueOf(timeSplit[1]);
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
                                editText.setText(String.format(getString(R.string.format_date),
                                        dayOfMonth, (month + 1), year));
                            }
                        }, initialYear, initialMonth, initialDay);
                datePickerDialog.show();
            }
        });
    }

    private void setupTimePicker(final Context context, @NonNull final EditText editText) {
        setupTimePicker(context, editText, 0);
    }

    private void setupTimePicker(final Context context, @NonNull final EditText editText, int hourModifier) {
        final Calendar calendar = Calendar.getInstance();
        int currentHour = (calendar.get(Calendar.HOUR_OF_DAY) + hourModifier) % 24;
        while (currentHour < 0) {
            currentHour += 24;
        }
        int currentMinute = calendar.get(Calendar.MINUTE);

        editText.setText(String.format(getString(R.string.format_time), currentHour, currentMinute));

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
                                editText.setText(String.format(getString(R.string.format_time), hour, minute));
                            }
                        }, initialHour, initialMinute, true);
                timePickerDialog.show();
            }
        });
    }

    private void setupTimeAmountPicker(final Context context, @NonNull final EditText editText, final int defaultHour, final int defaultMinute) {
        editText.setText(String.format(getString(R.string.format_timeWithSuffixes), defaultHour, defaultMinute));
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
                    hour = defaultHour;
                    minute = defaultMinute;
                }

                hourAndMinutePickerDialogFragment.setHourAndMinute(hour, minute);
                hourAndMinutePickerDialogFragment.setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(String.format(getString(R.string.format_timeWithSuffixes),
                                hourAndMinutePickerDialogFragment.getHour(), hourAndMinutePickerDialogFragment.getMinute()));
                        hourAndMinutePickerDialogFragment.dismiss();
                    }
                });
                hourAndMinutePickerDialogFragment.show(fragmentManager, HourAndMinutePickerDialogFragment.TAG);
            }
        });
    }

    private void setupTimeModeRadioButtons(@NonNull final RadioButton rangeRadioButton, @NonNull final RadioButton amountRadioButton, int checkedId) {
        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (buttonView.getId() == R.id.radioButton_addWorkTime_rangeTime) {
                        amountRadioButton.setChecked(false);
                        Utils.setEnabledControl(false,
                                (ViewGroup) amountRadioButton.getParent(),
                                R.id.radioButton_addWorkTime_amountTime);
                        Utils.setEnabledControl(true,
                                (ViewGroup) rangeRadioButton.getParent(),
                                R.id.radioButton_addWorkTime_rangeTime);
                    }
                    if (buttonView.getId() == R.id.radioButton_addWorkTime_amountTime) {
                        rangeRadioButton.setChecked(false);
                        Utils.setEnabledControl(false,
                                (ViewGroup) rangeRadioButton.getParent(),
                                R.id.radioButton_addWorkTime_rangeTime);
                        Utils.setEnabledControl(true,
                                (ViewGroup) amountRadioButton.getParent(),
                                R.id.radioButton_addWorkTime_amountTime);
                    }
                }
            }
        };

        rangeRadioButton.setOnCheckedChangeListener(checkedChangeListener);
        amountRadioButton.setOnCheckedChangeListener(checkedChangeListener);

        if (checkedId == R.id.radioButton_addWorkTime_amountTime) {
            rangeRadioButton.setChecked(false);
            Utils.setEnabledControl(false,
                    (ViewGroup) rangeRadioButton.getParent(),
                    R.id.radioButton_addWorkTime_rangeTime);
        } else {
            amountRadioButton.setChecked(false);
            Utils.setEnabledControl(false,
                    (ViewGroup) amountRadioButton.getParent(),
                    R.id.radioButton_addWorkTime_amountTime);
        }
    }
}
