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
import org.unimelb.itime.databinding.FragmentChangeCoverBinding;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventBigPhotoViewModel;

/**
 * Created by Qiushuo Huang on 2017/9/15.
 */

public class ChangeCoverFragment extends ItimeBaseFragment implements ToolbarInterface {
    private ToolbarViewModel toolbarViewModel;
    private FragmentChangeCoverBinding binding;
    private Event event;

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
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }
}
