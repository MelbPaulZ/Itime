package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.databinding.FragmentEventLocationBinding;
import org.unimelb.itime.ui.mvpview.event.EventLocationMvpView;
import org.unimelb.itime.ui.presenter.EventLocationPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventLocationViewModel;

/**
 * Created by Paul on 8/6/17.
 */

public class FragmentEventLocation extends ItimeBaseFragment<EventLocationMvpView, EventLocationPresenter<EventLocationMvpView>>
implements ToolbarInterface{

    private String location;
    private FragmentEventLocationBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventLocationViewModel vm;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public EventLocationPresenter<EventLocationMvpView> createPresenter() {
        return new EventLocationPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventLocationViewModel(getPresenter());
        vm.setLocation(location);
        binding.setVm(vm);

        toolbarViewModel= new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.toolbar_location));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        binding.setToolbarVM(toolbarViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_location, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onNext() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.location), location);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBack() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
