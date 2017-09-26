package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Paul on 30/1/17.
 */

@Entity
public class Region {
    @Id
    private long locationId;
    private String name;
    private int locationType;
    private long parentId;
    private int isVisible;
    private int hasChild; // 0 no child, 1 has child


    @Generated(hash = 875281276)
    public Region(long locationId, String name, int locationType, long parentId,
            int isVisible, int hasChild) {
        this.locationId = locationId;
        this.name = name;
        this.locationType = locationType;
        this.parentId = parentId;
        this.isVisible = isVisible;
        this.hasChild = hasChild;
    }

    @Generated(hash = 600106640)
    public Region() {
    }


    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getHasChild() {
        return hasChild;
    }

    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }
}
