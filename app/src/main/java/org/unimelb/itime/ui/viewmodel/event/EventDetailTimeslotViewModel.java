package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.TimeSlot;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/18.
 */

public class EventDetailTimeslotViewModel extends BaseObservable {
    Drawable icon;
    String firstTimeRow = "";
    String secondTimeRow = "";
    List<String> inviteePhotos;
    TimeSlot timeSlot;

    @Bindable
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        notifyPropertyChanged(BR.timeSlot);
    }

    @Bindable
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
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
