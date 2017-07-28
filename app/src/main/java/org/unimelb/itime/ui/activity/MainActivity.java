package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.fragment.EmptyFragment;
import org.unimelb.itime.ui.fragment.activity.FragmentItimeActivities;
import org.unimelb.itime.ui.fragment.contact.MainContactFragment;
import org.unimelb.itime.ui.fragment.meeting.FragmentMeeting;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendar;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ItimeBaseActivity implements MainTabBarView{
    public final static int MODE_MESSAGE = 89;
    public final static int MODE_CONTACT = 90;
    public final static String START_MODE = "start_mode";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MvpFragment[] tagFragments;

    private ActivityMainBinding binding;
    private MainTabBarViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MainTabBarViewModel(this);
        viewModel.setContext(getApplicationContext());
        viewModel.setUnReadNum(0+"");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setTabBarVM(viewModel);
        init();

//        EventBus.getDefault().register(this);
    }

//    @Subscribe
//    public void refreshMeeting(MessageEvent messageEvent){
//        if (messageEvent.task == MessageEvent.RELOAD_MEETING){
//            ((FragmentMeeting)tagFragments[0]).getPresenter().loadDataFromDB();
//        }
//    }

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
        tagFragments[1] = new FragmentItimeActivities();
        tagFragments[2] = new FragmentCalendar();
        tagFragments[3] = new MainContactFragment();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
