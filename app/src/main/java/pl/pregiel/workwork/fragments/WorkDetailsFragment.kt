package pl.pregiel.workwork.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_work_details.*

import pl.pregiel.workwork.R
import pl.pregiel.workwork.adapters.WorkTimeListAdapter
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.database.services.WorkTimeService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.data.pojo.WorkTime
import pl.pregiel.workwork.utils.CustomAlert
import pl.pregiel.workwork.utils.FragmentOpener
import pl.pregiel.workwork.utils.Utils
import java.text.ParseException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WorkDetailsFragment : Fragment(), TaggedFragment {

    override fun fragmentTag() = "WORK_DETAILS"

    private lateinit var workService: WorkService
    private lateinit var workTimeService: WorkTimeService
    private lateinit var work: Work

    private var workTimeList: MutableList<WorkTime>? = null
    private var workTimeListAdapter: WorkTimeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        workService = WorkService(context!!)
        workTimeService = WorkTimeService(context!!)

        work = workService.getById(arguments!!.getInt("work_id", 0))!!

        setTitle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_work_details, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater!!.inflate(R.menu.menu_workdetails, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_workDetails_add -> FragmentOpener.openFragment((context as FragmentActivity?)!!,
                    AddWorkTimeFragment(), FragmentOpener.OpenMode.ADD, work)

            R.id.action_workDetails_edit -> FragmentOpener.openFragment((context as FragmentActivity?)!!,
                    UpdateWorkFragment(), FragmentOpener.OpenMode.ADD, work)

            R.id.action_workDetails_delete -> CustomAlert.buildAlert(context, R.string.action_delete, R.string.alert_areYouSure,
                    R.string.action_delete, Runnable {
                workService.deleteById(work.id)
                FragmentOpener.openFragment((context as FragmentActivity?)!!,
                        WorkListFragment(), FragmentOpener.OpenMode.REPLACE)
            },
                    R.string.global_cancel, null).show()
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity == null || context == null)
            return

        setWorkTimeList()
        updateDetails()

        val hoursWorkedListView = activity!!.findViewById<ListView>(R.id.listView_workDetails_hoursWorked)

        if (workTimeListAdapter == null) {
            workTimeListAdapter = WorkTimeListAdapter(context!!, workTimeList!!.toList(), work, Runnable { reloadWorkTimeList() })
        }

        hoursWorkedListView.adapter = workTimeListAdapter

        val addWorkTimeFab = activity!!.findViewById<FloatingActionButton>(R.id.fab_workDetails_add)
        addWorkTimeFab.setOnClickListener {
            FragmentOpener.openFragment((context as FragmentActivity?)!!,
                    AddWorkTimeFragment(), FragmentOpener.OpenMode.ADD, work)
        }
    }

    fun setTitle() {
        activity?.title = work.title
    }

    private fun setWorkTimeList() {
        if (workTimeList == null) {
            workTimeList = workTimeService.getByWorkId(work.id).toMutableList()
        } else {
            workTimeList!!.clear()
            workTimeList!!.addAll(workTimeService.getByWorkId(work.id))
        }

        workTimeList?.sortedWith(Comparator { p1, p2 ->
            if (p1.day != p2.day) {
                try {
                    val calendar1 = Utils.stringToCalendar(p1.day, "dd.MM.yyyy")
                    val calendar2 = Utils.stringToCalendar(p2.day, "dd.MM.yyyy")
                    calendar1.compareTo(calendar2)
                } catch (_: ParseException) {
                    0
                }
            } else
                p1.timeFrom.compareTo(p2.timeFrom)
        })
    }

    private fun reloadWorkTimeList() {
        setWorkTimeList()
        workTimeListAdapter?.notifyDataSetChanged()
    }

    private fun updateDetails() {
        val workTimeByDate = HashMap<String, ArrayList<WorkTime>>()
        var totalTime = 0.0
        var totalSalary = 0.0

        for (workTime in workTimeList!!) {
            totalTime += workTime.time.toDouble()
            totalSalary += if (workTime.salaryMode == 0)
                workTime.salary.toDouble() / 100 * (workTime.time.toDouble() / 60)
            else
                workTime.salary.toDouble() / 100

            val day = workTime.day
            if (workTimeByDate.containsKey(day)) {
                val list = workTimeByDate[day]
                list?.add(workTime)
            } else {
                val list = ArrayList<WorkTime>()
                list.add(workTime)
                workTimeByDate[day] = list
            }
        }

        totalTime /= 60
        val daysWork = workTimeByDate.size
        val averageTime = totalTime / daysWork
        val averageHoursWage = totalSalary / totalTime

        textView_workDetails_totalTime.text = getString(R.string.format_hour, String.format(Locale.getDefault(), "%.2f", totalTime))
        textView_workDetails_averageTime.text = getString(R.string.format_hourPerDay, String.format(Locale.getDefault(), "%.2f", averageTime))
        textView_workDetails_daysWork.text = getString(if (daysWork > 1) R.string.format_days else R.string.format_day, daysWork)
        textView_workDetails_totalSalary.text = getString(R.string.format_pln,
                String.format(Locale.getDefault(), "%.2f", totalSalary),
                resources.getStringArray(R.array.global_currencies)[work.currency])
        textView_workDetails_averageHourlyWage.text = getString(R.string.format_plnPerHour,
                String.format(Locale.getDefault(), "%.2f", averageHoursWage),
                resources.getStringArray(R.array.global_currencies)[work.currency])
    }

    fun reload() {
        val workId = work.id
        work = workService.getById(workId)!!
        reloadWorkTimeList()
        updateDetails()
    }
}
