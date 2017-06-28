package org.unimelb.itime.ui.mvpview.event;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/26.
 */

public interface EventDetailConfirmMvpView extends ItimeBaseMvpView, TaskBasedMvpView<List<Event>> {

    void confirm();

    void changeAlert();
}
