package pl.pregiel.workwork.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;
import pl.pregiel.workwork.fragments.AddWorkTimeFragment;
import pl.pregiel.workwork.fragments.UpdateWorkTimeFragment;
import pl.pregiel.workwork.utils.CustomAlert;
import pl.pregiel.workwork.utils.FragmentOpener;

public class WorkTimeListAdapter extends ArrayAdapter<WorkTime> {

    private WorkService workService;
    private WorkTimeService workTimeService;

    private Work work;

    private Runnable refreshWorkDetailsListRunnable;

    public WorkTimeListAdapter(@NonNull Context context, @NonNull List<WorkTime> workTimeList) {
        super(context, 0, workTimeList);

        workService = new WorkService(context);
        workTimeService = new WorkTimeService(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final WorkTime workTime = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listelement_workdetails, parent, false);
        }

        if (workTime != null) {
            if (work == null) {
                work = workService.getById(workTime.getWorkId());
            }

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
                double salary = (double) workTime.getSalary() / 100;
                double totalTime = (double) workTime.getTime() / 60;
                salaryPerHour.setText(getContext().getString(R.string.format_salaryPerHour,
                        String.format(Locale.getDefault(), "%.2f", salary),
                        getContext().getResources().getStringArray(R.array.global_currencies)[work.getCurrency()]));

                salaryForAll.setText(getContext().getString(R.string.format_salaryForAll,
                        String.format(Locale.getDefault(), "%.2f", salary * totalTime),
                        getContext().getResources().getStringArray(R.array.global_currencies)[work.getCurrency()],
                        Utils.formatDoubleToString(totalTime)));
            } else {
                double salary = (double) workTime.getSalary() / 100;
                double totalTime = (double) workTime.getTime() / 60;
                salaryPerHour.setText(getContext().getString(R.string.format_salaryPerHour,
                        String.format(Locale.getDefault(), "%.2f", salary / totalTime),
                        getContext().getResources().getStringArray(R.array.global_currencies)[work.getCurrency()]));

                salaryForAll.setText(getContext().getString(R.string.format_salaryForAll,
                        String.format(Locale.getDefault(), "%.2f", salary),
                        getContext().getResources().getStringArray(R.array.global_currencies)[work.getCurrency()],
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
            final View finalConvertView = convertView;
            mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setupPopupMenu(finalConvertView, workTime);
                    return true;
                }
            });

        }
        return convertView;
    }

    private void setupPopupMenu(View view, final WorkTime workTime) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_workdetailslistelement, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_workDetailsListElement_edit:
                        FragmentOpener.openFragment((FragmentActivity) getContext(),
                                new UpdateWorkTimeFragment(), UpdateWorkTimeFragment.TAG, FragmentOpener.OpenMode.ADD, workTime);
                        return true;
                    case R.id.action_workDetailsListElement_delete:
                        CustomAlert.buildAlert(getContext(), R.string.action_delete, R.string.alert_areYouSure,
                                R.string.action_delete, new Runnable() {
                                    @Override
                                    public void run() {
                                        workTimeService.deleteById(workTime.getId());
                                        if (refreshWorkDetailsListRunnable != null)
                                            refreshWorkDetailsListRunnable.run();
                                    }
                                },
                                R.string.global_cancel, null).show();
                        return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

    public void setRefreshWorkDetailsListRunnable(Runnable refreshWorkDetailsListRunnable) {
        this.refreshWorkDetailsListRunnable = refreshWorkDetailsListRunnable;
    }
}
