package pl.pregiel.workwork.exceptions


import android.content.Context
import android.widget.Toast

import pl.pregiel.workwork.R

class EmptyFieldException(message: String) : ShowToastException(message) {

    override fun showToast(context: Context) {
        Toast.makeText(context, context.getString(R.string.error_emptyField, message), Toast.LENGTH_SHORT).show()
    }
}
