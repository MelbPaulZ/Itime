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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        FragmentEventDetail fragment = new FragmentEventDetail();
        fragment.setData(getEvent());
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
    }

    public Event getEvent(){
        Event event = new Event();
        event.setSummary("Garden Together MEL");
        Location location = new Location();
        location.setLocationString1("Main Yarra Trail loooooooooong Line 2 1232");
        location.setLocationString2("Main Yarra Trail loooooooooong Line 2 1232");
        event.setLocation(location);
        event.setCoverPhoto("http://s1.dwstatic.com/group1/M00/DA/29/7cb28a3fcf4e3c10459ecbdb89bc409e.jpg");
        event.setHost("1");
        event.setHostUserUid("1");

        Map<String, Invitee> invitees = new HashMap<>();
        Invitee invitee = new Invitee();
        invitee.setUserId("1");
        invitee.setUserUid("1");
        invitee.setInviteeUid("1");
        invitee.setAliasName("Tim");
        invitee.setAliasPhoto("http://s1.dwstatic.com/group1/M00/DA/29/7cb28a3fcf4e3c10459ecbdb89bc409e.jpg");
        invitees.put(invitee.getInviteeUid(), invitee);

        Invitee invitee2 = new Invitee();
        invitee2.setUserId("2");
        invitee2.setUserUid("2");
        invitee2.setInviteeUid("2");
        invitee2.setAliasName("John");
        invitee2.setAliasPhoto("http://s1.dwstatic.com/group1/M00/DA/29/7cb28a3fcf4e3c10459ecbdb89bc409e.jpg");
        invitees.put(invitee2.getInviteeUid(), invitee2);

        Invitee invitee3 = new Invitee();
        invitee3.setUserId("3");
        invitee3.setUserUid("3");
        invitee3.setInviteeUid("3");
        invitee3.setAliasName("David");
        invitee3.setAliasPhoto("http://s1.dwstatic.com/group1/M00/DA/29/7cb28a3fcf4e3c10459ecbdb89bc409e.jpg");
        invitees.put(invitee3.getInviteeUid(),invitee3);
        event.setInvitee(invitees);
        event.setNote("Width 670 ,margin left&right 20. Just bring whatever foods you like. I’ll take ca whatever foods you like. I’ll take care of all the tools.\n" +
                "Just bring whatever foods you like. I’ll take care of all the tools. Just bring whatever foods you like. I’ll take care of all the tools. Just bring whatever foods you like. I’ll take care of all the tools. \n" +
                "are of all the tools. Just bring whatever foods you like. I’ll take care of all the tools. Just bring whatever foods you like. I’ll take care of all the tools.");


        List<PhotoUrl> photos = new ArrayList<>();
        for(int i = 0;i<6;i++){
            PhotoUrl photoUrl = new PhotoUrl();
            photoUrl.setUrl("http://s1.dwstatic.com/group1/M00/DA/29/7cb28a3fcf4e3c10459ecbdb89bc409e.jpg");
            photos.add(photoUrl);
        }
        event.setPhotos(photos);
        event.setUrl("https://www.google.com.au/");

        Map<String, TimeSlot> timeSlots = new HashMap<>();
        for(int i=0;i<5;i++){
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setEndTime(System.currentTimeMillis());
            timeSlot.setStartTime(System.currentTimeMillis());
            timeSlots.put(timeSlot.getTimeslotUid(), timeSlot);
        }
        event.setTimeslot(timeSlots);
        return event;
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
