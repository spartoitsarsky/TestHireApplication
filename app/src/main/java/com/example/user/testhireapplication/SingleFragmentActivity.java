package com.example.user.testhireapplication;

/**
 * Created by user on 03.09.2016.
 */
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by v.borovkov on 02.09.2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    abstract Fragment getFragment();

    @LayoutRes
    int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.fragment_container);

    }
}