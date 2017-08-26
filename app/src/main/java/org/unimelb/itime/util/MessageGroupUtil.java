package org.unimelb.itime.util;

import org.unimelb.itime.bean.MessageGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 26/8/17.
 */

public class MessageGroupUtil {
    public static void sortMessageGroupByTime(List<MessageGroup> messageGroups){
        Collections.sort(messageGroups, (o1, o2) -> {
            Date d1 =  EventUtil.parseTimeZoneToDate(o1.getCreatedAt(), EventUtil.UPDATE_CREATE_AT);
            Date d2 = EventUtil.parseTimeZoneToDate(o2.getCreatedAt(), EventUtil.UPDATE_CREATE_AT);
            return (int) (d2.getTime() - d1.getTime());
        });
    }
}
