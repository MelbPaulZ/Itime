package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.contact.MyQRCodeFragment;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeActivity extends ItimeBaseActivity {
    private FragmentManager fragmentManager;
    public static int SCAN_QR_CODE = 1;
    public static String PREVIEW = "preview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_create);
        fragmentManager = getSupportFragmentManager();
        MyQRCodeFragment myQRCodeFragment = new MyQRCodeFragment();
//        fragmentManager.beginTransaction().add(R.id.setting_activity_framelayout, myQRCodeFragment).commit();
        fragmentManager.beginTransaction().replace(getFragmentContainerId(), myQRCodeFragment).commit();
        myQRCodeFragment.setPreview(getIntent().getIntExtra(MyQRCodeFragment.PREVIEW,0));
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frag_container_event_create;
    }
}
