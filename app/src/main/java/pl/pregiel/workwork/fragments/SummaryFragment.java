package pl.pregiel.workwork.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.pregiel.workwork.R;

public class SummaryFragment extends Fragment implements TaggedFragment {
    private static final String FRAGMENT_TAG = "SUMMARY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_summary);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }
}
