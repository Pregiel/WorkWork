package pl.pregiel.workwork.exceptions


import android.content.Context
import android.widget.Toast

import pl.pregiel.workwork.R

class TooShortFieldException(message: String, private val minimumCharAmount: Int) : ShowToastException(message) {

    override fun showToast(context: Context) {
        Toast.makeText(context, context.getString(R.string.error_tooShortField, message, minimumCharAmount),
                Toast.LENGTH_SHORT).show()
    }
}
