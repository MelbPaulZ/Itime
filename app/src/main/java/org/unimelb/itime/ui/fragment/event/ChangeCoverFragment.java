package org.unimelb.itime.ui.fragment.event;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
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
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentChangeCoverBinding;
import org.unimelb.itime.ui.activity.CameraActivity;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventBigPhotoViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.widget.PicassoImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/9/15.
 */

public class ChangeCoverFragment extends ItimeBaseFragment<TaskBasedMvpView, EventCreatePresenter<TaskBasedMvpView>> implements TaskBasedMvpView, ToolbarInterface {
    private ToolbarViewModel toolbarViewModel;
    private FragmentChangeCoverBinding binding;
    private Event event;
    public final static int REQ_PHOTO = 1004;

    public final static int REQUEST_CAMERA_PERMISSION = 100;
    public final static int REQUEST_PHOTO_PERMISSION = 101;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_cover, container, false);
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.event_change_cover_title));
        binding.collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCoverCollections();
            }
        });
        binding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MPermissions.requestPermissions(ChangeCoverFragment.this, REQUEST_CAMERA_PERMISSION, Manifest.permission.CAMERA);
            }
        });
        binding.chooseFromPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MPermissions.requestPermissions(ChangeCoverFragment.this, REQUEST_PHOTO_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });


        return binding.getRoot();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void toCoverCollections(){
        CoverCollectionFragment fragment = new CoverCollectionFragment();
        fragment.setEvent(event);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public EventCreatePresenter createPresenter() {
        return new EventCreatePresenter(getContext());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @PermissionGrant(REQUEST_CAMERA_PERMISSION)
    public void openCamera() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, REQ_PHOTO);
    }

    @PermissionDenied(REQUEST_PHOTO_PERMISSION)
    public void photoDenied(){
        Toast.makeText(getContext(), "need photo permission", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUEST_CAMERA_PERMISSION)
    public void cameraDenied(){
        Toast.makeText(getContext(), "need camera permission", Toast.LENGTH_SHORT).show();
    }

    @PermissionGrant(REQUEST_PHOTO_PERMISSION)
    public void startPhotoPicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setMultiMode(true);
        imagePicker.setShowCamera(false);
        imagePicker.setSelectLimit(1);

        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intent, REQ_PHOTO);
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
            case REQUEST_PHOTO_PERMISSION: {
                if (allPermissionGranted(grantResults)) {
                    startPhotoPicker();
                } else {
                    photoDenied();
                }
                break;
            }
            case REQUEST_CAMERA_PERMISSION:{
                if (allPermissionGranted(grantResults)) {
                    openCamera();
                } else {
                    cameraDenied();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            if (requestCode == REQ_PHOTO && resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(CameraActivity.KEY_RESULT);
                PhotoUrl photoUrl = EventUtil.fromStringToPhotoUrl(getContext(), result);
                presenter.uploadCover(photoUrl, event);
            }

            if (requestCode == REQ_PHOTO && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                List<PhotoUrl> photoUrls = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    photoUrls.add(EventUtil.fromStringToPhotoUrl(getContext(), images.get(i).path));
                }
                presenter.uploadCover(photoUrls.get(0), event);
            }
        }
    }

    @Override
    public void onTaskStart(int taskId) {
        progressDialog.show();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        progressDialog.hide();
        onBack();
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        progressDialog.hide();
        Toast.makeText(getContext(), R.string.network_error_please_try_again, Toast.LENGTH_SHORT).show();
    }
}
