package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Invitee;

/**
 * Created by Paul on 9/9/17.
 */

public class EventCreateBlockInviteeViewModel extends ItimeBaseViewModel{

    private Invitee invitee;

    @Bindable
    public Invitee getInvitee() {
        return invitee;
    }

    public void setInvitee(Invitee invitee) {
        this.invitee = invitee;
        notifyPropertyChanged(BR.invitee);
    }

    public String getBlockInviteeRowString(){
        return "•  " + invitee.getAliasName();
    }
}
