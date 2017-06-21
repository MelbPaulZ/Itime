package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.fragment.EmptyFragment;
import org.unimelb.itime.ui.fragment.calendar.FragmentCalendar;
import org.unimelb.itime.ui.fragment.event.FragmentEventCalendar;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateDuration;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreateTitle;
import org.unimelb.itime.ui.fragment.event.FragmentEventRepeat;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;

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
        tagFragments[0] = new FragmentCalendar();
        tagFragments[1] = new EmptyFragment();
        tagFragments[2] = new EmptyFragment();
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


}
