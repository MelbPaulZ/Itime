package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeCommonMvpView;
import org.unimelb.itime.bean.User;

/**
 * Created by Paul on 30/1/17.
 */

public interface SettingRegionMvpView extends ItimeCommonMvpView, TaskBasedMvpView<User> {
    void toSelectChildRegion(long locationId);
    void finishSelect(User user);
    void onCurrentLocationSuccess(String location);
    void onChangeLocationSetting();
}
