package org.unimelb.itime.ui.fragment.event;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventGreetingBinding;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.event.EventGreetingMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.presenter.LocalPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventGreetingViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.List;

/**
 * Created by Paul on 21/6/17.
 */

public class FragmentEventGreeting extends ItimeBaseFragment<EventGreetingMvpView, EventCreatePresenter<EventGreetingMvpView>>
        implements ToolbarInterface, EventGreetingMvpView {
    private FragmentEventGreetingBinding binding;
    private EventGreetingViewModel vm;
    private ToolbarViewModel toolbarVM;
    private Event event;
    private FragmentEventCreate.Mode taskMode;

    public void setTaskMode(FragmentEventCreate.Mode taskMode) {
        this.taskMode = taskMode;
    }

    @Override
    public EventCreatePresenter<EventGreetingMvpView> createPresenter() {
        return new EventCreatePresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_greeting, container, false);
        }
        return binding.getRoot();
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), taskMode.name(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = new EventGreetingViewModel(getPresenter());
        vm.setEvent(event);
        vm.setGreetingMessageInterface(new EventGreetingViewModel.GreetingMessageInterface() {
            @Override
            public void isTextLengthValid(boolean isValid) {
                toolbarVM.setRightEnable(isValid);
            }
        });
        binding.setVm(vm);

        toolbarVM = new ToolbarViewModel<>(this);
        toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarVM.setTitle(getString(R.string.toolbar_greeting));
        toolbarVM.setRightText(getString(R.string.toolbar_send));
        toolbarVM.setRightEnable(true);
        binding.setToolbarVM(toolbarVM);


    }

    @Override
    public void onStart() {
        super.onStart();
        EditText editText = (EditText) getActivity().findViewById(R.id.greeting_edit_text);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onNext() {
        if (taskMode == FragmentEventCreate.Mode.CREATE) {
            event.setEventType(Event.TYPE_GROUP);
            presenter.createEvent(event);
        }else if (taskMode == FragmentEventCreate.Mode.UPDATE){
            EventUtil.generateGroupEventAttributes(getContext(), event);
            presenter.updateEvent(event);
        }
    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        if (taskId == EventCreatePresenter.TASK_EVENT_CREATE){
//            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
//            List<Event> eventList = (List<Event>) data;
//            intent.putExtra(EventDetailActivity.EVENT, eventList.get(0));
//            startActivity(intent);
//            getActivity().finish();
            FragmentEventCreateSentFragment fragment = new FragmentEventCreateSentFragment();
            fragment.setEvent(event);
            getBaseActivity().openFragment(fragment);
        }else if (taskId == EventCreatePresenter.TASK_EVENT_UPDATE){
            Toast.makeText(getContext(), "update event successfully", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
