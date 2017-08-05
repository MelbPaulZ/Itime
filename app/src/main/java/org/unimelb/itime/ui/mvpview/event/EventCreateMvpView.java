package org.unimelb.itime.ui.mvpview.event;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by Paul on 2/6/17.
 */

public interface EventCreateMvpView extends TaskBasedMvpView {
    void toNote(Event event);
    void toUrl(Event event);
    void toRepeat(Event event);
    void toDuration(Event event);
    void toCalendars(Event event);
    void toLocation(Event event);
    void toTitle(Event event);
    void toTimeslot(Event event);
    void toAlert(Event event);
    void showPopupDialog(int startOrEnd);
    void toInvitee(Event event);
}

