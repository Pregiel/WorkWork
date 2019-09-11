package pl.pregiel.workwork.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import pl.pregiel.workwork.R;

public class CustomAlert {
    public static void buildAlert(Context context, int titleId, int messageId,
                                  final Runnable positiveRunnable, final Runnable negativeRunnable) {
        buildAlert(context, titleId, messageId, R.string.global_ok, positiveRunnable,
                R.string.global_cancel, negativeRunnable);
    }

    public static AlertDialog.Builder buildAlert(Context context, int titleId, int messageId,
                                                 int positiveTitleId, final Runnable positiveRunnable,
                                                 int negativeTitleId, final Runnable negativeRunnable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(positiveTitleId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (positiveRunnable != null)
                    positiveRunnable.run();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(negativeTitleId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (negativeRunnable != null)
                    negativeRunnable.run();
            }
        });
        return builder;
    }
}
