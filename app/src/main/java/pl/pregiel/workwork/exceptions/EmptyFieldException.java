package pl.pregiel.workwork.exceptions;


import android.content.Context;
import android.widget.Toast;

import pl.pregiel.workwork.R;

public class EmptyFieldException extends ShowToastException {
    public EmptyFieldException(String message) {
        super(message);
    }

    public void showToast(Context context) {
        Toast.makeText(context, context.getString(R.string.error_emptyField, getMessage()), Toast.LENGTH_SHORT).show();
    }
}
