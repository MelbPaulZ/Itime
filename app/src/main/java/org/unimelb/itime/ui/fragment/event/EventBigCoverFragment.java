package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentEventBigphotoBinding;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.event.EventBigPhotoMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.presenter.event.EventBigPhotoPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventBigPhotoViewModel;

import me.fesky.library.widget.ios.AlertDialog;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public class EventBigCoverFragment extends ItimeBaseFragment<TaskBasedMvpView, EventCreatePresenter<TaskBasedMvpView>> implements TaskBasedMvpView, ToolbarInterface {
    private ToolbarViewModel toolbarViewModel;
    private FragmentEventBigphotoBinding binding;
    private EventBigPhotoViewModel viewModel;
    private ObservableList<PhotoUrl> photos;
    private Event event;
    private int position;
    private boolean editable;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_bigphoto, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventBigPhotoViewModel(getPresenter());
        initToolbar();
        binding.setViewModel(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void loadData(){
        viewModel.setPhotos(photos);
        viewModel.setPosition(position);
        viewModel.setSize(photos.size());
        viewModel.setPageChangeListener(getOnPageChangeListener());
        toolbarViewModel.setTitle("");
        binding.executePendingBindings();
        binding.viewpager.setCurrentItem(position);
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPosition(position);
                toolbarViewModel.setTitle(getTitleString());
                viewModel.setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private String getTitleString(){
        return (position+1)+"/"+photos.size();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    private void initToolbar() {
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.photo_title));
        toolbarViewModel.setRightEnable(true);
        toolbarViewModel.setRightText(getString(R.string.toolbar_select));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPhotos(ObservableList<PhotoUrl> photos) {
        this.photos = photos;
    }

    @Override
    public EventCreatePresenter createPresenter() {
        return new EventCreatePresenter(getActivity());
    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        selectCover();
    }

    private void selectCover(){
        presenter.updateCover(photos.get(position), event);
    }

    @Override
    public void onTaskStart(int taskId) {
        progressDialog.show();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        progressDialog.hide();
        getFragmentManager().popBackStack(ChangeCoverFragment.class.getSimpleName(), 0);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        progressDialog.hide();
        Toast.makeText(getContext(), R.string.network_error_please_try_again, Toast.LENGTH_SHORT).show();
    }
}
