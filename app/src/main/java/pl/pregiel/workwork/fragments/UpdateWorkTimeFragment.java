package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import pl.pregiel.workwork.ControlSetup;
import pl.pregiel.workwork.R;
import pl.pregiel.workwork.Utils;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;
import pl.pregiel.workwork.exceptions.EmptyFieldException;
import pl.pregiel.workwork.exceptions.ShowToastException;
import pl.pregiel.workwork.utils.CustomAlert;
import pl.pregiel.workwork.utils.ErrorToasts;


public class UpdateWorkTimeFragment extends FormFragment {
    public static final String TAG = "UPDATE_WORKTIME";

    private WorkService workService;
    private WorkTimeService workTimeService;
    private Work work;
    private WorkTime workTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        workService = new WorkService(getContext());
        workTimeService = new WorkTimeService(getContext());

        if (arguments != null && getActivity() != null) {
            workTime = workTimeService.getById(arguments.getInt("worktime_id"));
            work = workService.getById(workTime.getWorkId());
            getActivity().setTitle(R.string.global_update);
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
        ControlSetup.setupDatePicker(getContext(), dateEditText, workTime.getDay());

        final EditText timeFromEditText = view.findViewById(R.id.editText_addWorkTime_from);
        final EditText timeToEditText = view.findViewById(R.id.editText_addWorkTime_to);

        final RadioButton timeRangeRadioButton = view.findViewById(R.id.radioButton_addWorkTime_rangeTime);
        final RadioButton timeAmountRadioButton = view.findViewById(R.id.radioButton_addWorkTime_amountTime);
        ControlSetup.setupCustomRadioButtonGroup(timeRangeRadioButton, timeAmountRadioButton);

        if (workTime.getTimeFrom() > -1 && workTime.getTimeTo() > -1) {
            ControlSetup.setupTimePicker(getContext(), timeFromEditText, workTime.getTimeFrom());
            ControlSetup.setupTimePicker(getContext(), timeToEditText, workTime.getTimeTo());
            timeRangeRadioButton.setChecked(true);
        } else {
            ControlSetup.setupTimePicker(getContext(), timeFromEditText, work.getTimeFrom());
            ControlSetup.setupTimePicker(getContext(), timeToEditText, work.getTimeFrom() + workTime.getTime());
            timeAmountRadioButton.setChecked(true);
        }

        final EditText timeAmountEditText = view.findViewById(R.id.editText_addWorkTime_amount);
        ControlSetup.setupTimeAmountPicker(getContext(), timeAmountEditText, workTime.getTime());

        final EditText salaryEditText = view.findViewById(R.id.editText_addWorkTime_salary);
        ControlSetup.setupSalaryEditText(salaryEditText, (double) workTime.getSalary() / 100);

        final Spinner currencySpinner = view.findViewById(R.id.spinner_addWorkTime_currency);
        ControlSetup.setupCurrencySpinner(getContext(), currencySpinner, work.getCurrency());

        final RadioButton salaryPerHourRadioButton = view.findViewById(R.id.radioButton_addWorkTime_salaryPerHour);
        final RadioButton salaryForAllRadioButton = view.findViewById(R.id.radioButton_addWorkTime_salaryForAll);

        if (workTime.getSalaryMode() == 0) {
            salaryPerHourRadioButton.setChecked(true);
        } else {
            salaryForAllRadioButton.setChecked(true);
        }

        final EditText infoEditText = view.findViewById(R.id.editText_addWorkTime_info);
        infoEditText.setText(workTime.getInfo());

        final Button addButton = view.findViewById(R.id.button_addWorkTime_add);
        addButton.setText(R.string.global_update);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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

                    String salaryString = salaryEditText.getText().toString().replace(",", ".");
                    if (salaryString.isEmpty()) {
                        throw new EmptyFieldException(getString(R.string.global_salary));
                    }

                    int salary = Math.round(Float.valueOf(salaryString) * 100);
                    workTime.setSalary(salary);
                    workTime.setSalaryMode(salaryPerHourRadioButton.isChecked() ? 0 : 1);
                    workTime.setInfo(infoEditText.getText().toString());
                    workTime.setWorkId(work.getId());

                    workTimeService.update(workTime);

                    if (work.getCurrency() != currencySpinner.getSelectedItemPosition()) {
                        work.setCurrency(currencySpinner.getSelectedItemPosition());
                        workService.update(work);
                    }

                    if (getContext() != null) {
                        ((FragmentActivity) getContext()).onBackPressed();
                    }
                } catch (ShowToastException e) {
                    e.showToast(getContext());
                } catch (Exception e) {
                    ErrorToasts.showUnknownErrorToast(getContext());
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
