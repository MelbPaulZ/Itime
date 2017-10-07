package org.unimelb.itime.ui.viewmodel.event;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.mvpview.event.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeFactory;
import org.unimelb.itime.widget.PhotoViewLayout;
import org.unimelb.itime.widget.ScalableLayout;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailViewModel extends BaseObservable{
    public static final int STATUS_NEED_VOTE = 1;
    public static final int STATUS_VOTED = 2;
    public static final int STATUS_NEED_CONFIRM = 3;
    public static final int STATUS_CONFIRMED = 4;
    public static final int STATUS_NEED_REVOTE = 5;
    public static final int STATUS_GOING = 6;
    public static final int STATUS_NOT_GOING = 7;
    public static final int STATUS_CANCELED = 8;
    public static final int STATUS_EXPIRED = 9;
    public static final int STATUS_NOT_GOING_PENDING = 10;
    public static final int STATUS_NEED_DECIDE = 11;

    private EventCreatePresenter<EventDetailMvpView> presenter;
    private Event event;
    private EventDetailMvpView mvpView;
    private Context context;
    private boolean showEventDetailTips = false;
    private boolean showTips = false;
    private boolean canSeeEachOther = true;
    private int[] alertTimes;
    private String alertString = "";
    private Invitee hostInvitee;
    private String coverImg="";
    private String eventTimeString;
    private int toolbarCollapseColor=0;
    private PopupMenu.OnItemClickListener onMenuClickListener;
    private List<PopupMenu.Item> menuItems;
    private List<String> avatarList = new ArrayList<>();
    private String calendarType = "";
    private List<TimeSlot> timeSlots;
    private List<TimeSlot> selectedTimeSlots = new ArrayList<>();
    private List<TimeSlot> originalSelectedTimeSlots = new ArrayList<>();
    private ObservableList<EventDetailTimeSlotItemViewModel> timeSlotsItems = new ObservableArrayList<>();
    private boolean showTimeSlotSheet = false;
    private int status = STATUS_NEED_CONFIRM;
    private int originalStatus;
    private boolean host;
    private boolean canVote = false;
    private ScalableLayout timeSlotSheet;
    private boolean confirmed;

    private int repliedNum = 0;
    private int cantGoNum = 0;
    private int noReplyNum = 0;
    private int inviteeNum = 0;

    private List<Invitee> goingInvitees = new ArrayList<>();
    private List<Invitee> notGoingInvitees = new ArrayList<>();
    private List<Invitee> noReplyInvitees = new ArrayList<>();

    private boolean showConfirmVoteButton;
    private boolean showCantGoVoteButton;
    private boolean showSubmitVoteButton;
    private boolean showSubmitVoteDisableButton;
    private boolean soloEvent = false;

    private ObjectAnimator bottomSheetHeaderShowAnimator;
    private ObjectAnimator bottomSheetHeaderHideAnimator;
    private static int ANIMATOR_DURATION = 300;
    private LayerDrawable bottomSheetHeaderDrawable;
    private int bottomSheetStatus;
    private String repeatString="";
    private String untilDateString="";
    private String sheetLeftText = "";

    private TextView noteTextView;
    private TextView readAllTextView;


    @Bindable
    public String getRepeatString() {
        return repeatString;
    }

    public void setRepeatString(String repeatString) {
        this.repeatString = repeatString;
        notifyPropertyChanged(BR.repeatString);
    }

    public boolean isSelectedTimeSlotsChanged(){
        return !(selectedTimeSlots.containsAll(originalSelectedTimeSlots)
                && originalSelectedTimeSlots.containsAll(selectedTimeSlots));
    }

    @Bindable
    public boolean isCanSeeEachOther() {
        return canSeeEachOther;
    }

    public void setCanSeeEachOther(boolean canSeeEachOther) {
        this.canSeeEachOther = canSeeEachOther;
        notifyPropertyChanged(BR.canSeeEachOther);
    }

    @Bindable
    public boolean isArchived() {
        return event.isArchived();
    }

    @Bindable
    public boolean isSoloEvent() {
        return soloEvent;
    }

    public void setSoloEvent(boolean soloEvent) {
        this.soloEvent = soloEvent;
        notifyPropertyChanged(BR.soloEvent);
    }

    private void initStatus(Event event) {
        String myStatus = EventUtil.getInviteeStatus(event);

        if(event.getStatus().equals(Event.STATUS_CANCELLED)){
            if(isHost()){
                status = STATUS_CANCELED;
            }else if(EventUtil.isConfirmed(event)){
                    status = STATUS_NOT_GOING;
                }else{
                status = STATUS_NOT_GOING_PENDING;
            }
            }

        if(event.getStatus().equals(Event.STATUS_PENDING)){
            if(isHost()){
                status = STATUS_NEED_CONFIRM;
            }else{
                if(myStatus!=null) {
                    switch (myStatus) {
                        case Invitee.STATUS_ACCEPTED:
                            status = STATUS_VOTED;
                            break;
                        case Invitee.STATUS_DECLINED:
                            status = STATUS_NOT_GOING_PENDING;
                            break;
                        case Invitee.STATUS_NEEDSACTION:
                            status = STATUS_NEED_VOTE;
                            break;
                    }
                }
            }
        }



        if(event.getStatus().equals(Event.STATUS_CONFIRMED)){

                if(myStatus!=null) {
                    switch (myStatus) {
                        case Invitee.STATUS_ACCEPTED:
                            status = STATUS_GOING;
                            break;
                        case Invitee.STATUS_DECLINED:
                            status = STATUS_NOT_GOING;
                            break;
                        case Invitee.STATUS_NEEDSACTION:
                            status = STATUS_NEED_DECIDE;
                            break;
                    }
                }
            }

            setStatus(status);
    }

    @Bindable
    public boolean isCanVote() {
        return canVote;
    }

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
        notifyPropertyChanged(BR.canVote);
    }

    @Bindable
    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
        notifyPropertyChanged(BR.confirmed);
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        if(status == STATUS_NEED_CONFIRM ||
                status == STATUS_NEED_VOTE ||
                status == STATUS_NOT_GOING_PENDING ||
                status == STATUS_VOTED){
            setCanVote(true);
            switch (status){
                case STATUS_NEED_CONFIRM:
                    setSheetLeftText(context.getString(R.string.event_detail_sheet_left_confirm_a_timeslot));
                    break;
                case STATUS_NEED_VOTE:
                    setSheetLeftText(context.getString(R.string.event_detail_sheet_left_choosetimeslots));
                    break;
                case STATUS_VOTED:
                    setSheetLeftText(context.getString(R.string.event_detail_sheet_left_voted));
                    break;
                case STATUS_NOT_GOING_PENDING:
                    setSheetLeftText(context.getString(R.string.event_detail_sheet_left_not_going));
                    break;
            }
        }else{
            setCanVote(false);
        }
        if(timeSlotSheet!=null){
            timeSlotSheet.notifyRemeasure();
        }
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
        notifyPropertyChanged(BR.host);
    }

    @Bindable
    public int getRepliedNum() {
        return repliedNum;
    }

    public void setRepliedNum(int repliedNum) {
        this.repliedNum = repliedNum;
        notifyPropertyChanged(BR.repliedNum);
    }

    @Bindable
    public int getCantGoNum() {
        return cantGoNum;
    }

    public void setCantGoNum(int cantGoNum) {
        this.cantGoNum = cantGoNum;
        notifyPropertyChanged(BR.cantGoNum);
    }

    @Bindable
    public int getNoReplyNum() {
        return noReplyNum;
    }

    public void setNoReplyNum(int noReplyNum) {
        this.noReplyNum = noReplyNum;
        notifyPropertyChanged(BR.noReplyNum);
    }

    @Bindable
    public int getInviteeNum() {
        return inviteeNum;
    }

    public void setInviteeNum(int inviteeNum) {
        this.inviteeNum = inviteeNum;
        notifyPropertyChanged(BR.inviteeNum);
    }

    public AdapterView.OnItemClickListener onTimeSlotClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventDetailTimeSlotItemViewModel vm = timeSlotsItems.get(i);
                if (vm.isOutdated()) {
                    Toast.makeText(context, context.getString(R.string.event_detail_thetimeslotisoutdated), Toast.LENGTH_SHORT).show();
                } else if (vm.isSelected()) {
                    vm.setSelected(false);
                    selectedTimeSlots.remove(vm.getTimeSlot());
                } else {
                        if (isHost()) {
                            selectedTimeSlots.clear();
                            for (EventDetailTimeSlotItemViewModel item : timeSlotsItems) {
                                item.setSelected(false);
                            }
                        }

                        vm.setSelected(true);
                        selectedTimeSlots.add(vm.getTimeSlot());
                        if (vm.isConflict()) {
                            Toast.makeText(context, context.getString(R.string.event_detail_youmayhaveconfilict), Toast.LENGTH_SHORT).show();
                        }
                    }
                if(!isHost()) {
                    updateStatus();
                }
                notifyPropertyChanged(BR.selectedTimeSlots);
                notifyPropertyChanged(BR.submitBtnString);
                notifyPropertyChanged(BR.votedBtnString);
            }
        };
    }

    private void updateStatus(){
        if (isSelectedTimeSlotsChanged()) {
            setStatus(STATUS_NEED_VOTE);
        } else{
            setStatus(originalStatus);
        }
    }

    @Bindable
    public String getEventTimeString() {
        return eventTimeString;
    }

    public void setEventTimeString(String eventTimeString) {
        this.eventTimeString = eventTimeString;
        notifyPropertyChanged(BR.eventTimeString);
    }

    private void generateEventTimeString(){
        TimeSlot tmpTimeSlot = new TimeSlot();
        tmpTimeSlot.setIsAllDay(event.isAllDay());
        tmpTimeSlot.setEndTime(event.getEndTime());
        tmpTimeSlot.setStartTime(event.getStartTime());

        String[] timeStrings = TimeFactory.getTimeStrings(context,tmpTimeSlot );
        setEventTimeString(timeStrings[0]
                + "\n"
                + timeStrings[1]);
    }

    @Bindable
    public int getBottomSheetStatus() {
        return bottomSheetStatus;
    }

    public void setBottomSheetStatus(int bottomSheetStatus) {
        this.bottomSheetStatus = bottomSheetStatus;
        notifyPropertyChanged(BR.bottomSheetStatus);
    }

    public Drawable getBottomSheetHeaderDrawable(){
        return bottomSheetHeaderDrawable;
    }

    @Bindable
    public boolean isShowSubmitVoteDisableButton() {
        return showSubmitVoteDisableButton;
    }

    public void setShowSubmitVoteDisableButton(boolean showSubmitVoteDisableButton) {
        this.showSubmitVoteDisableButton = showSubmitVoteDisableButton;
        notifyPropertyChanged(BR.showSubmitVoteDisableButton);
    }

    @Bindable
    public boolean isShowConfirmVoteButton() {
        return showConfirmVoteButton;
    }

    public void setShowConfirmVoteButton(boolean showConfirmVoteButton) {
        this.showConfirmVoteButton = showConfirmVoteButton;
        notifyPropertyChanged(BR.showConfirmVoteButton);

    }

    @Bindable
    public boolean isShowCantGoVoteButton() {
        return showCantGoVoteButton;
    }

    public void setShowCantGoVoteButton(boolean showCantGoVoteButton) {
        this.showCantGoVoteButton = showCantGoVoteButton;
        notifyPropertyChanged(BR.showCantGoVoteButton);
    }

    @Bindable
    public boolean isShowSubmitVoteButton() {
        return showSubmitVoteButton;
    }

    public void setShowSubmitVoteButton(boolean showSubmitVoteButton) {
        this.showSubmitVoteButton = showSubmitVoteButton;
        notifyPropertyChanged(BR.showSubmitVoteButton);
    }

    private void setTimeSlotBottomSheetButtonVisibilities(){
        setShowConfirmVoteButton(true);
    }

    @Bindable
    public boolean isShowTimeSlotSheet() {
        return showTimeSlotSheet;
    }

    public void setShowTimeSlotSheet(boolean showTimeSlotSheet) {
        this.showTimeSlotSheet = showTimeSlotSheet;
        if(showTimeSlotSheet){
            bottomSheetHeaderShowAnimator.start();
        }else{
            bottomSheetHeaderHideAnimator.start();
        }
        notifyPropertyChanged(BR.showTimeSlotSheet);
        notifyPropertyChanged(BR.bottomSheetMask);
    }

    public View.OnClickListener onSubmitVoteClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTimeSlots.isEmpty()) {
                    if(presenter!=null)
                        Toast.makeText(context, presenter.getContext().getString(R.string.event_detail_alert_empty_vote), Toast.LENGTH_SHORT).show();
                } else {
                    long firstAcceptTimeslot = 0;
                    HashMap<String, Object> params = new HashMap<>();
                    ArrayList<String> timeslotUids = new ArrayList<>();
                    for (TimeSlot timeSlot : selectedTimeSlots) {
                        timeslotUids.add(timeSlot.getTimeslotUid());
                        if (firstAcceptTimeslot == 0) {
                            // this is for recording where to scroll, first accept timeslot
                            firstAcceptTimeslot = timeSlot.getStartTime();
                        }
                    }
                    params.put("timeslots", timeslotUids);
                    presenter.acceptTimeslots(
                            event,
                            params,
                            firstAcceptTimeslot);
                }
            }
        };
    }

    public View.OnClickListener onConfirmClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null && !selectedTimeSlots.isEmpty()){
                    mvpView.gotoConfirm(selectedTimeSlots.get(0));
                }else if(selectedTimeSlots.isEmpty()){
                    Toast.makeText(context, context.getString(R.string.event_detail_alert_empty_confirm), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public View.OnClickListener onSnackGoingClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==STATUS_NOT_GOING||status==STATUS_NEED_DECIDE){
                    Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
                    presenter.acceptEvent(event.getCalendarUid(),
                            event.getEventUid(),
                            EventCreatePresenter.UPDATE_ALL,
                            event.getStart().getDateTime());
                }
            }
        };
    }

    public View.OnClickListener onSnackNotGoingClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==STATUS_GOING||status==STATUS_NEED_DECIDE){
                    presenter.quitEvent(event.getCalendarUid(), event.getEventUid(), EventCreatePresenter.UPDATE_ALL, event.getStart().getDateTime());
                }
            }
        };
    }

    public View.OnClickListener onCantGoAllClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mvpView.onRejectAll();
            }
        };
    }

    public ScalableLayout.OnStatusChangeListener getOnStatusChangeListener(){
        return new ScalableLayout.OnStatusChangeListener() {
            @Override
            public void onStatusChange(ScalableLayout layout, int oldStatus, int newStatus) {
                if(newStatus==ScalableLayout.STATUS_SCROLL){
                    return;
                }

                if(newStatus == ScalableLayout.STATUS_HIDE){
                    if(isShowTimeSlotSheet()) {
                        setShowTimeSlotSheet(false);
                    }
                }else{
                    if(!isShowTimeSlotSheet()){
                        setShowTimeSlotSheet(true);
                    }
                }
            }
        };
    }



    public RecyclerView.LayoutManager getTimeSlotLayoutManager(){
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    public ItemBinding<EventDetailTimeSlotItemViewModel> getTimeSlotItemBinding(){
//        return ItemBinding.of(BR.vm, R.layout.item_event_detail_timeslot);
        return ItemBinding.of(BR.vm, R.layout.item_event_detail_timeslot);
    }

    public ObservableList<EventDetailTimeSlotItemViewModel> getTimeSlotsItems() {
        return timeSlotsItems;
    }

    public void setTimeSlotsItems(ObservableList<EventDetailTimeSlotItemViewModel> timeSlotsItems) {
        this.timeSlotsItems = timeSlotsItems;
    }

    public void generateTimeSlotItems(){
        timeSlotsItems.clear();

        for(TimeSlot timeSlot:event.getTimeslot().values()){
            EventDetailTimeSlotItemViewModel vm = new EventDetailTimeSlotItemViewModel(context);
            vm.setTimeSlot(timeSlot);
            vm.setMvpView(mvpView);
            vm.setShowInvitee(canSeeEachOther);
            if(selectedTimeSlots.contains(timeSlot)){
                vm.setSelected(true);
            }else{
                vm.setSelected(false);
            }
            timeSlotsItems.add(vm);
        }
        Collections.sort(timeSlotsItems);
        updateStatus();
    }

    public View.OnClickListener getOnTimeSlotSheetClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showTimeSlotSheet){
                    setShowTimeSlotSheet(false);
                    setBottomSheetStatus(ScalableLayout.STATUS_HIDE);
                } else{
                    setShowTimeSlotSheet(true);
                    setBottomSheetStatus(ScalableLayout.STATUS_COLLAPSE);
                }
            }
        };
    }

    @Bindable
    public Drawable getBottomSheetMask(){
        if(showTimeSlotSheet){
            return new ColorDrawable(context.getResources().getColor(R.color.mask_cover));
        }else{
            return new ColorDrawable(context.getResources().getColor(R.color.transparent));
        }
    }

    @Bindable
    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
        notifyPropertyChanged(BR.timeSlots);
    }

    @Bindable
    public List<TimeSlot> getSelectedTimeSlots() {
        return selectedTimeSlots;
    }

    public void setSelectedTimeSlots(List<TimeSlot> selectedTimeSlots) {
        this.selectedTimeSlots = selectedTimeSlots;
        notifyPropertyChanged(BR.selectedTimeSlots);
        notifyPropertyChanged(BR.votedBtnString);
        notifyPropertyChanged(BR.submitBtnString);
    }

    @Bindable
    public String getSubmitBtnString() {
        return String.format(context.getResources().getString(R.string.event_detail_submit_n_vote),
               getSelectedTimeSlots().size(), timeSlots.size());
    }

    @Bindable
    public String getVotedBtnString() {
        return String.format(context.getResources().getString(R.string.event_detail_voted_for_n_timeslots),
                getSelectedTimeSlots().size());
    }

    @Bindable
    public String getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
        notifyPropertyChanged(BR.calendarType);
    }

    @Bindable
    public PhotoViewLayout.ImageClickListener getImageClickListener(){
        return new PhotoViewLayout.ImageClickListener() {
            @Override
            public void onBigImageClick(ImageView view, int position) {
                if(mvpView!=null){
                    mvpView.toBigPhoto(position);
                }
            }

            @Override
            public void onSmallImageClick() {
                if(mvpView!=null){
                    mvpView.gotoGridView();
                }
            }
        };
    }

    public View.OnClickListener onAllNoteClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter.getView()!=null){
                    presenter.getView().showAllNote();
                }
            }
        };
    }

    @Bindable
    public List<PopupMenu.Item> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<PopupMenu.Item> menuItems) {
        this.menuItems = menuItems;
        notifyPropertyChanged(BR.menuItems);
    }

    public void setAvatarList(List<String> avatarList){
        this.avatarList = avatarList;
        notifyPropertyChanged(BR.avatarList);
    }

    @Bindable
    public List<String> getAvatarList(){
        return avatarList;
    }

    @Bindable
    public PopupMenu.OnItemClickListener getOnMenuClickListener() {
        return onMenuClickListener;
    }

    public void setOnMenuClickListener(PopupMenu.OnItemClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
//        notifyPropertyChanged(BR.onMenuClickListener);
    }

    @Bindable
    public int getToolbarCollapseColor() {
        return toolbarCollapseColor;
    }

    public void setToolbarCollapseColor(int toolbarCollapseColor) {
        this.toolbarCollapseColor = toolbarCollapseColor;
        notifyPropertyChanged(BR.toolbarCollapseColor);
    }

    /**
     * Added by Qiushuo Huang
     */


    @Bindable
    public Invitee getHostInvitee(){
        return hostInvitee;
    }

    public void setHostInvitee(Invitee invitee){
        this.hostInvitee = invitee;
        notifyPropertyChanged(BR.hostInvitee);
    }

    @Bindable
    public String getCoverImg(){
        return coverImg;
    }

    public void setCoverImg(String img){
        coverImg = img;
        notifyPropertyChanged(BR.coverImg);
    }

    @Bindable
    public boolean getShowEventDetailTips() {
        return showEventDetailTips;
    }

    public void setShowEventDetailTips(boolean showEventDetailTips) {
        this.showEventDetailTips = showEventDetailTips;
        notifyPropertyChanged(BR.showEventDetailTips);
    }

    /**
     * the following is for binding photos
     */
    private List<PhotoUrl> photoUrls = new ArrayList<>();

    @Bindable
    public List<PhotoUrl> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<PhotoUrl> photoUrls) {
        this.photoUrls = photoUrls;
        notifyPropertyChanged(BR.photoUrls);
    }

    public EventDetailViewModel(EventCreatePresenter<EventDetailMvpView> presenter) {
        this.presenter = presenter;
        this.context = getContext();
//        this.wrapperTimeSlotList = new ArrayList<>();
        alertTimes = AppUtil.getDefaultAlertMins();
        mvpView = presenter.getView();
        init();
    }

    private void init(){
        bottomSheetHeaderDrawable = (LayerDrawable) context.getResources().getDrawable(R.drawable.bg_timeslot_vote_sheet_top);
        Drawable blueDrawable = bottomSheetHeaderDrawable.getDrawable(1);
        bottomSheetHeaderShowAnimator = ObjectAnimator.ofInt(blueDrawable, "alpha", 255, 0).setDuration(ANIMATOR_DURATION);
        bottomSheetHeaderHideAnimator = ObjectAnimator.ofInt(blueDrawable, "alpha", 0, 255).setDuration(ANIMATOR_DURATION);
    }

    public Context getContext() {
        return presenter.getContext();
    }

    public View.OnClickListener onClickViewInCalendar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.viewInCalendar();
                }
            }
        };
    }

