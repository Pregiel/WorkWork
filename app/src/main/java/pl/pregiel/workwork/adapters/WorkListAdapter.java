package pl.pregiel.workwork.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.fragments.WorkDetailsFragment;

public class WorkListAdapter extends ArrayAdapter<Work> {

    public WorkListAdapter(@NonNull Context context, List<Work> workList) {
        super(context, 0, workList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Work work = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listelement_worklist, parent, false);
        }

        final TextView title = convertView.findViewById(R.id.textView_workListElement_title);
        title.setText(work.getTitle());
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new WorkDetailsFragment();

                FragmentTransaction fragmentTransaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, "WORK_DETAILS");
                fragmentTransaction.commit();
            }
        });

        return convertView;
    }
}
