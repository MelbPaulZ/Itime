package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.User;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface UserMvpView extends TaskBasedMvpView<User>,ItimeBaseMvpView{

    void toEditPhotoPage();
    void toEditNamePage();
    void toEditEmailPage();
    void toEditPasswordPage();
    void toEditMyQrCodePage();
    void toEditGenderPage();
    void toEditRegionPage();
}