//    @Bindable
//    public boolean getIsViewInCalendarClickable() {
//        return isViewInCalendarClickable.get();
//    }
//
//    public void setIsViewInCalendarClickable(boolean isViewInCalendarClickable) {
//        this.isViewInCalendarClickable.set(isViewInCalendarClickable);
//        notifyPropertyChanged(BR.isViewInCalendarClickable);
//    }

    public View.OnClickListener gotoGridView() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView != null) {
                    mvpView.gotoGridView();
                }
            }
        };
    }

    public View.OnClickListener toResponse() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView != null) {
                    mvpView.toResponse();
                }
            }
        };
    }

    /**
     * copy current event, and change its status to pending,
     * in order to create another new event from this template
     * @return
     */
    public View.OnClickListener createEventFromThisTemplate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // create the template for new event
//                Event cpyEvent = EventUtil.copyEvent(event);
//                String eventUid = AppUtil.generateUuid();
//                cpyEvent.setEventUid(eventUid);
//                cpyEvent.setStatus(Event.STATUS_PENDING);
//                for (Invitee invitee : cpyEvent.getInvitee()) {
//                    invitee.setEventUid(eventUid);
//                    invitee.setStatus(Invitee.STATUS_NEEDSACTION); // maybe need to check if it is hostInvitee
//                    String inviteeUid = AppUtil.generateUuid();
//                    invitee.setInviteeUid(inviteeUid);
//                    invitee.setSlotResponses(new ArrayList<SlotResponse>());
//                }
//
//                cpyEvent.setTimeslot(new ArrayList<Timeslot>());
//                cpyEvent.setPhotos(new ArrayList<PhotoUrl>());
//
//                if (mvpView != null) {
//                    mvpView.createEventFromThisTemplate(cpyEvent);
//                }
            }
        };
    }

    public View.OnClickListener onClickUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.openUrl(event.getUrl());
                }
            }
        };
    }


    @Bindable
    public String getAlertString() {
        return alertString;
    }

    public void setAlertString(String alertString) {
        this.alertString = alertString;
        notifyPropertyChanged(BR.alertString);
    }


    /**
     * invitee click left button for unconfirm event
     * @return
     */
    public View.OnClickListener acceptTimeslots() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long firstAcceptTimeslot = 0;
                    HashMap<String, Object> params = new HashMap<>();
                    ArrayList<String> timeslotUids = new ArrayList<>();
                    for(TimeSlot timeSlot:selectedTimeSlots){
                            timeslotUids.add(timeSlot.getTimeslotUid());
                            if (firstAcceptTimeslot == 0) {
                                // this is for recording where to scroll, first accept timeslot
                                firstAcceptTimeslot = timeSlot.getStartTime();
                            }
                        }
                    params.put("timeslots", timeslotUids);
                    presenter.acceptTimeslots(
                            event,
                            params,
                            firstAcceptTimeslot);
            }
        };
    }

