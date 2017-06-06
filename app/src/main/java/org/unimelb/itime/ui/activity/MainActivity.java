package org.unimelb.itime.ui.activity;

import android.support.annotation.NonNull;
import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;

public class MainActivity extends ItimeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FragmentEventCreateNote fragment = new FragmentEventCreateNote();
//        FragmentEventCreateUrl fragment = new FragmentEventCreateUrl();
//        FragmentEventRepeatCustom fragment = new FragmentEventRepeatCustom();
//        FragmentEventEndRepeat fragment = new FragmentEventEndRepeat();
//        FragmentEventRepeat fragment = new FragmentEventRepeat();
//        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, fragment).commit();
        FragmentEventCreate fragmentEventCreate = new FragmentEventCreate();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,fragmentEventCreate, FragmentEventCreate.class.getSimpleName()).commit();
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frag_container;
    }
}
