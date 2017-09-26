package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by Paul on 9/9/17.
 */

public class RecommendRequest implements Serializable {
    private TZoneTime startRecommendTime;
    private TZoneTime endRecommendTime;
    private int duration = -1;
    private Event event;

    public TZoneTime getStartRecommendTime() {
        return startRecommendTime;
    }

    public void setStartRecommendTime(TZoneTime startRecommendTime) {
        this.startRecommendTime = startRecommendTime;
    }

    public TZoneTime getEndRecommendTime() {
        return endRecommendTime;
    }

    public void setEndRecommendTime(TZoneTime endRecommendTime) {
        this.endRecommendTime = endRecommendTime;
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
