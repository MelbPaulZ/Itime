package org.unimelb.itime.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.util.EventUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yuhaoliu on 11/7/17.
 * Using for mock data
 */

@Deprecated
public class DataGeneratorManager {
    private static final String EVENTS_FILE_NAME = "events.json";
    private static final String MEETING_FILE_NAME = "meeting.json";
    private static final String MEETING_LIST_FILE_NAME = "meetinglist.json";

    private static DataGeneratorManager ourInstance;
    private User user;
    private Context context;

    public static DataGeneratorManager getInstance(Context context) {

        if (ourInstance == null){
            ourInstance = new DataGeneratorManager(context);
            ourInstance.generateData();
        }

        return ourInstance;
    }

    public static DataGeneratorManager getInstance() {

        if (ourInstance == null){
            throw new RuntimeException("The DataGeneratorManager should be initialised with context param when calling firs time.");
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
        initUser();
//        initMeetingFromJson();
        initMeetingListFromJson();
//        initMeeting();
//        initEvent();
//        initContact();
    }

    private void clearDB(){
        DBManager.getInstance(context).clearDB();
    }

    /**
     * Not insert to db
     */
    private void initUser(){
        user = new User();
        user.setPersonalAlias("Chuandong Yin");
        user.setUserUid("johncdyin@gmail.com");
        user.setUserId("johncdyin@gmail.com");
        user.setPhoto("http://ac-Sk9FQYeP.clouddn.com/srLVmMIBvCGXsIBnUkSdQTD");
        user.setGender(User.MALE);
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
            meeting.setInfo("MainAC generated");
            meetings.add(meeting);
        }

        DBManager.getInstance(context).insertOrReplace(meetings);
    }

    private void initEvent(){
        String json = null;
        try {

            InputStream is = context.getAssets().open(EVENTS_FILE_NAME);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Event>>() {}.getType();
        List<Event> events = gson.fromJson(json, listType);


        for (Event event :events
             ) {
            EventManager.getInstance(context).addEvent(event);
        }
        DBManager.getInstance(context).insertOrReplace(events);
    }

    private void initContact(){
        DBManager.getInstance(context).insertOrReplace(getContacts());
    }

    public User getUser() {
        return user;
    }

    public List<Contact> getContacts(){
        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setAliasName("");
        contact1.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact1.setContactUid("1");

        int i=0;
        for(Contact contact:contacts){
            User user = new User();
            user.setPersonalAlias("xy");
            user.setEmail("123456@unimelb.edu.au");
            user.setUserId("123456@unimelb.edu.au");
            user.setUserUid(i+"");
            i++;
            contact.setUserUid(i+"");
            contact.setUserDetail(user);
        }

        return contacts;
    }


    public void initMeetingFromJson(){
        String json = null;
        try {

            InputStream is = context.getAssets().open(MEETING_FILE_NAME);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        Meeting meeting = gson.fromJson(json, Meeting.class);

        List<Meeting> list = new ArrayList<>();
        list.add(meeting);

        List<Event> eventlist = new ArrayList<>();
        eventlist.add(meeting.getEvent());

//        DBManager.getInstance(context).insertOrReplace(eventlist);
        DBManager.getInstance(context).insertOrReplace(list);
    }

    public void initMeetingListFromJson(){
        String json = null;
        try {

            InputStream is = context.getAssets().open(MEETING_LIST_FILE_NAME);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Meeting>>() {}.getType();
        List<Meeting> meetings = gson.fromJson(json, listType);

        DBManager.getInstance(context).insertOrReplace(meetings);
    }
}
