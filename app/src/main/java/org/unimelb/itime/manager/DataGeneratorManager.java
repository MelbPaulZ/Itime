package org.unimelb.itime.manager;

import android.content.Context;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yuhaoliu on 11/7/17.
 */

public class DataGeneratorManager {
    private static DataGeneratorManager ourInstance;
    private Context context;

    public static DataGeneratorManager getInstance(Context context) {

        if (ourInstance == null){
            ourInstance = new DataGeneratorManager(context);
            ourInstance.generateData();
        }

        return ourInstance;
    }

    private DataGeneratorManager(Context context) {
        this.context = context;
    }

    private void generateData(){
        initData();
    }

    private void initData() {
        clearDB();
        initMeeting();
        initEvent();
        initContact();
    }

    private void clearDB(){
        DBManager.getInstance(context).clearDB();
    }

    private void initMeeting(){
        List<Meeting> meetings = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        List<Event> events = new ArrayList<>();
        int[] type = {0, 1, 2};
        int[] status = {0, 1};
        long allDayInterval = (24 * 3600 * 1000);
        long halfDayInterval = (12 * 3600 * 1000);
        long interval = (3600 * 1000);
        long startTime = calendar.getTimeInMillis();
        long endTime;
        for (int i = 1; i < 5; i++) {
            endTime = startTime + interval;
            Event event = EventUtil.getNewEvent();
            event.setSummary("Telstra Competition Discussion");
            event.setCoverPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
            event.setGreeting("“We need to discuss about the next step. Please come to the meeting  ” ");
//            event.setIsAllDay(i%2==0);
            event.setIsAllDay(false);
            event.setLocation(new Location());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            events.add(event);

            startTime = startTime + halfDayInterval;

            Meeting meeting = new Meeting();
            meeting.setEvent(event);
            meeting.setSysMsg("MainAC generated");
            meetings.add(meeting);
        }

        DBManager.getInstance(context).insertOrReplace(meetings);
    }

    private void initEvent(){
        Calendar calendar = Calendar.getInstance();
        List<Event> events = new ArrayList<>();
        int[] type = {0, 1, 2};
        int[] status = {0, 1};
        long allDayInterval = (24 * 3600 * 1000);
        long halfDayInterval = (12 * 3600 * 1000);
        long interval = (3600 * 1000);
        long startTime = calendar.getTimeInMillis();
        long endTime;
        for (int i = 1; i < 20; i++) {
            endTime = startTime + interval;
            Event event = EventUtil.getNewEvent();
            event.setSummary("Telstra Competition Discussion");
            event.setCoverPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
            event.setGreeting("“We need to discuss about the next step. Please come to the meeting  ” ");
//            event.setIsAllDay(i%2==0);
            event.setIsAllDay(false);
            event.setLocation(new Location());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            events.add(event);

            startTime = startTime + halfDayInterval;
            EventManager.getInstance(context).addEvent(event);
        }

        DBManager.getInstance(context).insertOrReplace(events);
    }

    private void initContact(){
        DBManager.getInstance(context).insertOrReplace(getContacts());
    }

    public List<Contact> getContacts(){
        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setAliasName("a");
        contact1.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact1.setContactUid("1");

//        Contact contact2 = new Contact();
//        contact2.setAliasName("b");
//        contact2.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact2.setContactUid("2");
//
//        Contact contact3 = new Contact();
//        contact3.setAliasName("c");
//        contact3.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact3.setContactUid("3");
//
//        Contact contact4 = new Contact();
//        contact4.setAliasName("d");
//        contact4.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact4.setContactUid("4");
//
//        Contact contact5 = new Contact();
//        contact5.setAliasName("ad");
//        contact5.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact5.setContactUid("5");
//
        contacts.add(contact1);
//        contacts.add(contact5);
//        contacts.add(contact2);
//        contacts.add(contact3);
//        contacts.add(contact4);

        int i=0;
        for(Contact contact:contacts){
            User user = new User();
            user.setPersonalAlias("ab");
            user.setEmail("123456@unimelb.edu.au");
            user.setUserId("123456@unimelb.edu.au");
            user.setUserUid(i+"");
            i++;
            contact.setUserUid(i+"");
            contact.setUserDetail(user);
        }

        return contacts;
    }
}
