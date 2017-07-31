package org.unimelb.itime.util;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yuhaoliu on 31/7/17.
 */

public class MeetingUtil extends BaseUtil{

    public static int getCardTypeInvitation(Meeting meeting)
    {
        // beans entity
        Event event = meeting.getEvent();
        Invitee userInvitee = EventUtil.getUserInvitee(event);
        // attrs which are used for get card type
        String eventStatus = event.getStatus();
        String userInviteeStatus = userInvitee.getStatus();
        boolean hasResponded = userInvitee.getHasResponded();
        boolean isUpdated = meeting.getUpdateField() != null;

        // logical filter
        switch (eventStatus){
            case Event.STATUS_PENDING:{
                //if has responded
                if (hasResponded){
                    switch (userInviteeStatus){
                        case Invitee.STATUS_ACCEPTED:{
                            //return card 2 OR 9 : dif on Sys msg
                            return isUpdated ? 9 : 2;
                        }
                        case Invitee.STATUS_DECLINED:{
                            //impossible
                            //return card 8(updated)
//                            return 8;
                        }
                        case Invitee.STATUS_NEEDSACTION:{
                            //return card 5
                            return 5;
                        }
                    }
                }else {
                    //return card 1
                    return 1;
                }
                break;
            }
            case Event.STATUS_CONFIRMED:
                switch (userInviteeStatus){
                    case Invitee.STATUS_ACCEPTED:{
                        //return card 3 or 7 : dif on Sys msg
                        return isUpdated ? 7 : 3;
                    }
                    case Invitee.STATUS_DECLINED:{
                        // impossible
                        //return card 10 or 8(updated)
                        return isUpdated ? 8 : 10;
                    }
                    case Invitee.STATUS_NEEDSACTION:{
                        // return card 13 or 6 (responded)
                        return hasResponded ? 6 : 13;
                    }
                }
            case Event.STATUS_CANCELLED:{
                if (event.getDeleteLevel() > 0){
                    //event is deleted
                    // TODO: 18/7/17 return card 11 or 12 : dif on second title (repeated and instances)
                    return 11;

                }else {
                    //event still in active
                    if (userInviteeStatus.equals(Invitee.STATUS_DECLINED)){
                        return 4;
                    }
                }
            }
        }

        throw new RuntimeException("Event cannot match any type of card: " + event.getSummary());
    }

    /**
     * For event status
     * @return int[] {goingNum, notGoingNum, votedNum, cantGoNum, noReplyNum}
     */
    public static int[] getMeetingVotedStatus(Event event){
        int goingNum = 0;
        int notGoingNum = 0;
        int votedNum = 0;
        int cantGoNum = 0;
        int noReplyNum = 0;

        boolean isConfirmed = event.getStatus().endsWith(Event.STATUS_CONFIRMED);

        for (Invitee invitee:event.getInvitee().values()
                ) {
            String status = invitee.getStatus();

            switch (status){
                case Invitee.STATUS_ACCEPTED:
                    if (isConfirmed){
                        goingNum += 1;
                    }else {
                        votedNum += 1;
                    }
                    break;
                case Invitee.STATUS_NEEDSACTION:
                    noReplyNum += 1;
                    break;
                case Invitee.STATUS_DECLINED:
                    if (isConfirmed){
                        notGoingNum += 1;
                    }else {
                        cantGoNum += 1;
                    }
                    break;
            }
        }

        return new int[]{goingNum, notGoingNum, votedNum, cantGoNum, noReplyNum};
    }

    public static void restoreItem(Meeting meeting, MeetingPresenter.FilterResult filterResult){
        if (meeting.getHostUserUid().equals(meeting.getUserUid())){
            filterResult.hostingResult.add(meeting);
        }else {
            filterResult.invitationResult.add(meeting);
        }
    }


    public static SimpleDateFormat serverTimeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat meetingUpdatedTodayFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public static SimpleDateFormat meetingUpdatedOtherDayFmt = new SimpleDateFormat("dd MMM", Locale.getDefault());
    public static SimpleDateFormat meetingUpdatedOtherYearFmt = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public static String getMeetingUpdatedTimeStr(Meeting meeting){
        String serverUpdatedTimeStr = meeting.getUpdatedAt();

        try {
            Date dateUpdate = serverTimeFmt.parse(serverUpdatedTimeStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateUpdate);

            if (isToday(cal)){
                return meetingUpdatedTodayFmt.format(dateUpdate);
            }else if (isSameYear(cal)){
                return meetingUpdatedOtherDayFmt.format(dateUpdate);
            }else {
                return meetingUpdatedOtherYearFmt.format(dateUpdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
