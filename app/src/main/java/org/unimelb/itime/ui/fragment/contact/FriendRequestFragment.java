package org.unimelb.itime.ui.fragment.contact;

import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.mvpview.contact.FriendRequestMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;

/**
 * Created by Qiushuo Huang on 2017/8/16.
 */

public class FriendRequestFragment  extends ItimeBaseFragment<FriendRequestMvpView, ContactPresenter<FriendRequestMvpView>> implements FriendRequestMvpView, ToolbarInterface {



    @Override
    public ContactPresenter<FriendRequestMvpView> createPresenter() {
        return new ContactPresenter<>(getContext());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void toProfile() {

    }
}
