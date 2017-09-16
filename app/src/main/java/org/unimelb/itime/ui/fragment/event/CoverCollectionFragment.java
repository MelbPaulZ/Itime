package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentChangeCoverBinding;
import org.unimelb.itime.databinding.FragmentCoverCollectionBinding;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventBigPhotoViewModel;
import org.unimelb.itime.util.CoverPhotoUtil;
import org.unimelb.itime.widget.PhotoGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/9/15.
 */

public class CoverCollectionFragment extends ItimeBaseFragment implements ToolbarInterface {
    private ToolbarViewModel toolbarViewModel;
    private FragmentCoverCollectionBinding binding;
    private Event event;
    private List<PhotoUrl> covers;
    private PhotoGridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cover_collection, container, false);
            toolbarViewModel = new ToolbarViewModel(this);
            toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
            toolbarViewModel.setTitle(getString(R.string.cover_collections));
            initCovers();
            gridView = binding.gridviewPhoto;
        }
        return binding.getRoot();
    }

    private void initCovers(){
        covers = new ArrayList<>();
        for(String url:CoverPhotoUtil.getDefaultPhotos()){
            PhotoUrl photo = new PhotoUrl();
            photo.setUrl(url);
            covers.add(photo);
        }
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        PhotoGridView.bindingPhoto(gridView, false, covers, Integer.MAX_VALUE);
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
}
