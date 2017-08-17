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

public class FragmentComing extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterMeetings mAdapter;
    private Context context;
    private List<Meeting> data;
    private MeetingPresenter<MeetingMvpView> meetingPresenter;

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
        mAdapter = new RecyclerViewAdapterMeetings(context, RecyclerViewAdapterMeetings.Mode.COMING, meetingPresenter);
        mAdapter.setMode(Attributes.Mode.Single);

        if (data != null){
            mAdapter.setData(data);
            mAdapter.notifyDatasetChanged();
        }

        recyclerView.setAdapter(mAdapter);

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

    public void setData(List<Meeting> comingResult){
        this.data = comingResult;
        if (mAdapter != null){
            mAdapter.setData(this.data);
            mAdapter.notifyDatasetChanged();
        }
    }

    public void setMeetingPresenter(MeetingPresenter<MeetingMvpView> meetingPresenter) {
        this.meetingPresenter = meetingPresenter;
    }
}
