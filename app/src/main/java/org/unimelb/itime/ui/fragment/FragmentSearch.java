package org.unimelb.itime.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.databinding.FragmentSearchBinding;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;
import org.unimelb.itime.ui.viewmodel.SearchViewModel;

import java.util.List;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class FragmentSearch extends ItimeBaseFragment<SearchMvpView, SearchPresenter<SearchMvpView>> implements SearchMvpView{
    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    public SearchPresenter<SearchMvpView> createPresenter() {
        return new SearchPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new SearchViewModel(getPresenter());

//        viewModel.setMeetingDataSet(initMeetings());
//        viewModel.setSoloEventDataSet(initSoloEvents());
        viewModel.setContactDataSet(initContacts());

        binding.setVm(viewModel);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void ToInviteeProfile() {

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

    private List<Meeting> initMeetings(){
        return DBManager.getInstance(getContext()).getAll(Meeting.class);
    }

    private List<Contact> initContacts(){
        return DBManager.getInstance(getContext()).getAll(Contact.class);
    }

    private List<Event> initSoloEvents(){
        return DBManager.getInstance(getContext()).getAll(Event.class);
    }
}
