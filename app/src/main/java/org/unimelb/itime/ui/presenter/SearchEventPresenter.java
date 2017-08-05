package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.mvpview.calendar.SearchEventMvpView;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchEventPresenter<V extends SearchEventMvpView> extends ItimeBasePresenter<V> {
    private Context context;

    private List<Map.Entry<Long,List<Event>>> eventLists = new ArrayList<>(); // this use to display
    private List<Map.Entry<Long, List<Event>>> searchedEventList = new ArrayList<>(); // this is filtered events
    private int todayIndex = 0;

    public SearchEventPresenter(Context context) {
        super(context);
        this.context = context;
        refreshEventList(getEvents(),eventLists);
    }


    public void search(final String searchStr){

        Observable<List<Map.Entry<Long,List<Event>>>> observable = Observable.create(subscriber -> {
            //init dataSet
            List<Map.Entry<Long,List<Event>>> result = searchEvent(searchStr);
            subscriber.onNext(result);
        });

        Subscriber<List<Map.Entry<Long,List<Event>>>> subscriber = new Subscriber<List<Map.Entry<Long,List<Event>>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("d", "onError: ");
            }

            @Override
            public void onNext(List<Map.Entry<Long,List<Event>>>searchResult) {
                if (getView() != null){
                    getView().onEventResult(searchResult);
                }
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private List<Map.Entry<Long,List<Event>>> searchEvent(String searchStr){
        searchedEventList.clear();

        TreeMap<Long, List<Event>> matchMap = new TreeMap<>();

        for (Map.Entry<Long,List<Event>> entry: eventLists){
            for (Event event: entry.getValue()){
                if (event.getSummary().toLowerCase().contains(searchStr.toString().toLowerCase())
                        || event.getDescription().toLowerCase().contains(searchStr.toString().toLowerCase())
                        || event.getLocation().getLocationString1().toLowerCase().contains(searchStr.toString().toLowerCase())
                        || isInviteeInConstraint(event, searchStr)){
                    putIntoHash(matchMap, entry.getKey(), event);
                }
            }
        }

        boolean found = false;
        int currentIndex = 0;
        todayIndex = -1;
        long todayBegin = EventUtil.getDayBeginMilliseconds(Calendar.getInstance().getTimeInMillis());
        for (Map.Entry<Long, List<Event>> entry: matchMap.entrySet()){
            if (!found){
                todayIndex = currentIndex;
                //如果是未来的责取当天，没有当天就取最近的未来一天
                if (todayBegin <= entry.getKey()){
                    found = true;
                }
            }
            currentIndex++;

            searchedEventList.add(entry);
        }

        return searchedEventList;
    }

    private boolean isInviteeInConstraint(Event event , CharSequence constraint){
        for (Invitee invitee: event.getInvitee().values()){
            if (invitee.getAliasName().toLowerCase().contains(constraint.toString().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private void refreshEventList(TreeMap<Long,List<Event>>  maps,List<Map.Entry<Long, List<Event>>> list){
        list.clear();
        for (Map.Entry<Long,List<Event>> entry: maps.entrySet()){
            list.add(entry);
        }
    }

    public TreeMap<Long, List<Event>> getEvents(){
        TreeMap<Long, List<Event>> hashMap = new TreeMap<Long, List<Event>>() {};
        EventManager eventManager = EventManager.getInstance(getContext());
        findEvents(hashMap, eventManager.getRegularEventMap().entrySet());
        findEvents(hashMap, eventManager.getRepeatedEventMap().entrySet());

        return hashMap;
    }


    /**
     * this method will find events
     * @param treeMap
     * @param dataSet
     *
     */
    private void findEvents(TreeMap treeMap ,Set<Map.Entry<Long, List<ITimeEventInterface>>> dataSet){
        for (Map.Entry<Long, List<ITimeEventInterface>> entry: dataSet){
            for (ITimeEventInterface eventInterface: entry.getValue()){
                Event ev = (Event)eventInterface;
                putIntoHash(treeMap, entry.getKey(), ev);
            }
        }
    }


    /**
     * this method will add object into list<T> in hashmap, if no key contains, will create a k-v set
     * @param hashMap
     * @param key
     * @param o
     * @param <T>
     */
    private <T> void putIntoHash(TreeMap<Long, List<T>> hashMap, Long key, T o){
        List<T> values = hashMap.containsKey(key)? hashMap.get(key) : (List<T>) new ArrayList<>();
        if (o!=null){
            values.add(o);
        }
        hashMap.put(key, values);
    }

}
