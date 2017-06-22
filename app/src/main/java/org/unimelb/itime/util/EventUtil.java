package org.unimelb.itime.util;

import android.content.Context;
import android.support.annotation.Nullable;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.ITimeComparable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public static int durationString2Int(Context context, String duration){
        if (duration.equals(context.getString(R.string.duration_15_minutes))){
            return 15;
        }

        if (duration.equals(context.getString(R.string.duration_30_minutes))){
            return 30;
        }

        if (duration.equals(context.getString(R.string.duration_45_minutes))){
            return 45;
        }

        if (duration.equals(context.getString(R.string.duration_1_hour))){
            return 60;
        }

        if (duration.equals(context.getString(R.string.duration_2_hours))){
            return 120;
        }

        if (duration.equals(context.getString(R.string.duration_6_hours))){
            return 360;
        }

        if (duration.equals(context.getString(R.string.duration_all_day))){
            return 1440;
        }

        return -1;
    }

    public static String durationInt2String(Context context, int duration){
        if (duration==15){
            return context.getString(R.string.duration_15_minutes);
        }

        if (duration==30){
            return context.getString(R.string.duration_30_minutes);
        }

        if (duration == 45){
            return context.getString(R.string.duration_45_minutes);
        }

        if (duration == 60){
            return context.getString(R.string.duration_1_hour);
        }

        if (duration == 120){
            return context.getString(R.string.duration_2_hours);
        }

        if (duration == 360){
            return context.getString(R.string.duration_6_hours);
        }

        if (duration == 1440){
            return context.getString(R.string.duration_all_day);
        }

        return "N/A";
    }


    public static String HOUR_MIN = "kk:mm";
    public static String WEEK_DAY_MONTH = "EEE, dd MMM";
    public static String getFormatTimeString(long time, String format){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(c.getTime());
    }

    public static String reminderIntToString(Context context, int reminder){
        if (reminder == -1){
            return context.getString(R.string.event_alert_none);
        }
        if (reminder == 0){
            return context.getString(R.string.event_alert_at_time);
        }
        if (reminder == 5){
            return context.getString(R.string.event_alert_5_minutes_before);
        }
        if (reminder == 15){
            return context.getString(R.string.event_alert_15_minutes_before);
        }
        if (reminder == 30){
            return context.getString(R.string.event_alert_30_minutes_before);
        }
        if (reminder == 60){
            return context.getString(R.string.event_alert_1_hour_before);
        }
        if (reminder == 120){
            return context.getString(R.string.event_alert_2_hours_before);
        }
        if (reminder == 1440){
            return context.getString(R.string.event_alert_1_day_before);
        }
        if (reminder == 2880){
            return context.getString(R.string.event_alert_2_days_before);
        }
        if (reminder == 10080){
            return context.getString(R.string.event_alert_1_week_before);
        }
        return "N/A";
    }
}
