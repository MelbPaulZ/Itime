package org.unimelb.itime.ui.fragment.meeting;

import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by yuhaoliu on 18/7/17.
 */

public class CardTemplate {
    public int type;

    public String iconText;
    public int photoVisibility;
    public int iconTextColor;
    public int iconSrc;

    public int sidebarColor;
    public int sysMsgTextColor;

    public CardTemplate(int type, int photoVisibility, String iconText, int iconTextColor, int iconSrc, int sidebarColor, int sysMsgTextColor) {
        this.type = type;
        this.photoVisibility = photoVisibility;
        this.iconText = iconText;
        this.iconTextColor = iconTextColor;
        this.iconSrc = iconSrc;
        this.sidebarColor = sidebarColor;
        this.sysMsgTextColor = sysMsgTextColor;
    }

    public String getTitle(Meeting meeting) {
        return meeting.getEvent().getSummary();
    }

    public String getSubTitle(Meeting meeting) {
        return "\"" + meeting.getEvent().getGreeting() + "\"";
    }

    public String getPhotoUrl(Meeting meeting) {
        return EventUtil.getEventHostUser(meeting.getEvent()).getPhoto();
    }

    public String getSysMsg(Meeting meeting) {
        switch (type){
            case 1:case 4:case 13:
                return EventUtil.getEventHostUser(meeting.getEvent()).getPersonalAlias() + " invite you to";
            case 2:
                return "Pending host confirmation";
            case 3:case 10:
                return "Confirmed";
            case 5:case 7:case 8:case 9:
                String fields = "";
                for (String field:meeting.getUpdateField()
                     ) {
                    fields += field + ",";
                }
                return fields + "...changed";
            case 6:
                return "Different timeslot confirmed";
            case 11: case 12:
                return "";
            default:
                return "N/A";
        }
    }
}
