package pl.pregiel.workwork.utils


import android.content.Context
import android.widget.Toast

import pl.pregiel.workwork.R

object ErrorToasts {
    fun showUnknownErrorToast(context: Context) {
        Toast.makeText(context, context.getString(R.string.error_unknown), Toast.LENGTH_LONG).show()
    }
}
