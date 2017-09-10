package org.unimelb.itime.ui.fragment.event;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.DialogEventDetailNoteBinding;
import org.unimelb.itime.databinding.FragmentEventDetailBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.ItimeActivitiesActivity;
import org.unimelb.itime.ui.activity.ProfileActivity;
import org.unimelb.itime.ui.fragment.activity.FragmentItimeActivitiesDetail;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendarTimeslot;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendarViewInCalendar;
import org.unimelb.itime.ui.mvpview.event.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.event.EventDetailViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.OtherUtil;
import org.unimelb.itime.widget.CollapseHeadBar;
import org.unimelb.itime.widget.popupmenu.ModalPopupView;
import org.unimelb.itime.widget.popupmenu.PopupMenu;
import org.unimelb.itime.widget.popupmenu.SelectAlertTimeDialog;

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
    private FragmentEventCreate eventCreateFragment;
    private FragmentEventDetailAllInvitees allInviteesFragment;
    private CollapseHeadBar headBar;
    private FragmentItimeActivitiesDetail activitiesFragment = new FragmentItimeActivitiesDetail();
    private EventPhotoFragment eventPhotoFragment;


    private EventDetailViewModel contentViewModel;
    private ModalPopupView allNotePop;
    private DialogEventDetailNoteBinding noteBinding;
    private SelectAlertTimeDialog selectAlertTimeDialog;

    private boolean timeslotShow = true;

    // for display invitee response page
