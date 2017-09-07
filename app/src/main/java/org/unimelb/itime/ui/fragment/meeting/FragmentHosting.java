package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.daimajia.swipe.util.Attributes;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

/**
 * Created by yuhaoliu on 22/06/2017.
 */

public class FragmentHosting extends Fragment {
    private View bgView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings mAdapter;
    private Context context;
    private List<Meeting> data;
    private MeetingPresenter<MeetingMvpView> meetingPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Item Decorator:
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        // Adapter:
        mAdapter = new RecyclerViewAdapterMeetings(context, RecyclerViewAdapterMeetings.Mode.HOSTING, meetingPresenter);
        mAdapter.setMode(Attributes.Mode.Single);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateVisibility();
            }
        });
        if (data != null){
            mAdapter.setData(data);
            mAdapter.notifyDatasetChanged();
        }

        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.closeAllItems();
    }

    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

    public void setData(MeetingPresenter.FilterResult filterResult){
        this.data = filterResult.hostingResult;
        if (mAdapter != null){
            mAdapter.setOnMenuListener(new OnMeetingMenu(meetingPresenter,mAdapter,data,filterResult));
            mAdapter.setData(this.data);
            mAdapter.notifyDatasetChanged();
        }
    }

    public void setMeetingPresenter(MeetingPresenter<MeetingMvpView> meetingPresenter) {
        this.meetingPresenter = meetingPresenter;
    }

    private View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getContext();
        FrameLayout wrapper = new FrameLayout(context);

        this.bgView = inflater.inflate(R.layout.meeting_hosting_placeholder, wrapper, false);
        FrameLayout.LayoutParams layoutParamsBgView = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wrapper.addView(bgView,layoutParamsBgView);

        View recyclerView = inflater.inflate(R.layout.meeting_recyclerview, wrapper, false);
        this.recyclerView = (RecyclerView) recyclerView.findViewById(R.id.recycler_view);
        FrameLayout.LayoutParams layoutParamsRecyclerView = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wrapper.addView(recyclerView, layoutParamsRecyclerView);

        return wrapper;
    }

    private void updateVisibility(){
        if (this.data != null && this.data.size() != 0){
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE );
        }
    }
}
