package org.unimelb.itime.ui.fragment.settings;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.ProfileActivity;
import org.unimelb.itime.ui.activity.SettingActivity;
import org.unimelb.itime.ui.activity.SettingSwitchActivity;
import org.unimelb.itime.ui.mvpview.MainSettingMvpView;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EmailUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import me.fesky.library.widget.ios.ActionSheetDialog;

/**
 * Created by Paul on 25/12/2016.
 */

public class MainSettingFragment extends ItimeBaseFragment<MainSettingMvpView,SettingPresenter<MainSettingMvpView>> implements ToolbarInterface, MainSettingMvpView {
    private FragmentSettingBinding binding;
    private MainSettingViewModel contentViewModel;
    private ToolbarViewModel toolbarViewModel;
    private static final String HF_URL = "http://www.google.com";
    public static final int REQ_QRCODE = 2221;

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new MainSettingViewModel(getPresenter());
        User user = UserUtil.getInstance(getContext()).getUser();
        contentViewModel.setUser(user);
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitle(getString(R.string.setting));
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        binding.setToolbarVM(toolbarViewModel);
        binding.setContentVM(contentViewModel);
    }

    @Override
    public void toProfilePage() {
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_MY_PROFILE);
        startActivityForResult(intent, SettingSwitchActivity.TASK_TO_MY_PROFILE);

    }

    private void toQRcodePage() {
        MPermissions.requestPermissions(this, REQ_QRCODE, Manifest.permission.CAMERA);
    }


    @PermissionGrant(REQ_QRCODE)
    public void startQRCodeScanner(){
        Intent intent = new Intent(getActivity(), CaptureActivityContact.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_QR_CODE);
        startActivityForResult(intent, SettingSwitchActivity.TASK_TO_QR_CODE);
    }

    @PermissionDenied(REQ_QRCODE)
    public void permisionDenied(){
        showToast(getString(R.string.need_permission));
    }


    @Override
    public void toBlockedUserPage() {
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_BLOCK_USER);
        startActivity(intent);
    }

    @Override
    public void toNotificationPage() {
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_NOTICIFATION);
        startActivity(intent);
    }

    @Override
    public void toCalendarPreferencePage() {
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_CALENDAR_PREFERENCE);
        startActivityForResult(intent, SettingSwitchActivity.TASK_TO_CALENDAR_PREFERENCE);
    }

    @Override
    public void toHelpFdPage() {
        Uri uri = Uri.parse(HF_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void toAboutPage() {
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_ABOUT);
        startActivity(intent);
    }

    @Override
    public void onLogOut() {
        popupDialog();
    }

    @Subscribe
    public void logOut(MessageEvent messageEvent){
//        if (messageEvent.task == MessageEvent.LOGOUT){
//            onTaskSuccess(1, null);
////            UserUtil.getInstance(getContext()).clearAccount();
//            Intent i = new Intent(getContext(), SplashActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//            getActivity().finish();
//        }
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case SettingPresenter.TASK_SEARCH_USER:
                gotoProfile((Contact) data);
        }
    }

    public void gotoProfile(Contact contact){
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProfileActivity.USER_UID,contact.getUserDetail().getUserUid());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onHelpAndFeedback(){
        helpPopup();
    }

    @Override
    public void toLanguagePage() {
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_LANGUAGE);
        startActivity(intent);
    }

    private void gotoReport(){
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_REPORT);
        startActivity(intent);
    }

    private void gotoHelp(){
        Intent intent = new Intent(getActivity(), SettingSwitchActivity.class);
        intent.putExtra(SettingSwitchActivity.TASK, SettingSwitchActivity.TASK_TO_HELP);
        startActivity(intent);
    }

    private void helpPopup(){
        new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getString(R.string.help_center), null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                gotoHelp();
                            }
                        })
                .addSheetItem(getString(R.string.report_a_problem), null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                gotoReport();
                            }
                        }).show();
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId){
            case SettingPresenter.TASK_SEARCH_USER:
                showToast(getString(R.string.cannot_find_user));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void popupDialog(){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Log Out", null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                onTaskStart(1); // just give a random task id
                                AppUtil.stopRemoteService(getContext());
                                UserUtil.getInstance(getContext()).clearAccount();
                            }
                        })
                .addSheetItem("Clear DB & Log Out ", null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
//                                AppUtil.stopRemoteService(getContext());
//                                UserUtil.getInstance(getContext()).clearAccountWithDB();
//                                Intent i = new Intent(getContext(), SplashActivity.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(i);
//                                getActivity().finish();
                            }
                        });
                        actionSheetDialog.show();
    }


    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        toQRcodePage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SettingSwitchActivity.TASK_TO_MY_PROFILE:
                //update user
                contentViewModel.setUser(UserUtil.getInstance(getContext()).getUser());
                break;
            case SettingSwitchActivity.TASK_TO_QR_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String result = bundle.getString("result");
                        if(EmailUtil.getInstance(getContext()).isEmail(result)) {
                            presenter.findFriend(result);
                        }else{
                            onTaskError(SettingPresenter.TASK_SEARCH_USER, null);
                        }
                    }
                }
        }
    }
}
