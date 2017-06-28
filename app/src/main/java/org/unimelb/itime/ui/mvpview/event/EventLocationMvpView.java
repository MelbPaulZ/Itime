package org.unimelb.itime.ui.mvpview.event;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.ArrayList;

/**
 * Created by Paul on 8/6/17.
 */

public interface EventLocationMvpView extends ItimeBaseMvpView, TaskBasedMvpView{
    void onCurrentLocationSuccess(PlaceLikelihoodBuffer placeLikelihoods);
    void onCurrentLocationFailed(String errorMsg);
    void onAutoCompletePlaces(ArrayList<AutocompletePrediction> predictions);
    void onChooseLocation(String location1, String location2);
}
