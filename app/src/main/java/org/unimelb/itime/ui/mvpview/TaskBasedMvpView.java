package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.ItimeBaseMvpView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface TaskBasedMvpView<M> extends ItimeBaseMvpView{

    void onTaskStart(int taskId);
    void onTaskSuccess(int taskId, M data);
    void onTaskError(int taskId, Object data);
}
