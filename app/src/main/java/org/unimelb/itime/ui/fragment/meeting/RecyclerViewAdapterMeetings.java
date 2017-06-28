package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.databinding.MeetingHostingItemBinding;
import org.unimelb.itime.databinding.MeetingInvitationItemDetailedBinding;
import org.unimelb.itime.databinding.MeetingInvitationItemMessageBinding;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingHostingDetailCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingInvitationDetailCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingInvitationMsgCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingMenu1ViewModel;

import java.util.List;

public class RecyclerViewAdapterMeetings extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
    public enum Mode {
        INVITATION, HOSTING, COMING
    }

    public static final int INVITATION_DETAILS = 1;
    public static final int INVITATION_MESSAGE = 2;
    public static final int HOSTING_DETAILS = 3;

    public class InvitationDetailsViewHolder extends RecyclerView.ViewHolder {
        MeetingInvitationItemDetailedBinding binding;
        int position = 0;

        public InvitationDetailsViewHolder(MeetingInvitationItemDetailedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class InvitationMessageViewHolder extends RecyclerView.ViewHolder {
        MeetingInvitationItemMessageBinding binding;
        int position = 0;

        public InvitationMessageViewHolder(MeetingInvitationItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HostingDetailsViewHolder extends RecyclerView.ViewHolder {
        MeetingHostingItemBinding binding;
        int position = 0;

        public HostingDetailsViewHolder(MeetingHostingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private Context mContext;
    private List<Meeting> mDataset;
    private OnMenuListener onMenuListener;
    private Mode mode;

    public RecyclerViewAdapterMeetings(Context mContext, List<Meeting> mDataset, Mode mode) {
        this.mContext = mContext;
        this.mDataset = mDataset;
        this.mode = mode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case INVITATION_DETAILS:{
                MeetingInvitationItemDetailedBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_invitation_item_detailed,parent,false);
                holder = new InvitationDetailsViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmDetail(new MeetingInvitationDetailCardViewModel(mode));
                break;
            }
            case INVITATION_MESSAGE:{
                MeetingInvitationItemMessageBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_invitation_item_message,parent,false);
                holder = new InvitationMessageViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmMsg(new MeetingInvitationMsgCardViewModel(mode));
                break;
            }
            case HOSTING_DETAILS:{
                MeetingHostingItemBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_hosting_item,parent,false);
                holder = new HostingDetailsViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmDetail(new MeetingHostingDetailCardViewModel(mode));
                break;
            }
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = viewHolder.getItemViewType();
        final Meeting meeting = mDataset.get(position);
        switch (type){
            case INVITATION_DETAILS:{
                InvitationDetailsViewHolder holder = (InvitationDetailsViewHolder) viewHolder;
                holder.position = position;

                final MeetingInvitationDetailCardViewModel detailViewModel = holder.binding.getVmDetail();
                detailViewModel.setMeeting(meeting);

                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        detailViewModel.setMeeting(meeting);
                    }
                });
                break;
            }
            case INVITATION_MESSAGE:{
                InvitationMessageViewHolder holder = (InvitationMessageViewHolder) viewHolder;
                holder.position = position;

                final MeetingInvitationMsgCardViewModel msgViewModel = holder.binding.getVmMsg();
                msgViewModel.setMeeting(meeting);

                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        msgViewModel.setMeeting(meeting);
                    }
                });
                break;
            }
            case HOSTING_DETAILS:{
                HostingDetailsViewHolder holder = (HostingDetailsViewHolder) viewHolder;
                holder.position = position;

                final MeetingHostingDetailCardViewModel msgViewModel = holder.binding.getVmDetail();
                msgViewModel.setMeeting(meeting);

                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        msgViewModel.setMeeting(meeting);
                    }
                });
                break;
            }
            default:
                break;
        }

        mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null){
            return 0;
        }
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position%3 == 0){
            return  HOSTING_DETAILS;
        }

        if (position%3 == 1){
            return  INVITATION_MESSAGE;
        }

        if (position%3 == 2){
            return  INVITATION_DETAILS;
        }

        return -1;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public interface OnMenuListener<T> {
        void onPin(T obj);
        void onMute(T obj);
        void onArchive(T obj);
    }

    public OnMenuListener getOnMenuListener() {
        return onMenuListener;
    }

    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }
}
