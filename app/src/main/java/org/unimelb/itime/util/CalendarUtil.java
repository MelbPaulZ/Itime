package org.unimelb.itime.util;

import android.content.Context;

import org.greenrobot.greendao.AbstractDao;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.CalendarDao;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.manager.DBManager;

import java.util.List;


/**
 * Created by Paul on 24/09/2016.
 */
public class CalendarUtil {
    /**
     * CalendarUtil does not maintain any list of calendars, just insert it in DB
     */
    private static CalendarUtil instance;
    private Context context;
    private CalendarUtil(Context context){
        this.context = context;
    }

    public static CalendarUtil getInstance(Context context){
        if (instance == null){
            instance = new CalendarUtil(context);
        }
        return instance;
    }

    public List<Calendar> getCalendar() {
        return DBManager.getInstance(context).getAllAvailableCalendarsForUser();
    }

    public String getDefaultCalendarUid(){
        return UserUtil.getInstance(context).getUserUid();
    }


    public String getCalendarName(Event event){
        if (event.getCalendarUid().equals("")){
            String defaultUid = getDefaultCalendarUid();
            for (Calendar calendar: getCalendar()){
                if (calendar.getCalendarUid().equals(defaultUid)){
                    return calendar.getSummary();
                }
            }
        }

        AbstractDao dao = DBManager.getInstance(context).getQueryDao(Calendar.class);
        List<Calendar> cals = dao.queryBuilder().where(
                CalendarDao.Properties.CalendarUid.eq(event.getCalendarUid())
        ).list();

        if (cals.size() == 0){
            return "N/A";
        }

        return cals.get(0).getSummary();
    }


    public void clear(){
        instance = null;
    }



}
