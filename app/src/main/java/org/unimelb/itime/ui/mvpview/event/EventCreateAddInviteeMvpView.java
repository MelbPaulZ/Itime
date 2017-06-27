package org.unimelb.itime.ui.mvpview.event;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by Qiushuo Huang on 2017/6/21.
 */

public interface EventCreateAddInviteeMvpView extends TaskBasedMvpView{
    void gotoAddContact();
    void gotoSearch();
}
