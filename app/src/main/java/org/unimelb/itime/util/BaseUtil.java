package org.unimelb.itime.util;

import java.util.Calendar;

/**
 * Created by yuhaoliu on 31/7/17.
 */

public class BaseUtil extends david.itimecalendar.calendar.util.BaseUtil {

    public static boolean isSameYear(Calendar calendar){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        return calendar.get(Calendar.YEAR) == currentYear;
    }

}
