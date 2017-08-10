package org.unimelb.itime.ui.mvpview.calendar;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;

import java.util.List;
import java.util.Map;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public interface SearchEventMvpView extends TaskBasedMvpView {
    void onBack();
    void onEventClick(Event event);
    void onEventResult(List<Map.Entry<Long,List<Event>>> result);
}
