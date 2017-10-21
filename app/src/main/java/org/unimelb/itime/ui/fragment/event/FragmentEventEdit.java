package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentEventCreateBinding;
import org.unimelb.itime.databinding.FragmentEventEditBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.CameraActivity;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.LocationActivity;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendarTimeslot;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.PicassoImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 9/9/17.
 */

public class FragmentEventEdit extends ItimeBaseFragment<EventCreateMvpView, EventCreatePresenter<EventCreateMvpView>>
        implements EventCreateMvpView, ToolbarInterface {
    private FragmentEventEditBinding binding;
    private EventCreateViewModel vm;
    private ToolbarViewModel toolbarViewModel;
    private Event event;
    private boolean hasChange = false;
    public final static int REQ_LOCATION = 1000;
    public final static int REQ_INVITEE = 1001;
    public final static int REQ_TIMESLOT = 1002;
    public final static int REQ_CUSTOM_REPEAT = 1003;
    public final static int REQ_PHOTO = 1004;

    public final static int REQUEST_CAMERA_PERMISSION = 100;
    public final static int REQUEST_PHOTO_PERMISSION = 101;
    public final static int REQUEST_LOCATION_PERMISSION = 102;

    FragmentEventCreateAddInvitee fragmentEventCreateAddInvitee;

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
            vm = new EventCreateViewModel(getPresenter());
            vm.setEvent(event);
            binding.setVm(vm);
            toolbarViewModel = new ToolbarViewModel<>(this);
            toolbarViewModel.setTitle(getString(R.string.edit_event_toolbar_title));
            toolbarViewModel.setRightText(getString(R.string.toolbar_save));
            toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_close));
            toolbarViewModel.setRightEnable(true);
            binding.setToolbarvm(toolbarViewModel);
        }else{
            vm.setEvent(event);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit, container, false);
        }
        return binding.getRoot();
    }


    @Override
    public void onNext() {
        if (event.getTimeslot().size() == 0){
            toTimeslot(event);
        }else {
            EventUtil.generateGroupEventAttributes(getContext(), event);
            EventUtil.resetTimeslotUidForExistingInvitees(getContext(), event);
            presenter.updateEvent(event);
        }
        hasChange = true;
    }

    @Override
    public void onBack() {
        if (!hasChange){
            getFragmentManager().popBackStack();
            return;
        }

        getDialogBuidler()
                .content(R.string.event_edit_cancel_dialog_content)
                .contentColor(getResources().getColor(R.color.black))
                .contentGravity(GravityEnum.CENTER)
                .negativeText(R.string.event_dialog_discard)
                .positiveText(R.string.event_dialog_keep_editing)
                .onNegative(((dialog, which) -> getFragmentManager().popBackStack()))
                .show();

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
        fragment.setTargetFragment(this, -100); // TODO: 23/6/17 change -100
        getBaseActivity().openFragmentBottomUp(fragment);
    }

    @Override
    public void toDuration(Event event) {
        hasChange = true;
        FragmentEventCreateDuration fragment = new FragmentEventCreateDuration();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(fragment);
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
        intent.putExtra(getString(R.string.location_string2), event.getLocation().getLocationString2());
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
        hasChange = true;
        FragmentCalendarTimeslot fragment = new FragmentCalendarTimeslot();
        Event cpyEvent = EventManager.getInstance(getContext()).copyEvent(event);
        fragment.setMode(FragmentCalendarTimeslot.Mode.HOST_CREATE);
        fragment.setEvent(cpyEvent, new ArrayList<>(cpyEvent.getTimeslot().values()));
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toAlert(Event event) {
        hasChange = true;
    }

    @Override
    public void showPopupDialog(int startOrEnd) {

    }

    @Override
    public void toInvitee(Event event) {
        // click invitee cell
        hasChange = true;
        if(fragmentEventCreateAddInvitee==null)
            fragmentEventCreateAddInvitee = new FragmentEventCreateAddInvitee();
        fragmentEventCreateAddInvitee.setEvent(event);
        getBaseActivity().openFragment(fragmentEventCreateAddInvitee);
    }

    @Override
    public void toPhoto() {
        hasChange = true;
        startPhotoPicker();
    }

    @Override
    public void toGreeting(Event event) {
        FragmentEventGreeting fragmentEventGreeting = new FragmentEventGreeting();
        fragmentEventGreeting.setMode(FragmentEventGreeting.Mode.EDIT);
        fragmentEventGreeting.setEvent(event);
        getBaseActivity().openFragment(fragmentEventGreeting);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOCATION && resultCode == Activity.RESULT_OK){
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


    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        FragmentEventDetail fragmentEventDetail = (FragmentEventDetail) getFrom();
        fragmentEventDetail.setData(event);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
    }

    /**
     * only photo related permission granted, then can go to photo picker
     */
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

//    @Override
//    public void toPhotoPickerPage() {
//        if(event.getPhoto().isEmpty()) {
//            openPhotoActionSheetDialog();
//        }else{
//            toPhotoGridPage();
//        }
//    }

    private void toPhotoGridPage(){
        EventPhotoFragment eventPhotoFragment = new EventPhotoFragment();
        eventPhotoFragment.setEvent(event);
        getBaseActivity().openFragment(eventPhotoFragment);
    }

    @PermissionGrant(REQUEST_CAMERA_PERMISSION)
    public void openCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, REQ_PHOTO);
    }

    @Override
    public void toPhotoGridView() {
        EventPhotoFragment eventPhotoFragment = new EventPhotoFragment();
        eventPhotoFragment.setEditable(false);
        eventPhotoFragment.setEvent(event);
        getBaseActivity().openFragment(eventPhotoFragment);

    }

    @PermissionDenied(REQUEST_LOCATION_PERMISSION)
    public void locationDenied(){
        Toast.makeText(getContext(), "need location permission", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUEST_PHOTO_PERMISSION)
    public void photoDenied(){
        Toast.makeText(getContext(), "need photo permission", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUEST_CAMERA_PERMISSION)
    public void cameraDenied(){
        Toast.makeText(getContext(), "need camera permission", Toast.LENGTH_SHORT).show();
    }
}
