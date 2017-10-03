package org.unimelb.itime.ui.fragment.settings;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.setting.SettingRegionViewModel;

/**
 * Created by Paul on 10/2/17.
 */

public abstract class SettingRegionBaseFragment extends ItimeBaseFragment<SettingRegionMvpView, UserPresenter<SettingRegionMvpView>>
    implements SettingRegionMvpView, ToolbarInterface {

    protected SettingRegionViewModel contentViewModel;
    @Override
    public void finishSelect(User user) {
        presenter.updateProfile(user);
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, User data) {
        hideProgressDialog();
        SettingMyProfileFragment fragment = (SettingMyProfileFragment) getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName());
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getBaseActivity().backFragment(fragment);
        fragment.onActivityResult(SettingMyProfileFragment.REQ_REGION, Activity.RESULT_OK, null);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }
}
