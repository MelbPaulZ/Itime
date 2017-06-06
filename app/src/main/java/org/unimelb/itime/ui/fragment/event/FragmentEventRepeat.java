package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.databinding.FragmentEventRepeatBinding;
import org.unimelb.itime.ui.mvpview.event.EventRepeatMvpView;
import org.unimelb.itime.ui.presenter.EventRepeatPresenter;
import org.unimelb.itime.ui.viewmodel.event.EventRepeatViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 2/6/17.
 */

public class FragmentEventRepeat extends ItimeBaseFragment<EventRepeatMvpView, EventRepeatPresenter<EventRepeatMvpView>>{
    private FragmentEventRepeatBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventRepeatViewModel vm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_repeat, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventRepeatViewModel(getPresenter());
        toolbarViewModel = new ToolbarViewModel();

        toolbarViewModel.setTitle(getString(R.string.event_repeat_title));
        toolbarViewModel.setRightText(getString(R.string.event_repeat_toolbar_done));
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));

        binding.setVm(vm);
        binding.setToolbarvm(toolbarViewModel);


    }

    @Override
    public EventRepeatPresenter<EventRepeatMvpView> createPresenter() {
        return new EventRepeatPresenter<>(getContext());
    }


}
