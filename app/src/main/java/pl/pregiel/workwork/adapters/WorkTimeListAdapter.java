package pl.pregiel.workwork.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

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

            final TextView dayText = convertView.findViewById(R.id.textView_workDetailsListElement_day);
            dayText.setText(workTime.getDay());

            final TextView hoursText = convertView.findViewById(R.id.textView_workDetailsListElement_hours);
            if (workTime.getTimeFrom() > -1 && workTime.getTimeTo() > -1) {
                StringBuilder builder = new StringBuilder();
                builder.append(Utils.formatTimeAmount(getContext(), workTime.getTime()))
                        .append(" ")
                        .append(String.format(getContext().getString(R.string.format_timeRange),
                                Utils.timeMinutesToString(getContext(), workTime.getTimeFrom()),
                                Utils.timeMinutesToString(getContext(), workTime.getTimeTo())));
                hoursText.setText(builder);
            } else {
                hoursText.setText(Utils.formatTimeAmount(getContext(), workTime.getTime()));
            }

            final TextView salaryPerHour = convertView.findViewById(R.id.textView_workDetailsListElement_salaryPerHour);
            final TextView salaryForAll = convertView.findViewById(R.id.textView_workDetailsListElement_salaryForAll);

            if (workTime.getSalaryMode() == 0) {
                double salary = workTime.getSalary() / 100;
                double totalTime = workTime.getTime() / 60;
                salaryPerHour.setText(getContext().getString(R.string.format_salaryPerHour,
                        String.format(Locale.getDefault(), "%.2f", salary),
                        getContext().getResources().getStringArray(R.array.global_currencies)[workTime.getCurrency()]));

                salaryForAll.setText(getContext().getString(R.string.format_salaryForAll,
                        String.format(Locale.getDefault(), "%.2f", salary * totalTime),
                        getContext().getResources().getStringArray(R.array.global_currencies)[workTime.getCurrency()],
                        Utils.formatDoubleToString(totalTime)));
            } else {
                double salary = workTime.getSalary() / 100;
                double totalTime = workTime.getTime() / 60;
                salaryPerHour.setText(getContext().getString(R.string.format_salaryPerHour,
                        String.format(Locale.getDefault(), "%.2f", salary / totalTime),
                        getContext().getResources().getStringArray(R.array.global_currencies)[workTime.getCurrency()]));

                salaryForAll.setText(getContext().getString(R.string.format_salaryForAll,
                        String.format(Locale.getDefault(), "%.2f", salary),
                        getContext().getResources().getStringArray(R.array.global_currencies)[workTime.getCurrency()],
                        Utils.formatDoubleToString(totalTime)));
            }

            final LinearLayout infoLayout = convertView.findViewById(R.id.linearLayout_workDetailsListElement_info);
            if (workTime.getInfo().isEmpty()) {
                infoLayout.setVisibility(View.GONE);
            } else {
                final TextView infoTextView = convertView.findViewById(R.id.textView_workDetailsListElement_info);
                infoTextView.setText(workTime.getInfo());
            }

            final LinearLayout extraInfoLayout = convertView.findViewById(R.id.linearLayout_workDetailsListElement_extraInfo);
            extraInfoLayout.setVisibility(View.GONE);

            final LinearLayout mainLayout = convertView.findViewById(R.id.listElement_workDetails);
            final ImageView dropDownImageView = convertView.findViewById(R.id.imageView_workDetailsListElement_dropDown);
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (extraInfoLayout.getVisibility() == View.GONE) {
                        extraInfoLayout.setVisibility(View.VISIBLE);
                        dropDownImageView.setRotation(180);
                    } else {
                        extraInfoLayout.setVisibility(View.GONE);
                        dropDownImageView.setRotation(0);
                    }
                }
            });

        }
        return convertView;
    }
}
