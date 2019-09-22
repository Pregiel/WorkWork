package pl.pregiel.workwork.exceptions


import android.content.Context

abstract class ShowToastException(message: String) : Exception(message) {
    abstract fun showToast(context: Context)
}
