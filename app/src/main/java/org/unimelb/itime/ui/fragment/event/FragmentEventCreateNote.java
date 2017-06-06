package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateNoteBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateUrlMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateNoteViewModel;

/**
 * Created by Paul on 6/6/17.
 */

public class FragmentEventCreateNote extends ItimeBaseFragment<EventCreateUrlMvpView, LocalPresenter<EventCreateUrlMvpView>> implements ToolbarInterface{
    private FragmentEventCreateNoteBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventCreateNoteViewModel vm;
    private Event event;

    @Override
    public LocalPresenter<EventCreateUrlMvpView> createPresenter() {
        return new LocalPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_note, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventCreateNoteViewModel(getPresenter());
        vm.setEvent(event);
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.toolbar_note));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        binding.setToolbarVM(toolbarViewModel);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onNext() {
        FragmentEventCreate fragment = (FragmentEventCreate) getFragmentManager().findFragmentByTag(FragmentEventCreate.class.getSimpleName());
        fragment.setEvent(event);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }
}

