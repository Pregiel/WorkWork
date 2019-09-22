package pl.pregiel.workwork.adapters


import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

import pl.pregiel.workwork.R
import pl.pregiel.workwork.data.database.services.WorkTimeService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.data.pojo.WorkTime
import pl.pregiel.workwork.fragments.UpdateWorkTimeFragment
import pl.pregiel.workwork.utils.CustomAlert
import pl.pregiel.workwork.utils.FragmentOpener
import pl.pregiel.workwork.utils.Utils

class WorkTimeListAdapter(
        context: Context,
        workTimeList: List<WorkTime>,
        val work: Work,
        private val refreshWorkDetailsListRunnable: Runnable?
) : ArrayAdapter<WorkTime>(context, 0, workTimeList) {

    private val workTimeService: WorkTimeService = WorkTimeService(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.listelement_workdetails, parent, false)
        val workTime = getItem(position) ?: return view

        val dayText = view.findViewById<TextView>(R.id.textView_workDetailsListElement_day)
        dayText.text = workTime.day

        val hoursText = view.findViewById<TextView>(R.id.textView_workDetailsListElement_hours)
        hoursText.text = if (workTime.timeFrom > -1 && workTime.timeTo > -1)
            "${Utils.formatTimeAmount(workTime.time)} ${context.getString(R.string.format_timeRange,
                    Utils.timeMinutesToString(workTime.timeFrom), Utils.timeMinutesToString(workTime.timeTo))}"
        else
            Utils.formatTimeAmount(workTime.time)

        val salaryPerHour = view.findViewById<TextView>(R.id.textView_workDetailsListElement_salaryPerHour)
        val salaryForAll = view.findViewById<TextView>(R.id.textView_workDetailsListElement_salaryForAll)

        val salary = workTime.salary.toDouble() / 100
        val totalTime = workTime.time.toDouble() / 60

        if (workTime.salaryMode == 0) {
            salaryPerHour.text = context.getString(R.string.format_salaryPerHour,
                    String.format(Locale.getDefault(), "%.2f", salary),
                    context.resources.getStringArray(R.array.global_currencies)[work.currency])

            salaryForAll.text = context.getString(R.string.format_salaryForAll,
                    String.format(Locale.getDefault(), "%.2f", salary * totalTime),
                    context.resources.getStringArray(R.array.global_currencies)[work.currency],
                    Utils.formatDoubleToString(totalTime))
        } else {
            salaryPerHour.text = context.getString(R.string.format_salaryPerHour,
                    String.format(Locale.getDefault(), "%.2f", salary / totalTime),
                    context.resources.getStringArray(R.array.global_currencies)[work.currency])

            salaryForAll.text = context.getString(R.string.format_salaryForAll,
                    String.format(Locale.getDefault(), "%.2f", salary),
                    context.resources.getStringArray(R.array.global_currencies)[work.currency],
                    Utils.formatDoubleToString(totalTime))
        }

        val infoLayout = view.findViewById<LinearLayout>(R.id.linearLayout_workDetailsListElement_info)
        if (workTime.info.isEmpty()) {
            infoLayout.visibility = View.GONE
        } else {
            val infoTextView = view.findViewById<TextView>(R.id.textView_workDetailsListElement_info)
            infoTextView.text = workTime.info
        }

        val extraInfoLayout = view.findViewById<LinearLayout>(R.id.linearLayout_workDetailsListElement_extraInfo)
        extraInfoLayout.visibility = View.GONE

        val mainLayout = view.findViewById<LinearLayout>(R.id.listElement_workDetails)
        val dropDownImageView = view.findViewById<ImageView>(R.id.imageView_workDetailsListElement_dropDown)
        mainLayout.setOnClickListener {
            if (extraInfoLayout.visibility == View.GONE) {
                extraInfoLayout.visibility = View.VISIBLE
                dropDownImageView.rotation = 180f
            } else {
                extraInfoLayout.visibility = View.GONE
                dropDownImageView.rotation = 0f
            }
        }
        mainLayout.setOnLongClickListener {
            setupPopupMenu(view, workTime)
            true
        }

        return view
    }

    private fun setupPopupMenu(view: View, workTime: WorkTime) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_workdetailslistelement, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_workDetailsListElement_edit -> {
                    FragmentOpener.openFragment(context as FragmentActivity,
                            UpdateWorkTimeFragment(), FragmentOpener.OpenMode.ADD, workTime)
                    return@OnMenuItemClickListener true
                }
                R.id.action_workDetailsListElement_delete -> {
                    CustomAlert.buildAlert(context, R.string.action_delete, R.string.alert_areYouSure,
                            R.string.action_delete, Runnable {
                        workTimeService.deleteById(workTime.id)
                            refreshWorkDetailsListRunnable?.run()
                    },
                            R.string.global_cancel, null).show()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })

        popupMenu.show()
    }
}
