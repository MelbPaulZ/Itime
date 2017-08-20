package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qiushuo Huang on 2017/6/18.
 */

public class EventDetailActivity extends ItimeBaseActivity{
    public static String EVENT = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        FragmentEventDetail fragment = new FragmentEventDetail();
        Event event = (Event) getIntent().getSerializableExtra(EVENT);
        if(event!=null) {
            fragment.setData(event);
        }else {
            throw new RuntimeException("Event can't be null");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
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
