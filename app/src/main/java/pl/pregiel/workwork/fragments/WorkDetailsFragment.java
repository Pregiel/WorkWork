package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.adapters.WorkTimeListAdapter;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;

public class WorkDetailsFragment extends Fragment {
    public static final String TAG = "WORK_DETAILS";

    private WorkService workService;
    private WorkTimeService workTimeService;
    private WorkTimeListAdapter workTimeListAdapter;
    private Bundle arguments;
    private Work work;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arguments = getArguments();

        if (getActivity() != null) {
            getActivity().setTitle(R.string.title_worklist);
        }
        workService = new WorkService(getContext());
        workTimeService = new WorkTimeService(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        work = workService.getById(arguments.getInt("work_id", 0));

        if (getActivity() == null || getContext() == null)
            return;

        TextView totalTimeText = getActivity().findViewById(R.id.textView_workDetails_totalTime);
        TextView averageTimeText = getActivity().findViewById(R.id.textView_workDetails_averageTime);
        TextView daysWorkText = getActivity().findViewById(R.id.textView_workDetails_daysWork);
        TextView totalSalaryText = getActivity().findViewById(R.id.textView_workDetails_totalSalary);
        TextView averageHoursWageText = getActivity().findViewById(R.id.textView_workDetails_averageHourlyWage);

        List<WorkTime> workTimeList = workTimeService.getByWorkId(work.getId());
        double totalTime = 0, totalSalary = 0;

        for (WorkTime workTime : workTimeList) {
            totalTime += workTime.getTime();
            totalSalary += workTime.getSalary() / 100;
        }

        totalTime = totalTime / 60;
        int daysWork = workTimeList.size();
        double averageTime = totalTime / daysWork;
        double averageHoursWage = totalSalary / daysWork;

        totalTimeText.setText(getString(R.string.format_hour, String.format(Locale.getDefault(), "%.2f", totalTime)));
        averageTimeText.setText(getString(R.string.format_hourPerDay, String.format(Locale.getDefault(), "%.2f", averageTime)));
        daysWorkText.setText(getString(R.string.format_hour, String.valueOf(daysWork)));
        //TODO: zmienić walutę
        totalSalaryText.setText(getString(R.string.format_plnPerHour,
                String.format(Locale.getDefault(), "%.2f", totalSalary), "PLN"));
        averageHoursWageText.setText(getString(R.string.format_pln,
                String.format(Locale.getDefault(), "%.2f", averageHoursWage), "PLN"));

        ListView hoursWorkedListView = getActivity().findViewById(R.id.listView_workDetails_hoursWorked);

        if (workTimeListAdapter == null) {
            workTimeListAdapter = new WorkTimeListAdapter(getContext(), workTimeList);
        }

        hoursWorkedListView.setAdapter(workTimeListAdapter);

        FloatingActionButton addWorkTimeFab = getActivity().findViewById(R.id.fab_workDetails_add);
        addWorkTimeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddWorkTimeFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("work_id", work.getId());
                arguments.putString("work_title", work.getTitle());
                fragment.setArguments(arguments);

                FragmentTransaction fragmentTransaction = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frame, fragment, AddWorkTimeFragment.TAG);
                fragmentTransaction.commit();
            }
        });
    }

    public void setTitle() {
        if (getActivity()!= null)
            getActivity().setTitle(work.getTitle());
    }
}
