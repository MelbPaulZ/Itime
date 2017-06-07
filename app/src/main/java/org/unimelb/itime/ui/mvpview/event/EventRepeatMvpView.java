package org.unimelb.itime.ui.mvpview.event;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 2/6/17.
 */

public interface EventRepeatMvpView extends ItimeBaseMvpView {
    void toCustomRepeat(Event event);
    void toEndRepeat(Event event);
}
