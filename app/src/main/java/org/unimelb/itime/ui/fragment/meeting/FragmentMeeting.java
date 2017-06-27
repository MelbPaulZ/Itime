package org.unimelb.itime.ui.fragment.meeting;

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
    private EventManager eventManager;
    private FragmentMeetingBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meeting, container, false);
        eventManager = EventManager.getInstance(getContext());
        TabLayout tabLayout = (TabLayout) binding.getRoot().findViewById(R.id.tab_layout);
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
        viewPager.setSwipingDuration(300);
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


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private List<Fragment> initFragments(){
        List<Fragment> mData = new ArrayList<>();
        mData.add(new FragmentInvitation());
        mData.add(new FragmentHosting());
        mData.add(new FragmentComing());

        return mData;
    }

    private void initData(){
        List<Meeting> meetings = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        List<Event> events = new ArrayList<>();
        int[] type = {0, 1, 2};
        int[] status = {0, 1};
        long allDayInterval = (24 * 3600 * 1000);
        long interval = (3600 * 1000);
        long startTime = calendar.getTimeInMillis();
        long endTime;
        for (int i = 1; i < 20; i++) {
            Meeting meeting = new Meeting();
            endTime = startTime + interval;
            Event event = EventUtil.getNewEvent();
            event.setIsAllDay(i%2 == 0);
            event.setDisplayEventType(1);
            event.setDisplayStatus("#63ADF2|slash|icon_normal");
            event.setLocation(new Location());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            events.add(event);

            startTime = startTime + allDayInterval;
            meeting.setEvent(event);
            meetings.add(meeting);
        }
    }
}
