package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateSentBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateSentMvpView;
import org.unimelb.itime.ui.presenter.event.EventCreateSentPrensenter;
import org.unimelb.itime.ui.viewmodel.event.EventCreateSentViewModel;

/**
 * Created by Paul on 7/9/17.
 */

public class FragmentEventCreateSentFragment extends ItimeBaseFragment<EventCreateSentMvpView,
        EventCreateSentPrensenter<EventCreateSentMvpView>>
implements EventCreateSentMvpView{

    private FragmentEventCreateSentBinding binding;
    private EventCreateSentViewModel vm;
    private Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding==null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_sent, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new EventCreateSentViewModel(getPresenter());
        vm.setEvent(event);
        binding.setVm(vm);
    }

    @Override
    public EventCreateSentPrensenter<EventCreateSentMvpView> createPresenter() {
        return new EventCreateSentPrensenter<>(getContext());
    }

    @Override
    public void onClickDone() {
        getActivity().finish();
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
