package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import pl.pregiel.workwork.ControlSetup;
import pl.pregiel.workwork.R;
import pl.pregiel.workwork.Settings;
import pl.pregiel.workwork.Utils;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;
import pl.pregiel.workwork.utils.ErrorToasts;


public class AddWorkTimeFragment extends Fragment {
    public static final String TAG = "ADD_WORKTIME";

    private WorkTimeService workTimeService;
    private Work work;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        WorkService workService = new WorkService(getContext());
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
        ControlSetup.setupDatePicker(getContext(), dateEditText);

        final EditText timeFromEditText = view.findViewById(R.id.editText_addWorkTime_from);
        final EditText timeToEditText = view.findViewById(R.id.editText_addWorkTime_to);

        if (work.getTimeMode() == 1) {
            ControlSetup.setupTimePickerWithDelay(getContext(), timeFromEditText, work.getTimeToNow());
            ControlSetup.setupTimePicker(getContext(), timeToEditText);
        } else {
            ControlSetup.setupTimePicker(getContext(), timeFromEditText, work.getTimeFrom());
            ControlSetup.setupTimePicker(getContext(), timeToEditText, work.getTimeTo());
        }

        final EditText timeAmountEditText = view.findViewById(R.id.editText_addWorkTime_amount);
        ControlSetup.setupTimeAmountPicker(getContext(), timeAmountEditText, work.getTimeAmount());

        final RadioButton timeRangeRadioButton = view.findViewById(R.id.radioButton_addWorkTime_rangeTime);
        final RadioButton timeAmountRadioButton = view.findViewById(R.id.radioButton_addWorkTime_amountTime);
        ControlSetup.setupCustomRadioButtonGroup(timeRangeRadioButton, timeAmountRadioButton);

        final EditText salaryEditText = view.findViewById(R.id.editText_addWorkTime_salary);
        ControlSetup.setupSalaryEditText(salaryEditText, String.valueOf(Settings.DEFAULT_HOURLY_WAGE));

        final RadioButton salaryPerHourRadioButton = view.findViewById(R.id.radioButton_addWorkTime_salaryPerHour);
        final Spinner currencySpinner = view.findViewById(R.id.spinner_addWorkTime_currency);

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
                        workTime.setTimeTo(timeToInMinutes);
                        workTime.setTime(-1);

                        if (timeFromInMinutes < timeToInMinutes) {
                            workTime.setTime(timeToInMinutes - timeFromInMinutes);
                        } else {
                            workTime.setTime((1440 + timeToInMinutes) - timeFromInMinutes);
                        }
                    } else {
                        int timeAmountInMinutes = Utils.timeStringWithSuffixesToMinutes(timeAmountEditText.getText().toString());

                        workTime.setTimeFrom(-1);
                        workTime.setTimeTo(-1);
                        workTime.setTime(timeAmountInMinutes);
                    }

                    int salary = Math.round(Float.valueOf(salaryEditText.getText().toString()) * 100);
                    workTime.setSalary(salary);
                    workTime.setSalaryMode(salaryPerHourRadioButton.isChecked() ? 0 : 1);
                    workTime.setCurrency(currencySpinner.getSelectedItemPosition());
                    workTime.setInfo(infoEditText.getText().toString());
                    workTime.setWorkId(work.getId());

                    workTimeService.create(workTime);
                } catch (Exception e) {
                    ErrorToasts.showUnknownErrorToast(getContext());
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
}
