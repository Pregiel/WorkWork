package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.Utils;
import pl.pregiel.workwork.adapters.WorkTimeListAdapter;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;
import pl.pregiel.workwork.utils.CustomAlert;
import pl.pregiel.workwork.utils.FragmentOpener;

public class WorkDetailsFragment extends Fragment implements TaggedFragment {
    public static final String FRAGMENT_TAG = "WORK_DETAILS";

    private WorkService workService;
    private WorkTimeService workTimeService;

    private WorkTimeListAdapter workTimeListAdapter;

    private Work work;
    private List<WorkTime> workTimeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();

        workService = new WorkService(getContext());
        workTimeService = new WorkTimeService(getContext());

        if (arguments != null) {
            work = workService.getById(arguments.getInt("work_id", 0));
        }
        setTitle();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_details, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_workdetails, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_workDetails_add:
                FragmentOpener.openFragment((FragmentActivity) getContext(),
                        new AddWorkTimeFragment(), FragmentOpener.OpenMode.ADD, work);
                break;

            case R.id.action_workDetails_edit:
                FragmentOpener.openFragment((FragmentActivity) getContext(),
                        new UpdateWorkFragment(), FragmentOpener.OpenMode.ADD, work);
                break;

            case R.id.action_workDetails_delete:
                CustomAlert.buildAlert(getContext(), R.string.action_delete, R.string.alert_areYouSure,
                        R.string.action_delete, new Runnable() {
                            @Override
                            public void run() {
                                workService.deleteById(work.getId());
                                FragmentOpener.openFragment((FragmentActivity) getContext(),
                                        new WorkListFragment(),  FragmentOpener.OpenMode.REPLACE);
                            }
                        },
                        R.string.global_cancel, null).show();
                break;
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null || getContext() == null)
            return;

        setWorkTimeList();

        updateDetails();

        ListView hoursWorkedListView = getActivity().findViewById(R.id.listView_workDetails_hoursWorked);

        if (workTimeListAdapter == null) {
            workTimeListAdapter = new WorkTimeListAdapter(getContext(), workTimeList);
            workTimeListAdapter.setRefreshWorkDetailsListRunnable(new Runnable() {
                @Override
                public void run() {
                    reloadWorkTimeList();
                }
            });
        }

        hoursWorkedListView.setAdapter(workTimeListAdapter);

        FloatingActionButton addWorkTimeFab = getActivity().findViewById(R.id.fab_workDetails_add);
        addWorkTimeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentOpener.openFragment((FragmentActivity) getContext(),
                        new AddWorkTimeFragment(), FragmentOpener.OpenMode.ADD, work);
            }
        });
    }

    public void setTitle() {
        if (getActivity() != null)
            getActivity().setTitle(work.getTitle());
    }

    private void setWorkTimeList() {
        if (workTimeList == null) {
            workTimeList = workTimeService.getByWorkId(work.getId());
        } else {
            workTimeList.clear();
            workTimeList.addAll(workTimeService.getByWorkId(work.getId()));
        }
        Collections.sort(workTimeList, new Comparator<WorkTime>() {
            @Override
            public int compare(WorkTime o1, WorkTime o2) {
                if (!o1.getDay().contentEquals(o2.getDay())) {
                    try {
                        Calendar calendar1 = Utils.stringToCalendar(o1.getDay(), "dd.MM.yyyy");
                        Calendar calendar2 = Utils.stringToCalendar(o2.getDay(), "dd.MM.yyyy");
                        return calendar1.compareTo(calendar2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                } else {
                    return o1.getTimeFrom().compareTo(o2.getTimeFrom());
                }
            }
        });
    }

    private void reloadWorkTimeList() {
        setWorkTimeList();
        workTimeListAdapter.notifyDataSetChanged();
    }

    private void updateDetails() {
        if (getActivity() == null || getContext() == null)
            return;

        HashMap<String, List<WorkTime>> workTimeByDate = new HashMap<>();
        double totalTime = 0, totalSalary = 0;

        for (WorkTime workTime : workTimeList) {
            totalTime += workTime.getTime();
            if (workTime.getSalaryMode() == 0)
                totalSalary += ((double) workTime.getSalary() / 100) * ((double) workTime.getTime() / 60);
            else
                totalSalary += ((double) workTime.getSalary() / 100);

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

        TextView totalTimeText = getActivity().findViewById(R.id.textView_workDetails_totalTime);
        totalTimeText.setText(getString(R.string.format_hour, String.format(Locale.getDefault(), "%.2f", totalTime)));

        TextView averageTimeText = getActivity().findViewById(R.id.textView_workDetails_averageTime);
        averageTimeText.setText(getString(R.string.format_hourPerDay, String.format(Locale.getDefault(), "%.2f", averageTime)));

        TextView daysWorkText = getActivity().findViewById(R.id.textView_workDetails_daysWork);
        daysWorkText.setText(getString((daysWork > 1 ? R.string.format_days : R.string.format_day), daysWork));

        TextView totalSalaryText = getActivity().findViewById(R.id.textView_workDetails_totalSalary);
        totalSalaryText.setText(getString(R.string.format_pln,
                String.format(Locale.getDefault(), "%.2f", totalSalary),
                getResources().getStringArray(R.array.global_currencies)[work.getCurrency()]));

        TextView averageHoursWageText = getActivity().findViewById(R.id.textView_workDetails_averageHourlyWage);
        averageHoursWageText.setText(getString(R.string.format_plnPerHour,
                String.format(Locale.getDefault(), "%.2f", averageHoursWage),
                getResources().getStringArray(R.array.global_currencies)[work.getCurrency()]));
    }

    public void reload() {
        int workId = work.getId();
        work = workService.getById(workId);
        reloadWorkTimeList();
        updateDetails();
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }
}
