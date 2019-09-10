package pl.pregiel.workwork.exceptions;


import android.content.Context;

public abstract class ShowToastException extends Exception {
    public abstract void showToast(Context context);

    public ShowToastException(String message) {
        super(message);
    }
}
