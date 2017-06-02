package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.mvpview.EventRepeatMvpView;
import org.unimelb.itime.ui.presenter.EventRepeatPresenter;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 2/6/17.
 */

public class EventRepeatViewModel extends BaseObservable {
    private EventRepeatPresenter<EventRepeatMvpView> presenter;
    public ObservableList<RepeatLineViewModel> items = new ObservableArrayList<>();
    public ItemBinding<RepeatLineViewModel> itemBinding = ItemBinding.of(BR.repeatItem, R.layout.row_repeat);

    public EventRepeatViewModel(EventRepeatPresenter<EventRepeatMvpView> presenter) {
        this.presenter = presenter;
        init();
    }

    private void init(){
        items.add(new RepeatLineViewModel(getString(R.string.event_no_repeat),
                View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue),true));
        items.add(new RepeatLineViewModel(getString(R.string.event_repeat_everyday),
                View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue),true));
        items.add(new RepeatLineViewModel(getString(R.string.event_repeat_every_week),
                View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue),true));
        items.add(new RepeatLineViewModel(getString(R.string.event_repeat_every_two_weeks),
                View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue),true));
        items.add(new RepeatLineViewModel(getString(R.string.event_repeat_every_month),
                View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue),true));
        items.add(new RepeatLineViewModel(getString(R.string.event_repeat_every_year),
                View.GONE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_checkmark_blue),true));
        items.add(new RepeatLineViewModel(getString(R.string.event_repeat_custom),
                View.VISIBLE, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_disclosure), false));

        for (RepeatLineViewModel lineVm : items){
            lineVm.setOnClickCallBack(new RepeatLineViewModel.OnClickCallBack() {
                @Override
                public void beforeOnClick(RepeatLineViewModel repeatLineViewModel) {
                    if (repeatLineViewModel.getIconVisibility() == View.VISIBLE){
                        repeatLineViewModel.setIconVisibility(View.GONE);
                    }else{
                        // new click, need to reset all first
                        resetAllClick();
                        // then click this current line
                        repeatLineViewModel.setIconVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    /**
     * This method will reset all ticks, except the last custom one.
     */
    private void resetAllClick(){
        for (int i = 0; i < items.size()-1; i++){
            items.get(i).setIconVisibility(View.GONE);
        }
    }



    private String getString(int stringId){
        return presenter.getContext().getString(stringId);
    }

}
