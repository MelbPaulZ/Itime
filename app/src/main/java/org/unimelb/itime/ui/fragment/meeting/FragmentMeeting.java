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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.databinding.FragmentMeetingBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.ArchiveActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.activity.SearchActivity;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public class FragmentMeeting extends ItimeBaseFragment<MeetingMvpView, MeetingPresenter<MeetingMvpView>> implements MeetingMvpView{
    public final static int TO_ARCHIVE = 0;

    private FragmentMeetingBinding binding;
    private FragmentInvitation fragmentInvitation;
    private FragmentHosting fragmentHosting;
    private FragmentComing fragmentComing;
    private PagerAdapterMeeting adapter;
    private MeetingPresenter.FilterResult filterResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding!=null){
            return binding.getRoot();
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meeting, container, false);

        binding.getRoot().findViewById(R.id.search_bar).setOnClickListener(onSearchClick());
        binding.getRoot().findViewById(R.id.archive_entrance).setOnClickListener(onArchiveClick());

        final TabLayout tabLayout = (TabLayout) binding.getRoot().findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.meeting_tag_invitation));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.meeting_tag_hosting));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.meeting_tag_coming));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CusSwipeViewPager viewPager = (CusSwipeViewPager) binding.getRoot().findViewById(R.id.pager);

        adapter = new PagerAdapterMeeting
                (getFragmentManager(), tabLayout.getTabCount());
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
        adapter.setmData(initFragments());
        adapter.notifyDataSetChanged();
    }

    @Override
    public MeetingPresenter<MeetingMvpView> createPresenter() {
        return new MeetingPresenter<>(getContext());
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

    public View.OnClickListener onArchiveClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To archive activity
                Intent intent = new Intent(getActivity(), ArchiveActivity.class);
                intent.putExtra(ArchiveActivity.ARCHIVE_RECEIVE_RESULT, getPresenter().getFilterResult());
                startActivityForResult(intent, TO_ARCHIVE);
            }
        };
    }

    private List<Fragment> initFragments(){
        fragmentInvitation = new FragmentInvitation();
        fragmentInvitation.setMeetingPresenter(getPresenter());

        fragmentHosting = new FragmentHosting();
        fragmentHosting.setMeetingPresenter(getPresenter());

        fragmentComing = new FragmentComing();
        fragmentComing.setMeetingPresenter(getPresenter());

        List<Fragment> mData = new ArrayList<>();
        mData.add(fragmentInvitation);
        mData.add(fragmentHosting);
        mData.add(fragmentComing);

        return mData;
    }

    @Override
    public void onDataLoaded(MeetingPresenter.FilterResult meetings, List<Meeting> comingMeeting) {
        filterResult = meetings;
        fragmentInvitation.setData(meetings);
        fragmentHosting.setData(meetings);
        fragmentComing.setData(comingMeeting);
    }

    @Override
    public void onMeetingClick(Meeting meeting) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EVENT, meeting.getEvent());
        getActivity().startActivity(intent);
    }

    @Subscribe
    public void refreshMeeting(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_MEETING){
            getPresenter().loadDataFromDB();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        getPresenter().getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ArchiveActivity.ARCHIVE_BACK_RESULT_CODE){
            getPresenter().setFilterResult((MeetingPresenter.FilterResult) data.getSerializableExtra(ArchiveActivity.ARCHIVE_BACK_RESULT));
            getPresenter().refreshDisplayData();
        }
    }
}
