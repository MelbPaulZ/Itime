package org.unimelb.itime.ui.fragment.contact;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentProfileBinding;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.ui.presenter.contact.ProfilePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.util.FontUtil;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragment extends ItimeBaseFragment<ProfileMvpView, ProfilePresenter<ProfileMvpView>> implements ToolbarInterface,ProfileMvpView {
    public static final int MODE_CONTACT = 1;
    public static final int MODE_REQUEST = 2;
    public static final int MODE_STRANGER = 3;

    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;
    private EditAliasFragment editAliasFragment;
    private ToolbarViewModel toolbarViewModel;
    private int startMode = 1;
    private Contact contact;
    private String requestId;
    private String userUid = "";
    private boolean fromEditAlias = false;
    private View nextButton;


    public void setRequestId(String requestId){
        this.requestId = requestId;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public void setStartMode(int startMode) {
        this.startMode = startMode;
    }

    public View getContentView(){
        return getView();
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter(getActivity());
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(contact!=null) {
            viewModel.setContact(contact);
        }
    }

    public void onStart(){
        super.onStart();
        if(!fromEditAlias) {
            if (startMode == MODE_REQUEST) {
                presenter.getRequest(userUid);
            } else {
                presenter.getContact(userUid);
            }
        }else{
            fromEditAlias=false;
            viewModel.contactMode();
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new ProfileFragmentViewModel(presenter);
        viewModel.setMvpView(this);
        nextButton = binding.getRoot().findViewById(R.id.right_icon);
        viewModel.setRightButton(nextButton);
        binding.setViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.profile_profile));
        toolbarViewModel.setRightIcon(getContext().getResources().getDrawable(R.drawable.icon_more_black));
        toolbarViewModel.setRightEnable(true);
        binding.setToolbarVM(toolbarViewModel);

        setFonts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);

        return binding.getRoot();
    }

    private void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact(){
        return contact;
    }

    @Override
    public void goToInviteFragment(){
//        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
//        intent.putExtra("start_time", Calendar.getInstance().getTimeInMillis());
//        intent.putExtra("contact", contact);
//        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getContext(), R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
//        getActivity().startActivityForResult(intent, CalendarBaseViewFragment.REQ_EVENT_CREATE,bundleAnimation);
    }

    @Override
    public void goToEditAlias() {
        if(editAliasFragment == null) {
            editAliasFragment = new EditAliasFragment();
        }
        editAliasFragment.setContact(contact);
        getBaseActivity().openFragment(editAliasFragment, null, true);
        fromEditAlias = true;
    }

    @Override
    public void showBlockDialog() {
        getDialogBuidler()
                .title(getString(R.string.dialog_block_contact_title))
                .content(getString(R.string.dialog_block_contact_message))
                .positiveText(getString(R.string.dialog_block_block))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.blockUser(contact);
                    }
                })
                .negativeText(getString(R.string.dialog_cancel))
                .show();
    }

    @Override
    public void showDeleteDialog() {
        getDialogBuidler()
                .title(getString(R.string.dialog_delete_contact_title))
                .content(getString(R.string.dialog_delete_contact_message))
                .positiveText(getString(R.string.dialog_delete_delete))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteUser(contact);
                    }
                })
                .negativeText(getString(R.string.dialog_cancel))
                .show();
    }

    public Context getContext(){
        return getActivity();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        viewModel.onRightClicked();
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case ProfilePresenter.TASK_ACCEPT :
                viewModel.setShowAccept(false);
                viewModel.setShowAccepted(true);
                break;
            case ProfilePresenter.TASK_ADD :
                viewModel.setShowAdd(false);
                viewModel.setShowSent(true);
                Toast.makeText(getContext(),getString(R.string.contact_invitation_sent), Toast.LENGTH_SHORT).show();
                break;
            case ProfilePresenter.TASK_BLOCK :
                viewModel.blockSuccess();
                break;
            case ProfilePresenter.TASK_UNBLOCK :
                viewModel.unblockSuccess();
                break;
            case ProfilePresenter.TASK_DELETE :
                Toast.makeText(getContext(), getString(R.string.profile_delete_success), Toast.LENGTH_SHORT).show();
                onBack();
                break;
            case ProfilePresenter.TASK_CONTACT :
                contact = (Contact) data;
                viewModel.setContact(contact);
                viewModel.contactMode();
                break;
            case ProfilePresenter.TASK_STRANGER:
                contact = (Contact) data;
                viewModel.setContact(contact);
                viewModel.strangerMode();
                break;
            case ProfilePresenter.TASK_REQUEST :
                contact = (Contact) data;
                viewModel.setContact(contact);
                viewModel.requestMode();
                viewModel.setRequestId(requestId);
                break;

        }
    }

    private void setFonts(){
        TextView name = (TextView) getContentView().findViewById(R.id.nameTextView);
        TextView alias = (TextView) getContentView().findViewById(R.id.aliasTextView);
        Typeface sansLight = FontUtil.getSansLight(getContext());
        name.setTypeface(sansLight);
        alias.setTypeface(sansLight);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        Toast.makeText(getContext(), getString(R.string.network_error_retry), Toast.LENGTH_SHORT).show();
//        switch (taskId){
//            case ProfilePresenter.TASK_ACCEPT :
//                Toast.makeText(getContext(), getString(R.string.accept_fail), Toast.LENGTH_SHORT).show();
//                break;
//            case ProfilePresenter.TASK_ADD :
//                Toast.makeText(getContext(), getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
//                break;
//            case ProfilePresenter.TASK_BLOCK :
//                Toast.makeText(getContext(), getString(R.string.block_fail), Toast.LENGTH_SHORT).show();
//                break;
//            case ProfilePresenter.TASK_UNBLOCK :
//                Toast.makeText(getContext(), getString(R.string.unblock_fail), Toast.LENGTH_SHORT).show();
//                break;
//            case ProfilePresenter.TASK_DELETE :
//                Toast.makeText(getContext(), getString(R.string.delete_fail), Toast.LENGTH_SHORT).show();
//                break;
//            case ProfilePresenter.TASK_STRANGER :
//                Toast.makeText(getContext(), getString(R.string.access_fail), Toast.LENGTH_SHORT).show();
//                break;
//        }
    }
}
