package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class ProfileActivity extends ItimeBaseActivity {
    private FragmentManager fragmentManager;
    public static String USER_ID = "user";
    public static String USER_UID = "user_uid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_event_create);
        fragmentManager = getSupportFragmentManager();
        ProfileFragment fragment = new ProfileFragment();
//        String userId = getIntent().getStringExtra(USER_ID);
//        if(userId!=null) {
//            fragment.setUserId(userId);
//        }
        String userUid = getIntent().getStringExtra(USER_UID);
        if(userUid!=null) {
            fragment.setUserUid(userUid);
        }
        fragment.setStartMode(ProfileFragment.MODE_CONTACT);
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.frag_container_event_create;
    }
}
