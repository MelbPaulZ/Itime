package org.unimelb.itime.ui.presenter.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
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
        ArrayList<Integer> readIntegers = new ArrayList<>();
        readIntegers.add(messageGroup.getMessageGroupUid());
        Observable<HttpResult<List<MessageGroup>>> observable = iTimeActivityApi.read(readIntegers).map(ret -> {
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

            }

            @Override
            public void onNext(HttpResult<List<MessageGroup>> listHttpResult) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_ITIME_ACTIVITIES));
                Toast.makeText(getContext(), "Read", Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
