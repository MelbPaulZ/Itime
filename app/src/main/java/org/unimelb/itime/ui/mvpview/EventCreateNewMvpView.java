package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;

/**
 * Created by Paul on 25/08/2016.
 */
public interface EventCreateNewMvpView extends EventCommonMvpView{
    void gotoWeekViewCalendar();
    void pickLocation();
    void pickInvitee();
    void pickPhoto(String tag);

    /**
     * Created by Paul on 18/3/17.
     */

    interface SettingNotificationMvpView<T> extends TaskBasedMvpView<T>, ItimeBaseMvpView{
        void onClickDefaultAlert();
    }
}
