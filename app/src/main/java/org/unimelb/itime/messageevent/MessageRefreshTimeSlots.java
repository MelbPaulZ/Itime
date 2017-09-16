package org.unimelb.itime.messageevent;

import org.unimelb.itime.bean.TimeSlot;

import java.util.List;

/**
 * Created by yuhaoliu on 10/9/17.
 */

public class MessageRefreshTimeSlots {
    List<TimeSlot> rcdTimeslots;

    public MessageRefreshTimeSlots(List<TimeSlot> rcdTimeslots) {
        this.rcdTimeslots = rcdTimeslots;
    }

    public List<TimeSlot> getRcdTimeslots() {
        return rcdTimeslots;
    }
}
