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

import java.util.ArrayList;
import java.util.HashMap;
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
        HashMap<String, List<WorkTime>> workTimeByDate = new HashMap<>();
        double totalTime = 0, totalSalary = 0;

        for (WorkTime workTime : workTimeList) {
            totalTime += workTime.getTime();
            if (workTime.getSalaryMode() == 0)
                totalSalary += (workTime.getSalary() / 100) * (workTime.getTime()/60);
            else
                totalSalary += (workTime.getSalary() / 100);

            String day = workTime.getDay();
            if (workTimeByDate.containsKey(day)) {
                List<WorkTime> list = workTimeByDate.get(day);
                list.add(workTime);
            } else {
                List<WorkTime> list = new ArrayList<>();
                list.add(workTime);
                workTimeByDate.put(day, list);
            }
        }

        totalTime = totalTime / 60;
        int daysWork = workTimeByDate.size();
        double averageTime = totalTime / daysWork;
        double averageHoursWage = totalSalary / totalTime;

        totalTimeText.setText(getString(R.string.format_hour, String.format(Locale.getDefault(), "%.2f", totalTime)));
        averageTimeText.setText(getString(R.string.format_hourPerDay, String.format(Locale.getDefault(), "%.2f", averageTime)));
        daysWorkText.setText(getString((daysWork > 1 ? R.string.format_days : R.string.format_day), daysWork));
        //TODO: zmienić walutę
        totalSalaryText.setText(getString(R.string.format_pln,
                String.format(Locale.getDefault(), "%.2f", totalSalary), "PLN"));
        averageHoursWageText.setText(getString(R.string.format_plnPerHour,
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
