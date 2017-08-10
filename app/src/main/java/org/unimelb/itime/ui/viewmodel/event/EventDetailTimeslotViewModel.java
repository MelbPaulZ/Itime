package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.util.TimeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/18.
 */

public class EventDetailTimeslotViewModel extends BaseObservable {
    private Drawable leftIcon;
    private String firstTimeRow = "";
    private String secondTimeRow = "";
    private List<String> inviteePhotos;
    private TimeSlot timeSlot;
    private String inviteeCount;
    private boolean selected;
    private boolean outdated;
    private boolean conflict;
    private Context context;

    public EventDetailTimeslotViewModel(Context context){
        this.context = context;

    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        generateIcon();
        notifyPropertyChanged(BR.selected);
    }

    @Bindable
    public boolean isOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
        generateIcon();
        notifyPropertyChanged(BR.outdated);

    }

    @Bindable
    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
        generateIcon();
        notifyPropertyChanged(BR.conflict);

    }

    @Bindable
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        String[] time = TimeFactory.getTimeStrings(context, timeSlot);
        setFirstTimeRow(time[0]);
        setSecondTimeRow(time[1]);
        inviteePhotos = new ArrayList<>();
        for(Invitee invitee : timeSlot.getVoteInvitees()){
            inviteePhotos.add(invitee.getAliasPhoto());
        }
        setInviteePhotos(inviteePhotos);
        generateIcon();
        notifyPropertyChanged(BR.timeSlot);
    }

    @Bindable
    public Drawable getLeftIcon() {
        return leftIcon;
    }

    private void generateIcon(){

        if(selected){
            leftIcon = context.getResources().getDrawable(R.drawable.icon_details_check_selected);
        }else{
            leftIcon = context.getResources().getDrawable(R.drawable.icon_details_check_unselected);

            if(conflict){
                leftIcon = context.getResources().getDrawable(R.drawable.icon_details_check_conflicted);
            }

            if(outdated){
                leftIcon = context.getResources().getDrawable(R.drawable.icon_details_check_outdated);
            }
        }
        notifyPropertyChanged(BR.leftIcon);
    }

    @Bindable
    public String getFirstTimeRow() {
        return firstTimeRow;
    }

    public void setFirstTimeRow(String firstTimeRow) {
        this.firstTimeRow = firstTimeRow;
        notifyPropertyChanged(BR.firstTimeRow);
    }

    @Bindable
    public String getSecondTimeRow() {
        return secondTimeRow;
    }

    public void setSecondTimeRow(String secondTimeRow) {
        this.secondTimeRow = secondTimeRow;
        notifyPropertyChanged(BR.secondTimeRow);
    }

    @Bindable
    public List<String> getInviteePhotos() {
        return inviteePhotos;
    }

    public void setInviteePhotos(List<String> inviteePhotos) {
        this.inviteePhotos = inviteePhotos;
        notifyPropertyChanged(BR.inviteePhotos);
    }
}
