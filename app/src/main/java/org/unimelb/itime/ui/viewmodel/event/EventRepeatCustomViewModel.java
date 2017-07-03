package org.unimelb.itime.ui.viewmodel.event;

import android.animation.ValueAnimator;
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

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.presenter.LocalPresenter;

/**
 * Created by Paul on 5/6/17.
 */

public class EventRepeatCustomViewModel extends BaseObservable {
    private LocalPresenter presenter;

    private String frequencyString;
    private String gapString;
    private Event event;
    private ObservableBoolean showWheelPicker;

    public EventRepeatCustomViewModel(LocalPresenter presenter) {
        this.presenter = presenter;
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
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, v.getHeight());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isShow==null){
                    return;
                }
                v.setTranslationY(isShow.get() ? -v.getHeight() + (Integer) animation.getAnimatedValue() : - (Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}