//    ***************************************************************

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        setSoloEvent(event.getEventType().equals(Event.TYPE_SOLO));
        if (event.getPhoto() != null){
            setPhotoUrls(event.getPhoto());
        }

        setConfirmed(EventUtil.isConfirmed(event));

        EventUtil.initTimeSlotVoteStatus(event);
        setVoteStatus(event);

        if(EventUtil.isHost(event)){
            setHost(true);
        }else{
            setHost(false);
            //must be called before setTimeSlots
            setSelectedTimeSlots(EventUtil.getMyVoteTimeSlot(event));
            originalSelectedTimeSlots.clear();
            originalSelectedTimeSlots.addAll(getSelectedTimeSlots());
        }

        initCanSeeEachOther();
        generateAvatarList();
        setCalendarType(CalendarUtil.getInstance(context).getCalendarName(event));

        setTimeSlots(new ArrayList<>(event.getTimeslot().values()));
        setTimeSlotBottomSheetButtonVisibilities();
        generateEventTimeString();
        setAlertString(AppUtil.getDefaultAlertStr(event.getReminder()));

        initStatus(event);
        originalStatus = getStatus();
        generateTimeSlotItems();

        setRepeatString(generateRepeatString());
        setUntilDateString(generateUntilString());

        notifyPropertyChanged(BR.submitBtnString);
        notifyPropertyChanged(BR.event);
