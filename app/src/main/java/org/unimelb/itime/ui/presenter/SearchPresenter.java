package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchPresenter<V extends SearchMvpView> extends ItimeBasePresenter<V> {
    public enum Scope{
        MEETING, EVENT, CONTACT
    }

    public static class SearchResult{
        public List<Meeting> meetings;
        public List<Contact> contacts;
        public List<Event> events;
    }

    private Scope[] scope;
    private Context context;

    private List<Meeting> meetingDataSet;
    private List<Event> eventDataSet;
    private List<Contact> contactDataSet;

    public SearchPresenter(Context context) {
        super(context);
        this.context = context;
    }

    public Scope[] getScope() {
        return scope;
    }

    public void setScope(Scope... scope) {
        this.scope = scope;
    }

    public void search(final String searchStr){
        if (scope == null){
            throw new RuntimeException("Scope cannot be null");
        }

        Observable<SearchResult> observable = Observable.create(new Observable.OnSubscribe<SearchResult>() {
            @Override
            public void call(Subscriber<? super SearchResult> subscriber) {
                //init dataSet
                initDataFromDB(scope);
                subscriber.onNext(searchResult(scope, searchStr));
            }
        });

        Subscriber<SearchResult> subscriber = new Subscriber<SearchResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SearchResult searchResult) {
                if (getView() != null){
                    getView().onSearchResult(searchResult);
                }
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private SearchResult searchResult(Scope[] scopes, String searchStr){
        SearchResult result = new SearchResult();
        for (Scope scope:scopes
             ) {
            switch (scope){
                case MEETING:
                    result.meetings = searchMeeting(searchStr);
                    break;
                case EVENT:
                    result.events = searchEvent(searchStr);
                    break;
                case CONTACT:
                    result.contacts = searchContact(searchStr);
                    break;
            }
        }

        return result;
    }

    private List<Meeting> searchMeeting(String searchStr){
        List<Meeting> result = new ArrayList<>();
        for (Meeting meeting:this.meetingDataSet
             ) {
            Event event = meeting.getEvent();
            boolean matched = event.getSummary().toLowerCase().contains(searchStr);
            if (matched){
                result.add(meeting);
            }
        }

        return result;
    }

    private List<Event> searchEvent(String searchStr){
        List<Event> result = new ArrayList<>();
        for (Event event:this.eventDataSet
                ) {
            boolean matched = event.getSummary().toLowerCase().contains(searchStr);
            if (matched){
                result.add(event);
            }
        }

        return result;
    }

    private List<Contact> searchContact(String searchStr){
        List<Contact> result = new ArrayList<>();
        for (Contact contact:this.contactDataSet
                ) {
            String personalAlias = contact.getUserDetail().getPersonalAlias();
            String alias = contact.getAliasName();
            boolean matched =  alias.toLowerCase().contains(searchStr)
                    || personalAlias.toLowerCase().contains(searchStr)
                    || contact.getUserDetail().getUserId().toLowerCase().contains(searchStr);
            if (matched){
                result.add(contact);
            }
        }

        return result;
    }

    /**
     * Load all data is specified in scopes from DB
     * @param scopes types of data should be loaded
     */
    private void initDataFromDB(Scope[] scopes){
        for (Scope scope:scopes
             ) {
            Class mClass = null;

            switch (scope){
                case MEETING:
                    if (this.meetingDataSet != null)return;
                    mClass = Meeting.class;
                    break;
                case EVENT:
                    if (this.eventDataSet != null)return;
                    mClass = Event.class;
                    break;
                case CONTACT:
                    if (this.contactDataSet != null)return;
                    mClass = Contact.class;
                    break;
                default:
                    break;
            }

            initDataFromDB(mClass);
        }
    }

    /**
     * Load specific type of data from DB
     * @param tClass
     * @param <T>
     */
    private <T> void initDataFromDB(final Class<T> tClass) {
        List<T> data = DBManager.getInstance(context).getAll(tClass);

        if (tClass == Meeting.class) {
            meetingDataSet = (List<Meeting>) data;
        } else if (tClass == Event.class) {
            eventDataSet = (List<Event>) data;
        } else if (tClass == Contact.class) {
            contactDataSet = (List<Contact>) data;
        }
    }
}
