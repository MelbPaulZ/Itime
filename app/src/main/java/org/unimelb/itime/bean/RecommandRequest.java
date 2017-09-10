package org.unimelb.itime.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Paul on 9/9/17.
 */

public class RecommandRequest implements Serializable {
    private TZoneTime startRecommandTime;
    private TZoneTime endRecommandTime;
    private int duration = -1;
    private Event event;

    public TZoneTime getStartRecommandTime() {
        return startRecommandTime;
    }

    public void setStartRecommandTime(TZoneTime startRecommandTime) {
        this.startRecommandTime = startRecommandTime;
    }

    public TZoneTime getEndRecommandTime() {
        return endRecommandTime;
    }

    public void setEndRecommandTime(TZoneTime endRecommandTime) {
        this.endRecommandTime = endRecommandTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
