package org.unimelb.itime.ui.viewmodel.event;

import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.event.EventRepeatCustomMvpView;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;

/**
 * Created by Paul on 5/6/17.
 */

public class EventRepeatCustomViewModel extends BaseObservable {
    private LocalPresenter presenter;

    private String frequencyString;
    private String gapString;
    private Event event;
    private ObservableBoolean showWheelPicker;
    private EventRepeatCustomMvpView mvpView;

    public EventRepeatCustomViewModel(LocalPresenter<EventRepeatCustomMvpView> presenter) {
        this.presenter = presenter;
        this.mvpView = presenter.getView();

    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public ObservableBoolean getShowWheelPicker() {
        return showWheelPicker;
    }

    public void setShowWheelPicker(ObservableBoolean showWheelPicker) {
        this.showWheelPicker = showWheelPicker;
        notifyPropertyChanged(BR.showWheelPicker);
    }

    @Bindable
    public String getFrequencyString() {
        return frequencyString;
    }

    public void setFrequencyString(String frequencyString) {
        this.frequencyString = frequencyString;
        notifyPropertyChanged(BR.frequencyString);
    }

    @Bindable
    public String getGapString() {
        return gapString;
    }

    public void setGapString(String gapString) {
        this.gapString = gapString;
        notifyPropertyChanged(BR.gapString);
    }

    public void onClickFrequency(){
        if (showWheelPicker==null){
            showWheelPicker = new ObservableBoolean(true); // default show this wheelpicker
        }
        showWheelPicker.set(!getShowWheelPicker().get());
        setShowWheelPicker(showWheelPicker);
    }

    @BindingAdapter({"android:isShow"})
    public static void setOnHideShowAnimation(final View v, final ObservableBoolean isShow){
        moveDisY = v.getHeight();
        showAnimation(v, isShow, v.getHeight());
    }

    private static void showAnimation(View v, ObservableBoolean isShow, int distance){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, distance);
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new DecelerateInterpolator());

        valueAnimator.addUpdateListener(animation -> {
            if (isShow==null){
                return;
            }
            v.setTranslationY(isShow.get() ? -distance + (Integer) animation.getAnimatedValue() : - (Integer) animation.getAnimatedValue());
        });
        valueAnimator.start();
    }

    private static int moveDisY = 0;

    @BindingAdapter({"android:followShow"})
    public static void setOnFollowAnimation(final View v, final ObservableBoolean isShow){
        showAnimation(v, isShow, moveDisY);
    }

    public String getEndRepeatString(Context context, Event event){
        if (event.getRule().getUntil() == null){
            return context.getString(R.string.event_repeat_never);
        }else{
            Calendar c = Calendar.getInstance();
            c.setTime(event.getRule().getUntil());

            Calendar cYear = Calendar.getInstance();
            if (c.get(Calendar.YEAR) == cYear.get(Calendar.YEAR)){
                return EventUtil.getFormatTimeString(c.getTimeInMillis(), EventUtil.WEEK_DAY_MONTH);
            }else{
                return EventUtil.getFormatTimeString(c.getTimeInMillis(), EventUtil.DAY_MONTH_YEAR);
            }
        }
    }

    public View.OnClickListener onClickEndRepeat(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toEndRepeat(event);
                }
            }
        };
    }

}
