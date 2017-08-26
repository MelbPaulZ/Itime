package org.unimelb.itime.ui.fragment.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import org.unimelb.itime.databinding.FragmentMyQrCodeBinding;

import org.unimelb.itime.ui.mvpview.contact.MyQRCodeMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;

import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.MyQRCodeVieModel;

import org.unimelb.itime.util.EmailUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37925 on 2016/12/18.
 */

public class MyQRCodeFragment extends ItimeBaseFragment<MyQRCodeMvpView, ContactPresenter<MyQRCodeMvpView>> implements ToolbarInterface, MyQRCodeMvpView {

    public static int SCAN_QR_CODE = 1;
    public static String PREVIEW = "preview";
    public static final int REQ_STORAGE = 2;

    private FragmentMyQrCodeBinding binding;
    private MyQRCodeVieModel viewModel;
    private ToolbarViewModel toolbarViewModel;
    private int preview;
    private PopupMenu menu;
    private List<PopupMenu.Item> menuItem;
    private PopupMenu.OnItemClickListener onMenuItemClicked;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater,
                    R.layout.fragment_my_qr_code, container, false);
        }
        return binding.getRoot();
    }

    private void initMenu() {
        menu = new PopupMenu(presenter.getContext());
        menuItem = new ArrayList<>();

        menuItem.add(new PopupMenu.Item(R.drawable.icon_qr_save,
                presenter.getContext().getResources().getString(R.string.save_to_album)));

        menuItem.add(new PopupMenu.Item(R.drawable.icon_contacts_scanqr,
                presenter.getContext().getResources().getString(R.string.scan_qr_code)));

        menuItem.add(new PopupMenu.Item(R.drawable.icon_details_share_underedit,
                presenter.getContext().getResources().getString(R.string.event_detail_toolbar_share)));
        menu.setItems(menuItem);

        onMenuItemClicked = new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(int position, PopupMenu.Item item) {
                switch (position) {
                    case 0:
                        saveQRCode();
                        break;
                    case 1:
                        goToScanQRCode();
                        break;
                    case 2:
                        shareQRCode();
                        break;
                }
            }
        };
        menu.setOnItemClickListener(onMenuItemClicked);
    }

    private void shareQRCode() {
        Toast.makeText(baseActivity, "TODO Share", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewModel == null) {
            viewModel = new MyQRCodeVieModel(presenter);
            binding.setViewModel(viewModel);
        }

        if (toolbarViewModel == null) {
            toolbarViewModel = new ToolbarViewModel<>(this);
            toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
            toolbarViewModel.setTitle(getString(R.string.my_qr_code_title));
            toolbarViewModel.setRightEnable(true);
            toolbarViewModel.setRightIcon(getResources().getDrawable(R.drawable.icon_more_black));
            binding.setToolbarVM(toolbarViewModel);
        }
        initMenu();
    }

    public void setPreview(int preview) {
        this.preview = preview;
    }

    public void onStart() {
        super.onStart();
        viewModel.setUser(UserUtil.getInstance(getContext()).getUser());
    }

    public void saveQRCode() {
        MPermissions.requestPermissions(this, REQ_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void back() {
        getActivity().onBackPressed();
    }

    @Override
    public ContactPresenter<MyQRCodeMvpView> createPresenter() {
        return new ContactPresenter<>(getContext());
    }

    @Override
    public View getContentView() {
        return getView();
    }

    @Override
    public void goToScanQRCode() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CaptureActivityContact.class);
        startActivityForResult(intent, ContactPresenter.TASK_SEARCH_USER);
    }

    public void goToSettingFragment() {
//        SettingMyProfileFragment settingMyProfileFragment = new SettingMyProfileFragment();
//        getBaseActivity().openFragment(settingMyProfileFragment, null, true);
    }

    @Override
    public void onBack() {
        back();
    }

    @Override
    public void onNext() {
        menu.showLocation(binding.getRoot().findViewById(R.id.right_icon));
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId) {
            case ContactPresenter.TASK_SEARCH_USER:
                gotoProfile((Contact) data);
                break;
            case ContactPresenter.TASK_MYSELF:
                goToSettingFragment();
                break;
        }
    }

    public void gotoProfile(Contact contact) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setUserUid(contact.getUserDetail().getUserUid());
        getBaseActivity().openFragment(profileFragment);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        switch (taskId) {
            case ContactPresenter.TASK_SEARCH_USER:
                showToast(getString(R.string.cannot_find_user));
                break;
//            case ContactPresenter.ERROR_NETWORK:
//                showToast(getString(R.string.access_fail));
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ContactPresenter.TASK_SEARCH_USER:
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String result = bundle.getString("result");
                        if (EmailUtil.getInstance(getContext()).isEmail(result)) {
                            presenter.findFriend(result);
                        } else {
                            onTaskError(ContactPresenter.TASK_SEARCH_USER, null);
                        }
                    }
                }
        }
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
            case REQ_STORAGE: {
                if (allPermissionGranted(grantResults)) {
                    startSave();
                } else {
                    showToast(getString(R.string.need_permission));
                }
                break;
            }
        }
    }

    @PermissionGrant(REQ_STORAGE)
    public void startSave(){
        View view = binding.QRCodeView;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            try {
//                File extDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                String filename = "Timegenii/qr" + System.currentTimeMillis() + ".png";

                File extDir = Environment.getExternalStorageDirectory();
                String filename = "qr_" + System.currentTimeMillis()+".png";
                File folder = new File(extDir, "Timegenii");
                if(!folder.exists()){
                    folder.mkdir();
                }
                File file = new File(folder.getPath(), filename);
                if(!file.exists()) {
                    file.createNewFile();
                }
                file.setWritable(Boolean.TRUE);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100, out);
                out.flush();
                out.close();
                showToast(getString(R.string.save_qr_code_success));
            } catch (Exception e) {
                e.printStackTrace();
                showToast(getString(R.string.save_qr_code_failed));
            }
        } else {
            showToast(getString(R.string.save_qr_code_failed));
        }
    }

    @PermissionDenied(REQ_STORAGE)
    public void permissionError(){
        showToast(getString(R.string.need_permission));
    }
}
