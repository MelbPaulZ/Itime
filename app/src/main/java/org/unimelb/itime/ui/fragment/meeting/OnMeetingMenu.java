package org.unimelb.itime.ui.fragment.meeting;

import android.util.Log;

import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.util.List;

import static org.unimelb.itime.util.MeetingUtil.restoreItem;

/**
 * Created by yuhaoliu on 25/7/17.
 */

public class OnMeetingMenu implements RecyclerViewAdapterMeetings.OnMenuListener {
    private MeetingPresenter<MeetingMvpView> meetingPresenter;
    private RecyclerViewAdapterMeetings mAdapter;
    private List<Meeting> data;
    private MeetingPresenter.FilterResult filterResult;

    public OnMeetingMenu(MeetingPresenter<MeetingMvpView> meetingPresenter, RecyclerViewAdapterMeetings mAdapter, List<Meeting> data, MeetingPresenter.FilterResult filterResult) {
        this.meetingPresenter = meetingPresenter;
        this.mAdapter = mAdapter;
        this.data = data;
        this.filterResult = filterResult;
    }

    @Override
    public void onPin(Meeting obj) {
        meetingPresenter.pinOpt(obj,obj.getEvent().isPinned());
        int index = data.indexOf(obj);
//        mAdapter.notifyItemChanged(index);
        mAdapter.closeAllItems();
        mAdapter.notifyDatasetChanged();
    }

    @Override
    public void onMute(Meeting obj) {
        meetingPresenter.muteOpt(obj, obj.getEvent().isMute());
        int index = data.indexOf(obj);
        mAdapter.notifyItemChanged(index);
    }

    @Override
    public void onArchive(Meeting obj) {
        meetingPresenter.archiveOpt(obj, true);
        int index = data.indexOf(obj);
        data.remove(index);

        filterResult.archiveResult.add(obj);
        mAdapter.notifyItemRemoved(index);
        if (mAdapter.getItemCount() == 0){
            mAdapter.notifyDatasetChanged();
        }
    }

    @Override
    public void onDelete(Meeting obj) {
        int index = data.indexOf(obj);
        data.remove(index);
        mAdapter.notifyItemRemoved(index);
        meetingPresenter.deleteOpt(obj);
    }

    @Override
    public void onRestore(Meeting obj) {
        int index = data.indexOf(obj);
        data.remove(index);
        mAdapter.notifyItemRemoved(index);

        restoreItem(obj, filterResult);
        meetingPresenter.restoreOpt(obj);
    }
}
