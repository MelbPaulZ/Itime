package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.databinding.FragmentSettingCalendarsBinding;
import org.unimelb.itime.ui.mvpview.SettingCalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarDisplayFragment extends ItimeBaseFragment<SettingCalendarMvpView, CalendarPresenter<SettingCalendarMvpView>> implements ToolbarInterface, SettingCalendarMvpView {

    private FragmentSettingCalendarsBinding binding;
    private CalendarViewModel contentViewModel;
    private ToolbarViewModel<? extends ToolbarInterface> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendars, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public CalendarPresenter<SettingCalendarMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new CalendarViewModel(getPresenter());
        contentViewModel.setCalItemView(ItemBinding.of(BR.wrapper, R.layout.listview_setting_calendar));

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.calendar_title));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Calendar data) {
        hideProgressDialog();
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void toAddCalendar() {
        getBaseActivity().openFragment(new SettingCalendarCreateFragment());
    }

    @Override
    public void toEditCalendar(Calendar calendar) {
        try {
            Calendar calendarCP = calendar.clone();
            SettingCalendarEditFragment fragment = new SettingCalendarEditFragment();
            fragment.setData(calendarCP);
            getBaseActivity().openFragment(fragment);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toDeleteCalendar(Calendar calendar) {

    }

}
