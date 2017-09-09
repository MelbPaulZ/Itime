package org.unimelb.itime.ui.presenter.event;

import android.content.Context;

import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.ui.mvpview.event.EventCreateSentMvpView;

/**
 * Created by Paul on 7/9/17.
 */

public class EventCreateSentPrensenter<T extends EventCreateSentMvpView> extends ItimeBasePresenter<EventCreateSentMvpView> {

    public EventCreateSentPrensenter(Context context) {
        super(context);
    }
}
