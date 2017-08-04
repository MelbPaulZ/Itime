package org.unimelb.itime.ui.fragment.event;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.DialogEventDetailNoteBinding;
import org.unimelb.itime.databinding.FragmentEventDetailBinding;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.event.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.presenter.event.EventDetailPresenter;
import org.unimelb.itime.ui.viewmodel.event.EventDetailViewModel;
import org.unimelb.itime.widget.CollapseHeadBar;
import org.unimelb.itime.widget.popupmenu.ModalPopupView;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/09/2016.
 */
public class FragmentEventDetail extends ItimeBaseFragment<EventDetailMvpView, EventCreatePresenter<EventDetailMvpView>>
        implements EventDetailMvpView {
    private static final String SHOW_EVENT_DETAIL_TIPS = "event detail tips";
    private FragmentEventDetailBinding binding;
    private Event event;
    private FragmentEventDetailConfirm confirmFragment;
    private CollapseHeadBar headBar;

    private EventDetailViewModel contentViewModel;
    // for displaying timeslots
//    private List<SubTimeslotViewModel> timeslotVMList;
//    private List<WrapperTimeSlot> wrapperTimeSlotList;
    private ModalPopupView allNotePop;
    private DialogEventDetailNoteBinding noteBinding;

    private boolean timeslotShow = true;

    // for display invitee response page
//    private Map<String, List<EventUtil.StatusKeyStruct>> replyData = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public EventCreatePresenter<EventDetailMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    private void initStatusBar(){
        getActivity().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new EventDetailViewModel(getPresenter());
        contentViewModel.setEvent(event);
        contentViewModel.setShowEventDetailTips(false);
        initData();
        initToolbar();
        initAllNotePop();
        binding.setContentVM(contentViewModel);
        contentViewModel.setToolbarCollapseColor(getResources().getColor(R.color.lightBlueTwo));
//        initBottomSheet();
    }



    public void setData(Event event) {
        this.event = event;
    }

//    public void setData(Event event, List<WrapperTimeSlot> wrapperList){
//        this.event = event;
//        this.wrapperTimeSlotList = wrapperList;
//    }

    private void initData(){
//        if (event.getEventType().equals(Event.TYPE_SOLO)){
//            // if solo event, dont need init followings
//            return;
//        }
        initInvitee();
        initInviteeReplies();
        initTimeslots();

    }

    private void initAllNotePop(){
        allNotePop = new ModalPopupView(getContext());
        noteBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_event_detail_note, new FrameLayout(getContext()), false);
        noteBinding.setNote(event.getNote());
        View content = noteBinding.getRoot();
        allNotePop.setContentView(content);
        allNotePop.setBackground(getResources().getDrawable(R.color.mask_cover));
    }

    private void initInvitee(){
        // it needs to be puted on the begining, otherwise won't initialize
//        contentViewModel.generateInviteeList(EventUtil.getInviteeWithStatus(event.getInvitee(), Invitee.STATUS_ACCEPTED));
    }

    private void initTimeslots(){
//        if (wrapperTimeSlotList==null){
//            contentViewModel.setWrapperTimeSlotList(timeslotVMList);
//            return;
//        }
//        // if wrapperTimeslotList is not null, then initialise based on wrapperTimeslots
//        timeslotVMList = new ArrayList<>();
//        for (WrapperTimeSlot wrapper: wrapperTimeSlotList){
//            SubTimeslotViewModel vm = new SubTimeslotViewModel(this);
//            vm.setWrapper(wrapper);
//            vm.setHostEvent(EventUtil.isUserHostOfEvent(getContext(), event));
//            vm.setInviteeVisibility(
//                    event.getInviteeVisibility() == 0 && !EventUtil.isUserHostOfEvent(getContext(), event)? 0:1);
//            vm.setReplyData(replyData);
//            vm.setIconSelected(wrapper.isSelected());
//            timeslotVMList.add(vm);
//        }
//        contentViewModel.setWrapperTimeSlotList(timeslotVMList);

    }

    private void initInviteeReplies(){
//        replyData = EventUtil.getAdapterData(event);
    }

    private void initToolbar(){
        headBar = binding.appBar;
        headBar.setViewModel(contentViewModel);
    }



    private void toCalendar(int resultCode) {
        Intent intent = new Intent();
        getActivity().setResult(resultCode, intent);
        getActivity().finish();
    }

    public void gotoConfirm(TimeSlot timeSlot){
        if(confirmFragment == null){
            confirmFragment = new FragmentEventDetailConfirm();
        }
        confirmFragment.setEvent(event);
        confirmFragment.setTimeSlot(timeSlot);
        getBaseActivity().openFragment(confirmFragment);
    }

    @Override
    public void viewInCalendar() {
//        EventTimeSlotViewFragment timeSlotViewFragment = new EventTimeSlotViewFragment();
//
//        timeSlotViewFragment.setFragment_task(TASK_VIEW);
//        Event cpyEvent = EventUtil.copyEvent(event);
//        List<WrapperTimeSlot> cpyWrapperTimeslots = new ArrayList<>();
//        if(this.wrapperTimeSlotList!=null) {
//            for (WrapperTimeSlot wrapperTimeSlot : this.wrapperTimeSlotList) {
//                cpyWrapperTimeslots.add(wrapperTimeSlot.copyWrapperTimeslot());
//            }
//        }
//
//        if (event.getConfirmedCount()>0){
//            // if the event is confirmed, then no timeslot showing
//            timeSlotViewFragment.setData(event, null);
//            timeSlotViewFragment.setDisplayTimeslot(false);
//        }else {
//            timeSlotViewFragment.setData(cpyEvent, cpyWrapperTimeslots);
//        }
//
//        getBaseActivity().openFragment(timeSlotViewFragment);
    }


    @Override
    public void viewInviteeResponse(TimeSlot timeSlot) {

//        InviteeTimeslotFragment inviteeTimeslotFragment = new InviteeTimeslotFragment();
//        inviteeTimeslotFragment.setData(this.event, replyData.get(timeSlot.getTimeslotUid()), timeSlot);
//        getBaseActivity().openFragment(inviteeTimeslotFragment);
    }


    @Override
    public void openUrl(String url){

    }

    @Override
    public void gotoGridView() {
//        if(eventPhotoFragment==null){
//            eventPhotoFragment = new EventPhotoFragment();
//        }
//        eventPhotoFragment.setEvent(event);
//        eventPhotoFragment.setEditable(false);
//        getBaseActivity().openFragment(eventPhotoFragment);
    }

    public void showAllNote(){
        if(allNotePop!=null){
            allNotePop.show(getView());
        }
    }

    public void gotoEdit(){
        if (event.getEventType().equals(Event.TYPE_GROUP)){
            FragmentEventCreate fragment = new FragmentEventCreate();
            fragment.setEvent(event);
            getBaseActivity().openFragment(fragment);
        }else if (event.getEventType().equals(Event.TYPE_SOLO)){
            FragmentEventPrivateCreate fragment = new FragmentEventPrivateCreate();
            fragment.setEvent(event);
            getBaseActivity().openFragment(fragment);
        }
    }

    public void viewChange() {
//        FragmentEventCreateAddContact fragment = new FragmentEventCreateAddContact();
//        fragment.setEvent(event);
//        getBaseActivity().openFragment(fragment);
    }

