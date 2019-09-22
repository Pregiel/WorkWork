package pl.pregiel.workwork.utils


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

import pl.pregiel.workwork.R

object CustomAlert {
    fun buildAlert(context: Context, titleId: Int, messageId: Int,
                   positiveTitleId: Int = R.string.global_ok, positiveRunnable: Runnable?,
                   negativeTitleId: Int = R.string.global_cancel, negativeRunnable: Runnable?): AlertDialog.Builder {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(titleId)
        builder.setMessage(messageId)
        builder.setPositiveButton(positiveTitleId) { dialog, _ ->
            positiveRunnable?.run()
            dialog.dismiss()
        }
        builder.setNegativeButton(negativeTitleId) { dialog, _ -> dialog.cancel() }
        builder.setOnCancelListener {
            negativeRunnable?.run()
        }
        return builder
    }
}
