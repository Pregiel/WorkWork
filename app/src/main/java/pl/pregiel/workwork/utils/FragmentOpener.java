package pl.pregiel.workwork.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;

public class FragmentOpener {
    public enum OpenMode {
        ADD, REPLACE
    }

    public static void openFragment(FragmentActivity fragmentActivity, Fragment fragment, String tag, OpenMode openMode) {
        Bundle arguments = new Bundle();
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, tag, openMode, arguments);
    }

    public static void openFragment(FragmentActivity fragmentActivity, Fragment fragment, String tag, OpenMode openMode, WorkTime workTime) {
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, tag, openMode, workTime);
    }

    public static void openFragment(FragmentManager fragmentManager, Fragment fragment, String tag, OpenMode openMode, WorkTime workTime) {
        Bundle arguments = new Bundle();
        arguments.putInt("worktime_id", workTime.getId());
        openFragment(fragmentManager, fragment, tag, openMode, arguments);
    }

    public static void openFragment(FragmentActivity fragmentActivity, Fragment fragment, String tag, OpenMode openMode, Work work) {
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, tag, openMode, work);
    }

    public static void openFragment(FragmentManager fragmentManager, Fragment fragment, String tag, OpenMode openMode, Work work) {
        Bundle arguments = new Bundle();
        arguments.putInt("work_id", work.getId());
        openFragment(fragmentManager, fragment, tag, openMode, arguments);
    }

    public static void openFragment(FragmentManager fragmentManager, Fragment fragment, String tag, OpenMode openMode, Bundle arguments) {
        if (arguments != null)
            fragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        System.out.println(fragmentTransaction.isEmpty());
        switch (openMode) {
            case ADD:
                fragmentTransaction.add(R.id.frame, fragment, tag);
                break;
            case REPLACE:
                fragmentTransaction.replace(R.id.frame, fragment, tag);
                break;
        }
        fragmentTransaction.commit();
    }
}