//    private Map<String, List<EventUtil.StatusKeyStruct>> replyData = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(binding==null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public EventCreatePresenter<EventDetailMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doEventMessage(MessageEvent messageEvent){
        if(messageEvent.task==MessageEvent.RELOAD_EVENT && event!=null) {
           presenter.refreshEvent(event.getEventUid());
        }
    }

    public View getView(){
        return binding.getRoot();
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
        if(contentViewModel==null) {
            contentViewModel = new EventDetailViewModel(getPresenter());
            contentViewModel.setEvent(event);
            contentViewModel.setShowEventDetailTips(false);
            contentViewModel.setTimeSlotSheet(binding.timeSlotSheet);
            initToolbar();
            initAllNotePop();
            binding.setContentVM(contentViewModel);
            contentViewModel.setNodeViews(binding.noteText, binding.readAllText);
            contentViewModel.setToolbarCollapseColor(getResources().getColor(R.color.lightBlueTwo));
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    public void setData(Event event) {
        this.event = event;
        if(contentViewModel!=null){
            contentViewModel.setEvent(event);
        }
    }

    public void setSelectedTimeslot(List<TimeSlot> timeslot){
        if (contentViewModel != null){
            contentViewModel.setSelectedTimeSlots(timeslot);
        }
    }

    private void initAllNotePop(){
        allNotePop = new ModalPopupView(getContext());
        noteBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_event_detail_note, new FrameLayout(getContext()), false);
        noteBinding.setNote(event.getNote());
        View content = noteBinding.getRoot();
        allNotePop.setContentView(content);
        allNotePop.setBackground(getResources().getDrawable(R.color.mask_cover));
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
        if (event.isConfirmed()){
            toCalendarView();
        }else {
            toTimeslotView(EventUtil.isHost(event));
        }
    }

    private void toTimeslotView(boolean isHost){
        FragmentCalendarTimeslot.Mode mode = isHost ?
                FragmentCalendarTimeslot.Mode.HOST_CONFIRM : FragmentCalendarTimeslot.Mode.INVITEE_CONFIRM;
        FragmentCalendarTimeslot fragmentCalendarTimeslot = new FragmentCalendarTimeslot();
        fragmentCalendarTimeslot.setEvent(event, contentViewModel.getSelectedTimeSlots());
        fragmentCalendarTimeslot.setMode(mode);
        getBaseActivity().openFragment(fragmentCalendarTimeslot);
    }

    private void toCalendarView(){
        FragmentCalendarViewInCalendar fragmentCalendarViewInCalendar = new FragmentCalendarViewInCalendar();
        fragmentCalendarViewInCalendar.setEvent(event);
        getBaseActivity().openFragment(fragmentCalendarViewInCalendar);
    }


    @Override
    public void viewInviteeResponse(TimeSlot timeSlot) {


    }


    @Override
    public void openUrl(String url){
        OtherUtil.openUrl(getContext(), url);
    }

    @Override
    public void gotoGridView() {
        if(eventPhotoFragment==null){
            eventPhotoFragment = new EventPhotoFragment();
            eventPhotoFragment.setEditable(false);
        }
        eventPhotoFragment.setEvent(event);
        getBaseActivity().openFragment(eventPhotoFragment);
    }

    @Override
    public void toBigPhoto(int position){
        if(eventPhotoFragment==null){
            eventPhotoFragment = new EventPhotoFragment();
            eventPhotoFragment.setEditable(false);
        }
        eventPhotoFragment.setEvent(event);
        getBaseActivity().openFragment(eventPhotoFragment);
        eventPhotoFragment.openBigPhoto(position);
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
            fragment.setTaskMode(FragmentEventCreate.Mode.UPDATE);
            getBaseActivity().openFragment(fragment);
        }else if (event.getEventType().equals(Event.TYPE_SOLO)){
            FragmentEventPrivateCreate fragment = new FragmentEventPrivateCreate();
            fragment.setEvent(event);
            fragment.setTaskMode(FragmentEventCreate.Mode.UPDATE);
            getBaseActivity().openFragment(fragment);
        }
    }

    @Override
    public void onDelete(boolean repeat, boolean host) {
        MaterialDialog.Builder builder = getDialogBuidler().positiveText(R.string.dialog_delete_delete)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteEvent(event, true, host, dialog.getInputEditText().getText().toString());
                    }
                })
                .negativeText(R.string.dialog_cancel)
                .content(R.string.event_detail_delete_dialog_message)
                .input(R.string.event_detail_delete_dialog_input_hints, 0, true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                });
        if(repeat){
            builder.title(R.string.event_detail_delete_dialog_repeat_title);
        }else{
            builder.title(R.string.event_detail_delete_dialog_title);
        }
        builder.show();
    }

    @Override
    public void showAlerTimeDialog() {
        if(selectAlertTimeDialog == null){
            selectAlertTimeDialog = new SelectAlertTimeDialog(getContext());
            selectAlertTimeDialog.setListener(getOnTimeClickListener());
        }
        selectAlertTimeDialog.setSelectedTime(event.getReminder());
        selectAlertTimeDialog.show(getView());
    }

    private SelectAlertTimeDialog.OnTimeClickListener getOnTimeClickListener(){
        return new SelectAlertTimeDialog.OnTimeClickListener() {
            @Override
            public void onClick(View v, int time) {
                Event e = event.clone();
                e.setReminder(time);
                presenter.updateReminder(e);
            }
        };
    }

    public void viewChange() {
//        FragmentEventCreateAddContact fragment = new FragmentEventCreateAddContact();
//        fragment.setEvent(event);
//        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toResponse() {
        MessageGroup messageGroup = presenter.getMessageGroup(event.getEventUid());
        if(messageGroup!=null) {
//            Intent intent = new Intent();
//            intent.setClass(getActivity(), ItimeActivitiesActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(ItimeActivitiesActivity.ACTIVITIES_MEETING, messageGroup);
//            intent.putExtras(bundle);
//            startActivity(intent);
            FragmentItimeActivitiesDetail fragmentItimeActivitiesDetail = new FragmentItimeActivitiesDetail();
            fragmentItimeActivitiesDetail.setMessageGroup(messageGroup);
            getBaseActivity().openFragment(fragmentItimeActivitiesDetail);
        }
    }

    public void toAllInvitees(){
        if(allInviteesFragment==null) {
            allInviteesFragment = new FragmentEventDetailAllInvitees();
        }
        allInviteesFragment.setEvent(event);
        allInviteesFragment.setStartMode(FragmentEventDetailAllInvitees.MODE_EVENT);
        getBaseActivity().openFragment(allInviteesFragment);
    }

    public void toTimeSlotInvitees(TimeSlot timeSlot){
        if(allInviteesFragment==null) {
            allInviteesFragment = new FragmentEventDetailAllInvitees();
        }
        allInviteesFragment.setEvent(event);
        allInviteesFragment.setTimeSlot(timeSlot);
        allInviteesFragment.setStartMode(FragmentEventDetailAllInvitees.MODE_TIMESLOT);
        getBaseActivity().openFragment(allInviteesFragment);
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

    public void toDuplicate(Event event){
        Event e = EventUtil.duplicateEvent(event);
        if (event.getEventType() == Event.TYPE_GROUP) {
            if (eventCreateFragment == null) {
                eventCreateFragment = new FragmentEventCreate();
            }

            eventCreateFragment.setEvent(e);
            getBaseActivity().openFragment(eventCreateFragment);
        }else{
            FragmentEventPrivateCreate soloFragment = new FragmentEventPrivateCreate();
            soloFragment.setEvent(e);

            getBaseActivity().openFragment(soloFragment);
        }
    }


    public void onRejectAll(){
        getDialogBuidler()
                .positiveText(R.string.dialog_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.rejectTimeslots(event.getCalendarUid(), event.getEventUid(), dialog.getInputEditText().getText().toString());
                    }
                })
                .negativeText(R.string.dialog_cancel)
                .title(R.string.event_detail_dialog_cantgo_title)
                .content(R.string.event_detail_dialog_cantgo_message)
                .input(R.string.event_detail_dialog_cantgo_hint, 0, true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .show();

    }

    public void remindEveryone(){
        getDialogBuidler()
                .positiveText(R.string.event_detail_dialog_remind)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .negativeText(R.string.dialog_cancel)
                .title(R.string.event_detail_dialog_remind_title)
                .show();
    }

    @Override
    public void onTaskStart(int task) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case EventCreatePresenter.TASK_REFRESH_EVENT:
                if(contentViewModel!=null && data instanceof Event){
                    Event e = (Event) data;
                    if(!event.equals(e)) {
                        setData((Event) data);
                    }
                }
                break;
            case EventCreatePresenter.TASK_EVENT_DELETE:
                getActivity().finish();
                break;
        }
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
        if(contentViewModel.isSelectedTimeSlotsChanged()){
            showTimeSlotNotSaveDialog();
        }else{
            getActivity().finish();
        }
//        toCalendar(EventPresenter.TASK_BACK);
    }

    private void showTimeSlotNotSaveDialog(){
        if(contentViewModel.isHost()) {
            getDialogBuidler()
                    .positiveText(R.string.event_detail_dialog_stay)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getBaseActivity().onBackPressed();
                        }
                    })
                    .negativeText(R.string.event_detail_dialog_discard)
                    .title(R.string.event_detail_alert_unsaved_confirm)
                    .show();
        }else {
            getDialogBuidler()
                    .positiveText(R.string.event_detail_dialog_keep_voting)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getBaseActivity().onBackPressed();
                        }
                    })
                    .negativeText(R.string.event_detail_dialog_discard)
                    .title(R.string.event_detail_alert_unsaved_vote)
                    .show();
        }
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
        contentViewModel.setSelectedTimeSlots(contentViewModel.getSelectedTimeSlots());
        contentViewModel.generateTimeSlotItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
