package org.unimelb.itime.ui.fragment.settings;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingGenderBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel.GenderWrapper;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingProfileGenderFragment  extends ItimeBaseFragment<TaskBasedMvpView<User>, UserPresenter<TaskBasedMvpView<User>>> implements TaskBasedMvpView<User>, ItimeBaseMvpView, ToolbarInterface {

    private FragmentSettingGenderBinding binding;

    private UserProfileViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;
    private UserPresenter<TaskBasedMvpView<User>> presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_gender, container, false);
        return binding.getRoot();
    }

    @Override
    public UserPresenter<TaskBasedMvpView<User>> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = getPresenter();

        contentViewModel = new UserProfileViewModel(presenter);
        User user = UserUtil.getInstance(getContext()).copyUser();
        contentViewModel.setUser(user);

        contentViewModel.setGenderWrapperList(prepareData());
        contentViewModel.setGenderItemView(ItemBinding.of(BR.wrapper, R.layout.listview_setting_gender));

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.setting_gender));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private List<GenderWrapper> prepareData(){
        List<GenderWrapper> list = new ArrayList<>();
        // don't change the order
        String gCode = UserUtil.getInstance(getContext()).getUser().getGender();
        list.add(new GenderWrapper("0", gCode.equals("0")));
        list.add(new GenderWrapper("1", gCode.equals("1")));
        list.add(new GenderWrapper("2", gCode.equals("2")));

        return list;
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        presenter.updateProfile(contentViewModel.getUser());
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, User data) {
        hideProgressDialog();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }
}