//        setCalendarType(CalendarUtil.getInstance(context).getCalendarName(event));
    }

    public void initTimeSheet(){
        if(getStatus()==STATUS_NEED_VOTE) {
            setShowTimeSlotSheet(true);
            timeSlotSheet.setInitStatus(ScalableLayout.STATUS_COLLAPSE);
            bottomSheetStatus = ScalableLayout.STATUS_COLLAPSE;
        }else{
            setShowTimeSlotSheet(false);
            timeSlotSheet.setInitStatus(ScalableLayout.STATUS_HIDE);
            bottomSheetStatus = ScalableLayout.STATUS_HIDE;
        }
    }

    private String generateRepeatString(){
        return String.format(getContext().getString(R.string.repeat_prefix),EventUtil.getRepeatString(context, event));
    }

    private String generateUntilString(){
        Date until = event.getRule().getUntil();
        if(until!=null) {
            return EventUtil.getFormatTimeString(until.getTime(), EventUtil.HOUR_MIN_WEEK_DAY_MONTH);
        }else{
            return "";
        }
    }

    public void initCanSeeEachOther(){
        setCanSeeEachOther(isHost()||event.getInviteeVisibility()==Event.CAN_SEE_EACH_OTHER);
    }

    private void generateAvatarList(){
        ArrayList<String> photos = new ArrayList<>();
        for(Invitee invitee:event.getInvitee().values()){
            if(invitee.getInviteeUid().equals(event.getHost())){
                setHostInvitee(invitee);
            }
            photos.add(invitee.getPhoto());
        }
        setAvatarList(photos);
    }

    private void setVoteStatus(Event event){
        goingInvitees.clear();
        noReplyInvitees.clear();
        notGoingInvitees.clear();
        for (Invitee invitee:event.getInvitee().values()) {
            String status = invitee.getStatus();

            switch (status) {
                case Invitee.STATUS_ACCEPTED:
                    goingInvitees.add(invitee);
                    break;
                case Invitee.STATUS_NEEDSACTION:
                    noReplyInvitees.add(invitee);
                    break;
                case Invitee.STATUS_DECLINED:
                    notGoingInvitees.add(invitee);
                    break;
            }
        }
        setRepliedNum(goingInvitees.size());
        setCantGoNum(notGoingInvitees.size());
        setNoReplyNum(noReplyInvitees.size());
        setInviteeNum(event.getInvitee().size());
    }

    public List<Invitee> getGoingInvitees() {
        return goingInvitees;
    }

    public List<Invitee> getNotGoingInvitees() {
        return notGoingInvitees;
    }

    public List<Invitee> getNoReplyInvitees() {
        return noReplyInvitees;
    }

    public int getLocationVisibility(Event event) {
        if (!event.getLocation().equals("")) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public int getNoteVisibility(Event event) {
        if (!event.getNote().isEmpty()) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public View.OnClickListener onMuteClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter!=null){
                    presenter.muteEvent(event, !event.getMute());
                }
                event.setMute(!event.getMute());
                notifyPropertyChanged(BR.event);
            }
        };
    }

    public View.OnClickListener onPinClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter!=null){
                    presenter.pinEvent(event, !event.getPin());
                }
                event.setPinned(!event.getPin());
                notifyPropertyChanged(BR.event);
            }
        };
    }

    public View.OnClickListener onArchiveClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presenter!=null){
                    presenter.archiveEvent(event, !event.getArchive());
                }
                event.setArchived(!event.getArchive());
                notifyPropertyChanged(BR.event);
            }
        };
    }

    public View.OnClickListener onShareClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    public View.OnClickListener onRemindClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.remindEveryone();
                }
            }
        };
    }

    public View.OnClickListener onHistoryClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.toResponse();
                }
            }
        };
    }

    public View.OnClickListener onDuplicateClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.toDuplicate(event);
                }
            }
        };
    }

    public View.OnClickListener onDeleteClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onDelete(event.getRecurrence().length!=0, isHost());
                }
            }
        };
    }

    public View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.onBack();
            }
        };
    }

    public View.OnClickListener onEditEvent(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.gotoEdit();
                }
            }
        };
    }

    public View.OnClickListener onAllInviteesClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.toAllInvitees();
                }
            }
        };
    }

    public View.OnClickListener onAlertTimeClicked(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.showAlerTimeDialog();
                }
            }
        };
    }

    @Bindable
    public String getSheetLeftText() {
        return sheetLeftText;
    }

    public void setSheetLeftText(String sheetLeftText) {
        this.sheetLeftText = sheetLeftText;
        notifyPropertyChanged(BR.sheetLeftText);
    }

    public ScalableLayout getTimeSlotSheet() {
        return timeSlotSheet;
    }

    public void setTimeSlotSheet(ScalableLayout timeSlotSheet) {
        this.timeSlotSheet = timeSlotSheet;
    }

    public void setNodeViews(TextView note, TextView readAll) {
        noteTextView = note;
        readAllTextView = readAll;

        noteTextView.post(new Runnable() {
            @Override
            public void run() {
                Layout l = noteTextView.getLayout();
                readAllTextView.setVisibility(View.GONE);
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            readAllTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }


//        ViewTreeObserver vto = noteTextView.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean on() {
//                int lineCount = noteTextView.getLineCount();
//                if(lineCount>=noteTextView.getMaxLines()){
//                    readAllTextView.setVisibility(View.VISIBLE);
//                }else{
//                    readAll.setVisibility(View.GONE);
//                }
//                return true;
//            }
//        });

    public void showDeleteDialog(){
//        PopupMenu menu = new PopupMenu(presenter.getContext());
//        ArrayList<PopupMenu.Item>menuItem = new ArrayList<>();
//
//        menuItem.add(new PopupMenu.Item(R.drawable.icon_contacts_edit,
//                presenter.getContext().getResources().getString(R.string.profile_edit_alias)));
//
//        if(isAlreadyContact()){
//            menuItem.add(new PopupMenu.Item(R.drawable.icon_contacts_delete,
//                    presenter.getContext().getResources().getString(R.string.profile_delete), R.color.brand_warning));
//        }
//
//        menu.setItems(menuItem);
//
//        onMenuItemClicked = new PopupMenu.OnItemClickListener() {
//            @Override
//            public void onClick(int position, PopupMenu.Item item) {
//                if(mvpView==null){
//                    return;
//                }
//                switch (position){
//                    case 0:
//                        mvpView.goToEditAlias();
//                        break;
//                    case 1:
//                        if(!getBlocked()){
//                            mvpView.showBlockDialog();
//                        } else {
//                            presenter.unblockUser(contact);
//                        }
//                        break;
//                    case 2:
//                        mvpView.showDeleteDialog();
//                        break;
//                }
//            }
//        };
//        menu.setOnItemClickListener(onMenuItemClicked);
    }

    @Bindable
    public String getUntilDateString() {
        return untilDateString;
    }

    public void setUntilDateString(String untilDateString) {
        this.untilDateString = untilDateString;
        notifyPropertyChanged(BR.untilDateString);
    }

    public View.OnClickListener getOnBackgroundClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    if(isHost()) {
                        mvpView.toChangeCover();
                    }
                }
            }
        };
    }

}
