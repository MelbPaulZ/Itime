package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventPrivateCreate;

/**
 * Created by Paul on 15/6/17.
 */

public class TestActivity extends ItimeBaseActivity {


    @Override
    protected int getFragmentContainerId() {
        return R.id.test_container;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        FragmentEventCreateAlert fragment = new FragmentEventCreateAlert();
        FragmentEventPrivateCreate fragment = new FragmentEventPrivateCreate();
//        FragmentEventCreate fragment = new FragmentEventCreate();
        getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }
}
