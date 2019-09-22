package pl.pregiel.workwork.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_work_list.*
import pl.pregiel.workwork.R
import pl.pregiel.workwork.adapters.WorkListAdapter
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.utils.FragmentOpener

class WorkListFragment : Fragment(), TaggedFragment {

    override fun fragmentTag(): String {
        return "WORK_LIST"
    }

    private lateinit var workService: WorkService

    private var workListAdapter: WorkListAdapter? = null
    private var workList: MutableList<Work>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workService = WorkService(context!!)

        activity!!.setTitle(R.string.title_worklist)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_work_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workList = workService.getAll().toMutableList()

        fab_worklist_add.setOnClickListener {
            FragmentOpener.openFragment((context as FragmentActivity?)!!,
                    AddWorkFragment(), FragmentOpener.OpenMode.ADD)
        }

        if (workListAdapter == null) {
            workListAdapter = WorkListAdapter(context!!, workList!!, Runnable { this.reloadWorkList() })
            listView_workList_list.adapter = workListAdapter
        }

        reloadWorkList()
    }

    private fun reloadWorkList() {
        workList?.clear()
        workList?.addAll(workService.getAll())

        workListAdapter?.notifyDataSetChanged()
    }
}
