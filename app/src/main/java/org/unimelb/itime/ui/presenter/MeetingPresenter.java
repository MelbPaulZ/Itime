package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.greenrobot.greendao.AbstractDao;
simport org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.EventDao;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.MeetingDao;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.DataGeneratorManager;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public class MeetingPresenter <V extends MeetingMvpView> extends ItimeBasePresenter<V>{
    private FilterResult filterResult;
    private List<Meeting> comingResult;

    public static class FilterResult implements Serializable{
        public List<Meeting> invitationResult;
        public List<Meeting> hostingResult;
        public List<Meeting> archiveResult;
    }

    public MeetingPresenter(Context context) {
        super(context);
    }

    public void loadData(){
        if (filterResult !=null && getView() != null){
            getView().onDataLoaded(filterResult);
            return;
        }

        Observable<FilterResult> filterResultObservable = Observable.create(subscriber -> {
            FilterResult filterResult1 = new FilterResult();
            String userUid = DataGeneratorManager.getInstance().getUser().getUserUid();

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
//                    new WhereCondition.StringCondition(MeetingDao.Properties.HostUserUid + " = " + MeetingDao.Properties.UserUid)
            ).list();

            List<Meeting> invitationResult = meetingDao.queryBuilder().where(
                    MeetingDao.Properties.EventUid.notIn(keys),MeetingDao.Properties.HostUserUid.notEq(userUid)
            ).list();

//                List<Meeting> comingDataSet = eventDao.queryBuilder().where(
//                        MeetingDao.Properties.HostUserUid.eq(MeetingDao.Properties.UserUid)
//                ).list();
            filterResult1.archiveResult = archiveResult;
            filterResult1.invitationResult = invitationResult;
            filterResult1.hostingResult = hostingResult;

            subscriber.onNext(filterResult1);

            filterResult = filterResult1;
            comingResult = new ArrayList<>();

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

    public FilterResult getFilterResult() {
        return filterResult;
    }

    public void setFilterResult(FilterResult filterResult) {
        this.filterResult = filterResult;
    }

    public List<Meeting> getComingResult() {
        return comingResult;
    }

    public void setComingResult(List<Meeting> comingResult) {
        this.comingResult = comingResult;
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
}
