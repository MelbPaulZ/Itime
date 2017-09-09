package org.unimelb.itime.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.fragment.login.LoginMainFragment;

/**
 * Created by Paul on 29/8/17.
 */

public class ItimeLoginActivity extends ItimeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itime_login);

        if (!isServiceRunning(RemoteService.class)) {
            Toast.makeText(this, "RemoteService Start", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RemoteService.class);
            startService(intent);
        }

        LoginMainFragment fragment = new LoginMainFragment();
        getSupportFragmentManager().beginTransaction().add( getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_activity_container;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
