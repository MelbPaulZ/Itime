package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.fragment.EmptyFragment;
import org.unimelb.itime.ui.fragment.meeting.FragmentMeeting;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendar;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ItimeBaseActivity implements MainTabBarView{
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MvpFragment[] tagFragments;
    private EventManager eventManager;

    private ActivityMainBinding binding;
    private MainTabBarViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainTabBarViewModel(this);
        viewModel.setContext(getApplicationContext());
        viewModel.setUnReadNum(0+"");

        eventManager = EventManager.getInstance(getApplicationContext());
        /**
         * for testing
         */
        initData();
        // end of testing
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setTabBarVM(viewModel);
        init();
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

    private void init(){
        tagFragments = new MvpFragment[4];
        tagFragments[0] = new FragmentMeeting();
        tagFragments[1] = new EmptyFragment();
        tagFragments[2] = new FragmentCalendar();
        tagFragments[3] = new EmptyFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_container, tagFragments[0]);
        fragmentTransaction.add(R.id.frag_container, tagFragments[1]);
        fragmentTransaction.add(R.id.frag_container, tagFragments[2]);
        fragmentTransaction.add(R.id.frag_container, tagFragments[3]);

        fragmentTransaction.commit();
        showFragmentById(0);
    }

    @Override
    public void showFragmentById(int pageId) {
        fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < tagFragments.length; i++){
            if (pageId == i){
                fragmentTransaction.show(tagFragments[i]);
            }else{
                fragmentTransaction.hide(tagFragments[i]);
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void gotoCreateMeeting() {
        Intent intent = new Intent(this, EventCreateActivity.class);
        intent.putExtra(getString(R.string.event_type), getString(R.string.event_type_group));
        startActivity(intent);
    }

    @Override
    public void gotoCreateEvent() {
        Intent intent = new Intent(this, EventCreateActivity.class);
        intent.putExtra(getString(R.string.event_type), getString(R.string.event_type_solo));
        startActivity(intent);
    }

    /**
     * For TESTING
     */
    private void initData() {
        Calendar calendar = Calendar.getInstance();
        List<Event> events = new ArrayList<>();
        int[] type = {0, 1, 2};
        int[] status = {0, 1};
        long allDayInterval = (24 * 3600 * 1000);
        long interval = (3600 * 1000);
        long startTime = calendar.getTimeInMillis();
        long endTime;
        for (int i = 1; i < 20; i++) {
            endTime = startTime + interval;
            Event event = EventUtil.getNewEvent();
            event.setIsAllDay(i%2);
            event.setLocation(new Location());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            events.add(event);

            startTime = startTime + allDayInterval;
            EventManager.getInstance(getApplicationContext()).addEvent(event);
        }
    }
}
