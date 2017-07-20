package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.MeetingHostingItemDetailsBinding;
import org.unimelb.itime.databinding.MeetingHostingItemMessageBinding;
import org.unimelb.itime.databinding.MeetingInvitationItemDetailedBinding;
import org.unimelb.itime.databinding.MeetingInvitationItemMessageBinding;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingHostingDetailCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingHostingMsgCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingInvitationDetailCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingInvitationMsgCardViewModel;
import org.unimelb.itime.ui.viewmodel.meeting.MeetingMenu1ViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterMeetings extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
    public enum Mode {
        INVITATION, HOSTING, COMING
    }

    public static final int INVITATION_DETAILS = 1;
    public static final int INVITATION_MESSAGE = 2;
    public static final int HOSTING_DETAILS = 3;
    public static final int HOSTING_MESSAGE = 4;

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
        MeetingHostingItemDetailsBinding binding;
        int position = 0;

        public HostingDetailsViewHolder(MeetingHostingItemDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HostingMessageViewHolder extends RecyclerView.ViewHolder {
        MeetingHostingItemMessageBinding binding;
        int position = 0;

        public HostingMessageViewHolder(MeetingHostingItemMessageBinding binding) {
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

        switch (mode){
            case COMING:
                Collections.sort(mDataset);
                break;
            default:
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case INVITATION_DETAILS:{
                MeetingInvitationItemDetailedBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_invitation_item_detailed,parent,false);
                holder = new InvitationDetailsViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmDetail(new MeetingInvitationDetailCardViewModel(context,mode));
                break;
            }
            case INVITATION_MESSAGE:{
                MeetingInvitationItemMessageBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_invitation_item_message,parent,false);
                holder = new InvitationMessageViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmMsg(new MeetingInvitationMsgCardViewModel(context,mode));
                break;
            }
            case HOSTING_DETAILS:{
                MeetingHostingItemDetailsBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_hosting_item_details,parent,false);
                holder = new HostingDetailsViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmDetail(new MeetingHostingDetailCardViewModel(context,mode));
                break;
            }
            case HOSTING_MESSAGE:{
                MeetingHostingItemMessageBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.meeting_hosting_item_message,parent,false);
                holder = new HostingMessageViewHolder(binding);
                binding.setVmMenu1(new MeetingMenu1ViewModel());
                binding.setVmMsg(new MeetingHostingMsgCardViewModel(context,mode));
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
                detailViewModel.setMeetings(mDataset);
                detailViewModel.setMeeting(meeting);


                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        detailViewModel.setMeeting(meeting);
                    }
                });

                holder.binding.invalidateAll();
                break;
            }
            case INVITATION_MESSAGE:{
                InvitationMessageViewHolder holder = (InvitationMessageViewHolder) viewHolder;
                holder.position = position;

                final MeetingInvitationMsgCardViewModel msgViewModel = holder.binding.getVmMsg();
                msgViewModel.setMeetings(mDataset);
                msgViewModel.setMeeting(meeting);

                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        msgViewModel.setMeeting(meeting);
                    }
                });

                holder.binding.invalidateAll();
                break;
            }
            case HOSTING_DETAILS:{
                HostingDetailsViewHolder holder = (HostingDetailsViewHolder) viewHolder;
                holder.position = position;

                final MeetingHostingDetailCardViewModel msgViewModel = holder.binding.getVmDetail();
                msgViewModel.setMeetings(mDataset);
                msgViewModel.setMeeting(meeting);

                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        msgViewModel.setMeeting(meeting);
                    }
                });

                holder.binding.invalidateAll();
                break;
            }
            case HOSTING_MESSAGE:{
                HostingMessageViewHolder holder = (HostingMessageViewHolder) viewHolder;
                holder.position = position;

                final MeetingHostingMsgCardViewModel msgViewModel = holder.binding.getVmMsg();
                msgViewModel.setMeetings(mDataset);
                msgViewModel.setMeeting(meeting);

                final MeetingMenu1ViewModel menu1ViewModel = holder.binding.getVmMenu1();
                menu1ViewModel.setMeeting(meeting);
                menu1ViewModel.setmOnMenuClick(new MeetingMenu1ViewModel.OnMenuClick() {
                    @Override
                    public void onDataChange() {
                        msgViewModel.setMeeting(meeting);
                    }
                });

                holder.binding.invalidateAll();
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
        Meeting meeting = mDataset.get(position);
        Event event = meeting.getEvent();
        boolean isCancelled = event.getStatus().equals(Event.STATUS_CANCELLED);
        boolean isHost = EventUtil.isHost(event);

        if (mode == Mode.INVITATION){
            return isCancelled ? INVITATION_MESSAGE : INVITATION_DETAILS;
        }

        if (mode == Mode.HOSTING){
            return isCancelled ? HOSTING_MESSAGE : HOSTING_DETAILS;
        }

        if (mode == Mode.COMING){
            if (isHost){
                return isCancelled ? HOSTING_MESSAGE : HOSTING_DETAILS;
            }else{
                return isCancelled ? INVITATION_MESSAGE : INVITATION_DETAILS;
            }
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
