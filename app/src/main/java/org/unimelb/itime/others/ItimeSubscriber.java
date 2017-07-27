package org.unimelb.itime.others;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.messageevent.MessageNetwork;
import org.unimelb.itime.util.HttpUtil;

import rx.Subscriber;

/**
 * Created by Paul on 13/3/17.
 */

public abstract class ItimeSubscriber<T> extends Subscriber<T> implements HttpUtil.OnErrorInterface{

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        EventBus.getDefault().post(new MessageNetwork(MessageNetwork.NETWORK_ERROR));
        onHttpError(e);
    }

}
