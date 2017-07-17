package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
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

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.manager.EventManager;
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

public class FragmentComing extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings mAdapter;
    private Context context;

    private EventManager.EventsPackage eventsPackage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meeting_recyclerview, container, false);
        context = getContext();
        eventsPackage = EventManager.getInstance(context).getEventsPackage();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Item Decorator:
        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        // Adapter:
        mAdapter = new RecyclerViewAdapterMeetings(context, getDisplayData(), RecyclerViewAdapterMeetings.Mode.COMING);
//        mAdapter.setOnMenuListener(new RecyclerViewAdapterMeetings.OnMenuListener<String>() {
//            @Override
//            public void onPin(String obj) {
//                Toast.makeText(context,obj + " P", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onMute(String obj) {
//                Toast.makeText(context,obj + " M", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onArchive(String obj) {
//                Toast.makeText(context,obj + " A", Toast.LENGTH_SHORT).show();
//            }
//        });
        mAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

    private List<Meeting> getDisplayData(){
        long todayBegin = EventUtil.getDayBeginMilliseconds(Calendar.getInstance().getTimeInMillis());

//        Map<Long, List<ITimeEventInterface>> eventMap = new HashMap<>();
        List<ITimeEventInterface> eventSet = new ArrayList<>();

        Map<Long, List<ITimeEventInterface>> regularMap = eventsPackage.getRegularEventDayMap();
        Map<Long, List<ITimeEventInterface>> repeatedMap = eventsPackage.getRepeatedEventDayMap();

        // extract regular events
        for (Map.Entry<Long, List<ITimeEventInterface>> entry : regularMap.entrySet()) {
            Long key = entry.getKey();
            List<ITimeEventInterface> events = entry.getValue();
            if (key >= todayBegin){
                eventSet.addAll(events);
//                if (eventMap.containsKey(key)){
//                    eventMap.get(key).addAll(events);
//                }else {
//                    eventMap.put(key, events);
//                }
            }
        }
        // extract repeated events
        for (Map.Entry<Long, List<ITimeEventInterface>> entry : repeatedMap.entrySet()) {
            Long key = entry.getKey();
            List<ITimeEventInterface> events = entry.getValue();
            if (key >= todayBegin){
                eventSet.addAll(events);
//                if (eventMap.containsKey(key)){
//                    eventMap.get(key).addAll(events);
//                }else {
//                    eventMap.put(key, events);
//                }
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
}
