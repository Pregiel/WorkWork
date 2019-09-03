package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.data.database.services.WorkService;

public class WorkDetailsFragment extends Fragment {
    private WorkService workService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_worklist);
        workService = new WorkService(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_details, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
