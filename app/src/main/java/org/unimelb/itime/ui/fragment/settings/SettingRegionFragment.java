package org.unimelb.itime.ui.fragment.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSettingRegionBinding;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.setting.SettingRegionViewModel;
import org.unimelb.itime.util.PermissionUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;

import static org.unimelb.itime.R.drawable.icon_bluelocation_1;

/**
 * Created by Paul on 30/1/17.
 */

public class SettingRegionFragment extends SettingRegionBaseFragment
    implements SettingRegionMvpView {

    private FragmentSettingRegionBinding binding;
    private View headerView;
    private boolean isGetCurrentRegion = false;
    private ImageView icon; // current location icon
    private TextView tv; // current location
    private final static int REQ_LOCATION = 100;
    public final static int REQ_LOCATION_SETTING = 200;
    private boolean needToGetLocation; // false when not come from my profile fragment
    private String currentLocation = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needToGetLocation = true;
    }

    @Override
    public UserPresenter<SettingRegionMvpView> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_region, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ToolbarViewModel toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.region));
        binding.setToolbarVM(toolbarViewModel);

        contentViewModel = new SettingRegionViewModel(getPresenter(), -1); // -1 refers to init countries
        binding.setContentVM(contentViewModel);
        initHeaderView();

    }

    private void initHeaderView(){
        ListView regionList = (ListView) getActivity().findViewById(R.id.region_list);
        headerView = View.inflate(getContext(), R.layout.listview_region_header, null);
        headerView.findViewById(R.id.current_location_cell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    // if user not permit, click this then return
                    return;
                }
                if (!isGetCurrentRegion){
                    checkLocationService();
                    return;
                }
                contentViewModel.getUser().setRegion((String) tv.getText());
                presenter.updateProfile(contentViewModel.getUser());
            }
        });

        regionList.addHeaderView(headerView);
        icon = (ImageView) getActivity().findViewById(R.id.icon_current_location);
        tv = (TextView) getActivity().findViewById(R.id.current_location_textview);
        if (isGetCurrentRegion){
            icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_bluelocation_1));
            tv.setText(currentLocation);
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onTaskError(UserPresenter.REQUEST_PERMISSIONS,null);

            // if havent had permission, then request permission
            if (needToGetLocation) {
                requestPermissions(
                        PermissionUtil.needPermissions(getContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION),
                        REQ_LOCATION);
            }
            return;
        }

        if (needToGetLocation) {
            getPresenter().getCurrentLocation();
        }
    }


    private void checkLocationService(){
        presenter.checkLocationService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION){
            if (allPermissionGranted(grantResults)){
                getLocation();
            }else{
                permissionError();
            }
        }
    }

    @PermissionGrant(REQ_LOCATION)
    public void getLocation(){
        if (!isGetCurrentRegion){
            checkLocationService();
        }else{
            getCurrentLocation();
        }
    }

    @PermissionDenied(REQ_LOCATION)
    public void permissionError(){
        showToast(getString(R.string.need_permission));
    }

    public void getCurrentLocation(){
        presenter.getCurrentLocation();
    }



    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void toSelectChildRegion(long locationId) {
        needToGetLocation = false;
        SettingRegionCityFragment fragment = new SettingRegionCityFragment();
        fragment.setTargetFragment(getTargetFragment(), SettingMyProfileFragment.REQ_REGION);
        fragment.setLocationId(locationId);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onCurrentLocationSuccess(String location) {
        hideProgressDialog();
        icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_bluelocation_1));
        tv.setText(location);
        currentLocation = location;
        isGetCurrentRegion = true;
    }

    @Override
    public void onChangeLocationSetting() {
        showTwoButtonDialog(
                "please turn on location service in setting",
                getString(R.string.cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTaskError(UserPresenter.TASK_OPEN_LOCATION_SERVIE, null);
                    }
                },
                getString(R.string.dialog_ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(callGPSSettingIntent , REQ_LOCATION_SETTING);
                    }
                }
        );
    }


    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        if (taskId== UserPresenter.TASK_LOCATION) {
            icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_red_location));
            tv.setText(getString(R.string.unable_to_access_your_current_location));
            isGetCurrentRegion = false;
        }else if (taskId == UserPresenter.TASK_OPEN_LOCATION_SERVIE){
            showToast("Please go to setting and open location setting");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_LOCATION_SETTING){
            presenter.getCurrentLocation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
