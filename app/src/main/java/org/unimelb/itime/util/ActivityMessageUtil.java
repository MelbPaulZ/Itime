package org.unimelb.itime.util;

import org.unimelb.itime.bean.Message;
import org.unimelb.itime.ui.viewmodel.activity.ActivityMessageViewModel;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 28/10/17.
 */

public class ActivityMessageUtil {

    public static void sortActivityMessageViewModelByTime(List<Message> list){
        Collections.sort(list, (o1, o2) ->
        {
            Date d1 =  EventUtil.parseTimeZoneToDate(o1.getCreatedAt(), EventUtil.UPDATE_CREATE_AT);
            Date d2 = EventUtil.parseTimeZoneToDate(o2.getCreatedAt(), EventUtil.UPDATE_CREATE_AT);
            return (int) (d2.getTime() - d1.getTime());
        });
    }
}
