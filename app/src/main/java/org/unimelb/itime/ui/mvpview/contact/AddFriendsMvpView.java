package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface AddFriendsMvpView extends ItimeBaseMvpView, TaskBasedMvpView {
    Activity getActivity();

    void goToScanQRCode();

    void goToProfileFragment(String userUid);
}
