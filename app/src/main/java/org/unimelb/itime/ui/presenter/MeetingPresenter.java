package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.greenrobot.greendao.AbstractDao;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.EventDao;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.MeetingDao;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public class MeetingPresenter <V extends MeetingMvpView> extends ItimeBasePresenter<V>{
    public static boolean outOfDate = false;

    private FilterResult filterResult;

    public static class FilterResult implements Serializable{
        public List<Meeting> invitationResult;
        public List<Meeting> hostingResult;
        public List<Meeting> archiveResult;
        public transient List<Meeting> comingResult;
    }

    public MeetingPresenter(Context context) {
        super(context);
    }

    private void transformRepeatedMeeting(FilterResult filterResult){
        List<Meeting> invitationResult = filterResult.invitationResult;
        List<Meeting> hostingResult = filterResult.hostingResult;
        List<Meeting> archiveResult = filterResult.archiveResult;
        transformRepeatedMeeting(invitationResult);
        transformRepeatedMeeting(hostingResult);
        transformRepeatedMeeting(archiveResult);
    }

    private void transformRepeatedMeeting(List<Meeting> meetings){
        for (Meeting meeting:meetings
             ) {
            Event event = meeting.getEvent();
            if (event.getRecurrence().length != 0){
                EventUtil.transformRepeatEventToLatestInstance(event);
            }
        }
    }

    public synchronized void loadDataFromDB(){
        Observable<FilterResult> filterResultObservable = Observable.create((Subscriber<? super FilterResult> subscriber) -> {
            FilterResult filterResult = new FilterResult();
            String userUid = UserUtil.getInstance(getContext()).getUserUid();

            AbstractDao<Meeting, Void> meetingDao = DBManager.getInstance(getContext()).getQueryDao(Meeting.class);
            AbstractDao<Event, Void> eventDao = DBManager.getInstance(getContext()).getQueryDao(Event.class);

            //get archive meetings
            List<Event> archiveEvents = eventDao.queryBuilder().where(
                    EventDao.Properties.IsArchived.eq(true)
            ).list();

            Collection<String> keys = Stream.of(archiveEvents).map(event -> event.getEventUid()).collect(Collectors.toList());

            List<Meeting> archiveResult = meetingDao.queryBuilder().where(
                    MeetingDao.Properties.EventUid.in(keys)
            ).list();

            List<Meeting> hostingResult = meetingDao.queryBuilder().where(
                    MeetingDao.Properties.EventUid.notIn(keys),
                    MeetingDao.Properties.HostUserUid.eq(userUid)
            ).list();

            List<Meeting> invitationResult = meetingDao.queryBuilder().where(
                    MeetingDao.Properties.EventUid.notIn(keys),MeetingDao.Properties.HostUserUid.notEq(userUid)
            ).list();

            filterResult.archiveResult = archiveResult == null ? new ArrayList<>() : archiveResult;
            filterResult.invitationResult = invitationResult == null ? new ArrayList<>() : invitationResult;
            filterResult.hostingResult = hostingResult == null ? new ArrayList<>() : hostingResult;
            //handle repeated events
            transformRepeatedMeeting(filterResult);

            filterResult.comingResult = getFutureMeeting(filterResult);

            subscriber.onNext(filterResult);
        });

        Subscriber<FilterResult> subscriber = new Subscriber<FilterResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(FilterResult filterResult) {
                outOfDate = false;
                MeetingPresenter.this.filterResult = filterResult;
                if (getView() != null){
                    getView().onDataLoaded(filterResult);
                }
            }
        };

        filterResultObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getData(){
        if (getView() == null){
            return;
        }

        if (outOfDate){
            loadDataFromDB();
            outOfDate = false;
            return;
        }

        if (filterResult != null){
            getView().onDataLoaded(filterResult);
            return;
        }

        loadDataFromDB();
    }

    public FilterResult getFilterResult() {
        return filterResult;
    }

    public void setFilterResult(FilterResult filterResult) {
        this.filterResult = filterResult;
    }

    public void refreshDisplayData(){
        if (getView() != null){
            getView().onDataLoaded(filterResult);
        }
    }

    public void updateItem(Meeting data){

    }

//    public void pinOpt(Meeting data, boolean value){
//        data.getEvent().setIsPinned(value);
//        data.getEvent().update();
//        data.update();
//    }
//
//    public void muteOpt(Meeting data, boolean value){
//        data.getEvent().setIsMute(value);
//        data.getEvent().update();
//        data.update();
//    }
//
//    public void archiveOpt(Meeting data, boolean value){
//        data.getEvent().setIsArchived(value);
//        data.getEvent().update();
//        data.update();
//    }

    public void deleteOpt(Meeting data){
    }

    public void deleteAllArchive(){
        this.filterResult.archiveResult.clear();
    }

//    public void restoreOpt(Meeting data){
//        data.getEvent().setIsArchived(false);
//        data.getEvent().update();
//        data.update();
//    }


    /**
     * get coming meetings from invitation and hosting results
     * @return
     */
    private List<Meeting> getFutureMeeting(FilterResult filterResult){
        List<Meeting> comingResult = new ArrayList<>();
        int range = 0;//day
        long todayBegin = EventUtil.getDayBeginMilliseconds(Calendar.getInstance().getTimeInMillis() - range * EventUtil.allDayMilliseconds);

        List<Meeting> fields = new ArrayList<>(filterResult.invitationResult);
        fields.addAll(filterResult.hostingResult);
        fields.addAll(filterResult.archiveResult);

        for (Meeting meeting:fields
             ) {
            Event event = meeting.getEvent();
            if (event.getStartTime() > todayBegin){
                comingResult.add(meeting);
            }
        }

        return comingResult;
    }

    /**
     * Filtering to get future group event then wrapping to meeting
     * @param map
     * @param endDay
     * @return
     */
    private List<Meeting> wrapFutureEventToMeeting(Map<Long, List<ITimeEventInterface>> map, long endDay){
        List<Meeting> comingResult = new ArrayList<>();

        for (Map.Entry<Long, List<ITimeEventInterface>> entry : map.entrySet())
        {
            if (entry.getKey() > endDay){break;}

            List<ITimeEventInterface> events = entry.getValue();

            for (ITimeEventInterface event : events
                    ) {
                if (event.getEventType().equals(Event.EVENT_TYPE_SOLO)){
                    continue;
                }

                Meeting meeting = wrapEventToMeeting((Event) event);
                comingResult.add(meeting);
            }
        }

        return comingResult;
    }

    /**
     * Wrapping single event to meeting
     * @param event
     * @return
     */
    private Meeting wrapEventToMeeting(Event event){
        Meeting meeting = new Meeting();
        meeting.setEvent(event);

        return meeting;
    }
}
