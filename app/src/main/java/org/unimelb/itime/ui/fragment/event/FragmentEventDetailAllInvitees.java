package org.unimelb.itime.ui.fragment.event;

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
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentCreateEventAddInviteeBinding;
import org.unimelb.itime.databinding.FragmentEventDetailAllInviteeBinding;
import org.unimelb.itime.ui.fragment.meeting.CusSwipeViewPager;
import org.unimelb.itime.ui.fragment.meeting.FragmentComing;
import org.unimelb.itime.ui.fragment.meeting.FragmentHosting;
import org.unimelb.itime.ui.fragment.meeting.FragmentInvitation;
import org.unimelb.itime.ui.fragment.meeting.PagerAdapterMeeting;
import org.unimelb.itime.ui.mvpview.contact.EventDetailAllInviteeMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddInviteeMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateAddInviteeViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventDetailAllInviteesViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/8/10.
 */

public class FragmentEventDetailAllInvitees  extends ItimeBaseFragment<EventDetailAllInviteeMvpView, EventCreatePresenter<EventDetailAllInviteeMvpView>>
        implements EventCreateAddInviteeMvpView, ToolbarInterface {

    public static final int MODE_EVENT = 1;
    public static final int MODE_TIMESLOT = 2;
    private FragmentEventDetailAllInviteeBinding binding;
    private FragmentEventCreateAddInvitee fragmentEventCreateAddInvitee;
    private Event event;
    private TimeSlot timeSlot;
    private EventDetailAllInviteesViewModel contentVM;
    private ToolbarViewModel toolbarVM;
    private PagerAdapterMeeting adapter;
    private FragmentEventDetailInviteeList goingFragment = new FragmentEventDetailInviteeList();
    private FragmentEventDetailInviteeList notGoingFragment = new FragmentEventDetailInviteeList();
    private FragmentEventDetailInviteeList noReplyFragment = new FragmentEventDetailInviteeList();
    private TabLayout tabLayout;
    private final int TAB_COUNT = 3;
    private int startMode = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(binding!=null){
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_all_invitee, container, false);
        tabLayout = (TabLayout) binding.getRoot().findViewById(R.id.tab_layout);
        final CusSwipeViewPager viewPager = (CusSwipeViewPager) binding.getRoot().findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapterMeeting
                (getFragmentManager(), TAB_COUNT);
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

    public int getStartMode() {
        return startMode;
    }

    public void setStartMode(int startMode) {
        this.startMode = startMode;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(toolbarVM==null) {
            toolbarVM = new ToolbarViewModel(this);
            toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
//            toolbarVM.setRightIcon(getResources().getDrawable(R.drawable.icon_contacts_edit));
//            toolbarVM.setRightEnable(true);
        }

        if(contentVM==null) {
            contentVM = new EventDetailAllInviteesViewModel();
            contentVM.setContext(getContext());
        }
        binding.setContentVM(contentVM);
        binding.setToolbarVM(toolbarVM);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setmData(initFragments());
        adapter.notifyDataSetChanged();
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(
                String.format(getString(R.string.event_detail_all_invitee_tag_going), contentVM.getRepliedNum())));
        tabLayout.addTab(tabLayout.newTab().setText(
                String.format(getString(R.string.event_detail_all_invitee_tag_notgoing), contentVM.getCantGoNum())));
        tabLayout.addTab(tabLayout.newTab().setText(
                String.format(getString(R.string.event_detail_all_invitee_tag_noreply), contentVM.getNoReplyNum())));
    }

    private List<Fragment> initFragments(){
        if(startMode==MODE_EVENT && event!=null){
            contentVM.generateByEvent(event);
            toolbarVM.setTitle(
                    String.format(getString(R.string.event_detail_all_invitee_title), contentVM.getInviteeNum()));
        }else if(startMode==MODE_TIMESLOT && event!=null && timeSlot!=null){
            contentVM.generateByTimeSlot(event, timeSlot);
            toolbarVM.setTitle(
                    String.format(getString(R.string.event_detail_all_invitee_title), contentVM.getRepliedNum()));
        }



        goingFragment.setInvitees(contentVM.getGoingInvitees());
        notGoingFragment.setInvitees(contentVM.getNotGoingInvitees());
        noReplyFragment.setInvitees(contentVM.getNoReplyInvitees());

        List<Fragment> mData = new ArrayList<>();
        mData.add(goingFragment);
        mData.add(notGoingFragment);
        mData.add(noReplyFragment);
        return mData;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public EventCreatePresenter<EventDetailAllInviteeMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Override
    public void onNext() {
        FragmentEventCreate fragmentEventCreate = new FragmentEventCreate();
        fragmentEventCreate.setEvent(contentVM.getEvent());
        getBaseActivity().openFragment(fragmentEventCreate);
    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void gotoAddContact() {

    }

    @Override
    public void gotoSearch() {

    }
}
