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
import org.unimelb.itime.ui.fragment.settings.MainSettingFragment;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */

public class SettingActivity extends ItimeBaseActivity {
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_event_create);
        fragmentManager = getSupportFragmentManager();
        MainSettingFragment fragment = new MainSettingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
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
