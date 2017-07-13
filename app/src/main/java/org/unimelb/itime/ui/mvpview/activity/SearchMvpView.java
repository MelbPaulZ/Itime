package org.unimelb.itime.ui.mvpview.activity;

import android.support.annotation.Nullable;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

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
    void onMeetingShowMoreClick(String searchStr, List<Meeting> meetingDataSet);
    void onEventShowMoreClick(String searchStr, List<Event> eventDataSet);
    void onContactShowMoreClick(String searchStr, List<Contact> contactDataSet);
    <T> void onDataLoaded(Class<T> tClass, List<T> data);
}
