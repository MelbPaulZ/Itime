package org.unimelb.itime.ui.presenter.event;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Qiushuo Huang on 2017/6/17.
 */

public class EventDetailPresenter<V extends TaskBasedMvpView<List<Event>>> extends MvpBasePresenter<V> {
    private static final String TAG = "EventDetailPresenter";
    private Context context;
    private EventApi eventApi;

    public EventDetailPresenter(Context context) {
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void muteEvent(Event event, boolean isMute){

        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        int mute = isMute?EventApi.OPERATION_TRUE:EventApi.OPERATION_FALSE;

        Observable<HttpResult<List<Event>>> observable = eventApi.mute(event.getCalendarUid(), event.getEventUid(), mute, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Event>> result) {
//                updateEventToken(result.getSyncToken());
//                synchronizeLocal(result.getData());
////                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                if(getView() != null){
//                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }

    public void pinEvent(Event event, boolean isPin){

        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        int pin = isPin?EventApi.OPERATION_TRUE:EventApi.OPERATION_FALSE;

        Observable<HttpResult<List<Event>>> observable = eventApi.pin(event.getCalendarUid(), event.getEventUid(), pin, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Event>> result) {
//                updateEventToken(result.getSyncToken());
//                synchronizeLocal(result.getData());
////                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                if(getView() != null){
//                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }

    public void archiveEvent(Event event, boolean isArchive){

        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        int archive = isArchive?EventApi.OPERATION_TRUE:EventApi.OPERATION_FALSE;

        Observable<HttpResult<List<Event>>> observable = eventApi.archive(event.getCalendarUid(), event.getEventUid(), archive, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Event>> result) {
//                updateEventToken(result.getSyncToken());
//                synchronizeLocal(result.getData());
////                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                if(getView() != null){
//                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }


}
