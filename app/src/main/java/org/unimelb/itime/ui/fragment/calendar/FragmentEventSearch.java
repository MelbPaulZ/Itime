package org.unimelb.itime.ui.fragment.calendar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.databinding.FragmentEventSearchBinding;
import org.unimelb.itime.databinding.FragmentSearchBinding;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.mvpview.calendar.SearchEventMvpView;
import org.unimelb.itime.ui.presenter.SearchEventPresenter;
import org.unimelb.itime.ui.presenter.SearchPresenter;
import org.unimelb.itime.ui.viewmodel.SearchEventViewModel;
import org.unimelb.itime.ui.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.ui.agendaview.AgendaViewBody;
import david.itimecalendar.calendar.util.MyCalendar;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class FragmentEventSearch extends ItimeBaseFragment<SearchEventMvpView, SearchEventPresenter<SearchEventMvpView>> implements SearchEventMvpView {
    public final static int TASK_ACTIVITY_BACK = 0;
    public final static int TASK_FRAGMENT_BACK = 1;

    private int taskId = 0;

    private FragmentEventSearchBinding binding;
    private SearchEventViewModel viewModel;
    private List<Map.Entry<Long,List<Event>>> map = new ArrayList<>();

    private RecyclerView eventRecyclerView;

    private AgendaViewBody.OnEventClickListener onEventClickListener = iTimeEventInterface -> {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EVENT, (Event)iTimeEventInterface);
        getActivity().startActivity(intent);
    };

    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itime_agenda_view_body_view, parent, false);
            return new RecyclerView.ViewHolder(v) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Map.Entry<Long, List<Event>> entry = map.get(position);
            AgendaViewBody body = (AgendaViewBody) holder.itemView;
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(entry.getKey());
            body.setCalendar(new MyCalendar(cal));
            body.setOnEventClickListener(onEventClickListener);
            ArrayList<ITimeEventInterface> interfaces = new ArrayList<>();
            interfaces.addAll(entry.getValue());

            body.setEventList(interfaces);
        }

        @Override
        public int getItemCount() {
            return map.size();
        }
    };

    /**
     * In this fragment, {@link SearchPresenter<SearchMvpView> createPresenter} will not will called,
     * the presenter should be set by caller.
     *
     * @return
     */
    @Override
    public SearchEventPresenter<SearchEventMvpView> createPresenter() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_search, container, false);
            eventRecyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.event_recycler_view);
            eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewModel == null) {
            viewModel = new SearchEventViewModel(getPresenter());
            binding.setVm(viewModel);
        }

        if (eventRecyclerView != null){
            eventRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBack() {
        switch (taskId) {
            case TASK_FRAGMENT_BACK: {
                getFragmentManager().popBackStack();
                break;
            }
            case TASK_ACTIVITY_BACK: {
                getActivity().finish();
                break;
            }
        }
    }

    @Override
    public void onEventClick(Event event) {

    }

    @Override
    public void onEventResult(List<Map.Entry<Long,List<Event>>> result) {
        this.viewModel.setEventResult(result);
        this.map.clear();
        this.map.addAll(result);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    public String getSearchStr() {
        return viewModel.getSearchStr();
    }

    public void setSearchStr(String searchStr) {
        this.viewModel.setSearchStr(searchStr);
    }
}
