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
import org.unimelb.itime.bean.TZoneTime;
import org.unimelb.itime.databinding.FragmentEventPrivateCreateBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.LocationActivity;
import org.unimelb.itime.ui.fragment.component.FragmentEventTime;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreatePrivateViewModel;

import java.util.HashMap;

import static org.unimelb.itime.ui.fragment.event.FragmentEventCreate.REQ_LOCATION;

/**
 * Created by Paul on 15/6/17.
 */

public class FragmentEventPrivateCreate extends ItimeBaseFragment<EventCreateMvpView, EventCreatePresenter<EventCreateMvpView>>
implements EventCreateMvpView, ToolbarInterface{
    private FragmentEventPrivateCreateBinding binding;
    private EventCreatePrivateViewModel vm;
    private ToolbarViewModel toolbarVM;
    private Event event;


    @Override
    public EventCreatePresenter<EventCreateMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_private_create, container, false);
        }
        return binding.getRoot();
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

    public void setEvent(Event event) {
        this.event = event;
    }

    private void reLoadPage(){
        vm.setEvent(event);
    }

    private void firstLoad(){
        mockEvent();

        vm = new EventCreatePrivateViewModel(getPresenter(), getOrderHashMap());
        vm.setEvent(event);


        binding.setVm(vm);

        toolbarVM = new ToolbarViewModel<>(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setTitle(getString(R.string.new_event));
        toolbarVM.setRightText(getString(R.string.toolbar_save));
        toolbarVM.setRightEnable(true);
        binding.setToolbarVM(toolbarVM);
    }

    private void mockEvent(){
        event = new Event();
        event.setStart(new TZoneTime("2017-05-23T12:25:00+10:00","Australia/Sydney"));
        event.setEnd(new TZoneTime("2017-05-23T13:25:00+10:00", "Australia/Sydney"));
    }


    private HashMap<String, Integer> getOrderHashMap(){
        HashMap<String,Integer> orderHashMap = new HashMap<>();
        orderHashMap.put(getString(R.string.alert_toolbar_btn), 0);
        orderHashMap.put(getString(R.string.photos_toolbar_btn), 1);
        orderHashMap.put(getString(R.string.note_toolbar_btn), 2);
        orderHashMap.put(getString(R.string.url_toolbar_btn),3);
        orderHashMap.put(getString(R.string.repeat_toolbar_btn),4);
        return orderHashMap;
    }

    @Override
    public void onNext() {

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
        fragment.setTargetFragment(this, -100);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void toDuration(Event event) {
        // solo event no need
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
        // solo event no need
    }

    @Override
    public void toAlert(Event event) {
        FragmentEventCreateAlert fragment = new FragmentEventCreateAlert();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void showPopupDialog() {
        Fragment pre = getFragmentManager().findFragmentByTag("dialog");
        if (pre!=null) {
            getFragmentManager().beginTransaction().remove(pre);
        }
        FragmentEventTime newFragment = new FragmentEventTime();

        newFragment.show(getFragmentManager(), "dialog");
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
