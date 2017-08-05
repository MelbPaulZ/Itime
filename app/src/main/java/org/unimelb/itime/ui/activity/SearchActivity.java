package org.unimelb.itime.ui.activity;

import android.graphics.ImageFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.FragmentMeetingSearch;
import org.unimelb.itime.ui.fragment.calendar.FragmentEventSearch;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.mvpview.calendar.SearchEventMvpView;
import org.unimelb.itime.ui.presenter.SearchEventPresenter;
import org.unimelb.itime.ui.presenter.SearchPresenter;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchActivity extends ItimeBaseActivity {
    public static final int MULTIPLE_SEARCH = 0;
    public static final int CALENDAR_SEARCH = 1;
    public static final String TASK = "TASK";
    private int task = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        task = getIntent().getExtras().getInt(TASK);

        if (task == -1){
            return;
        }

        if (task == MULTIPLE_SEARCH){
            initMeetingSearch();
            return;
        }

        if (task == CALENDAR_SEARCH){
            initCalendarEventSearch();
        }
    }


    private void initMeetingSearch(){
        SearchPresenter<SearchMvpView> presenter =  new SearchPresenter<>(getBaseContext());
        FragmentMeetingSearch fragmentSearch = new FragmentMeetingSearch();
        fragmentSearch.setPresenter(presenter);
        fragmentSearch.setScope(SearchPresenter.Scope.MEETING,SearchPresenter.Scope.EVENT,SearchPresenter.Scope.CONTACT);
        getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragmentSearch).commit();
    }

    private void initCalendarEventSearch(){
        SearchEventPresenter<SearchEventMvpView> presenter =  new SearchEventPresenter<>(getBaseContext());
        FragmentEventSearch fragmentSearch = new FragmentEventSearch();
        fragmentSearch.setPresenter(presenter);
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
