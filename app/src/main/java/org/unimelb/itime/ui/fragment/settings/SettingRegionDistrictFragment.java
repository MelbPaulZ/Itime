package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentRegionCityBinding;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.setting.SettingRegionViewModel;

/**
 * Created by Paul on 6/2/17.
 */

public class SettingRegionDistrictFragment extends SettingRegionBaseFragment
        implements SettingRegionMvpView {
    private FragmentRegionCityBinding binding;
    private long locationId;
    @Override
    public UserPresenter<SettingRegionMvpView> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_region_city, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ToolbarViewModel toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.region));
        binding.setToolbarVM(toolbarViewModel);

        contentViewModel= new SettingRegionViewModel(getPresenter(), locationId);
        binding.setContentVM(contentViewModel);
        initListViewHeader();
    }

    private void initListViewHeader(){
        ListView cityList = (ListView) getActivity().findViewById(R.id.city_list);
        View header = View.inflate(getContext(), R.layout.listview_city_header, null);
        cityList.addHeaderView(header);
    }

    public void setLocationId(long locationId){
        this.locationId = locationId;
    }

    @Override
    public void toSelectChildRegion(long locationId) {

    }

    @Override
    public void onCurrentLocationSuccess(String location) {

    }

    @Override
    public void onChangeLocationSetting() {

    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        presenter.updateProfile(contentViewModel.getUser());
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        super.onTaskError(taskId, data);
    }
}
