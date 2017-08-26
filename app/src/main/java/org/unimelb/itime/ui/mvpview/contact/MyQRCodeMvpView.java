package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;
import android.view.View;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by 37925 on 2016/12/18.
 */

public interface MyQRCodeMvpView extends ItimeBaseMvpView, TaskBasedMvpView {
    Activity getActivity();

    View getContentView();

    void goToScanQRCode();

    void saveQRCode();

    void back();
}
