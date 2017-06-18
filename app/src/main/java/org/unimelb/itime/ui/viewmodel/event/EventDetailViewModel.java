package org.unimelb.itime.ui.viewmodel.event;


import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.mvpview.event.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.event.EventDetailPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.widget.PhotoViewLayout;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailViewModel extends BaseObservable{
    private EventDetailPresenter<EventDetailMvpView> presenter;
    private Event event;
    private EventDetailMvpView mvpView;
    private Context context;
    private boolean showEventDetailTips = false;
    private boolean showTips = false;
    private int[] alertTimes;
    private String alertString = null;
    private Invitee host;
    private String coverImg="";
    private int toolbarCollapseColor=0;
    private PopupMenu.OnItemClickListener onMenuClickListener;
    private List<PopupMenu.Item> menuItems;
    private List<String> avatarList = new ArrayList<>();
    private String calendarType = "";

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
                Toast.makeText(context, photoUrls.get(position).getUrl(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSmallImageClick() {
                if(presenter.getView()!=null) {
                    presenter.getView().gotoGridView();
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
    public Invitee getHost(){
        return host;
    }

    public void setHost(Invitee invitee){
        this.host = invitee;
        notifyPropertyChanged(BR.host);
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

    public EventDetailViewModel(EventDetailPresenter<EventDetailMvpView> presenter) {
        this.presenter = presenter;
        this.context = getContext();
//        this.wrapperTimeSlotList = new ArrayList<>();
        alertTimes = AppUtil.getDefaultAlertMins();
        mvpView = presenter.getView();
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
//                    invitee.setStatus(Invitee.STATUS_NEEDSACTION); // maybe need to check if it is host
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
     * confirm event click left button to accept event
     * @return
     */
    public View.OnClickListener acceptEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Event orgEvent = EventManager.getInstance(context).getCurrentEvent();
//                presenter.acceptEvent(event.getCalendarUid(),
//                            event.getEventUid(),
//                            EventPresenter.UPDATE_ALL,
//                            orgEvent.getStartTime());
            }
        };
    }

    /**
     * invitee click left button for unconfirm event
     * @return
     */
    public View.OnClickListener acceptTimeslots() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                long firstAcceptTimeslot = 0;
//                    HashMap<String, Object> params = new HashMap<>();
//                    ArrayList<String> timeslotUids = new ArrayList<>();
//                    for (SubTimeslotViewModel viewModel : wrapperTimeSlotList) {
//                        if (viewModel.getWrapper().isSelected()) {
//                            TimeSlot timeslot = (TimeSlot) viewModel.getWrapper().getTimeSlot();
//                            timeslotUids.add(timeslot.getTimeslotUid());
//                            if (firstAcceptTimeslot == 0) {
//                                // this is for recording where to scroll, first accept timeslot
//                                firstAcceptTimeslot = timeslot.getStartTime();
//                            }
//                        }
//                    }
//                    params.put("timeslots", timeslotUids);
//                    presenter.acceptTimeslots(
//                            event,
//                            params,
//                            firstAcceptTimeslot);
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
        setPhotoUrls(event.getPhotos());

        ArrayList<String> photos = new ArrayList<>();
        for(Invitee invitee:event.getInvitees()){
            if(invitee.getIsHost()==1){
                setHost(invitee);
            }
            photos.add(invitee.getPhoto());
        }
        setAvatarList(photos);
//        setCalendarType(CalendarUtil.getInstance(context).getCalendarName(event));
    }

    //***********************************************************
//    public int confirmVisibility(Event event) {
//
//        if (event.getEventType().equals(Event.TYPE_SOLO)) {
//            return View.GONE;
//        }
//
//        if(event.getDeleteLevel()>0){
//            return View.GONE;
//        }
//
//        if (event.getConfirmedCount()>0) {
//            return View.VISIBLE;
//        } else {
//            return View.GONE;
//        }
//    }

//    /**
//     *
//     * @param event
//     * @return View.Visible if this is invitee and event is unconfirm
//     */
//    public int unconfirmVisibility(Event event) {
//        if (event.getDeleteLevel()>0){
//            return View.GONE;
//        }
//
//        if (event.getConfirmedCount()>0){
//            return View.GONE;
//        }
//
//        if (isUserHostOfEvent(context, event)){
//            return View.GONE;
//        }
//
//        return View.VISIBLE;
//    }

//    public int unconfirmHostVisibility(Event event) {
//
//        if (event.getDeleteLevel()>0){
//            return View.GONE;
//        }
//        if (event.getConfirmedCount()==0 && isUserHostOfEvent(context, event)){
//            return View.VISIBLE;
//        }
//
//        return View.GONE;
//    }

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

    //***********************************************************


//    /**
//     * if solo event or not allow invitee to see each other, then return View.GONE else return View.VISIBLE
//     * @return
//     */
//    @Bindable
//    public int getInviteeVisibility() {
//        if (!EventUtil.isGroupEvent(context, event)){
//            return View.GONE;
//        }
//
//        if (event.getDeleteLevel()>0){
//            return View.GONE;
//        }
//
//        if (event.getInviteeVisibility()==0 && !EventUtil.isUserHostOfEvent(context, event)){
//            return View.GONE;
//        }
//        return View.VISIBLE;
//    }

//    @Bindable
//    public ItemView getTimeslotItemView() {
//        return timeslotItemView;
//    }
//
//    public void setTimeslotItemView(ItemView timeslotItemView) {
//        this.timeslotItemView = timeslotItemView;
//        notifyPropertyChanged(BR.timeslotItemView);
//    }
}
