package org.unimelb.itime.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.ITimeComparable;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.bean.TimeslotInvitee;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.util.rulefactory.FrequencyEnum;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;


/**
 * Created by yuhaoliu on 10/06/2017.
 */

public class EventUtil extends BaseUtil{
    public static double latitude;
    public static double longitude;


    private static String TAG = "EventUtil";
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
        event.setSummary("New Event");
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
    public static String DAY_MONTH_YEAR = "dd MMM yyyy";
    public static String TIME_ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    public static String getFormatTimeString(long time, String format){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(c.getTime());
    }

    public static Date parseTimeZoneToDate(String dateTime) {
        Date date = null;
        try {
            date = new SimpleDateFormat(TIME_ZONE_PATTERN, Locale.getDefault()).parse(dateTime);
        } catch (ParseException e) {
            Log.i(TAG, "timeZoneToDate: parse error " + dateTime);
        }
        return date;

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

    public static int reminderStringToInt(Context context, String reminderString){
        if (reminderString.equals(context.getString(R.string.event_alert_none))){
            return -1;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_at_time))){
            return 0;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_5_minutes_before))){
            return 5;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_15_minutes_before))){
            return 15;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_30_minutes_before))){
            return 30;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_1_hour_before))){
            return 60;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_2_hours_before))){
            return 120;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_1_day_before))){
            return 1440;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_2_days_before))){
            return 2880;
        }
        if (reminderString.equals(context.getString(R.string.event_alert_1_week_before))){
            return 10080;
        }
        return -100;
    }


    public static String[] getRepeatFreqStr(){
        return new String[]{"Daily","Weekly","Monthly","Annually"};
    }

    public static String[] getRepeatIntervalStr(){
        return new String[] {"1","2","3","4","5","6","7","8","9","10"};
    }

    public static FrequencyEnum getFreqEnum(String code){
        final String[] values = getRepeatFreqStr();

        if (code.equals(values[0])){
            return FrequencyEnum.DAILY;
        }else if(code.equals(values[1])){
            return FrequencyEnum.WEEKLY;
        }else if(code.equals(values[2])){
            return FrequencyEnum.MONTHLY;
        }else if(code.equals(values[3])){
            return FrequencyEnum.YEARLY;
        }

        return null;
    }

    public static String getRepeatStrByFreq(FrequencyEnum frequencyEnum){
        final String[] values = getRepeatFreqStr();

        if (frequencyEnum == null){
            return values[0];
        }

        switch (frequencyEnum){
            case DAILY:
                return values[0];
            case WEEKLY:
                return values[1];
            case MONTHLY:
                return values[2];
            case YEARLY:
                return values[3];

        }

        return "UnKnow";
    }

    /**
     * This get Repeat String methods return the message that should be displayed on screen
     */
    public static String getRepeatString(Context context, Event event) {
        Date d = parseTimeZoneToDate(event.getStart().getDateTime());
        String dayOfWeek = getFormatTimeString(d.getTime(), "EEE");
        FrequencyEnum frequencyEnum = event.getRule().getFrequencyEnum();
        int interval = event.getRule().getInterval();

        // for event detail and edit event, the frequencyEnum will be null,
        // but the recurrence is not null, so need to get the frequenceEnum from the recurrence
        if (frequencyEnum==null){
            RuleModel ruleModel = RuleFactory.getInstance().getRuleModel(event);
            event.setRule(ruleModel);
            frequencyEnum = ruleModel.getFrequencyEnum();
            interval = event.getRule().getInterval();
        }

        // when view event details, the fraquencyEnum will be null
        if (frequencyEnum == null){
            return "None";
        }

        switch (frequencyEnum){
            case DAILY:
                return String.format(context.getString(R.string.repeat_everyday_cus),interval==1?"":" "+interval+" ");
            case WEEKLY:
                return String.format(context.getString(R.string.repeat_everyweek_cus),interval==1?" ":" "+interval+" ",dayOfWeek);
            case MONTHLY:
                return String.format(context.getString(R.string.repeat_every_month_cus),interval==1?" ":" "+interval+" ");
            case YEARLY:
                return String.format(context.getString(R.string.repeat_every_year_cus),interval==1?" ":" "+interval+" ");
            default:
                return String.format(context.getString(R.string.event_no_repeat));
        }
    }

    public static Invitee generateInvitee(Event event, Contact contact){
        Invitee invitee = new Invitee();
        invitee.setUserId(contact.getUserDetail().getUserId());
        invitee.setUserUid(contact.getUserDetail().getUserUid());
        invitee.setEventUid(event.getEventUid());
        invitee.setContact(contact);
        return invitee;
    }

    public static Invitee generateInvitee(Event event, User user){
        Invitee invitee = new Invitee();
        invitee.setUser(user);
        invitee.setUserUid(user.getUserUid());
        invitee.setEventUid(event.getEventUid());
        return invitee;
    }

    public static Invitee generateInvitee(Event event, String email){
        Invitee invitee = new Invitee();
        invitee.setAliasName(email);
        invitee.setAliasName(email);
        invitee.setUserUid("-1");
        invitee.setEventUid(event.getEventUid());
        return invitee;
    }

    public static String getEventTitlebarDateStr(Date date){
        Calendar cal = Calendar.getInstance();
        String titleMonth = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        String titleYear = cal.get(Calendar.YEAR) + "";
        String title = titleMonth + " " + titleYear;

        return title;
    }

    public static String getEventDateStr(Event event){
        long time = event.getStartTime();
        long currentDayTime = Calendar.getInstance().getTimeInMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        String monthTh =  cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        String dayOfMonth = cal.get(Calendar.DAY_OF_MONTH) + "";
        String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        int type = getDatesRelationType(currentDayTime, time);
        switch (type){
            case 1:
                dayOfWeek = "Tomorrow";
                break;
            case 0:
                dayOfWeek = "Today";
                break;
            case -1:
                dayOfWeek = "Yesterday";
                break;
            default:
                break;
        }

        return dayOfWeek + " " + dayOfMonth + " " + monthTh;
    }


    public static int getDatesRelationType(long todayM, long currentDayM){
        todayM = getDayBeginMilliseconds(todayM);
        currentDayM = getDayBeginMilliseconds(currentDayM);
        // -2 no relation, 1 tomorrow, 0 today, -1 yesterday
        int type = -2;
        int dayM = 24 * 60 * 60 * 1000;
        long diff = (currentDayM - todayM);
        if (diff >0 && diff <= dayM){
            type = 1;
        }else if(diff < 0 && diff >= -dayM){
            type = -1;
        }else if (diff == 0){
            type = 0;
        }

        return type;
    }

    public static boolean isEventInVisibleCalendar(Event event, Context context) {
        List<org.unimelb.itime.bean.Calendar> calendarList = DBManager.getInstance(context).getAllAvailableCalendarsForUser();
        String calendarUid = event.getCalendarUid();
        org.unimelb.itime.bean.Calendar cal = null;
        for (org.unimelb.itime.bean.Calendar calendar : calendarList) {
            if (calendar.getCalendarUid().equals(calendarUid)) {
                cal = calendar;
                break;
            }
        }

        if (cal == null) {
            return false;
        }

        return cal.getVisibility() > 0;

    }

    public static String getSuggestTimeStringFromLong(Context context, Long startTime, Long endTime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar calendarBegin = Calendar.getInstance();
        calendarBegin.setTimeInMillis(startTime);

        String dayOfWeekBegin =  calendarBegin.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, context.getResources().getConfiguration().locale);
        dayOfWeekBegin = dayOfWeekBegin.toUpperCase();
        String dayBegin = String.format("%02d", calendarBegin.get(Calendar.DAY_OF_MONTH));
        String monthBegin = String.format("%02d",calendarBegin.get(Calendar.MONTH) + 1);
        String yearBegin = String.format("%02d",calendarBegin.get(Calendar.YEAR));
        String startTimeStr = df.format(calendarBegin.getTime());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(endTime);
        String dayOfWeekEnd =  calendarEnd.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, context.getResources().getConfiguration().locale);
        dayOfWeekEnd = dayOfWeekEnd.toUpperCase();
        String dayEnd = String.format("%02d", calendarEnd.get(Calendar.DAY_OF_MONTH));
        String monthEnd= String.format("%02d",calendarEnd.get(Calendar.MONTH) + 1);
        String yearEnd = String.format("%02d",calendarEnd.get(Calendar.YEAR));
        String endTimeStr = df.format(calendarEnd.getTime());

        String yearToday = String.format("%02d",Calendar.getInstance().get(Calendar.YEAR));

        String dateBeginStr = dayBegin + "/" + monthBegin;
        String dateEndStr = dayEnd + "/" + monthEnd;

        String yearBeginStr = (yearBegin.equals(yearEnd) && yearBegin.equals(yearToday))?"": ("/"+yearBegin);
        String yearEndStr = (yearEnd.equals(yearBegin) && yearEnd.equals(yearToday))?"": ("/"+ yearEnd);


        return dayOfWeekBegin + " " + dateBeginStr + yearBeginStr + " " + startTimeStr
                + " - "
                + (dayOfWeekBegin.equals(dayOfWeekEnd)?"":(dayOfWeekEnd + " "))
                +(dateBeginStr.equals(dateEndStr)?"":dateEndStr + yearEndStr + " ") + endTimeStr;
    }

    /************** Start of ************ Event Status Helper **********************************/

    public static boolean isHost(Event event){
        return event.getUserUid().equals(event.getHostUserUid());
    }

    /**
     * get the invitee model of the user in event
     * @param event
     * @return
     */
    public static Invitee getUserInvitee(Event event){
        for (Invitee invitee:event.getInvitee().values()
                ) {
            if (invitee.getUserUid().equals(UserUtil.getInstance().getUserUid())){
                return invitee;
            }
        }

        throw new RuntimeException("User is not invitee in event: " + event.getSummary());
    }

    /**
     * get user model of the host
     * @param event
     * @return
     */
    public static User getEventHostUser(Event event){
        for (Invitee invitee:event.getInvitee().values()
                ) {
            if (invitee.getUserUid().equals(event.getHostUserUid())){
                return invitee.getUser();
            }
        }

        throw new RuntimeException("User is not invitee in event: " + event.getSummary());
    }


    public static String getInviteeStatus(Event event){
        for (Invitee invitee:event.getInvitee().values()
                ) {
            if (invitee.getUserUid().equals(event.getHostUserUid())){
                return invitee.getStatus();
            }
        }

        return null;
    }



    public static void initTimeSlotVoteStatus(Event event){
        for(TimeSlot timeSlot:event.getTimeslot().values()){
            timeSlot.getVoteInvitees().clear();
        }

        for(TimeslotInvitee ti:event.getTimeslotInvitee().values()){
            if(ti.getStatus().equals(TimeslotInvitee.STATUS_ACCEPTED)){
                TimeSlot timeSlot = event.getTimeslot().get(ti.getTimeslotUid());
                timeSlot.getVoteInvitees().add(event.getInvitee().get(ti.getInviteeUid()));
            }
        }
    }

    public static List<TimeSlot> getMyVoteTimeSlot(Event event){
        List<TimeSlot> result = new ArrayList<>();
        for(TimeslotInvitee ti:event.getTimeslotInvitee().values()){
            if(ti.getInviteeUid().equals(event.getSelf())
                    && ti.getStatus().equals(TimeslotInvitee.STATUS_ACCEPTED)){
                result.add(event.getTimeslot().get(ti.getTimeslotUid()));
            }
        }
        return result;
    }

    /************** End of ************** Event Status Helper **********************************/


    /**
     * create event from event interface
     */
    @Deprecated
    public static Event createEventFromInterface(ITimeEventInterface iTimeEventInterface){
        Event event = new Event();
        event.setStartTime(iTimeEventInterface.getStartTime());
        event.setEndTime(iTimeEventInterface.getEndTime());
        return event;
    }

    public static Invitee createSelfInviteeForEvent(Context context, Event event){
        Invitee invitee = new Invitee();
        invitee.setEventUid(event.getEventUid());
        invitee.setUserUid(UserUtil.getInstance(context).getUserUid());
        invitee.setInviteeUid(AppUtil.generateUuid());
        invitee.setUser(UserUtil.getInstance(context).getUser());
        return invitee;
    }

    public static boolean isMyselfInEvent(Context context, Event event){
        String userUid = UserUtil.getInstance(context).getUserUid();
        for(Invitee invitee : event.getInvitee().values()){
            if (invitee.getUserUid().equals(userUid)){
                return true;
            }
        }
        return false;
    }

    public static void generateNeededHostAttributes(Context context, Event event){
        event.setShowLevel(1);
        event.setEventUid(AppUtil.generateUuid());
        event.setHostUserUid(UserUtil.getInstance(context).getUserUid());
        event.setCalendarUid(CalendarUtil.getInstance(context).getDefaultCalendarUid());
        if (!EventUtil.isMyselfInEvent(context, event)){
            Invitee invitee = createSelfInviteeForEvent(context, event);
            event.getInvitee().put(event.getInvitee().size() + "", invitee);
        }
    }

    public static void generateGroupEventAttributes(Context context, Event event){
        for (Invitee invitee : event.getInvitee().values()){
            invitee.setEventUid(event.getEventUid());
        }

        TimeSlot firstTimeSlot = getFirstTimeSlot(event.getTimeslot());
        event.setStartTime(firstTimeSlot.getStartTime());
        event.setEndTime(firstTimeSlot.getEndTime());

    }

    public static TimeSlot getFirstTimeSlot(Map<String,TimeSlot> map){
        long startTime = Long.MAX_VALUE;
        TimeSlot rst = null;
        for (TimeSlot timeSlot : map.values()){
            if (timeSlot.getStartTime() < startTime){
                startTime = timeSlot.getStartTime();
                rst = timeSlot;
            }
        }
        return rst;
    }
}
