package org.unimelb.itime.ui.mvpview.activity;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;

/**
 * Created by Paul on 3/7/17.
 */

public interface ItimeActivitiesMvpView extends ItimeBaseMvpView {
    void onClickViewMore(int messageGroupUid);
    void onDisplayMessages(MessageGroup messageGroup);
    void onReadMessages(Message message);
}
