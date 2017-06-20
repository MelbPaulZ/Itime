package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.event.FragmentEventLocation;

/**
 * Created by Paul on 8/6/17.
 */

public class LocationActivity extends ItimeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        String location = intent.getStringExtra(getString(R.string.location_string1));

        if (location==null) {
            location = "";// TODO: 8/6/17 delete when not testing
        }
        FragmentEventLocation fragmentEventLocation = new FragmentEventLocation();
        fragmentEventLocation.setLocation(location);

        getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragmentEventLocation).commit();

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.location_container;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
