package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.event.EventCreateSentMvpView;
import org.unimelb.itime.ui.presenter.event.EventCreateSentPrensenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 7/9/17.
 */

public class EventCreateSentViewModel extends ItimeBaseViewModel {
    private EventCreateSentPrensenter<EventCreateSentMvpView> prensenter;
    private Event event;
    private boolean isSuccessful = true;
    private int contentVisibility = View.GONE;
    private List<EventCreateBlockInviteeViewModel> blockInvitees = new ArrayList<>();
    private ItemBinding<EventCreateBlockInviteeViewModel> onItemBind = ItemBinding.of(BR.blockInviteeVM, R.layout.row_block_invitee);


    public EventCreateSentViewModel(EventCreateSentPrensenter<EventCreateSentMvpView> prensenter) {
        this.prensenter = prensenter;
    }

    public void setEvent(Event event) {
        isSuccessful = true;
        contentVisibility = View.GONE;
        this.event = event;
        for(Invitee invitee: event.getInvitee().values()){
//            todo
            isSuccessful = false;
            contentVisibility = View.VISIBLE;
        }
    }

    @Bindable
    public int getContentVisibility() {
        return contentVisibility;
    }

    public void onClickDone(){
        if (prensenter.getView()!=null) {
            prensenter.getView().onClickDone();
        }
    }
}
