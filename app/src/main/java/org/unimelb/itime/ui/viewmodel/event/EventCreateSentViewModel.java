package org.unimelb.itime.ui.viewmodel.event;

import android.widget.Toast;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.presenter.event.EventCreateSentPrensenter;

/**
 * Created by Paul on 7/9/17.
 */

public class EventCreateSentViewModel extends ItimeBaseViewModel {
    private EventCreateSentPrensenter prensenter;

    public EventCreateSentViewModel(EventCreateSentPrensenter prensenter) {
        this.prensenter = prensenter;
    }

    public void onClickDone(){
//        Toast.makeText(prensenter.getContext(), "Done", Toast.LENGTH_SHORT).show();
    }
}
