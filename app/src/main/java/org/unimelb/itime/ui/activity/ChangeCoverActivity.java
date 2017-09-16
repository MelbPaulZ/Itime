package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.event.ChangeCoverFragment;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventDetail;

/**
 * Created by Qiushuo Huang on 2017/9/16.
 */

public class ChangeCoverActivity extends ItimeBaseActivity {
    public static String EVENT = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        ChangeCoverFragment fragment = new ChangeCoverFragment();
        Event event = (Event) getIntent().getSerializableExtra(EVENT);
        if(event!=null) {
            fragment.setEvent(event);
        }else {
            throw new RuntimeException("Event can't be null");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_event_create,fragment, ChangeCoverFragment.class.getSimpleName()).commit();
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
