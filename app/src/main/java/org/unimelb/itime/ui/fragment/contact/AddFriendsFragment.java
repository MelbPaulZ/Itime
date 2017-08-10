package org.unimelb.itime.ui.fragment.contact;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentAddFriendBinding;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.AddFriendsViewModel;

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
        binding.setMainViewModel(mainViewModel);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.morefriends_title));
        binding.setToolbarVM(toolbarViewModel);
    }


    public void goToProfileFragment(Contact user){
        if(profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUserUid(user.getUserDetail().getUserUid());
        profileFragment.setStartMode(ProfileFragment.MODE_CONTACT);
        getBaseActivity().openFragment(profileFragment, null, true);
    }

    public void goToSettingFragment(){
//        if(settingMyProfileFragment == null) {
//            settingMyProfileFragment = new SettingMyProfileFragment();
//        }
//        getBaseActivity().openFragment(settingMyProfileFragment, null, true);
    }

    public void goToScanQRCode(){
        MPermissions.requestPermissions(this, REQ_QRCODE, Manifest.permission.CAMERA);
    }

//    /**
//     * Added by Qiushuo Huang
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQ_QRCODE: {
//                if (allPermissionGranted(grantResults)) {
//                    startQRCodeScanner();
//                } else {
//                    showToast(getString(R.string.need_permission));
//                }
//                break;
//            }
//        }
//    }

//    @PermissionGrant(REQ_QRCODE)
//    public void startQRCodeScanner(){
//        startActivityForResult(new Intent(getActivity(), CaptureActivityContact.class), 0);
//    }
//
//    @PermissionDenied(REQ_QRCODE)
//    public void permissionError(){
//        showToast(getString(R.string.need_permission));
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == getActivity().RESULT_OK) {
//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                String result = bundle.getString("result");
//                if(ContactCheckUtil.getInsstance().isEmail(result)) {
//                    presenter.findFriend(result);
//                }else{
//                    onTaskError(AddFriendsPresenter.TASK_SEARCH_USER, null);
//                }
//            }
//        }
//    }


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
    public void onTaskSuccess(int taskId, Contact data) {
        hideProgressDialog();
        switch (taskId){
            case ContactPresenter.TASK_SEARCH_USER:
                goToProfileFragment(data);
                break;
            case ContactPresenter.TASK_INVITE:
                Toast.makeText(getContext(), getContext().getString(R.string.contact_invitation_sent), Toast.LENGTH_SHORT).show();
                break;
            case ContactPresenter.TASK_MYSELF:
                goToSettingFragment();
                break;
        }

    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case ContactPresenter.TASK_SEARCH_USER:
                mainViewModel.showNotFound();
                break;
            case ContactPresenter.TASK_INVITE:
                Toast.makeText(getContext(), getContext().getString(R.string.contact_invite_fail), Toast.LENGTH_SHORT).show();
                break;
//            case ContactPresenter.ERROR_INVALID_EMAIL:
//                showDialog(getString(R.string.domain_alert_message), "");
        }
    }
}
