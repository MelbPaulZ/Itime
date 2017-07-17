package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.FragmentSearch;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;

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
        SearchPresenter<SearchMvpView> presenter =  new SearchPresenter<>(getBaseContext());
        FragmentSearch fragmentSearch = new FragmentSearch();
        fragmentSearch.setPresenter(presenter);
        fragmentSearch.setScope(SearchPresenter.Scope.MEETING,SearchPresenter.Scope.EVENT,SearchPresenter.Scope.CONTACT);
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
