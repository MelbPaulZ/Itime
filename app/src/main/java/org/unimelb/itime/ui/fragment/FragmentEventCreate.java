package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.databinding.FragmentEventCreateBinding;
import org.unimelb.itime.ui.mvpview.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 2/6/17.
 */

public class FragmentEventCreate extends ItimeBaseFragment<EventCreateMvpView, EventCreatePresenter<EventCreateMvpView>>
        implements EventCreateMvpView {
    private FragmentEventCreateBinding binding;
    private EventCreateViewModel vm;
    private ToolbarViewModel toolbarViewModel;

    @Override
    public EventCreatePresenter<EventCreateMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new EventCreateViewModel(getPresenter());
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel();
        toolbarViewModel.setTitle(getString(R.string.new_event_toolbar_title));
        toolbarViewModel.setRightText(getString(R.string.new_event_toolbar_next));
        binding.setToolbarvm(toolbarViewModel);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create, container, false);
        return binding.getRoot();
    }

}

