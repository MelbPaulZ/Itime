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
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;
import org.unimelb.itime.ui.viewmodel.SearchViewModel;

import java.util.List;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class FragmentSearch extends ItimeBaseFragment<SearchMvpView, SearchPresenter<SearchMvpView>> implements SearchMvpView {
    public final static int TASK_ACTIVITY_BACK = 0;
    public final static int TASK_FRAGMENT_BACK = 1;

    private int taskId = 0;

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private Class[] scope;

    @Override
    public SearchPresenter<SearchMvpView> createPresenter() {
        return new SearchPresenter<>(getContext());
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

        //load data
        if (scope != null) {
            for (Class mClass : scope
                    ) {
                presenter.loadData(mClass);
            }
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
    public void onMeetingClick(Meeting meeting) {

    }

    @Override
    public void onEventClick(Event event) {

    }

    @Override
    public void onContactClick(Contact contact) {

    }

    @Override
    public void onMeetingShowMoreClick(String searchStr, List<Meeting> meetingDataSet) {
        FragmentSearch detailSearch = new FragmentSearch();
        detailSearch.setScope(Meeting.class);
        getBaseActivity().openFragment(detailSearch);
        //ensure set viewModel things after openFrag
        detailSearch.setTaskId(TASK_FRAGMENT_BACK);
        detailSearch.setSearchStr(searchStr);
        detailSearch.setShowMoreEnabled(false);
    }

    @Override
    public void onEventShowMoreClick(String searchStr, List<Event> eventDataSet) {
        FragmentSearch detailSearch = new FragmentSearch();
        detailSearch.setScope(Event.class);
        getBaseActivity().openFragment(detailSearch);
        //ensure set viewModel things after openFrag
        detailSearch.setTaskId(TASK_FRAGMENT_BACK);
        detailSearch.setSearchStr(searchStr);
        detailSearch.setShowMoreEnabled(false);

    }

    @Override
    public void onContactShowMoreClick(String searchStr, List<Contact> contactDataSet) {
        FragmentSearch detailSearch = new FragmentSearch();
        detailSearch.setScope(Contact.class);
        getBaseActivity().openFragment(detailSearch);
        //ensure set viewModel things after openFrag
        detailSearch.setTaskId(TASK_FRAGMENT_BACK);
        detailSearch.setSearchStr(searchStr);
        detailSearch.setShowMoreEnabled(false);
    }

    @Override
    public <T> void onDataLoaded(Class<T> tClass, List<T> data) {
        if (tClass == Meeting.class) {
            viewModel.setMeetingDataSet((List<Meeting>) data);
        } else if (tClass == Event.class) {
            viewModel.setSoloEventDataSet((List<Event>) data);
        } else if (tClass == Contact.class) {
            viewModel.setContactDataSet((List<Contact>) data);
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

    public void setScope(Class... tClass) {
        scope = tClass;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
