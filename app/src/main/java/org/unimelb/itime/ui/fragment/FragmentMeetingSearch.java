package org.unimelb.itime.ui.fragment;

import android.content.Intent;
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
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;
import org.unimelb.itime.ui.viewmodel.SearchViewModel;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class FragmentMeetingSearch extends ItimeBaseFragment<SearchMvpView, SearchPresenter<SearchMvpView>> implements SearchMvpView {
    public final static int TASK_ACTIVITY_BACK = 0;
    public final static int TASK_FRAGMENT_BACK = 1;

    public final static int TYPE_MEETING = 1;
    public final static int TYPE_EVENT = 2;
    public final static int TYPE_CONTACT = 3;

    private int taskId = 0;

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private SearchPresenter.Scope[] scopes;

    /**
     * In this fragment, {@link SearchPresenter<SearchMvpView> createPresenter} will not will called,
     * the presenter should be set by caller.
     *
     * @return
     */
    @Override
    public SearchPresenter<SearchMvpView> createPresenter() {
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (viewModel == null) {
            viewModel = new SearchViewModel(getPresenter());
            binding.setVm(viewModel);
        }

        getPresenter().setScope(scopes);
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
    public void onMeetingClick(Meeting meeting) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EVENT, meeting.getEvent());
        getActivity().startActivity(intent);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EVENT, event);
        getActivity().startActivity(intent);
    }

    @Override
    public void onContactClick(Contact contact) {
        
    }

    @Override
    public void onShowMoreClick(int type, String searchStr) {
        FragmentMeetingSearch detailSearch = new FragmentMeetingSearch();

        switch (type){
            case TYPE_MEETING:
                detailSearch.setScope(SearchPresenter.Scope.MEETING);
                break;
            case TYPE_EVENT:
                detailSearch.setScope(SearchPresenter.Scope.EVENT);
                break;
            case TYPE_CONTACT:
                detailSearch.setScope(SearchPresenter.Scope.CONTACT);
                break;
            default:
                return;
        }

        detailSearch.setPresenter(getPresenter());
        getBaseActivity().openFragment(detailSearch);
        //ensure set viewModel things after openFrag
        detailSearch.setTaskId(TASK_FRAGMENT_BACK);
        detailSearch.setSearchStr(searchStr);
        detailSearch.setShowMoreEnabled(false);
    }

    @Override
    public void onSearchResult(SearchPresenter.SearchResult result) {
        if (result.meetings != null){
            viewModel.setMeetingSearchResult(result.meetings);
        }

        if (result.events != null){
            viewModel.setEventSearchResult(result.events);
        }

        if (result.contacts != null){
            viewModel.setContactSearchResult(result.contacts);
        }
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

    public void setTitleEnabled(boolean titleEnabled) {
        this.viewModel.titleEnabled.set(titleEnabled);
    }

    public void setShowMoreEnabled(boolean showMoreEnabled) {
        this.viewModel.showMoreEnabled.set(showMoreEnabled);
    }

    public void setScope(SearchPresenter.Scope... scopes) {
        this.scopes = scopes;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
