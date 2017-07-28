package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;
import android.view.View;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface ProfileMvpView extends ItimeBaseMvpView, TaskBasedMvpView {
    Activity getActivity();

    View getContentView();

    void goToInviteFragment();

    void goToEditAlias();
}
