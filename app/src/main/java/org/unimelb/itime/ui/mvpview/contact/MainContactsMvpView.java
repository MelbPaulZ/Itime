package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;
import android.view.View;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentMainContactsBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface MainContactsMvpView extends ItimeBaseMvpView, TaskBasedMvpView {

    Activity getActivity();

    void goToNewFriendFragment();

    void goToAddFriendsFragment();

    void goToProfileFragment(View view, Contact user);

    void gotoSearch();

    void goToScanQR();

    void toMyProfile();
}
