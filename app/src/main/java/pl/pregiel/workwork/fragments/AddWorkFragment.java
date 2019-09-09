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
import pl.pregiel.workwork.data.pojo.Work;


public class AddWorkFragment extends Fragment {
    public static final String TAG = "ADD_WORK";

    private WorkService workService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workService = new WorkService(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addwork, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText titleEditText = view.findViewById(R.id.editText_addWork_title);

        final EditText timeFromEditText = view.findViewById(R.id.editText_addWork_from);
        ControlSetup.setupTimePicker(getContext(), timeFromEditText, 8, 0);

        final EditText timeToEditText = view.findViewById(R.id.editText_addWork_to);
        ControlSetup.setupTimePicker(getContext(), timeToEditText, 16, 0);

        final EditText timeAmountEditText = view.findViewById(R.id.editText_addWork_amount);
        ControlSetup.setupTimeAmountPicker(getContext(), timeAmountEditText, 8, 0);

        final EditText timeToNowEditText = view.findViewById(R.id.editText_addWork_toNow);
        ControlSetup.setupTimeAmountPicker(getContext(), timeToNowEditText, 1, 0);

        final RadioButton timeRangeRadioButton = view.findViewById(R.id.radioButton_addWork_rangeTime);
        final RadioButton timeToNowRadioButton = view.findViewById(R.id.radioButton_addWork_toNow);
        final RadioButton timeAmountRadioButton = view.findViewById(R.id.radioButton_addWork_amountTime);
        ControlSetup.setupCustomRadioButtonGroup(false, timeRangeRadioButton, timeToNowRadioButton, timeAmountRadioButton);

        final EditText salaryEditText = view.findViewById(R.id.editText_addWork_salary);
        ControlSetup.setupSalaryEditText(salaryEditText, String.valueOf(Settings.DEFAULT_HOURLY_WAGE));

        final RadioButton salaryPerHourRadioButton = view.findViewById(R.id.radioButton_addWork_salaryPerHour);

        final EditText infoEditText = view.findViewById(R.id.editText_addWork_info);
        final Spinner currencySpinner = view.findViewById(R.id.spinner_addWork_currency);

        final Button addButton = view.findViewById(R.id.button_addWork_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Work work = new Work();

                    work.setTitle(titleEditText.getText().toString());

                    if (timeRangeRadioButton.isChecked()) {
                        work.setTimeMode(0);
                    } else if (timeToNowRadioButton.isChecked()) {
                        work.setTimeMode(1);
                    } else {
                        work.setTimeMode(2);
                    }

                    int timeFromInMinutes = Utils.timeStringToMinutes(timeFromEditText.getText().toString());
                    int timeToInMinutes = Utils.timeStringToMinutes(timeToEditText.getText().toString());
                    int timeToNowInMinutes = Utils.timeStringWithSuffixesToMinutes(timeToNowEditText.getText().toString());
                    int timeAmountInMinutes = Utils.timeStringWithSuffixesToMinutes(timeAmountEditText.getText().toString());

                    work.setTimeFrom(timeFromInMinutes);
                    work.setTimeTo(timeToInMinutes);
                    work.setTimeAmount(timeAmountInMinutes);
                    work.setTimeToNow(-timeToNowInMinutes);

                    int salary = Math.round(Float.valueOf(salaryEditText.getText().toString()) * 100);
                    work.setSalary(salary);
                    work.setSalaryMode(salaryPerHourRadioButton.isChecked() ? 0 : 1);
                    work.setCurrency(currencySpinner.getSelectedItemPosition());
                    work.setInfo(infoEditText.getText().toString());

                    workService.create(work);
                } catch (Exception e) {
                    Toast.makeText(getContext(), R.string.error_unknown, Toast.LENGTH_LONG).show();
                }
                if (getContext() != null) {
                    ((FragmentActivity) getContext()).onBackPressed();
                }
            }
        });

        final Button cancelButton = view.findViewById(R.id.button_addWork_cancel);
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
