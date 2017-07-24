package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
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
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

/**
 * Created by yuhaoliu on 22/06/2017.
 */

public class FragmentArchive extends ItimeBaseFragment<MeetingMvpView,MeetingPresenter<MeetingMvpView>> implements ToolbarInterface {
    private FragmentMeetingArchiveBinding binding;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings mAdapter;
    private Context context;

    private EventManager.EventsPackage eventsPackage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_meeting_archive,container,false);
        View view = binding.getRoot();

        eventsPackage = EventManager.getInstance(context).getEventsPackage();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Item Decorator:
        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        // Adapter:
        mAdapter = new RecyclerViewAdapterMeetings(context, getDisplayData(), RecyclerViewAdapterMeetings.Mode.ARCHIVE);
        mAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

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

    @Override
    public MeetingPresenter<MeetingMvpView> createPresenter() {
        return new MeetingPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private List<Meeting> getDisplayData(){
        long todayBegin = EventUtil.getDayBeginMilliseconds(Calendar.getInstance().getTimeInMillis());

        List<ITimeEventInterface> eventSet = new ArrayList<>();

        Map<Long, List<ITimeEventInterface>> regularMap = eventsPackage.getRegularEventDayMap();
        Map<Long, List<ITimeEventInterface>> repeatedMap = eventsPackage.getRepeatedEventDayMap();

        // extract regular events
        for (Map.Entry<Long, List<ITimeEventInterface>> entry : regularMap.entrySet()) {
            Long key = entry.getKey();
            List<ITimeEventInterface> events = entry.getValue();
            if (key >= todayBegin){
                eventSet.addAll(events);
            }
        }
        // extract repeated events
        for (Map.Entry<Long, List<ITimeEventInterface>> entry : repeatedMap.entrySet()) {
            Long key = entry.getKey();
            List<ITimeEventInterface> events = entry.getValue();
            if (key >= todayBegin){
                eventSet.addAll(events);
            }
        }

        List<Meeting> meetingSet = new ArrayList<>();
        for (ITimeEventInterface event:eventSet
             ) {
            Meeting meeting = new Meeting();
            meeting.setEvent((Event) event);
            meeting.setInfo("Coming");
            meetingSet.add(meeting);
        }

        return meetingSet;
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {
        getBaseActivity().finish();
    }
}
