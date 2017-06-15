package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.databinding.FragmentEventAlertBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateAlertMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateAlertViewModel;

/**
 * Created by Paul on 15/6/17.
 */

public class EventCreateAlertFragment extends ItimeBaseFragment<EventCreateAlertMvpView, LocalPresenter<EventCreateAlertMvpView>> implements ToolbarInterface{

    private FragmentEventAlertBinding binding;
    private EventCreateAlertViewModel vm;
    private ToolbarViewModel toolbarVM;

    @Override
    public LocalPresenter<EventCreateAlertMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new EventCreateAlertViewModel(getPresenter());
        binding.setVm(vm);

        toolbarVM = new ToolbarViewModel(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setTitle(getString(R.string.toolbar_alert));
        toolbarVM.setRightText(getString(R.string.new_event_toolbar_next));
        binding.setToolbarVM(toolbarVM);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_alert, container,false);
        }
        return binding.getRoot();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

    }
}
