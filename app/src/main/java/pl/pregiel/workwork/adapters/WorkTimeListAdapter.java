package pl.pregiel.workwork.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.Utils;
import pl.pregiel.workwork.data.pojo.WorkTime;

public class WorkTimeListAdapter extends ArrayAdapter<WorkTime> {

    public WorkTimeListAdapter(@NonNull Context context, @NonNull List<WorkTime> workTimeList) {
        super(context, 0, workTimeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final WorkTime workTime = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listelement_workdetails, parent, false);
        }

        if (workTime != null) {

            final TextView dayText = convertView.findViewById(R.id.textView_workListElement_day);
            dayText.setText(workTime.getDay());

            final TextView hoursText = convertView.findViewById(R.id.textView_workListElement_hours);
            hoursText.setText(Utils.formatTime(getContext(), workTime.getTime()));

        }
        return convertView;
    }
}
