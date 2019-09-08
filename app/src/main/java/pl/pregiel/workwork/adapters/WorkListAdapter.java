package pl.pregiel.workwork.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.fragments.AddWorkTimeFragment;
import pl.pregiel.workwork.fragments.WorkDetailsFragment;

public class WorkListAdapter extends ArrayAdapter<Work> {

    private WorkTimeService workTimeService;

    public WorkListAdapter(@NonNull Context context, List<Work> workList) {
        super(context, 0, workList);

        workTimeService = new WorkTimeService(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Work work = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listelement_worklist, parent, false);
        }

        if (work == null) {
            return convertView;
        }

        final TextView title = convertView.findViewById(R.id.textView_workListElement_title);
        title.setText(work.getTitle());
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new WorkDetailsFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("work_id", work.getId());
                fragment.setArguments(arguments);

                FragmentTransaction fragmentTransaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, WorkDetailsFragment.TAG);
                fragmentTransaction.commit();
            }
        });

        final ImageButton addButton = convertView.findViewById(R.id.button_workListElement_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new AddWorkTimeFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("work_id", work.getId());
                arguments.putString("work_title", work.getTitle());
                fragment.setArguments(arguments);

                FragmentTransaction fragmentTransaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, AddWorkTimeFragment.TAG);
                fragmentTransaction.commit();

//                FragmentManager fragmentManager = ((FragmentActivity)getContext()).getSupportFragmentManager();
//                AddWorkTimeFragment addWorkTimeDialogFragment = new AddWorkTimeFragment();
//                addWorkTimeDialogFragment.show(fragmentManager,"dialogFragment_addWorkTime");

//                WorkTime workTime = new WorkTime();
//                workTime.setTime(120);
//                workTime.setSalary(25);
//                workTime.setNotes("none");
//                workTime.setWorkId(work.getId());
//
//                workTimeService.create(workTime);
            }
        });

        return convertView;
    }
}
