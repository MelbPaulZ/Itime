package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.ui.fragment.contact.AddFriendsFragment;
import org.unimelb.itime.ui.fragment.contact.FriendRequestFragment;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;

import static android.databinding.DataBindingUtil.setContentView;

/**
 * Created by Qiushuo Huang on 2017/8/18.
 */

public class NewFriendActivity extends ItimeBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        FriendRequestFragment fragment = new FriendRequestFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frag_container_event_create;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}