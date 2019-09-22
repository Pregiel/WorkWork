package pl.pregiel.workwork.adapters


import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

import pl.pregiel.workwork.R
import pl.pregiel.workwork.data.database.services.WorkService
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.fragments.AddWorkTimeFragment
import pl.pregiel.workwork.fragments.UpdateWorkFragment
import pl.pregiel.workwork.fragments.WorkDetailsFragment
import pl.pregiel.workwork.utils.CustomAlert
import pl.pregiel.workwork.utils.FragmentOpener

class WorkListAdapter(context: Context, workList: List<Work>, private val refreshWorkListRunnable: Runnable?) : ArrayAdapter<Work>(context, 0, workList) {

    private val workService: WorkService = WorkService(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.listelement_worklist, parent, false)
        val work = getItem(position) ?: return view

        val title = view.findViewById<TextView>(R.id.textView_workListElement_title)
        title.text = work.title
        title.setOnClickListener {
            FragmentOpener.openFragment(context as FragmentActivity,
                    WorkDetailsFragment(), FragmentOpener.OpenMode.REPLACE, work)
        }

        title.setOnLongClickListener {
            setupPopupMenu(view, work)
            true
        }

        val addButton = view.findViewById<ImageButton>(R.id.button_workListElement_add)
        addButton.setOnClickListener {
            FragmentOpener.openFragment(context as FragmentActivity,
                    AddWorkTimeFragment(), FragmentOpener.OpenMode.REPLACE, work)
        }

        return view
    }

    private fun setupPopupMenu(view: View, work: Work) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_worklistelement, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_workListElement_open -> {
                    FragmentOpener.openFragment(context as FragmentActivity,
                            WorkDetailsFragment(), FragmentOpener.OpenMode.REPLACE, work)
                    return@OnMenuItemClickListener true
                }
                R.id.action_workListElement_add -> {
                    FragmentOpener.openFragment(context as FragmentActivity,
                            AddWorkTimeFragment(), FragmentOpener.OpenMode.REPLACE, work)
                    return@OnMenuItemClickListener true
                }
                R.id.action_workListElement_edit -> {
                    FragmentOpener.openFragment(context as FragmentActivity,
                            UpdateWorkFragment(), FragmentOpener.OpenMode.REPLACE, work)
                    return@OnMenuItemClickListener true
                }
                R.id.action_workListElement_delete -> {
                    CustomAlert.buildAlert(context, R.string.action_delete, R.string.alert_areYouSure,
                            R.string.action_delete, Runnable {
                        workService.deleteById(work.id)
                        refreshWorkListRunnable?.run()
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
