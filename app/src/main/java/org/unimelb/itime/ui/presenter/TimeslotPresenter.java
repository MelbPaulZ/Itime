package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.developer.paul.itimerecycleviewgroup.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.RecommandRequest;
import org.unimelb.itime.bean.RecommandTime;
import org.unimelb.itime.bean.TZoneTime;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.messageevent.MessageRefreshTimeSlots;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.calendar.TimeslotMvpView;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TimeFactory;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yuhaoliu on 9/8/17.
 */

public class TimeslotPresenter <V extends TimeslotMvpView> extends ItimeBasePresenter<V> {
    private EventApi eventApi;
    private String TAG = "TimeslotPresenter";
    public TimeslotPresenter(Context context) {
        super(context);
        eventApi = HttpUtil.createService(context, EventApi.class);
    }


    /**
     * To david, use this when scroll, need to ensure event has a duration.
     * @param event
     * @param startTime
     */
    public void fetchRecommendedTimeslots(Event event, Date startTime){
        Date end = new Date();
        end.setTime(startTime.getTime() + 3 * 24 * 60 * 60 * 1000);
        fetchRecommendedTimeslots(event, startTime, end);
    }



    public void fetchRecommendedTimeslots(Event event, Date startTime, Date endTime){
        RecommandRequest recommandRequest = new RecommandRequest();
        recommandRequest.setEvent(event);
        recommandRequest.setDuration(event.getDuration());
        TZoneTime start = new TZoneTime();
        start.setDateTime(EventUtil.getFormatTimeString(startTime.getTime(), EventUtil.TIME_ZONE_PATTERN));
        recommandRequest.setStartRecommandTime(start);
        TZoneTime end = new TZoneTime();
        end.setDateTime(EventUtil.getFormatTimeString(endTime.getTime(), EventUtil.TIME_ZONE_PATTERN));
        recommandRequest.setEndRecommandTime(end);
        Observable<HttpResult<List<TimeSlot>>> observable = eventApi.recommend(recommandRequest);
        Subscriber<HttpResult<List<TimeSlot>>> subscriber = new Subscriber<HttpResult<List<TimeSlot>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<TimeSlot>> listHttpResult) {
                //post rcd timeslots
                String dateStr = TimeFactory.getFormatTimeString(startTime, TimeFactory.DAY_MONTH_YEAR);
                EventBus.getDefault().post(new MessageRefreshTimeSlots(listHttpResult.getData(),dateStr));
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
