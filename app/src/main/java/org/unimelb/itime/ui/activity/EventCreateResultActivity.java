package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateSentFragment;

/**
 * Created by Paul on 23/9/17.
 */

public class EventCreateResultActivity extends ItimeBaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create_result);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("Event");
        FragmentEventCreateSentFragment fragment = new FragmentEventCreateSentFragment();
        fragment.setEvent(event);
        getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragment).commit();

    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }
}
