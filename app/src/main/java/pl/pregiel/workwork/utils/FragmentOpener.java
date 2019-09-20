package pl.pregiel.workwork.utils;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import pl.pregiel.workwork.R;
import pl.pregiel.workwork.data.pojo.Work;
import pl.pregiel.workwork.data.pojo.WorkTime;
import pl.pregiel.workwork.fragments.TaggedFragment;

public class FragmentOpener {
    public enum OpenMode {
        ADD, REPLACE
    }

    public static void openFragment(FragmentActivity fragmentActivity, TaggedFragment fragment, OpenMode openMode) {
        Bundle arguments = new Bundle();
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, openMode, arguments);
    }

    public static void openFragment(FragmentActivity fragmentActivity, TaggedFragment fragment, OpenMode openMode, WorkTime workTime) {
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, openMode, workTime);
    }

    public static void openFragment(FragmentManager fragmentManager, TaggedFragment fragment, OpenMode openMode) {
        Bundle arguments = new Bundle();
        openFragment(fragmentManager, fragment, openMode, arguments);
    }

    public static void openFragment(FragmentManager fragmentManager, TaggedFragment fragment, OpenMode openMode, WorkTime workTime) {
        Bundle arguments = new Bundle();
        arguments.putInt("worktime_id", workTime.getId());
        openFragment(fragmentManager, fragment, openMode, arguments);
    }

    public static void openFragment(FragmentActivity fragmentActivity, TaggedFragment fragment, OpenMode openMode, Work work) {
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, openMode, work);
    }

    public static void openFragment(FragmentManager fragmentManager, TaggedFragment fragment, OpenMode openMode, Work work) {
        Bundle arguments = new Bundle();
        arguments.putInt("work_id", work.getId());
        openFragment(fragmentManager, fragment, openMode, arguments);
    }

    public static void openFragment(FragmentActivity fragmentActivity, TaggedFragment fragment, OpenMode openMode, Bundle arguments) {
        openFragment(fragmentActivity.getSupportFragmentManager(), fragment, openMode, arguments);
    }

    public static void openFragment(FragmentManager fragmentManager, TaggedFragment fragment, OpenMode openMode, Bundle arguments) {
        if (arguments != null)
            ((android.support.v4.app.Fragment) fragment).setArguments(arguments);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (openMode) {
            case ADD:
                fragmentTransaction.add(R.id.frame, (android.support.v4.app.Fragment) fragment, fragment.getFragmentTag());
                break;
            case REPLACE:
                fragmentTransaction.replace(R.id.frame, (android.support.v4.app.Fragment) fragment, fragment.getFragmentTag());
                break;
        }
        fragmentTransaction.commit();
    }
}
