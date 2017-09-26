package org.unimelb.itime.ui.fragment.event;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.zhy.m.permission.MPermissions;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.databinding.FragmentEventLocationBinding;
import org.unimelb.itime.ui.mvpview.event.EventLocationMvpView;
import org.unimelb.itime.ui.presenter.EventLocationPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventLocationViewModel;
import org.unimelb.itime.util.LocationUtil;

import java.util.ArrayList;


/**
 * Created by Paul on 8/6/17.
 */

public class FragmentEventLocation extends Fragment
implements ToolbarInterface, EventLocationMvpView, GoogleApiClient.OnConnectionFailedListener{

    private String location;
    private FragmentEventLocationBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private EventLocationViewModel vm;
    private EventLocationPresenter.PlaceAutoCompleteAdapter mAdapter;
    private EventLocationPresenter eventLocationPresenter;

    protected GoogleApiClient mGoogleApiClient;

    public void setLocation(String location) {
        this.location = location;
    }

    public EventLocationPresenter<EventLocationMvpView> createPresenter() {
        initGoogleApiClient();
        return new EventLocationPresenter<>(getContext(), mGoogleApiClient, getActivity(), this);
    }

    private void initGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), 0 /* clientId */,this )
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private void initAutoCompleteAdapter(){
        mAdapter = new EventLocationPresenter.PlaceAutoCompleteAdapter(getContext(), mGoogleApiClient, getLocationBounds(), null);
        mAdapter.setMvpView(this);
        eventLocationPresenter.setmAdapter(mAdapter);
    }

    private LatLngBounds getLocationBounds(){
        return new LatLngBounds(new LatLng(LocationUtil.mLatitude - 0.1, LocationUtil.mLongitude - 0.1),
                new LatLng(LocationUtil.mLatitude + 0.1 , LocationUtil.mLongitude + 0.1));
    }

    private void recordLocation(double latitude, double longitude){
        LocationUtil.mLatitude = latitude;
        LocationUtil.mLongitude = longitude;

    }

    private void updateAdapterBound(double latitude, double longitude){
        mAdapter.setBounds(new LatLngBounds(new LatLng(latitude-0.1, longitude-0.1), new LatLng(latitude+0.1, latitude+0.1)));
    }

    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, EventLocationPresenter.REQ_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestPermission();
        eventLocationPresenter = createPresenter();
        initAutoCompleteAdapter();

        vm = new EventLocationViewModel(eventLocationPresenter);
        vm.setLocationString1(location);
        binding.setVm(vm);

        toolbarViewModel= new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.toolbar_location));
        toolbarViewModel.setRightText(getString(R.string.toolbar_done));
        toolbarViewModel.setRightEnable(true);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_location, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onNext() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.location_string1), vm.getLocationString1());
        intent.putExtra(getString(R.string.location_string2), vm.getLocationString2());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBack() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCurrentLocationSuccess(PlaceLikelihoodBuffer placeLikelihoods) {
        vm.setPlaceLikelihoods(placeLikelihoods);
        if (placeLikelihoods.getCount()==0){
            return;
        }
        double lat = placeLikelihoods.get(0).getPlace().getLatLng().latitude;
        double lon = placeLikelihoods.get(0).getPlace().getLatLng().longitude;
        recordLocation(lat, lon);
        updateAdapterBound(lat, lon);
    }


    @Override
    public void onCurrentLocationFailed(String errorMsg) {

    }

    @Override
    public void onAutoCompletePlaces(ArrayList<AutocompletePrediction> predictions) {
        vm.setSearchResults(predictions);
    }

    @Override
    public void onChooseLocation(String location1, String location2) {
        onNext();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        eventLocationPresenter.currentLocationValidation();
    }


}
