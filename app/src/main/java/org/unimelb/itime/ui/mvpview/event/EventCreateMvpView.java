package org.unimelb.itime.ui.mvpview.event;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 2/6/17.
 */

public interface EventCreateMvpView extends ItimeBaseMvpView {
    void toNote(Event event);
    void toUrl(Event event);
    void toRepeat(Event event);
    void toDuration(Event event);
    void toCalendars(Event event);
    void toLocation(Event event);
    void toTitle(Event event);
}

