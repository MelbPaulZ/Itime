package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateAddInvitee;
import org.unimelb.itime.ui.fragment.event.FragmentEventPrivateCreate;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;

/**
 * Created by Paul on 12/6/17.
 */

public class EventCreateActivity extends ItimeBaseActivity {
    public static final int CREATE_EVENT = 1001;
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        String type = getIntent().getStringExtra(getString(R.string.event_type));
        Fragment fragment;
        if (type!=null && type.equals(getString(R.string.event_type_group))){
            FragmentEventCreateAddInvitee addInviteeFragment = new FragmentEventCreateAddInvitee();
            Event event = new Event();
            event.setStatus(Event.STATUS_PENDING);
            event.setCalendarUid(CalendarUtil.getInstance(getApplicationContext()).getDefaultCalendarUid());
            addInviteeFragment.setEvent(event);
            fragment = addInviteeFragment;

            Invitee invitee = null;
            Contact contact = (Contact) getIntent().getSerializableExtra(KEY_CONTACT);
            User user = (User) getIntent().getSerializableExtra(KEY_USER);
            if(contact!=null){
                invitee = EventUtil.generateInvitee(event, contact);
            }
            if(user!=null){
                invitee = EventUtil.generateInvitee(event, user);
            }
            if(invitee!=null){
                event.getInvitee().put(invitee.getUserId(), invitee);
            }
        }else{
            //For solo event
            //auto-fill solo event detail
            Event ev = (Event) getIntent().getSerializableExtra("Event");
            if (ev == null){
                ev = new Event();
                Calendar c = Calendar.getInstance();
                ev.setStartTime(c.getTimeInMillis());
                ev.setEndTime(c.getTimeInMillis() + 1000 * 60 * 60);
            }
            ev.setCalendarUid(CalendarUtil.getInstance(getApplicationContext()).getDefaultCalendarUid());
            ev.setEventType(Event.EVENT_TYPE_SOLO);
            ev.setStatus(Event.STATUS_CONFIRMED);

            fragment = new FragmentEventPrivateCreate();
            ((FragmentEventPrivateCreate)fragment).setTaskMode(FragmentEventCreate.Mode.CREATE);
            ((FragmentEventPrivateCreate)fragment).setEvent(ev);
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
