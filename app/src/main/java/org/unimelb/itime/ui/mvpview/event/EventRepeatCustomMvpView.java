package org.unimelb.itime.ui.mvpview.event;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 5/6/17.
 */

public interface EventRepeatCustomMvpView extends ItimeBaseMvpView {
    public void toEndRepeat(Event event);
}
