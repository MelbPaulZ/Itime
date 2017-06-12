package org.unimelb.itime.util;

import android.support.annotation.Nullable;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.ITimeComparable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by yuhaoliu on 10/06/2017.
 */

public class EventUtil {
    public final static long allDayMilliseconds = 24 * 60 * 60 * 1000;

    public static Calendar getBeginOfDayCalendar(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cal.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static long getDayBeginMilliseconds(long startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTimeInMillis();
    }

    public static long getDayEndMilliseconds(long endTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);

        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static <T extends ITimeComparable> void removeWhileLooping(ArrayList<T> list, T rmObj){
        Iterator<T> i = list.iterator();
        while (i.hasNext()) {
            T obj = i.next(); // must be called before you can call i.remove()
            if (obj.iTimeEquals(rmObj)){
                i.remove();
                break;
            }
        }
    }

    public static Date untilConverter(long orgStartTime, Date orgDate, @Nullable TimeZone fromTZ){
        if (fromTZ == null){
            fromTZ = TimeZone.getTimeZone("UTC");
        }

        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(orgStartTime);

        int toUTCOffset = TimeZone.getDefault().getOffset(orgDate.getTime());
        int fromUTCOffset = fromTZ.getOffset(orgDate.getTime());
        int offsetTZ = toUTCOffset - fromUTCOffset;
        int mGMTOffset = -(offsetTZ/(60 * 60 * 1000));

        int offset = 0;
        if (mGMTOffset + temp.get(Calendar.HOUR_OF_DAY) > 24){
            offset = 1;
        }else if (mGMTOffset + temp.get(Calendar.HOUR_OF_DAY) < 0){
            offset = -1;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(orgDate.getTime());
        cal.add(Calendar.DATE,-offset);
        cal.set(Calendar.HOUR_OF_DAY,temp.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE,temp.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND,59);
        Date result = new Date();
        result.setTime(cal.getTimeInMillis());

        return result;
    }

    public static Event getNewEvent(){
        Event event = new Event();
        event.setTitle("New Event");
        event.setShowLevel(1);
        event.setEventUid(UUID.randomUUID().toString());
        return event;
    }
}
