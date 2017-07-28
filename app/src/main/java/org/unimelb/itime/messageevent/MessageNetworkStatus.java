package org.unimelb.itime.messageevent;

/**
 * Created by Qiushuo Huang on 2017/3/12.
 */

public class MessageNetworkStatus {
    public boolean isAvailable;
    public boolean isWifi;
    public boolean isMobile;

    public MessageNetworkStatus(boolean isAvailable, boolean isWifi, boolean isMobile) {
        this.isAvailable = isAvailable;
        this.isWifi = isWifi;
        this.isMobile = isMobile;
    }
}
