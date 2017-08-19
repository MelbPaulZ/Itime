package org.unimelb.itime.ui.presenter.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.bean.MessageGroupRetrofitBody;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.ITimeActivityApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/7/17.
 */

public class ItimeActivitiesPresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {

    private ITimeActivityApi iTimeActivityApi;
    private DBManager dbManager;
    public ItimeActivitiesPresenter(Context context) {
        super(context);
        iTimeActivityApi = HttpUtil.createService(context, ITimeActivityApi.class);
        dbManager = DBManager.getInstance(getContext());
    }

    public void readMessageGroup(MessageGroup messageGroup){
        MessageGroupRetrofitBody body = new MessageGroupRetrofitBody();
        body.setIsRead(1); // this means read....
        List<Integer> readIntegers = body.getMessageGroupUids();
        readIntegers.add(messageGroup.getMessageGroupUid());
        body.setMessageGroupUids(readIntegers);
        Observable<HttpResult<List<MessageGroup>>> observable = iTimeActivityApi.read(body).map(ret -> {
            if (ret.getData().size()>0){
                dbManager.insertOrReplace(ret.getData());
            }
            return ret;
        });
        Subscriber<HttpResult<List<MessageGroup>>> subscriber = new Subscriber<HttpResult<List<MessageGroup>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(" error ", "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<MessageGroup>> listHttpResult) {
                DBManager.getInstance(getContext()).insertOrReplace(listHttpResult.getData());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_ITIME_ACTIVITIES));
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    // TODO: 14/8/17  read all messages
    public void readAll(){
        Observable<HttpResult<List<MessageGroup>>> observable = iTimeActivityApi.readAll(1);
        Subscriber<HttpResult<List<MessageGroup>>> subscriber = new Subscriber<HttpResult<List<MessageGroup>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<List<MessageGroup>> listHttpResult) {
                DBManager.getInstance(getContext()).insertOrReplace(listHttpResult.getData());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_ITIME_ACTIVITIES));
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
