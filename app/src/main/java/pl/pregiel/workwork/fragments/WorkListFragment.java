package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.adapters.WorkListAdapter;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.pojo.Work;

public class WorkListFragment extends Fragment {
    private ListView workListView;
    private WorkService workService;
    private WorkListAdapter workListAdapter;
    private List<Work> workList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_worklist);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workService = new WorkService(getContext());
        workList = workService.getAll();

        FloatingActionButton fabAdd = getActivity().findViewById(R.id.fab_worklist_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWork();
            }
        });

        workListView = getActivity().findViewById(R.id.listView_workList_list);

        if (workListAdapter == null) {
            workListAdapter = new WorkListAdapter(getContext(), workList);
            workListView.setAdapter(workListAdapter);
        }

        reloadWorkList();
    }

    public void addWork() {
        Work work = new Work();
        work.setTitle(RandomStringUtils.randomAlphanumeric(12));

        workService.create(work);
        reloadWorkList();
    }

    public void removeWork(Work work) {
        workService.deleteById(work.getId());
        reloadWorkList();
    }

    private void reloadWorkList() {
        workList.clear();
        workList.addAll(workService.getAll());

        workListAdapter.notifyDataSetChanged();
    }



}