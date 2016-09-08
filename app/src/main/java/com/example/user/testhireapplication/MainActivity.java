package com.example.user.testhireapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends SingleFragmentActivity implements MenuFragment.MenuClickListener {
    private final static String TAG = "MainActivity";
    private final static String ABOUT_DIALOG_FRAGMENT="dialog_fragment";
    FragmentManager fm;

    @Override
    Fragment getFragment() {
        return MenuFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);
        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        /*
        check if fragment does not exist
        */

        if (fragment == null) {
            fm.beginTransaction().replace(R.id.fragment_container, getFragment()).commit();
        }

    }

    @Override
    public void proceedToSchedule() {
        Log.i(TAG, "sched_button pressed, need to proceed to Schedule");
        switchFragment(ScheduleFragment.newInstance());

    }

    @Override
    public void proceedToAbout() {
        Log.i(TAG, "about_button pressed, need to proceed to About");
        switchFragment(AboutFragment.newInstance());

    }

    public void switchFragment(Fragment fragment) {
        fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}
