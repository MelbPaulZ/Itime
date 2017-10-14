package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
import android.text.TextUtils;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by yuhaoliu on 18/7/17.
 */

public class CardTemplate {

    public int type;
    public int photoVisibility;
    public int iconTextColor;
    public int iconSrc;

    public int sidebarColor;
    public int sysMsgTextColor;

    private int iconTextResId;

    public CardTemplate(int type, int photoVisibility, int iconTextResId, int iconTextColor, int iconSrc, int sidebarColor, int sysMsgTextColor) {
        this.type = type;
        this.photoVisibility = photoVisibility;
        this.iconTextResId = iconTextResId;
        this.iconTextColor = iconTextColor;
        this.iconSrc = iconSrc;
        this.sidebarColor = sidebarColor;
        this.sysMsgTextColor = sysMsgTextColor;
    }

    public String getIconText(Context context){
        return context.getResources().getString(iconTextResId);
    }

    public String getTitle(Meeting meeting) {
        return meeting.getEvent().getSummary();
    }

    public String getSubTitle(Meeting meeting) {
        if (TextUtils.isEmpty(meeting.getEvent().getGreeting())){
            return "";
        }

        return "\"" + meeting.getEvent().getGreeting() + "\"";
    }

    public String getPhotoUrl(Meeting meeting) {
        return EventUtil.getEventHostUser(meeting.getEvent()).getPhoto();
    }

    public String getSysMsg(Context context, Meeting meeting) {
        switch (type){
            case 1:case 4:case 13:
                return EventUtil.getEventHostUser(meeting.getEvent()).getPersonalAlias() + context.getString(R.string.meeting_sys_msg_invitation);
            case 2:
                return context.getString(R.string.meeting_sys_msg_wait_host_confirm);
            case 3:case 10:
                return context.getString(R.string.meeting_sys_msg_confirmed);
            case 5:case 7:case 8:case 9:
                String fields = "";
                for (String field:meeting.getUpdateField()
                     ) {
                    fields += field + ",";
                }
                return fields + context.getString(R.string.meeting_sys_msg_updated);
            case 6:
                return context.getString(R.string.meeting_sys_msg_diff_timeslot_confirmed);
            case 11: case 12:
                return "";
            default:
                return "N/A";
        }
    }
}
