package org.unimelb.itime.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;

/**
 * Created by Paul on 8/6/17.
 */

public class EventLocationPresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {

    protected GoogleApiClient mGoogleApiClient;

    public EventLocationPresenter(Context context, GoogleApiClient mGoogleApiClient) {
        super(context);
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public void getCurrentLocation(){

    }
}
