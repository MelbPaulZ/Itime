package org.unimelb.itime.ui.fragment.event;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentPhotogridBinding;
import org.unimelb.itime.ui.activity.CameraActivity;
import org.unimelb.itime.ui.mvpview.event.EventPhotoGridMvpView;
import org.unimelb.itime.ui.presenter.event.EventPhotoPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.PhotoGridViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.PicassoImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.fesky.library.widget.ios.AlertDialog;

/**
 * Created by Qiushuo Huang on 2017/1/23.
 */

public class EventPhotoFragment extends ItimeBaseFragment<EventPhotoGridMvpView, EventPhotoPresenter> implements EventPhotoGridMvpView, ToolbarInterface {
    public final static int REQ_PHOTO = 1004;
    public static int MODE_NOMAL = 1;
    public static int MODE_BIG = 1;
    private static final int CHOOSE_FROM_LIBRARY = 3333 ;
    private static final int TAKE_PHOTO = 3323 ;

    private int maxNum = 9;
    public final static int REQUEST_PHOTO_PERMISSION = 101;
    private List<String> permissionList;

    private FragmentPhotogridBinding binding;
    private Event event;
    private ObservableList<PhotoUrl> tmpPhotos;
    private ToolbarViewModel toolbarViewModel;
    private PhotoGridViewModel viewModel;
    private boolean editable = true;

    private ImagePicker imagePicker;

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    private void initImagePicker(){
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(maxNum);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(binding==null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photogrid, container, false);
        return binding.getRoot();
    }

    @Override
    public EventPhotoPresenter createPresenter() {
        return new EventPhotoPresenter(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(viewModel==null) {
            viewModel = new PhotoGridViewModel(getPresenter());
        }
        loadData();
        initToolbar();
        binding.setViewModel(viewModel);
        binding.setToolbarVM(toolbarViewModel);

        initImagePicker();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    @Override
    public void onStart(){
        super.onStart();
        loadData();
    }

    private void loadData(){
        viewModel.setPhotos(tmpPhotos);
        viewModel.setEditable(editable);
        viewModel.setMaxNum(maxNum);
    }

    private void initToolbar() {
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.photo_title));
        toolbarViewModel.setRightEnable(true);
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
    }

    public void setEvent(Event event) {
        this.event = event;
        tmpPhotos = new ObservableArrayList<>();
        tmpPhotos.addAll(event.getPhoto());
    }

    @Override
    public void onBack() {
        if(!event.getPhoto().equals(tmpPhotos)){
            openAlertDialog();
        }else{
            System.gc();
            getBaseActivity().onBackPressed();
        }
    }

    private void openAlertDialog(){
        getDialogBuidler()
                .title(getString(R.string.cancel_photos_alert_msg))
                .positiveText(getString(R.string.dialog_leave))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getBaseActivity().onBackPressed();
                    }
                })
                .negativeText(getString(R.string.cancel))
                .show();
    }

    @Override
    public void onNext() {
        event.getPhoto().clear();
        event.getPhoto().addAll(tmpPhotos);
        getBaseActivity().onBackPressed();
    }

    @Override
    public void openCamera() {
        MPermissions.requestPermissions(this, REQUEST_PHOTO_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    @Override
    public void openAlbum(){
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        imagePicker.setSelectLimit(maxNum - tmpPhotos.size());
        startActivityForResult(intent, CHOOSE_FROM_LIBRARY);
    }

    @Override
    public void openBigPhoto(int position){
        EventBigPhotoFragment bigPhotoFragment = new EventBigPhotoFragment();
        bigPhotoFragment.setPosition(position);
        bigPhotoFragment.setPhotos(viewModel.getPhotos());
        bigPhotoFragment.setEditable(editable);
        getBaseActivity().openFragment(bigPhotoFragment, null, true);
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_PHOTO && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra(CameraActivity.KEY_RESULT);
            PhotoUrl photoUrls = EventUtil.fromStringToPhotoUrl(getContext(), result);
            tmpPhotos.add(photoUrls);
            viewModel.setPhotos(tmpPhotos);
        }

        if(data!=null) {
            if (requestCode == CHOOSE_FROM_LIBRARY && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                if (data != null && (requestCode == CHOOSE_FROM_LIBRARY || requestCode == ImagePicker.REQUEST_CODE_CROP)) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    List<PhotoUrl> photoUrls = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        photoUrls.add(EventUtil.fromStringToPhotoUrl(getContext(), images.get(i).path));
                    }
                    tmpPhotos.addAll(photoUrls);
                    viewModel.setPhotos(tmpPhotos);
                }
            }
        }
    }

    /**
     * only photo related permission granted, then can go to photo picker
     */
    @PermissionGrant(REQUEST_PHOTO_PERMISSION)
    public void startPhotoPicker() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, REQ_PHOTO);
    }

    @PermissionDenied(REQUEST_PHOTO_PERMISSION)
    public void permisionDenied(){
        showToast(getString(R.string.need_permission));
    }
}
