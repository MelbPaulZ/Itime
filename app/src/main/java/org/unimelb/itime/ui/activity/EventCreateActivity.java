package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateAddInvitee;
import org.unimelb.itime.ui.fragment.event.FragmentEventPrivateCreate;

/**
 * Created by Paul on 12/6/17.
 */

public class EventCreateActivity extends ItimeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        String type = getIntent().getStringExtra(getString(R.string.event_type));
        Fragment fragment;
        if (type!=null && type.equals(getString(R.string.event_type_group))){
            FragmentEventCreateAddInvitee addInviteeFragment = new FragmentEventCreateAddInvitee();
            Event event = new Event();
            addInviteeFragment.setEvent(event);
            fragment = addInviteeFragment;
        }else{
            fragment = new FragmentEventPrivateCreate();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frag_container_event_create;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
