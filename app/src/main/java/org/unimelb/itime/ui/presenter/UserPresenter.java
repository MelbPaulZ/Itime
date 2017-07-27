package org.unimelb.itime.ui.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Domain;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static org.unimelb.itime.util.EventUtil.latitude;
import static org.unimelb.itime.util.EventUtil.longitude;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class UserPresenter<V extends TaskBasedMvpView<User>> extends MvpBasePresenter<V>
        implements GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "UserPresenter";

    public static final int TASK_USER_UPDATE = 0;
    public static final int TASK_USER_PSW_NOT_MATCH = 1;
    public static final int TASK_NETWORK_FAILED = 2;
    public static final int TASK_PSW_WRONG_LENGTH = 3;
    public static final int TASK_UPDATE_TIMESLOT = 4;
    public static final int TASK_LOCATION = 5;
    public static final int REQUEST_PERMISSIONS = 6;
    public static final int TASK_OPEN_LOCATION_SERVIE = 7;

    private Context context;
    private UserApi userApi;

    private LocationRequest mLocationRequest;
    private GoogleApiClient googleApiClient;

    public UserPresenter(Context context) {
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void updateProfile(User user) {
        if (getView() != null) {
            getView().onTaskStart(TASK_USER_UPDATE);
        }

        Observable<HttpResult<User>> observable = userApi.updateProfile(user);
        ItimeSubscriber<HttpResult<User>> subscriber = new ItimeSubscriber<HttpResult<User>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_USER_UPDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<User> userHttpResult) {
                //update UserLoginRes
                UserUtil.getInstance(getContext()).getUserLoginRes().setUser(userHttpResult.getData());
                //update sharedPreference
                UserUtil.getInstance(getContext()).saveLoginUser(UserUtil.getInstance(getContext()).getUserLoginRes());

                if (getView() != null) {
                    getView().onTaskSuccess(TASK_USER_UPDATE, userHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    /**
     * Added by Qiushuo Huang
     */
    public void updateTimezone() {
        UserApi userApi = HttpUtil.createService(getContext(), UserApi.class);

        String timeZone = AppUtil.getCurrentTimeZone();

        HashMap<String, String> body = new HashMap<>();
        body.put("timezone", timeZone);

        Observable<HttpResult<Object>> observable = userApi.updateTimezone(body);
        ItimeSubscriber<HttpResult<Object>> subscriber = new ItimeSubscriber<HttpResult<Object>>() {
            @Override
            public void onHttpError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<Object> objectHttpResult) {
                if (objectHttpResult.getStatus() == 0) {
                    Log.i("TimeZone", "update success");
                } else {
                    Log.i("TimeZone", "update failed");
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void updatePassword(String psw, String confirmedPsw) {
        if (getView() != null) {
            getView().onTaskStart(TASK_USER_UPDATE);
        }

        HashMap<String, Object> pswPackage = new HashMap<>();
        pswPackage.put("password", psw);
        pswPackage.put("password_confirmation", confirmedPsw);

        Observable<HttpResult<User>> observable = userApi.updatePassword(pswPackage);
        ItimeSubscriber<HttpResult<User>> subscriber = new ItimeSubscriber<HttpResult<User>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_USER_UPDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<User> userHttpResult) {
                if (getView() == null) {
                    return;
                }
                if (userHttpResult.getStatus() == -2001) {
                    getView().onTaskError(TASK_PSW_WRONG_LENGTH, null);
                    return;
                }
                if (userHttpResult.getStatus() != 1) {
                    getView().onTaskError(TASK_NETWORK_FAILED, null);
                } else
                    getView().onTaskSuccess(TASK_USER_UPDATE, userHttpResult.getData());
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void checkLocationService(){
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates settingStatus = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getCurrentLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        if (getView()!=null){
                            ((SettingRegionMvpView)getView()).onChangeLocationSetting();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                       if(getView()!=null){
                           getView().onTaskError(TASK_NETWORK_FAILED, null);
                       }
                        break;
                }
            }
        });
    }

    /**
     * this method is to get current location, will call location presenter to connect google api client,
     * in the call back method, onConnect, the location latitude and longitude will be gotten.
     */
    public void getCurrentLocation() {
        if (getView() != null) {
            getView().onTaskStart(0);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location == null) {
            getCurrentLocationFromGoogle(googleApiClient);
        } else {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            asynchronizedGetAddress(latitude, longitude);
        }
    }

    private void getCurrentLocationFromGoogle(GoogleApiClient googleApiClient){

        googleApiClient.connect();
        if (googleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(googleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
                    if(!placeLikelihoods.getStatus().isSuccess()){
                        if (getView()!=null) {
                            getView().onTaskError(TASK_LOCATION, null);
                        }
                        placeLikelihoods.release();
                        return;
                    }

                    double latitude=0.0;
                    double longitude = 0.0;
                    float maxLikelihood=0.0f;
                    for (PlaceLikelihood place: placeLikelihoods){
                        if (place.getLikelihood() > maxLikelihood){
                            maxLikelihood = place.getLikelihood();
                            latitude = place.getPlace().getLatLng().latitude;
                            longitude = place.getPlace().getLatLng().longitude;
                        }
                    }

                    latitude = latitude;
                    longitude = longitude;
                    asynchronizedGetAddress(latitude, longitude);
                    placeLikelihoods.release();
                }
            });
        }
    }

    /**
     * Use java rx to observe and subscribe get location method.
     * @param latitude
     * @param longitude
     */
    private void asynchronizedGetAddress(final double latitude, final double longitude) {
        Observable observable = Observable.create(new Observable.OnSubscribe<List<Address>>() {
            @Override
            public void call(Subscriber<? super List<Address>> subscriber) {
                subscriber.onNext(getAddress(latitude, longitude));
            }
        });

        ItimeSubscriber<List<Address>> subscriber = new ItimeSubscriber<List<Address>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_LOCATION, null);
                }
            }

            @Override
            public void onNext(List<Address> addresses) {
                Address address = addresses.get(0);
                String location = address.getAdminArea() + ", " + address.getCountryName();
                if (getView() != null) {
                    ((SettingRegionMvpView) getView()).onCurrentLocationSuccess(location);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    /**
     * call google GeoCoder to get current location information based on latitude and longitude
     * @param latitude
     * @param longitude
     * @return
     */
    private List<Address> getAddress(double latitude, double longitude) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    // for google
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            });
            checkLocationService();

        } else {
            //If everything went fine lets get latitude and longitude
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            asynchronizedGetAddress(latitude, longitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");
    }

    public void fetchDomainsFromServer(){
        Observable<HttpResult<List<Domain>>> observable = userApi.getDomainList()
                .map(new Func1<HttpResult<List<Domain>>, HttpResult<List<Domain>>>() {
            @Override
            public HttpResult<List<Domain>> call(HttpResult<List<Domain>> result) {
                if(result.getStatus()!=1){

                }else{
                    DBManager dbManager = DBManager.getInstance(context);
                    List<Domain> list = result.getData();
                    dbManager.insertOrReplace(list);
                }
                return result;
            }
        });
        Subscriber subscriber = new Subscriber<HttpResult<List<Domain>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(-1, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Domain>> result) {
                if(result.getStatus()!=1){

                }else{

                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public List<Domain> getDomains(){
        DBManager dbManager = DBManager.getInstance(context);
        return dbManager.getAllDomains();
    }
}
