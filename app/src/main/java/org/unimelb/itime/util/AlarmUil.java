package org.unimelb.itime.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yuhaoliu on 4/03/2017.
 */

public class AlarmUil {
    public static final int ALARM_UNIQUE_CODE = 10086;
    public static final String START_TIME = "ALARM_TIME";
    public static final String EVENT_UID = "EVENT_UID";
    public static final long tenDaysLong = 10 * 24 * 60 * 1000;

    public static void synchronizeSysAlarm(Context context){
        if (UserUtil.getInstance(context).isLogin()){
            Event event = getNextAlarmEvent(context);
            if (event != null){
                AlarmUil.addAlarmToSystem(context, event);
            }
        }
    }

    private static Event getNextAlarmEvent(Context context){
        EventManager manager = EventManager.getInstance(context);

        //means app not running currently
        if (manager == null){
            manager = EventManager.getInstance(context);
            manager.refreshEventManager();
        }

        List<Event> candidates = manager.getAllEvents();
        List<Event> events = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        long maxTime = currentTime + tenDaysLong;

        for (Event event:candidates
             ) {
            if (event.getReminder() != -1
                    && (event.getStartTime() - event.getReminder() * 1000) > currentTime
                    && event.getStartTime() < maxTime
                    ){
                events.add(event);
            }
        }

        if (events.size() <= 0){
            return null;
        }

        return Collections.min(events, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                long alarmTimeL = lhs.getStartTime() - lhs.getReminder() * 1000;
                long alarmTimeR = rhs.getStartTime() - rhs.getReminder() * 1000;

                return Long.compare(alarmTimeL,alarmTimeR);
            }
        });
    }

    private static void addAlarmToSystem(Context from, Event event){
        cancelExistedAlarm(from);

        long alarmTime = event.getStartTime() - event.getReminder() * 1000;
        Intent intent = new Intent(from, AlarmReceiver.class);
        intent.putExtra(EVENT_UID,event.getEventUid());
        intent.putExtra(START_TIME,event.getStartTime());

        PendingIntent pi = PendingIntent.getBroadcast(from, ALARM_UNIQUE_CODE , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)from.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);

    }

    private static void cancelExistedAlarm(Context from){
        Intent intent = new Intent(from, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(from, ALARM_UNIQUE_CODE , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)from.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }
}
