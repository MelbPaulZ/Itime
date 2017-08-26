package org.unimelb.itime.ui.fragment.contact;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.RecomandContact;
import org.unimelb.itime.databinding.FragmentAddFriendBinding;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.AddFriendsViewModel;
import org.unimelb.itime.util.EmailUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import java.util.List;

/**
 * Created by 37925 on 2016/12/10.
 */

public class AddFriendsFragment extends ItimeBaseFragment<AddFriendsMvpView, ContactPresenter<AddFriendsMvpView>> implements AddFriendsMvpView, ToolbarInterface{

    private static final int REQ_QRCODE = 2222;
    private FragmentAddFriendBinding binding;
    private AddFriendsViewModel mainViewModel;
//    private SettingMyProfileFragment settingMyProfileFragment;
    private ProfileFragment profileFragment;
    private ToolbarViewModel toolbarViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_friend, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        mainViewModel = new AddFriendsViewModel(presenter);
        mainViewModel.setMvpView(this);
        binding.setMainViewModel(mainViewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.morefriends_title));
        binding.setToolbarVM(toolbarViewModel);
        presenter.getRecommendContacts();
    }


    public void goToProfileFragment(String userUid){
        if(profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUserUid(userUid);
        profileFragment.setStartMode(ProfileFragment.MODE_CONTACT);
        getBaseActivity().openFragment(profileFragment, null, true);
    }

    public void goToSettingFragment(){
        Toast.makeText(baseActivity, "TODO Go to setting", Toast.LENGTH_SHORT).show();
//        if(settingMyProfileFragment == null) {
//            settingMyProfileFragment = new SettingMyProfileFragment();
//        }
//        getBaseActivity().openFragment(settingMyProfileFragment, null, true);
    }

    public void goToScanQRCode(){
        MPermissions.requestPermissions(this, REQ_QRCODE, Manifest.permission.CAMERA);
    }


    /**
     * Added by Qiushuo Huang
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_QRCODE: {
                if (allPermissionGranted(grantResults)) {
                    startQRCodeScanner();
                } else {
                    showToast(getString(R.string.need_permission));
                }
                break;
            }
        }
    }

    @PermissionGrant(REQ_QRCODE)
    public void startQRCodeScanner(){
        startActivityForResult(new Intent(getActivity(), CaptureActivityContact.class), 0);
    }

    @PermissionDenied(REQ_QRCODE)
    public void permissionError(){
        showToast(getString(R.string.need_permission));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                if(EmailUtil.getInstance(getContext()).isEmail(result)) {
                    presenter.findFriend(result);
                }else{
//                    onTaskError(ContactPresenter.TASK_SEARCH_USER, null);
                    Toast.makeText(baseActivity, R.string.contact_user_not_found_dialog_title, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void sendInviteEmail(){
        String email = mainViewModel.getPureSearchText();
        String message = String.format(getString(R.string.contact_user_not_found_dialog_message)
                , email);
        getDialogBuidler()
                .title(R.string.contact_user_not_found_dialog_title)
                .content(message)
                .negativeText(R.string.dialog_cancel)
                .positiveText(R.string.dialog_invite)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.sendInvite(email);
                    }
                })
                .show();
    }

    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter(getContext());
    }

    @Override
    public void onBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case ContactPresenter.TASK_SEARCH_USER:
                goToProfileFragment(((Contact)data).getUserDetail().getUserUid());
                break;
            case ContactPresenter.TASK_INVITE:
                Toast.makeText(getContext(), getContext().getString(R.string.contact_invitation_sent), Toast.LENGTH_SHORT).show();
                break;
            case ContactPresenter.TASK_MYSELF:
                goToSettingFragment();
                break;
            case ContactPresenter.TASK_ADD:
                Toast.makeText(getContext(), getContext().getString(R.string.profile_request_sent), Toast.LENGTH_SHORT).show();
                mainViewModel.requestSendSuccess((String) data);
                break;
            case ContactPresenter.TASK_RECOMMEND:
                mainViewModel.setRecommendContacts((List<RecomandContact>) data);
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case ContactPresenter.TASK_SEARCH_USER:
                sendInviteEmail();
                break;
            case ContactPresenter.TASK_INVITE:
                Toast.makeText(getContext(), getContext().getString(R.string.contact_invite_fail), Toast.LENGTH_SHORT).show();
                break;
            case ContactPresenter.TASK_ADD:
                Toast.makeText(getContext(), getContext().getString(R.string.network_error_retry), Toast.LENGTH_SHORT).show();
                break;
//            case ContactPresenter.ERROR_INVALID_EMAIL:
//                showDialog(getString(R.string.domain_alert_message), "");
        }
    }
}
