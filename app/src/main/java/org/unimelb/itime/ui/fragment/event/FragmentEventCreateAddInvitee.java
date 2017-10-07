package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.databinding.FragmentCreateEventAddInviteeBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddInviteeMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventCreateAddInviteeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/6/21.
 */

public class FragmentEventCreateAddInvitee extends ItimeBaseFragment<EventCreateAddInviteeMvpView, EventCreatePresenter<EventCreateAddInviteeMvpView>>
        implements EventCreateAddInviteeMvpView, ToolbarInterface {

    public static final int MODE_CREATE = 1;
    public static final int MODE_EDIT = 2;

    private FragmentCreateEventAddInviteeBinding binding;
    private FragmentEventCreate fragmentEventCreate;
    private FragmentEventCreateSearchInvitee fragmentSearchInvitee;
    private Event event;
    private EventCreateAddInviteeViewModel contentVM;
    private ToolbarViewModel toolbarVM;
    private FragmentEventCreateAddContact addContactFragment;
    private Fragment from;

    private int mode = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_event_add_invitee, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(toolbarVM==null) {
            toolbarVM = new ToolbarViewModel(this);
            toolbarVM.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_close));
            toolbarVM.setRightText(getString(R.string.new_event_toolbar_next));
            toolbarVM.setRightEnable(false);
            toolbarVM.setTitle(getString(R.string.event_create_addinvitee));
        }

        if(contentVM==null) {
            from = getFrom();
            contentVM = new EventCreateAddInviteeViewModel(getPresenter());
            contentVM.setToolbarViewModel(toolbarVM);
            contentVM.setMvpView(this);
            contentVM.setContext(getContext());
            contentVM.setEvent(event);
        }

        binding.setToolbarVM(toolbarVM);
        binding.setVm(contentVM);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setEvent(Event event){
        this.event = event;
    }

    @Override
    public void onResume() {
        super.onResume();
        contentVM.loadData();
    }

    @Override
    public EventCreatePresenter createPresenter() {
        return new EventCreatePresenter(getContext());
    }

    @Override
    public void onNext() {
        Event event = contentVM.getEditedEvent();
        if(from instanceof FragmentEventCreate){
            ((FragmentEventCreate) from).setEvent(event);
            getFragmentManager().popBackStack(FragmentEventCreate.class.getSimpleName(), 0);
            return;
        }

        if(from instanceof FragmentEventEdit){
            ((FragmentEventEdit) from).setEvent(event);
            getFragmentManager().popBackStack(FragmentEventEdit.class.getSimpleName(), 0);
            return;
        }

        if(fragmentEventCreate==null){
            fragmentEventCreate = new FragmentEventCreate();
        }
        fragmentEventCreate.setEvent(event);
        getBaseActivity().openFragment(fragmentEventCreate);
    }

    @Override
    public void onBack() {
       getDialogBuidler()
                .content(R.string.event_create_cancel_dialog_content)
                .contentColor(getResources().getColor(R.color.black))
                .contentGravity(GravityEnum.CENTER)
                .negativeText(R.string.event_dialog_discard)
                .positiveText(R.string.event_dialog_keep_editing)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getBaseActivity().onBackPressed();
                    }
                })
                .show();
    }


    @Override
    public void gotoAddContact() {
        if(addContactFragment==null){
            addContactFragment = new FragmentEventCreateAddContact();
        }
        addContactFragment.setInvitees(contentVM.getInvitees());
        addContactFragment.setEvent(contentVM.getEvent());
        getBaseActivity().openFragment(addContactFragment);
    }

    @Override
    public void gotoSearch() {
        if(fragmentSearchInvitee==null){
            fragmentSearchInvitee = new FragmentEventCreateSearchInvitee();
        }
        fragmentSearchInvitee.setInviteeList(contentVM.getInvitees());
        fragmentSearchInvitee.setEvent(contentVM.getEvent());
        getBaseActivity().openFragment(fragmentSearchInvitee);
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
}
