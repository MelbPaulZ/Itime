package org.unimelb.itime.ui.fragment.meeting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.databinding.FragmentMeetingBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.SearchActivity;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentMeeting extends ItimeBaseFragment<CalendarMvpView, CalendarPresenter<CalendarMvpView>> implements ToolbarInterface {
    private FragmentMeetingBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meeting, container, false);

        binding.getRoot().findViewById(R.id.search_bar).setOnClickListener(onSearchClick());

        final TabLayout tabLayout = (TabLayout) binding.getRoot().findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.meeting_tag_invitation));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.meeting_tag_hosting));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.meeting_tag_coming));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CusSwipeViewPager viewPager = (CusSwipeViewPager) binding.getRoot().findViewById(R.id.pager);
        final PagerAdapterMeeting adapter = new PagerAdapterMeeting
                (getFragmentManager(), tabLayout.getTabCount());
        adapter.setmData(initFragments());
        //must be 2, otherwise get performance issue
        viewPager.setOffscreenPageLimit(2);
        viewPager.setSwipeEnable(false);
        viewPager.setSwipingDuration(250);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public CalendarPresenter<CalendarMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

    }

    public View.OnClickListener onSearchClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To search activity
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        };
    }

    private List<Fragment> initFragments(){
        List<Fragment> mData = new ArrayList<>();

        mData.add(new FragmentInvitation());
        mData.add(new FragmentHosting());
        mData.add(new FragmentComing());

        return mData;
    }

}
