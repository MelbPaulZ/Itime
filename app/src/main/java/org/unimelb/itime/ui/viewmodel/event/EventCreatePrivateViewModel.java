package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;

import java.util.HashMap;

/**
 * Created by Paul on 15/6/17.
 */

public class EventCreatePrivateViewModel extends EventCreateViewModel {
    public EventCreatePrivateViewModel(EventCreatePresenter presenter, HashMap<String, Integer> orderHashMap) {
        super(presenter);
        setOrderHashMap(orderHashMap);
        init();
    }


    private void init(){
        ButtonItem buttonItem = createButtonItem(getString(R.string.alert_toolbar_btn));
        getButtonItems().add(buttonItem);

    }

    @Override
    protected ButtonItem createButtonItem(String name) {
        if (name.equals(getString(R.string.alert_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_alert), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return super.createButtonItem(name);
    }

    @Override
    protected ButtonItem createButtonItem(String name, Drawable drawable, View.OnClickListener onClickListener) {
        return super.createButtonItem(name, drawable, onClickListener);
    }
}
