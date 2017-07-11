package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.FragmentSearch;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchActivity extends ItimeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init(){
        FragmentSearch fragmentSearch = new FragmentSearch();
        getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragmentSearch).commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.search_container;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
