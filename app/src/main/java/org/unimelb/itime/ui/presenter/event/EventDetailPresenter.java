package org.unimelb.itime.ui.presenter.event;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/17.
 */

public class EventDetailPresenter<V extends TaskBasedMvpView<List<Event>>> extends MvpBasePresenter<V> {
    private Context context;

    public EventDetailPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
