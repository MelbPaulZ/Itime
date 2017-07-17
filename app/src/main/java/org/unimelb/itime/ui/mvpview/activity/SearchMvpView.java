package org.unimelb.itime.ui.mvpview.activity;

import android.support.annotation.Nullable;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;

import java.util.List;
import java.util.Objects;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public interface SearchMvpView extends TaskBasedMvpView {
    void onBack();
    void onMeetingClick(Meeting meeting);
    void onEventClick(Event event);
    void onContactClick(Contact contact);
    void onShowMoreClick(int type, String searchStr);
    void onSearchResult(SearchPresenter.SearchResult result);
}
