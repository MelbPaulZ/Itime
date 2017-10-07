package org.unimelb.itime.ui.fragment.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.databinding.FragmentSettingCalendarEditBinding;
import org.unimelb.itime.ui.mvpview.SettingCalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarEditFragment extends ItimeBaseFragment<SettingCalendarMvpView, CalendarPresenter<SettingCalendarMvpView>>
        implements SettingCalendarMvpView, ToolbarInterface {

    private FragmentSettingCalendarEditBinding binding;

    private CalendarViewModel contentViewModel;

    private ToolbarViewModel<? extends ToolbarInterface> toolbarViewModel;
    private CalendarPresenter presenter;
    private Calendar calendar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendar_edit, container, false);
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

        contentViewModel = new CalendarViewModel(presenter);
        contentViewModel.setCalendar(calendar);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.setting_edit_calendar));
        toolbarViewModel.setRightText(getString(R.string.done));

        contentViewModel.setToolbarViewModel(toolbarViewModel);

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    public void setData(Calendar calendar){
        this.calendar = calendar;
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        contentViewModel.onEditDoneClick().onClick(null);
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Calendar data) {
        hideProgressDialog();
        onBack();
    }



    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }

    @Override
    public void toAddCalendar() {

    }

    @Override
    public void toEditCalendar(Calendar calendar) {

    }

    @Override
    public void toDeleteCalendar(final Calendar calendar) {
        TextView unsupportedEmailTitle = new TextView(getContext());
        unsupportedEmailTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        unsupportedEmailTitle.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        unsupportedEmailTitle.setText("Notice");
        unsupportedEmailTitle.setTextSize(18);
        unsupportedEmailTitle.setPadding(0,50,0,0);
        unsupportedEmailTitle.setTextColor(getResources().getColor(R.color.black));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(unsupportedEmailTitle)
                .setMessage("All events in this calendar will also be deleted.")
                .setCancelable(true)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.delete(calendar);

                    }
                });
        builder.create().show();
    }
}