//    @Override
//    public void onTimeslotClick(WrapperTimeSlot wrapper) {
//        // this wrapper has been changed, if host, then has already two selected wrappers
//        if (EventUtil.isUserHostOfEvent(getContext(), event)){
//            if (wrapper.isSelected() && TimeSlotUtil.numberSelectedWrapper(timeslotVMList) >1){
//                unselectWrappersExclude(wrapper);
//            }
//        }
//
//        // two people event can only select one timeslot, need to unselect other timeslots
//        if (!EventUtil.isUserHostOfEvent(getContext(), event)){
//            if (wrapper.isSelected()
//                    && TimeSlotUtil.numberSelectedWrapper(timeslotVMList) > 1
//                    && event.getInvitee().size() == 2){
//                unselectWrappersExclude(wrapper);
//            }
//        }
//        contentViewModel.setWrapperTimeSlotList(timeslotVMList);
//    }

//    /**
//     * this method unselect other timeslots, if the host select more than one timeslots
//     * @param exclusiveWrapper
//     */
//    private void unselectWrappersExclude(WrapperTimeSlot exclusiveWrapper){
//        for (SubTimeslotViewModel viewModel: timeslotVMList){
//            WrapperTimeSlot wrapperTimeSlot = viewModel.getWrapper();
//            if (!wrapperTimeSlot.equals(exclusiveWrapper)){
//                wrapperTimeSlot.setSelected(false);
//                viewModel.setIconSelected(false);
//            }
//        }
//    }

    @Override
    public void toResponse() {
//        EventResponseFragment eventResponseFragment = new EventResponseFragment();
//        eventResponseFragment.setData(event);
//        getBaseActivity().openFragment(eventResponseFragment);
    }

    @Override
    public void createEventFromThisTemplate(Event event) {
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra("event", (Serializable) event);
        startActivity(intent);
    }

    @Override
    public void proposeNewTimeslots(Event event) {
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra("event", (Serializable) event);
        startActivity(intent);
    }

    @Override
    public void viewUserProfile(String userUid) {
//        ProfileFragment userProfileFrag = new ProfileFragment();
//        userProfileFrag.setUserUid(userUid);
//        getBaseActivity().openFragment(userProfileFrag);
    }

    @Override
    public void onViewGoingInvitees() {
//        EventConfirmInviteeFragment confirmInviteeFragment = new EventConfirmInviteeFragment();
//        confirmInviteeFragment.setEvent(event);
//        getBaseActivity().openFragment(confirmInviteeFragment);
    }


    @Override
    public void onTaskStart(int task) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {
        hideProgressDialog();
//        if (taskId == EventPresenter.TASK_TIMESLOT_ACCEPT){
//            toCalendar(Activity.RESULT_OK);
//        }else if (taskId == EventPresenter.TASK_EVENT_CONFIRM){
//            toCalendar(Activity.RESULT_OK);
//        }else if (taskId == EventPresenter.TASK_TIMESLOT_REJECT){
//            toCalendar(Activity.RESULT_OK);
//        }else if (taskId == EventPresenter.TASK_BACK){
//            toCalendar(Activity.RESULT_CANCELED);
//        }else if (taskId == EventPresenter.TASK_EVENT_ACCEPT){
//            toCalendar(Activity.RESULT_OK);
//        }else if (taskId == EventPresenter.TASK_EVENT_REJECT){
//            toCalendar(Activity.RESULT_OK);
//        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
    }

    @Override
    public void onBack() {
        getActivity().finish(); // paul add.
//        toCalendar(EventPresenter.TASK_BACK);
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onNext() {
//        if (EventUtil.isUserHostOfEvent(getContext(), event)) {
//            EventEditFragment eventEditFragment = new EventEditFragment();
//            Event cpyEvent = EventUtil.copyEvent(event);
//            cpyEvent.setStatus(Event.STATUS_UPDATING); // from here to edit event, must be updating
//            eventEditFragment.setEvent(cpyEvent);
//            getBaseActivity().openFragment(eventEditFragment);
//        }
    }


//    private boolean isShowTips(){
//        return EventUtil.isUserHostOfEvent(getContext(), event) &&
//                AppUtil.getSharedPreferences(getContext()).getBoolean(SHOW_EVENT_DETAIL_TIPS, true);
//    }

//    private boolean isGroupEvent(){
//        return event.getEventType().equals(Event.TYPE_GROUP);
//    }

//    private void initTips(){
//        if(isShowTips()&&isGroupEvent()){
//            SharedPreferences.Editor editor = AppUtil.getSharedPreferences(getActivity()).edit();
//            editor.putBoolean(SHOW_EVENT_DETAIL_TIPS, false);
//            editor.apply();
//            contentViewModel.setShowEventDetailTips(true);
//            contentViewModel.setShowTips(true);
//        }else{
//            contentViewModel.setShowTips(false);
//            contentViewModel.setShowEventDetailTips(false);
//        }
//    }

    /**
     * Added by Qiushuo Huang
     *
     */
    @Override
    public void onResume(){
        super.onResume();
//        initTips();
//        if(timeslotShow) {
//            bottomSheet.show();
//        }else{
//            bottomSheet.hide();
//        }
    }

//    public void initBottomSheet(){
//        final TextView show=binding.showButton;
//        bottomSheet =binding.multibottomlayout;
//        show.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        bottomSheet.toggle();
//                    }
//                }
//        );
//        bottomSheet.setOnStateChangeListener(new MultiBottomSheetLayout.OnStateChangeListener() {
//            @Override
//            public void onStateChange(int newState) {
//                if(newState == MultiBottomSheetLayout.STATE_EXPANDED){
//                    show.setText("hide");
//                    timeslotShow=true;
//                }else{
//                    show.setText("show");
//                    timeslotShow=false;
//                }
//            }
//        });
//    }

    public PopupMenu.OnItemClickListener getOnMenuItemClick(){
        return new PopupMenu.OnItemClickListener() {

            @Override
            public void onClick(int position, PopupMenu.Item item) {
                if(position==0){
                    viewChange();
                }
                Toast.makeText(getContext(), item.name, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public ArrayList<PopupMenu.Item> getMenuList(){
        ArrayList<PopupMenu.Item> list = new ArrayList<>();
        list.add(new PopupMenu.Item(R.drawable.icon_calendar_blue_notext, "edit"));
        list.add(new PopupMenu.Item(R.drawable.icon_calendar_blue_notext, "add"));
        list.add(new PopupMenu.Item( R.drawable.icon_calendar_blue_notext, "invite"));
        list.add(new PopupMenu.Item( R.drawable.icon_calendar_blue_notext, "duration"));

//
//        list.add(new PopupMenu.Item("edit"));
//        list.add(new PopupMenu.Item("add"));
//        list.add(new PopupMenu.Item("invite"));
//        list.add(new PopupMenu.Item("duration"));
        return list;
    }
}
