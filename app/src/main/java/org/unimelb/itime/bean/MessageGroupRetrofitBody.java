package org.unimelb.itime.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 11/8/17.
 */

public class MessageGroupRetrofitBody {
    private List<Integer> messageGroupUids = new ArrayList<>();
    private int isRead = 0;

    public List<Integer> getMessageGroupUids() {
        return messageGroupUids;
    }

    public void setMessageGroupUids(List<Integer> messageGroupUids) {
        this.messageGroupUids = messageGroupUids;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
