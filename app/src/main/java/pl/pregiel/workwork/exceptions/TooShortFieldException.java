package pl.pregiel.workwork.exceptions;


import android.content.Context;
import android.widget.Toast;

import pl.pregiel.workwork.R;

public class TooShortFieldException extends ShowToastException  {
    private int minimumCharAmount = 0;

    public TooShortFieldException(String message, int minimumCharAmount) {
        super(message);

        this.minimumCharAmount = minimumCharAmount;
    }

    @Override
    public void showToast(Context context) {
        Toast.makeText(context, context.getString(R.string.error_tooShortField, getMessage(), minimumCharAmount),
                Toast.LENGTH_SHORT).show();
    }
}
