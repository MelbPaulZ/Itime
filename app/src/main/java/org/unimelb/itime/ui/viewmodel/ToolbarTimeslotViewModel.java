package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendarTimeslot;
import org.unimelb.itime.util.TimeFactory;

import java.util.ArrayList;
import java.util.List;

import david.itimecalendar.calendar.ui.weekview.TimeSlotView;

/**
 * Created by yuhaoliu on 9/8/17.
 */

public class ToolbarTimeslotViewModel <V extends ToolbarInterface & ToolbarTimeslotViewModel.ToolbarTimeSlotComponents> extends BaseObservable {
    private TimeSlotView timeSlotView;
    @Bindable
    private List<TimeSlot> selectedTimeslot = new ArrayList<>();
    @Bindable
    private TimeSlot confirmedTimeslot = null;
    @Bindable
    private ObservableBoolean isTimeslotEnable = new ObservableBoolean(true);

    @Bindable
    private ObservableBoolean rightBtnClickable = new ObservableBoolean(false);

    private V view;
    private FragmentCalendarTimeslot.Mode mode;

    public ToolbarTimeslotViewModel(V view) {
        this.view = view;
        this.mode = view.getTimeSlotViewMode();
        this.timeSlotView = view.getTimeSlotView();
    }

    public void setSelectedTimeslot(List<TimeSlot> selectedTimeslot) {
        this.selectedTimeslot = selectedTimeslot;
        notifyPropertyChanged(BR.selectedTimeslot);
        setRightBtnClickable(new ObservableBoolean(selectedTimeslot.size() != 0));
    }

    public List<TimeSlot> getSelectedTimeslot() {
        return selectedTimeslot;
    }

    public TimeSlot getConfirmedTimeslot() {
        return confirmedTimeslot;
    }

    public void setConfirmedTimeslot(TimeSlot confirmedTimeslot) {
        this.confirmedTimeslot = confirmedTimeslot;
        notifyPropertyChanged(BR.confirmedTimeslot);
        setRightBtnClickable(new ObservableBoolean(confirmedTimeslot != null));
    }

    public ObservableBoolean getIsTimeslotEnable() {
        return isTimeslotEnable;
    }

    public void setIsTimeslotEnable(ObservableBoolean isTimeslotEnable) {
        this.isTimeslotEnable = isTimeslotEnable;
    }

    public Drawable getTimeslotSwitcherIcon(Context context, boolean isTimeslotEnable){
        Drawable icon;
        if (isTimeslotEnable){
            icon = context.getResources().getDrawable(R.drawable.icon_calendar_showtimeslot);
        }else {
            icon = context.getResources().getDrawable(R.drawable.icon_calendar_hidetimeslot);
        }

        return icon;
    }

    public int getTimeslotSwitcherVisibility(){
        if (this.mode != FragmentCalendarTimeslot.Mode.HOST_CREATE){
            return View.VISIBLE;
        }
        return View.GONE;
    }

    public SpannableString getTitle(Context context, List<TimeSlot> selectedTimeslot, TimeSlot confirmedTimeslot){
        String title = "";

        if (confirmedTimeslot != null){
            return new SpannableString(title);
        }

        int selectedCount = selectedTimeslot.size();
        if (selectedCount == 0){
            return new SpannableString(context.getResources().getString(R.string.toolbar_choose_timeslots));
        }

        if (this.mode == FragmentCalendarTimeslot.Mode.HOST_CREATE
                || this.mode == FragmentCalendarTimeslot.Mode.INVITEE_CONFIRM){
            String timeslotInfo = String.format(
                    context.getString(R.string.toolbar_timeslots_select)
                    , selectedCount
                    , context.getResources().getInteger(R.integer.timeslot_create_max_count));
            return changeMatchColor(context, timeslotInfo, String.valueOf(selectedCount));
        }

        return new SpannableString(title);
    }

    private SpannableString changeMatchColor(Context context, String str, String matchStr){
        SpannableString span = new SpannableString(str);
        if(matchStr.equals("")){
            return span;
        }
        int begin = str.toLowerCase().indexOf(matchStr.toLowerCase());
        int end = begin+matchStr.length();
        if(begin==-1){
            return span;
        }
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.brand_main)),
                begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    public String getConfirmedTimeslotStartTime(TimeSlot confirmedTimeslot){
        if (confirmedTimeslot == null){
            return "";
        }

        return TimeFactory.getFormatTimeString(confirmedTimeslot.getStartTime(),TimeFactory.HOUR_MIN);
    }

    public String getConfirmedTimeslotEndTime(TimeSlot confirmedTimeslot){
        if (confirmedTimeslot == null){
            return "";
        }

        return TimeFactory.getFormatTimeString(confirmedTimeslot.getEndTime(),TimeFactory.HOUR_MIN);
    }

    public String getConfirmedTimeslotDate(Context context, TimeSlot confirmedTimeslot){
        if (confirmedTimeslot == null){
            return "";
        }

        return TimeFactory.getTimeStrings(context,confirmedTimeslot)[1];
    }

    public int getTimeArrowVisibility(TimeSlot confirmedTimeslot){
        return confirmedTimeslot == null ? View.GONE : View.VISIBLE;
    }

    public View.OnClickListener getBackClick(){
        return v -> view.onBack();
    }

    public View.OnClickListener getNextClick(){
        return v -> view.onNext();
    }

    public View.OnClickListener getSwitcherClick(){
        return v -> {
            isTimeslotEnable.set(!isTimeslotEnable.get());
            notifyPropertyChanged(BR.isTimeslotEnable);

            if (isTimeslotEnable.get()){
                timeSlotView.showTimeslot();
            }else {
                timeSlotView.hideTimeslot();
            }
        };
    }

    public String getRightBtnText(Context context) {
        switch (mode){
            case HOST_CREATE:
                return context.getResources().getString(R.string.toolbar_done);
            case HOST_CONFIRM:
                return context.getResources().getString(R.string.toolbar_confirm);
            case INVITEE_CONFIRM:
                return context.getResources().getString(R.string.toolbar_vote);
        }

        return "Error";
    }

    public int getRightBtnTextColor(Context context, boolean clickable) {
        return clickable ?
                context.getResources().getColor(R.color.text_enable)
                : context.getResources().getColor(R.color.text_disable);
    }

    public ObservableBoolean getRightBtnClickable() {
       return rightBtnClickable;
    }

    public void setRightBtnClickable(ObservableBoolean rightBtnClickable) {
        this.rightBtnClickable = rightBtnClickable;
        notifyPropertyChanged(BR.rightBtnClickable);
    }

    public interface ToolbarTimeSlotComponents {
        TimeSlotView getTimeSlotView();
        FragmentCalendarTimeslot.Mode getTimeSlotViewMode();
    }
}
