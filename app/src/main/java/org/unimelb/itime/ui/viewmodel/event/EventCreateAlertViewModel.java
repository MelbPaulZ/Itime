package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.presenter.LocalPresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 15/6/17.
 */

public class EventCreateAlertViewModel extends BaseObservable {

    private LocalPresenter presenter;
    private List<RepeatLineViewModel> alerts = new ArrayList<>();
    public ItemBinding<RepeatLineViewModel> itemBinding = ItemBinding.of(BR.repeatItem, R.layout.row_repeat);


    public EventCreateAlertViewModel(LocalPresenter presenter) {
        this.presenter = presenter;
        init();
    }


    private void init(){
        for (String alert: getAlertStrings()){
            RepeatLineViewModel repeatLineViewModel = new RepeatLineViewModel(alert, View.GONE,
                    getDrawableById(R.drawable.icon_event_checkmark_blue), true);
            repeatLineViewModel.setOnClickCallBack(new RepeatLineViewModel.OnClickCallBack() {
                @Override
                public void beforeOnClick(RepeatLineViewModel repeatLineViewModel) {
                    if (repeatLineViewModel.getIconVisibility()==View.GONE){
                        // new click, need to reset all first
                        resetAllClick();
                        // then click this current line
                        repeatLineViewModel.setIconVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onClickCustom() {

                }
            });
            alerts.add(repeatLineViewModel);
        }
    }

    public View.OnClickListener onClickNone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllClick();
            }
        };
    }

    public int getNoneTickVisibility(List<RepeatLineViewModel> alerts){
        for (RepeatLineViewModel repeatLineViewModel: alerts){
            if (repeatLineViewModel.getIconVisibility()==View.VISIBLE){
                return View.GONE;
            }
        }
        return View.VISIBLE;
    }

    private void resetAllClick(){
        for (RepeatLineViewModel repeatLineViewModel: alerts){
            repeatLineViewModel.setIconVisibility(View.GONE);
        }
        setAlerts(alerts);
    }

    public void setAlerts(List<RepeatLineViewModel> alerts) {
        this.alerts = alerts;
        notifyPropertyChanged(BR.alerts);
    }

    @Bindable
    public List<RepeatLineViewModel> getAlerts() {
        return alerts;
    }

    private String[] getAlertStrings(){
        return new String[]{
                getStringById(R.string.event_alert_at_time),
                getStringById(R.string.event_alert_5_minutes_before),
                getStringById(R.string.event_alert_15_minutes_before),
                getStringById(R.string.event_alert_30_minutes_before),
                getStringById(R.string.event_alert_1_hour_before),
                getStringById(R.string.event_alert_2_hours_before),
                getStringById(R.string.event_alert_1_day_before),
                getStringById(R.string.event_alert_2_days_before),
                getStringById(R.string.event_alert_1_week_before)};
    }

    private String getStringById(int id){
        return presenter.getContext().getString(id);
    }

    private Drawable getDrawableById(int id){
        return presenter.getContext().getResources().getDrawable(id);
    }
}
