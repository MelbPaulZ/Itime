package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
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
import org.unimelb.itime.databinding.FragmentEventCreateBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.LocationActivity;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendarTimeslot;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.event.EventCreateViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 2/6/17.
 */

public class FragmentEventCreate extends ItimeBaseFragment<EventCreateMvpView, EventCreatePresenter<EventCreateMvpView>>
        implements EventCreateMvpView, ToolbarInterface{
    private FragmentEventCreateBinding binding;
    private EventCreateViewModel vm;
    private ToolbarViewModel toolbarViewModel;
    private Event event;

    public final static int REQ_LOCATION = 1001;

    @Override
    public EventCreatePresenter<EventCreateMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState==null) {
            if (event==null) {
                firstLoad();
            }else{
                reLoadPage();
            }
        }else{
            // rotate screen
            event = (Event) savedInstanceState.getSerializable(getString(R.string.event));
        }
    }

    private void firstLoad(){
        mockEvent();
        vm = new EventCreateViewModel(getPresenter());
        vm.setEvent(event);
        binding.setVm(vm);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitle(getString(R.string.new_event_toolbar_title));
        toolbarViewModel.setRightText(getString(R.string.new_event_toolbar_next));
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_close));
        binding.setToolbarvm(toolbarViewModel);
    }

    private void reLoadPage(){
        vm.setEvent(event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.event), event);
    }

    private void mockEvent(){
        event = new Event();
        event.setNote("asdasdasfh ahsiduahiu daisfia gsfiya gidadis hidf gaiy gfaifgsdidsac ai viasu i ufiwf asis aigsiag fisagiasfg iasdc bisayd ufygfiaf i ib");

    }

    public void setEvent(Event event) {
        this.event = event;
        vm.setEvent(event);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onNext() {
        Toast.makeText(getContext(), "todo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }

    @Override
    public void toNote(Event event) {
        FragmentEventCreateNote fragment = new FragmentEventCreateNote();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toUrl(Event event) {
        FragmentEventCreateUrl fragment = new FragmentEventCreateUrl();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toRepeat(Event event) {
        FragmentEventRepeat fragment = new FragmentEventRepeat();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toDuration(Event event) {
        FragmentEventCreateDuration fragment = new FragmentEventCreateDuration();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toCalendars(Event event) {
        FragmentEventCalendar fragment = new FragmentEventCalendar();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toLocation(Event event) {
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        intent.putExtra(getString(R.string.location), event.getLocation());
        startActivityForResult(intent, REQ_LOCATION);
    }

    @Override
    public void toTitle(Event event) {
        FragmentEventCreateTitle fragment = new FragmentEventCreateTitle();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toTimeslot(Event event) {
        FragmentCalendarTimeslot fragment = new FragmentCalendarTimeslot();
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_LOCATION && resultCode == Activity.RESULT_OK){
            String location = data.getStringExtra(getString(R.string.location));
            event.setLocation(location);
            setEvent(event);
        }
    }


}

