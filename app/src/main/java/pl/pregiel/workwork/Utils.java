package pl.pregiel.workwork;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public class Utils {
    public static String formatDoubleToString(double value, int precision) {
        return String.format("%." + precision + "f", value);
    }

    @SuppressLint("DefaultLocale")
    public static String formatDoubleToString(double value) {
        return String.format("%.2f", value);
    }

    public static String formatTime(Context context, int minutes) {
        return context.getString(R.string.format_hour, formatDoubleToString(minutes / 60));
    }

    public static int timeStringToMinutes(String time) throws NumberFormatException {
        String[] timeSplit = time.split(":");

        int hour = Integer.valueOf(timeSplit[0]);
        int minute = Integer.valueOf(timeSplit[1]);

        return hour * 60 + minute;
    }

    public static int timeStringWithSuffixesToMinutes(String time) throws NumberFormatException {
        String[] values = time.split("h ");

        int hour = Integer.valueOf(values[0].replaceAll("\\D", ""));
        int minute = Integer.valueOf(values[1].replaceAll("\\D", ""));

        return hour * 60 + minute;
    }

    public static void setEnabledControl(boolean enable, ViewGroup vg, int... exclude) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            if (!checkIfArrayContain(exclude, child.getId())) {
                child.setEnabled(enable);
                if (child instanceof ViewGroup) {
                    setEnabledControl(enable, (ViewGroup) child);
                }
            }
        }
    }

    private static boolean checkIfArrayContain(int[] array, int value) {
        for (int item : array) {
            if (item == value)
                return true;
        }
        return false;
    }
}
