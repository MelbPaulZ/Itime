package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.developer.paul.itimerecycleviewgroup.LogUtil;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.MyRecyclerViewAdapter;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.mvpview.event.EventLocationMvpView;
import org.unimelb.itime.ui.presenter.EventLocationPresenter;
import org.unimelb.itime.util.LocationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 8/6/17.
 */

public class EventLocationViewModel extends ItimeBaseViewModel {

    public int TYPE_RECENT_TITLE = 100;
    public int TYPE_RECENT = 101;
    public int TYPE_NEARBY = 102;
    public int TYPE_NEARBY_TITLE;

    private int nearbyLocationNum = 3;

    private String location;
    private PlaceLikelihoodBuffer placeLikelihoods;

    private EventLocationPresenter<EventLocationMvpView> presenter;

    // this is for no location use
    private List<LocationRowViewModel> locationRows = new ArrayList<>();
    private OnItemBind<LocationRowViewModel> onItemBind = new OnItemBind<LocationRowViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, LocationRowViewModel item) {
            if (item.type==TYPE_RECENT_TITLE||item.type==TYPE_NEARBY_TITLE){
                itemBinding.set(BR.locationRowVm,R.layout.row_location_title);
            }else{
                itemBinding.set(BR.locationRowVm, R.layout.row_location_content);
            }
        }
    };


    //this is for has location use
    private List<LocationRowViewModel> searchedLocationRows = new ArrayList<>();
    public ItemBinding<LocationRowViewModel> searchedLocationItemBinding = ItemBinding.of(BR.locationRowVm, R.layout.row_location_content);



    // get new current location
    public void setPlaceLikelihoods(PlaceLikelihoodBuffer placeLikelihoods) {
        this.placeLikelihoods = placeLikelihoods;
        List<PlaceLikelihood> topLists = findTopLikelihoods(placeLikelihoods);
        updateLocationRow(topLists);
    }

    private void updateLocationRow(List<PlaceLikelihood> topList){

        List<LocationRowViewModel> locationRowViewModels = new ArrayList<>();
        if (topList.size()==0){
            // no nearby location fetched
            return;
        }

        // add nearby locations
        locationRowViewModels.add(getNearbyTitle());
        int nearbySize = topList.size() <= nearbyLocationNum ? topList.size() : nearbyLocationNum;
        int start = 1;
        for (PlaceLikelihood placelikelihood: topList.subList(0, nearbySize)){
            Place place = placelikelihood.getPlace();
            locationRowViewModels.add(start, new LocationRowViewModel((String)place.getName(), (String)place.getAddress(), TYPE_NEARBY));
            start++;
        }

        locationRowViewModels.add(getRecentTitle());

        for (LocationRowViewModel locationRowViewModel: getRecentRows()){
            locationRowViewModels.add(locationRowViewModel);
        }

        setLocationRows(locationRowViewModels);

    }

    private List<PlaceLikelihood> findTopLikelihoods(PlaceLikelihoodBuffer placeLikelihoods){
        List<PlaceLikelihood> topPossibleList = new ArrayList<>();
        for (PlaceLikelihood placeLikelihood: placeLikelihoods){
            topPossibleList.add(placeLikelihood);
        }

        Collections.sort(topPossibleList, new Comparator<PlaceLikelihood>() {
            @Override
            public int compare(PlaceLikelihood o1, PlaceLikelihood o2) {
                return (int) (o1.getLikelihood()*1000 - o2.getLikelihood()*1000);
            }
        });

        return topPossibleList;
    }


    public EventLocationViewModel(EventLocationPresenter<EventLocationMvpView> presenter) {
        this.presenter = presenter;
        mockData();
    }

    public TextWatcher locationInputWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    presenter.filterLocation(s.toString());
                }
            }
        };
    }

    @Bindable
    public List<LocationRowViewModel> getSearchedLocationRows() {
        return searchedLocationRows;
    }

    public void setSearchedLocationRows(List<LocationRowViewModel> searchedLocationRows) {
        this.searchedLocationRows = searchedLocationRows;
        notifyPropertyChanged(BR.searchedLocationRows);
    }

    @Bindable
    public List<LocationRowViewModel> getLocationRows() {
        return locationRows;
    }

    public void setLocationRows(List<LocationRowViewModel> locationRows) {
        this.locationRows = locationRows;
        notifyPropertyChanged(BR.locationRows);
    }

    @Bindable
    public OnItemBind<LocationRowViewModel> getOnItemBind() {
        return onItemBind;
    }

    public void setOnItemBind(OnItemBind<LocationRowViewModel> onItemBind) {
        this.onItemBind = onItemBind;
        notifyPropertyChanged(BR.onItemBind);
    }

    private void mockData(){
        locationRows.add(new LocationRowViewModel(presenter.getContext().getString(R.string.location_nearby),"", TYPE_NEARBY_TITLE));
        locationRows.add(new LocationRowViewModel("Doug McDonell Build", "123456", TYPE_NEARBY));
        locationRows.add(new LocationRowViewModel("House of Cards", "09876544321", TYPE_NEARBY));
        locationRows.add(new LocationRowViewModel("Asssss ddd gfaofaou", "1231231 asdasdasd asdqqrqrewrds adas da dsdad ad ags ea d!! asdasdasdasdasdasadsdasdassaassasasas", TYPE_NEARBY));

        locationRows.add(new LocationRowViewModel(presenter.getContext().getString(R.string.location_recent), "", TYPE_RECENT_TITLE));
        for (LocationRowViewModel rowViewModel: getRecentRows()){
            locationRows.add(rowViewModel);
        }

//        searchedLocationRows.add(new LocationRowViewModel("Doug McDonell Build", "123456", TYPE_NEARBY));
//        searchedLocationRows.add(new LocationRowViewModel("Asssss ddd gfaofaou", "1231231 asdasdasd asdqqrqrewrds adas da dsdad ad ags ea d!! asdasdasdasdasdasadsdasdassaassasasas", TYPE_NEARBY));
//        searchedLocationRows.add(new LocationRowViewModel("asdad", "123 ", TYPE_NEARBY));
//        searchedLocationRows.add(new LocationRowViewModel("asa", "asadsda", TYPE_NEARBY));

    }

    public void setSearchResults(ArrayList<AutocompletePrediction> predictions){
        List<LocationRowViewModel> searchedRstList = new ArrayList<>();
        for (AutocompletePrediction autocompletePrediction: predictions){
            searchedRstList.add(new LocationRowViewModel((String)autocompletePrediction.getPrimaryText(null), (String)autocompletePrediction.getSecondaryText(null),TYPE_NEARBY));
        }

        setSearchedLocationRows(searchedRstList);
    }

    private LocationRowViewModel getNearbyTitle(){
        return new LocationRowViewModel(presenter.getContext().getString(R.string.location_nearby),"", TYPE_NEARBY_TITLE);
    }

    private LocationRowViewModel getRecentTitle(){
        return new LocationRowViewModel(presenter.getContext().getString(R.string.location_recent), "", TYPE_RECENT_TITLE);
    }

    private List<LocationRowViewModel> getRecentRows(){
        List<LocationRowViewModel> recentRows = new ArrayList<>();
        recentRows.add(new LocationRowViewModel("Recent Address One", "1231231faf asda d faga fas asda a asdad  asda ad adasdas as a", TYPE_RECENT));
        recentRows.add(new LocationRowViewModel("asdad", "123 ", TYPE_RECENT));
        recentRows.add(new LocationRowViewModel("asa", "asadsda", TYPE_RECENT));
        recentRows.add(new LocationRowViewModel("asa", "asadsda", TYPE_RECENT));
        return recentRows;
    }

    public View.OnClickListener onClickClean(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation("");
                closeKeyBoard(v);
            }
        };
    }

    public int getNoLocationVisibility(String location){
        if (location.equals("")){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int getHasLocationVisibility(String location){
        return getNoLocationVisibility(location) == View.VISIBLE? View.GONE: View.VISIBLE;
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public class LocationRowViewModel extends BaseObservable{
        private int type;
        private String str1;
        private String str2;


        public LocationRowViewModel(String str1, String str2, int type) {
            this.str1 = str1;
            this.str2 = str2;
            this.type = type;
        }

        public View.OnClickListener onClickLocation(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocation(str1);
                }
            };
        }

        @Bindable
        public String getStr1() {
            return str1;
        }

        public void setStr1(String str1) {
            this.str1 = str1;
            notifyPropertyChanged(BR.str1);
        }

        @Bindable
        public String getStr2() {
            return str2;
        }

        public void setStr2(String str2) {
            this.str2 = str2;
            notifyPropertyChanged(BR.str2);
        }
    }


}
