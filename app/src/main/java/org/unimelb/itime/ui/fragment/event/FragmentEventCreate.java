package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.databinding.FragmentEventCreateBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.LocationActivity;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendarTimeslot;
import org.unimelb.itime.ui.fragment.component.FragmentEventTime;
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
            init();
        }else{
            // rotate screen
            event = (Event) savedInstanceState.getSerializable(getString(R.string.event));
        }
    }

    private void init(){
        if (vm==null) {
            mockEvent();
            vm = new EventCreateViewModel(getPresenter());
            vm.setEvent(event);
            binding.setVm(vm);

            toolbarViewModel = new ToolbarViewModel<>(this);
            toolbarViewModel.setTitle(getString(R.string.new_event_toolbar_title));
            toolbarViewModel.setRightText(getString(R.string.new_event_toolbar_next));
            toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_close));
            toolbarViewModel.setRightEnable(true);
            binding.setToolbarvm(toolbarViewModel);
        }else{
            vm.setEvent(event);
        }
    }

    private void mockEvent(){
        event= new Event();
        event.getStart().setDateTime("2017-05-23T12:25:00+10:00");
        event.getEnd().setDateTime("2017-05-23T13:25:00+10:00");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.event), event);
    }


    public void setEvent(Event event) {
        this.event = event;
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
        FragmentEventGreeting fragment = new FragmentEventGreeting();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onBack() {
        new MaterialDialog.Builder(getContext())
                .content(R.string.event_create_cancel_dialog_content)
                .contentColor(getResources().getColor(R.color.black))
                .contentGravity(GravityEnum.CENTER)
                .negativeText(R.string.event_dialog_discard)
                .positiveText(R.string.event_dialog_keep_editing)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
                    }
                })
                .show();
    }

    @Override
    public void toNote(Event event) {
        FragmentEventCreateNote fragment = new FragmentEventCreateNote();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void toUrl(Event event) {
        FragmentEventCreateUrl fragment = new FragmentEventCreateUrl();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void toRepeat(Event event) {
        FragmentEventRepeat fragment = new FragmentEventRepeat();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        fragment.setTargetFragment(this, -100); // TODO: 23/6/17 change -100
        getBaseActivity().openFragmentBottomUp(fragment);
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
        intent.putExtra(getString(R.string.location_string1), event.getLocation().getLocationString1());
        intent.putExtra(getString(R.string.location_string2), event.getLocation().getLocationString2());
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
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toAlert(Event event) {

    }

    @Override
    public void showPopupDialog() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_LOCATION && resultCode == Activity.RESULT_OK){
            String locationString1 = data.getStringExtra(getString(R.string.location_string1));
            String locationString2 = data.getStringExtra(getString(R.string.location_string2));
            Location location = new Location();
            location.setLocationString1(locationString1);
            location.setLocationString2(locationString2);
            event.setLocation(location);
            vm.setEvent(event);
        }
    }


}

