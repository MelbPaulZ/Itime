package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface MainSettingMvpView<T extends Object> extends TaskBasedMvpView<T>, ItimeBaseMvpView{

    void toProfilePage();
    void toBlockedUserPage();
    void toNotificationPage();
    void toCalendarPreferencePage();
    void toHelpFdPage();
    void toAboutPage();
    void onLogOut();
    void onHelpAndFeedback();
    void toLanguagePage();
}
