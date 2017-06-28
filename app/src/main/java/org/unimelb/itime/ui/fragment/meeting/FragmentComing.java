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
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

/**
 * Created by yuhaoliu on 22/06/2017.
 */

public class FragmentComing extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings mAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meeting_recyclerview, container, false);
        context = getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Item Decorator:
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        // Adapter:
        mAdapter = new RecyclerViewAdapterMeetings(context, initData(), RecyclerViewAdapterMeetings.Mode.COMING);
        mAdapter.setOnMenuListener(new RecyclerViewAdapterMeetings.OnMenuListener<String>() {
            @Override
            public void onPin(String obj) {
                Toast.makeText(context,obj + " P", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMute(String obj) {
                Toast.makeText(context,obj + " M", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onArchive(String obj) {
                Toast.makeText(context,obj + " A", Toast.LENGTH_SHORT).show();
            }
        });
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

    private List<Meeting> initData(){
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
            event.setIsAllDay(i%2);
            event.setLocation(new Location());
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            events.add(event);

            startTime = startTime + allDayInterval;
            meeting.setEvent(event);
            meetings.add(meeting);
        }

        return meetings;
    }
}
