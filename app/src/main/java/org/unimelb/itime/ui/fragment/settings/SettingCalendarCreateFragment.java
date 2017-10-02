package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.databinding.FragmentSettingCalendarCreateBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarCreateFragment extends ItimeBaseFragment<TaskBasedMvpView<Calendar>, CalendarPresenter<TaskBasedMvpView<Calendar>>> implements TaskBasedMvpView<Calendar>,ItimeBaseMvpView, ToolbarInterface {

    private FragmentSettingCalendarCreateBinding binding;

    private CalendarViewModel contentViewModel;
    private ToolbarViewModel<? extends ToolbarInterface> toolbarViewModel;
    private CalendarPresenter presenter;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendar_create, container, false);
        return binding.getRoot();
    }

    @Override
    public CalendarPresenter createPresenter() {
        return new CalendarPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = getPresenter();

        this.calendar = new Calendar();
        this.calendar.setCalendarUid(AppUtil.generateUuid());
        this.calendar.setUserUid(UserUtil.getInstance(getContext()).getUserUid());

        contentViewModel = new CalendarViewModel(presenter);
        contentViewModel.setCalendar(calendar);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.add_calendar));
        toolbarViewModel.setRightText(getString(R.string.done));
        toolbarViewModel.setRightEnable(false);

        contentViewModel.setToolbarViewModel(toolbarViewModel);
        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        contentViewModel.onCreateDoneClick().onClick(null);
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Calendar data) {
        hideProgressDialog();
        switch (taskId){
            case CalendarPresenter.TASK_CALENDAR_UPDATE:{
                onBack();
                break;
            }
            case CalendarPresenter.TASK_CALENDAR_DELETE:{
                onBack();
                break;
            }
            case CalendarPresenter.TASK_CALENDAR_INSERT:{
                onBack();
                break;
            }
        }
    }


    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }
}
