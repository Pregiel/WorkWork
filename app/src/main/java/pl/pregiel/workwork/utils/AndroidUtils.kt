package pl.pregiel.workwork.utils

import android.view.ViewGroup


object AndroidUtils {

    @JvmStatic
    fun setEnabledControl(enable: Boolean, vg: ViewGroup, vararg exclude: Int) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            if (!exclude.contains(child.id)) {
                child.isEnabled = enable
                if (child is ViewGroup) {
                    setEnabledControl(enable, child)
                }
            }
        }
    }
}