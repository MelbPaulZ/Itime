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
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.m.permission.PermissionGrant;


import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.TZoneTime;
import org.unimelb.itime.databinding.FragmentEventPrivateCreateBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.CameraActivity;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.activity.LocationActivity;
import org.unimelb.itime.ui.fragment.component.FragmentEventTime;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreatePrivateViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.PicassoImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.unimelb.itime.ui.fragment.event.FragmentEventCreate.REQUEST_CAMERA_PERMISSION;
import static org.unimelb.itime.ui.fragment.event.FragmentEventCreate.REQUEST_PHOTO_PERMISSION;
import static org.unimelb.itime.ui.fragment.event.FragmentEventCreate.REQ_LOCATION;
import static org.unimelb.itime.ui.fragment.event.FragmentEventCreate.REQ_PHOTO;

/**
 * Created by Paul on 15/6/17.
 */

public class FragmentEventPrivateCreate extends ItimeBaseFragment<EventCreateMvpView, EventCreatePresenter<EventCreateMvpView>>
implements EventCreateMvpView, ToolbarInterface{
    private FragmentEventPrivateCreateBinding binding;
    private EventCreatePrivateViewModel vm;
    private ToolbarViewModel toolbarVM;
    private Event event;
    private boolean hasChange = false;
    private FragmentEventCreate.Mode taskMode = FragmentEventCreate.Mode.CREATE;

    public void setTaskMode(FragmentEventCreate.Mode taskMode) {
        this.taskMode = taskMode;
    }

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
            if (vm==null) {
                firstLoad();
            }else{
                reLoadPage();
            }
        }else{
            // rotate screen
            event = (Event) savedInstanceState.getSerializable(getString(R.string.event));
        }

        Switch alldaySwitch = (Switch) binding.getRoot().findViewById(R.id.allday_switch);
        alldaySwitch.setChecked(event.isAllDay());
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    private void reLoadPage(){
        vm.setEvent(event);
    }

    private void firstLoad(){
        vm = new EventCreatePrivateViewModel(getPresenter(), getOrderHashMap());
        vm.setEvent(event);
        binding.setVm(vm);
        toolbarVM = new ToolbarViewModel<>(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setTitle(taskMode == FragmentEventCreate.Mode.CREATE? getString(R.string.new_event): getString(R.string.edit_event_toolbar_title));
        toolbarVM.setRightText(getString(R.string.toolbar_save));
        toolbarVM.setRightEnable(true);
        binding.setToolbarVM(toolbarVM);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), taskMode.name(), Toast.LENGTH_SHORT).show();

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
        if (taskMode == FragmentEventCreate.Mode.CREATE) {
            presenter.createEvent(event);
        }else if (taskMode == FragmentEventCreate.Mode.UPDATE){
            presenter.updateEvent(event);
        }
        hasChange = true;
    }

    @Override
    public void onBack() {
        if (!hasChange){
            getActivity().finish();
            return;
        }
        if (getActivity() instanceof EventCreateActivity) {
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
        } else{
            getDialogBuidler()
                    .content(R.string.event_edit_cancel_dialog_content)
                    .contentColor(getResources().getColor(R.color.black))
                    .contentGravity(GravityEnum.CENTER)
                    .negativeText(R.string.event_dialog_discard)
                    .positiveText(R.string.event_dialog_keep_editing)
                    .onNegative(((dialog, which) -> getFragmentManager().popBackStack()))
                    .show();
        }
    }

    @Override
    public void toNote(Event event) {
        hasChange = true;
        FragmentEventCreateNote fragment = new FragmentEventCreateNote();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void toUrl(Event event) {
        hasChange = true;
        FragmentEventCreateUrl fragment = new FragmentEventCreateUrl();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void toRepeat(Event event) {
        hasChange = true;
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
        hasChange = true;
        FragmentEventCalendar fragment = new FragmentEventCalendar();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toLocation(Event event) {
        hasChange = true;
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        intent.putExtra(getString(R.string.location_string1), event.getLocation().getLocationString1());
        startActivityForResult(intent, REQ_LOCATION);
    }

    @Override
    public void toTitle(Event event) {
        hasChange = true;
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
        hasChange = true;
        FragmentEventCreateAlert fragment = new FragmentEventCreateAlert();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void showPopupDialog(int startOrEnd) {
        Fragment pre = getFragmentManager().findFragmentByTag("dialog");
        if (pre!=null) {
            getFragmentManager().beginTransaction().remove(pre);
        }
        FragmentEventTime newFragment = new FragmentEventTime();
        newFragment.setEvent(event);
        newFragment.setFirstShowStartOrEnd(startOrEnd);
        newFragment.setItimeDialogSaveCallBack(new FragmentEventTime.ItimeDialogSaveCallBack() {
            @Override
            public void onSave(Event event) {
                FragmentEventPrivateCreate.this.event = event;
                vm.setEvent(event);
            }
        });
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void toInvitee(Event event) {
        // dont write anything here, solo event does not have this method
    }

    @Override
    public void toPhoto() {
        hasChange = true;
        startPhotoPicker();
    }

    @Override
    public void toGreeting(Event event) {
//        dont do anything here
    }

    @PermissionGrant(REQUEST_CAMERA_PERMISSION)
    public void openCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, REQ_PHOTO);
    }

    @Override
    public void toPhotoGridView() {
        toPhotoGridPage();
    }

    @PermissionGrant(REQUEST_PHOTO_PERMISSION)
    public void startPhotoPicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(9);

        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intent, REQ_PHOTO);
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
        if (requestCode == REQ_PHOTO && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(CameraActivity.KEY_RESULT);
            PhotoUrl photoUrl = EventUtil.fromStringToPhotoUrl(getContext(), result);
            List<PhotoUrl> photos = new ArrayList<>();
            photos.add(photoUrl);
            event.setPhoto(photos);
            setEvent(event);
            toPhotoGridPage();
        }

        if(data!=null) {
            if (requestCode == REQ_PHOTO && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                List<PhotoUrl> photoUrls = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    photoUrls.add(EventUtil.fromStringToPhotoUrl(getContext(), images.get(i).path));
                }
                event.setPhoto(photoUrls);
                setEvent(event);
                toPhotoGridPage();
            }
        }
    }

    private void toPhotoGridPage(){
        EventPhotoFragment eventPhotoFragment = new EventPhotoFragment();
        eventPhotoFragment.setEvent(event);
        getBaseActivity().openFragment(eventPhotoFragment);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        if (taskId == EventCreatePresenter.TASK_EVENT_CREATE){
            EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            // scroll calendar to event
            getBaseActivity().finishWithScrollDate(Activity.RESULT_OK,new Date(event.getStartTime()));
        } else if (taskId == EventCreatePresenter.TASK_EVENT_UPDATE){
            FragmentEventDetail fragmentEventDetail = (FragmentEventDetail) getFrom();
            fragmentEventDetail.setData(event);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
