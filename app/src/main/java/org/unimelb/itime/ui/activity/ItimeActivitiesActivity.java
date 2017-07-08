package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.ui.fragment.activity.FragmentItimeActivitiesDetail;

/**
 * Created by Paul on 4/7/17.
 */

public class ItimeActivitiesActivity extends ItimeBaseActivity {

    public static final String ACTIVITIES_MEETING = "ACTIVITIES MEETING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itime_activities);


        MessageGroup messageGroup = (MessageGroup) getIntent().getSerializableExtra(ACTIVITIES_MEETING);
        FragmentItimeActivitiesDetail fragment = new FragmentItimeActivitiesDetail();
        if (messageGroup == null){
            messageGroup = mockMessageGroup();
        }
        fragment.setMessageGroup(messageGroup);
        getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    private MessageGroup mockMessageGroup(){
        MessageGroup messageGroup = new MessageGroup();
        messageGroup.setTitle("this is a mock message group");
        messageGroup.setType(MessageGroup.TYPE_EVENT_MESSAGE_GROUP);
        messageGroup.setMute(true);
        return messageGroup;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.itime_activities_fragment_container;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
