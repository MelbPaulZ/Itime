package org.unimelb.itime.ui.activity;

import android.support.annotation.NonNull;
import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendar;
import org.unimelb.itime.ui.fragment.event.FragmentEventCalendar;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateDuration;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateTitle;
import org.unimelb.itime.ui.fragment.event.FragmentEventRepeat;

public class MainActivity extends ItimeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FragmentEventCreateNote fragment = new FragmentEventCreateNote();
//        FragmentEventCreateUrl fragment = new FragmentEventCreateUrl();
//        FragmentEventRepeatCustom fragment = new FragmentEventRepeatCustom();
//        FragmentEventEndRepeat fragment = new FragmentEventEndRepeat();
//        FragmentEventRepeat fragment = new FragmentEventRepeat();
//        FragmentEventCreateDuration fragment = new FragmentEventCreateDuration();
//        FragmentEventCalendar fragment = new FragmentEventCalendar();
//        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, fragment).commit();
//        FragmentEventCreate fragment = new FragmentEventCreate();
//        FragmentEventCreateTitle fragment = new FragmentEventCreateTitle();
//        fragment.setEvent(new Event());
//        FragmentEventCreate fragment = new FragmentEventCreate();
//        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,fragment, FragmentEventCreate.class.getSimpleName()).commit();        FragmentEventCreate fragment = new FragmentEventCreate();
        FragmentCalendar fragment = new FragmentCalendar();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,fragment, FragmentEventCreate.class.getSimpleName()).commit();
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frag_container;
    }
}
