package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.databinding.FragmentImportUniversityBinding;
import org.unimelb.itime.ui.mvpview.CalendarUniversityImportMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarImportUniversityViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 9/2/17.
 */

public class SettingImportUniversityFragment extends ItimeBaseFragment<CalendarUniversityImportMvpView, CalendarPresenter<CalendarUniversityImportMvpView>>
        implements CalendarUniversityImportMvpView, ToolbarInterface {

    private FragmentImportUniversityBinding binding;
    private CalendarImportUniversityViewModel viewModel;
    private ToolbarViewModel<ToolbarInterface> toolbarViewModel;
    private String source;

    @Override
    public CalendarPresenter<CalendarUniversityImportMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_import_university, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitle();
        initContent();
    }

    private void initContent() {
        viewModel = new CalendarImportUniversityViewModel(getPresenter());
        binding.setContentVM(viewModel);
    }

    private void initTitle() {
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.setting_import_calendar));
        toolbarViewModel.setRightText(getString(R.string.done));
        toolbarViewModel.setRightEnable(true);
        binding.setToolbarVM(toolbarViewModel);
    }


    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        presenter.bindUniversity(viewModel.getImportCalendarUserName(), viewModel.getImportCalendarPassword(), source);
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        showDialog(getString(
                R.string.success),
                getString(R.string.setting_import_melbourne_university_calendar_successfully),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().popBackStack();
                    }
                });
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        showDialog(
                getString(R.string.failure),
                getString(R.string.some_error_happens));
    }

    public void setSource(String source) {
        this.source = source;
    }
}
