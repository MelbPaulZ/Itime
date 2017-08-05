package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public static String[] getTimeStrings(Context context, TimeSlot timeSlot){
        String[] result = new String[2];
        if(timeSlot!=null){
            if(timeSlot.isAllDay()){
                result[0] = getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH);
                result[1] = context.getString(R.string.all_day);

            }else if (isDifferentYear(timeSlot)){
                result[0] = getFormatTimeString(timeSlot.getStartTime(), HOUR_MIN_A)
                +" "
                +getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH);

                result[1] = getFormatTimeString(timeSlot.getEndTime(), HOUR_MIN_A)
                        +" "
                        +getFormatTimeString(timeSlot.getEndTime(), DAY_MONTH_YEAR);

            }else if(isDifferentDay(timeSlot)){
                result[0] = getFormatTimeString(timeSlot.getStartTime(), HOUR_MIN_A)
                        +" "
                        +getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH);

                result[1] = getFormatTimeString(timeSlot.getEndTime(), HOUR_MIN_A)
                        +" "
                        +getFormatTimeString(timeSlot.getEndTime(), WEEK_DAY_MONTH);
            }else{
                result[0] = getFormatTimeString(timeSlot.getStartTime(), HOUR_MIN_A)
                        +" → "
                        + getFormatTimeString(timeSlot.getEndTime(), HOUR_MIN_A);
                result[1] = getFormatTimeString(timeSlot.getStartTime(), WEEK_DAY_MONTH);
            }
        }
        return result;
    }

    public static boolean isDifferentYear(TimeSlot timeSlot){
        return !getYear(timeSlot.getStartTime()).equals(getYear(timeSlot.getEndTime()));
    }

    public static boolean isDifferentDay(TimeSlot timeSlot){
        return !getFormatTimeString(timeSlot.getStartTime(), DAY_MONTH_YEAR)
                .equals(getFormatTimeString(timeSlot.getEndTime(), DAY_MONTH_YEAR));
    }

    public static String getYear(long time){
        return getFormatTimeString(time, YEAR);
    }

    public static String getMonth(long time){
        return getFormatTimeString(time, MONTH);
    }

    public static String getDay(long time){
        return getFormatTimeString(time, DAY);
    }

    public static String getFormatTimeString(long time, String format){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(c.getTime());
    }

}
