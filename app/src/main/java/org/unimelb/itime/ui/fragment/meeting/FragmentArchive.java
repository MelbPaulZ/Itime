package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.databinding.FragmentMeetingArchiveBinding;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.ui.activity.ArchiveActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static org.unimelb.itime.ui.activity.ArchiveActivity.ARCHIVE_BACK_RESULT_CODE;

/**
 * Created by yuhaoliu on 22/06/2017.
 */

public class FragmentArchive extends ItimeBaseFragment<MeetingMvpView,MeetingPresenter<MeetingMvpView>> implements MeetingMvpView,ToolbarInterface {
    private FragmentMeetingArchiveBinding binding;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings mAdapter;
    private Context context;
    private List<Meeting> data;
    private MeetingPresenter.FilterResult filterResult;
    private MeetingPresenter<MeetingMvpView> meetingPresenter;
    private OnMeetingMenu onMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_archive,container,false);
        View view = binding.getRoot();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Item Decorator:
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        setUpToolbar();

        return view;
    }

    private void setUpToolbar(){
        ToolbarViewModel toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftEnable(true);
        toolbarViewModel.setLeftIcon(context.getResources().getDrawable(R.drawable.icon_calendar_arrowleft));
        toolbarViewModel.setTitle("Archive");
        toolbarViewModel.setRightEnable(true);
        toolbarViewModel.setRightText("Clear All");
        toolbarViewModel.setRightTextColor(context.getResources().getColor(R.color.brand_warning));
        binding.setVmToolbar(toolbarViewModel);
    }

    public void setData(MeetingPresenter.FilterResult filterResult){
        this.filterResult = filterResult;
        this.data = filterResult.archiveResult;

    }

    @Override
    public MeetingPresenter<MeetingMvpView> createPresenter() {
        meetingPresenter = new MeetingPresenter<>(getContext());
        return meetingPresenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onMenu = new OnMeetingMenu(meetingPresenter,mAdapter,data,filterResult);

        meetingPresenter.setFilterResult(filterResult);

        // Adapter: init here because of timing for presenter
        mAdapter = new RecyclerViewAdapterMeetings(context, RecyclerViewAdapterMeetings.Mode.ARCHIVE, meetingPresenter);
        mAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnMenuListener(onMenu);
        mAdapter.setmDataset(data);

        mAdapter.notifyDatasetChanged();
    }

    /**
     * Clear All btn
     */
    @Override
    public void onNext() {
        mAdapter.notifyItemRangeRemoved(0,filterResult.archiveResult.size());
        meetingPresenter.deleteAllArchive();
    }

    @Override
    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(ArchiveActivity.ARCHIVE_BACK_RESULT, filterResult);
        getBaseActivity().setResult(ARCHIVE_BACK_RESULT_CODE, intent);
        getBaseActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Do nothing, data is passed in
     */
    @Override
    public void onDataLoaded(MeetingPresenter.FilterResult meetings, List<Meeting> comingMeeting) {

    }

    @Override
    public void onMeetingClick(Meeting meeting) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EVENT, meeting.getEvent());
        getActivity().startActivity(intent);
    }
}
