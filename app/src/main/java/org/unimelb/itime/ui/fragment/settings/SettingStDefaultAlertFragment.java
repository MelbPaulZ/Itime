package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.databinding.FragmentSettingDefaultAlertBinding;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.ui.viewmodel.SettingViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingStDefaultAlertFragment extends ItimeBaseFragment<EventCreateNewMvpView.SettingNotificationMvpView<Setting>, SettingPresenter<EventCreateNewMvpView.SettingNotificationMvpView<Setting>>> implements ToolbarInterface, EventCreateNewMvpView.SettingNotificationMvpView {

    private FragmentSettingDefaultAlertBinding binding;
    private SettingViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_default_alert, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentViewModel = new SettingViewModel(getPresenter());
        contentViewModel.setSetting(UserUtil.getInstance(getContext()).copySetting());

        int[] alertKeys = AppUtil.getDefaultAlertMins();
        contentViewModel.setAlertSet(alertWrapperMaker(alertKeys));


        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.setting_default_alert));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter<>(getContext());
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

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

    private ArrayList<SettingViewModel.AlertWrapper> alertWrapperMaker(int[] keys){
        ArrayList<SettingViewModel.AlertWrapper> wrappers = new ArrayList<>();
        for (int key:keys
             ) {
            wrappers.add(new SettingViewModel.AlertWrapper(false, AppUtil.getDefaultAlertStr(key), key));
        }

        return wrappers;
    }

    @Override
    public void onClickDefaultAlert() {

    }
}
