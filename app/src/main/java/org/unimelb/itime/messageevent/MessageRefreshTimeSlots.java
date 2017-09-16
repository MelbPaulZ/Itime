package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.TimeSlot;

import java.util.List;

/**
 * Created by yuhaoliu on 10/9/17.
 */

public class MessageRefreshTimeSlots {
    private List<TimeSlot> rcdTimeslots;
    private String rcdDate;

    public MessageRefreshTimeSlots(List<TimeSlot> rcdTimeslots, String rcdDate) {
        this.rcdDate =rcdDate;
        this.rcdTimeslots = rcdTimeslots;
    }

    public List<TimeSlot> getRcdTimeslots() {
        return rcdTimeslots;
    }

    public String getRcdDate() {
        return rcdDate;
    }

    public void setRcdDate(String rcdDate) {
        this.rcdDate = rcdDate;
    }
}
