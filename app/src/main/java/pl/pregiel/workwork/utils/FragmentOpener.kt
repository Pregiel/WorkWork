package pl.pregiel.workwork.utils


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

import pl.pregiel.workwork.R
import pl.pregiel.workwork.data.pojo.Work
import pl.pregiel.workwork.data.pojo.WorkTime
import pl.pregiel.workwork.fragments.TaggedFragment

object FragmentOpener {
    enum class OpenMode {
        ADD, REPLACE
    }

    fun openFragment(fragmentActivity: FragmentActivity, fragment: TaggedFragment, openMode: OpenMode, workTime: WorkTime) {
        openFragment(fragmentActivity.supportFragmentManager, fragment, openMode, workTime)
    }

    fun openFragment(fragmentManager: FragmentManager, fragment: TaggedFragment, openMode: OpenMode, workTime: WorkTime) {
        val arguments = Bundle()
        arguments.putInt("worktime_id", workTime.id)
        openFragment(fragmentManager, fragment, openMode, arguments)
    }

    fun openFragment(fragmentActivity: FragmentActivity, fragment: TaggedFragment, openMode: OpenMode, work: Work) {
        openFragment(fragmentActivity.supportFragmentManager, fragment, openMode, work)
    }

    fun openFragment(fragmentManager: FragmentManager, fragment: TaggedFragment, openMode: OpenMode, work: Work) {
        val arguments = Bundle()
        arguments.putInt("work_id", work.id)
        openFragment(fragmentManager, fragment, openMode, arguments)
    }

    fun openFragment(fragmentActivity: FragmentActivity, fragment: TaggedFragment, openMode: OpenMode, arguments: Bundle = Bundle()) {
        openFragment(fragmentActivity.supportFragmentManager, fragment, openMode, arguments)
    }

    fun openFragment(fragmentManager: FragmentManager, fragment: TaggedFragment, openMode: OpenMode, arguments: Bundle = Bundle()) {
            (fragment as Fragment).arguments = arguments

        val fragmentTransaction = fragmentManager.beginTransaction()
        when (openMode) {
            FragmentOpener.OpenMode.ADD -> fragmentTransaction.add(R.id.frame, fragment as Fragment, fragment.fragmentTag())
            FragmentOpener.OpenMode.REPLACE -> fragmentTransaction.replace(R.id.frame, fragment as Fragment, fragment.fragmentTag())
        }
        fragmentTransaction.commit()
    }
}
