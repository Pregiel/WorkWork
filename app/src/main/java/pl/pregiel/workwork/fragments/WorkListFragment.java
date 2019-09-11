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
import android.widget.Button;
import android.widget.ListView;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.adapters.WorkListAdapter;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.utils.ErrorToasts;
import pl.pregiel.workwork.utils.FragmentOpener;

public class WorkListFragment extends Fragment {
    public static final String TAG = "WORK_LIST";

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

        if (getContext() == null) {
            return;
        }

        workService = new WorkService(getContext());
        workList = workService.getAll();

        FloatingActionButton fabAdd = view.findViewById(R.id.fab_worklist_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentOpener.openFragment((FragmentActivity) getContext(),
                        new AddWorkFragment(), AddWorkFragment.TAG, FragmentOpener.OpenMode.ADD );
            }
        });

        workListView = view.findViewById(R.id.listView_workList_list);

        if (workListAdapter == null) {
            workListAdapter = new WorkListAdapter(getContext(), workList);
            workListAdapter.setRefreshWorkListRunnable(new Runnable() {
                @Override
                public void run() {
                    reloadWorkList();
                }
            });
            workListView.setAdapter(workListAdapter);
        }

        reloadWorkList();
    }

    private void reloadWorkList() {
        workList.clear();
        workList.addAll(workService.getAll());

        workListAdapter.notifyDataSetChanged();
    }


}
