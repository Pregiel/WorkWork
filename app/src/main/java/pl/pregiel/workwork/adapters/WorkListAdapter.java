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
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.data.database.services.WorkService;
import pl.pregiel.workwork.data.database.services.WorkTimeService;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.fragments.AddWorkTimeFragment;
import pl.pregiel.workwork.fragments.UpdateWorkFragment;
import pl.pregiel.workwork.fragments.WorkDetailsFragment;
import pl.pregiel.workwork.utils.CustomAlert;
import pl.pregiel.workwork.utils.FragmentOpener;

public class WorkListAdapter extends ArrayAdapter<Work> {

    private WorkService workService;
    private WorkTimeService workTimeService;

    private Runnable refreshWorkListRunnable;

    public WorkListAdapter(@NonNull Context context, List<Work> workList) {
        super(context, 0, workList);

        workService = new WorkService(context);
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
                FragmentOpener.openFragment((FragmentActivity) getContext(),
                        new WorkDetailsFragment(), FragmentOpener.OpenMode.REPLACE, work);
            }
        });

        final View finalConvertView = convertView;
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setupPopupMenu(finalConvertView, work);
                return true;
            }
        });

        final ImageButton addButton = convertView.findViewById(R.id.button_workListElement_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentOpener.openFragment((FragmentActivity) getContext(),
                        new AddWorkTimeFragment(), FragmentOpener.OpenMode.REPLACE, work);
            }
        });

        return convertView;
    }

    private void setupPopupMenu(View view, final Work work) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_worklistelement, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_workListElement_open:
                        FragmentOpener.openFragment((FragmentActivity) getContext(),
                                new WorkDetailsFragment(), FragmentOpener.OpenMode.REPLACE, work);
                        return true;
                    case R.id.action_workListElement_add:
                        FragmentOpener.openFragment((FragmentActivity) getContext(),
                                new AddWorkTimeFragment(), FragmentOpener.OpenMode.REPLACE, work);
                        return true;
                    case R.id.action_workListElement_edit:
                        FragmentOpener.openFragment((FragmentActivity) getContext(),
                                new UpdateWorkFragment(), FragmentOpener.OpenMode.REPLACE, work);
                        return true;
                    case R.id.action_workListElement_delete:
                        CustomAlert.buildAlert(getContext(), R.string.action_delete, R.string.alert_areYouSure,
                                R.string.action_delete, new Runnable() {
                                    @Override
                                    public void run() {
                                        workService.deleteById(work.getId());
                                        if (refreshWorkListRunnable != null)
                                            refreshWorkListRunnable.run();
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

    public void setRefreshWorkListRunnable(Runnable refreshWorkListRunnable) {
        this.refreshWorkListRunnable = refreshWorkListRunnable;
    }
}
