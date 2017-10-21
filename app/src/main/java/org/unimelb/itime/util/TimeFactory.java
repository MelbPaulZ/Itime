package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.TimeSlot;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Qiushuo Huang on 2017/8/4.
 */

public class TimeFactory {
    public static String YEAR="yyyy";
    public static String MONTH="MM";
    public static String DAY="dd";

    public static String HOUR_MIN = "kk:mm";
    public static String HOUR_MIN_A = "kk:mm a";
    public static String WEEK_DAY_MONTH = "EEE, dd MMM";
    public static String HOUR_MIN_WEEK_DAY_MONTH = "kk:mm a EEE,dd MMM";
    public static String DAY_MONTH_YEAR = "dd MMM yyyy";
    public static String TIME_ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
    public static String UTC_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String[] getTimeStrings(Context context, TimeSlot timeSlot){
        Locale locale = context.getResources().getConfiguration().locale;
        String[] result = new String[2];
        if(timeSlot!=null){
            if(timeSlot.isAllDay()){
                result[0] = getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH, locale);
                result[1] = context.getString(R.string.all_day);

            }else if (isDifferentYear(timeSlot, locale)){
                result[0] = getFormatTimeString(timeSlot.getStartTime(), HOUR_MIN_A, locale)
                +" "
                +getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH, locale);

                result[1] = getFormatTimeString(timeSlot.getEndTime(), HOUR_MIN_A, locale)
                        +" "
                        +getFormatTimeString(timeSlot.getEndTime(), DAY_MONTH_YEAR, locale);

            }else if(isDifferentDay(timeSlot, locale)){
                result[0] = getFormatTimeString(timeSlot.getStartTime(), HOUR_MIN_A, locale)
                        +" "
                        +getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH, locale);

                result[1] = getFormatTimeString(timeSlot.getEndTime(), HOUR_MIN_A, locale)
                        +" "
                        +getFormatTimeString(timeSlot.getEndTime(), WEEK_DAY_MONTH, locale);
            }else{
                result[0] = getFormatTimeString(timeSlot.getStartTime(), HOUR_MIN_A, locale)
                        +" → "
                        + getFormatTimeString(timeSlot.getEndTime(), HOUR_MIN_A, locale);
                result[1] = getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH, locale);
            }
        }
        return result;
    }

    public static boolean isDifferentYear(TimeSlot timeSlot, Locale locale){
        return !getYear(timeSlot.getStartTime(), locale).equals(getYear(timeSlot.getEndTime(), locale));
    }

    public static boolean isDifferentDay(TimeSlot timeSlot, Locale local){
        return !getFormatTimeString(timeSlot.getStartTime(), DAY_MONTH_YEAR,local)
                .equals(getFormatTimeString(timeSlot.getEndTime(), DAY_MONTH_YEAR,local));
    }

    public static String getYear(long time, Locale locale){
        return getFormatTimeString(time, YEAR, locale);
    }

    public static String getMonth(long time, Locale locale){
        return getFormatTimeString(time, MONTH, locale);
    }

    public static String getDay(long time, Locale locale){
        return getFormatTimeString(time, DAY, locale);
    }

    public static String getFormatTimeString(long time, String format, Locale locale){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat fmt = new SimpleDateFormat(format, locale);
        return fmt.format(c.getTime());
    }

    public static String getFormatTimeString(Date date, String format, Locale locale){
        SimpleDateFormat fmt = new SimpleDateFormat(format, locale);
        return fmt.format(date);
    }


    public static Long getFormatTimeLong(String time, String format, Locale locale){
        SimpleDateFormat fmt = new SimpleDateFormat(format, locale);
        try {
            return fmt.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        throw new RuntimeException(time + "Cannot be converted by " + format);
    }

    /**
     * Return days & hours & minutes which means the diff between the
     * input time and current time
     * @param time
     * @return
     */
    public static long[] getTimeDiffWithToday(long time){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long diff = time - currentTime;
        long minutesTotal = TimeUnit.MILLISECONDS.toMinutes(diff);

        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff)%24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff)%60;
        return new long[] {days,hours,minutes,minutesTotal};
    }

    public static Long getUpdatedAtLong(String updatedAt, Locale locale){
        return getFormatTimeLong(updatedAt, UTC_PATTERN, locale);
    }

}
