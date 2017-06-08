package org.unimelb.itime.ui.viewmodel.event;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.mvpview.event.EventLocationMvpView;
import org.unimelb.itime.ui.presenter.EventLocationPresenter;
import org.unimelb.itime.ui.presenter.LocalPresenter;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 8/6/17.
 */

public class EventLocationViewModel extends ItimeBaseViewModel {

    private String location;

    private EventLocationPresenter<EventLocationMvpView> presenter;

    // this is for no location use
    public ObservableList<LocationRowViewModel> locationRows = new ObservableArrayList<>();
    public final OnItemBind<LocationRowViewModel> onItemBind = new OnItemBind<LocationRowViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, LocationRowViewModel item) {
            if (position==0||position==4){
                itemBinding.set(BR.locationRowVm,R.layout.row_location_title);
            }else{
                itemBinding.set(BR.locationRowVm, R.layout.row_location_content);
            }
        }
    };


    //this is for has location use
    public ObservableList<LocationRowViewModel> searchedLocationRows = new ObservableArrayList<>();
    public ItemBinding<LocationRowViewModel> searchedLocationItemBinding = ItemBinding.of(BR.locationRowVm, R.layout.row_location_content);



    public EventLocationViewModel(EventLocationPresenter<EventLocationMvpView> presenter) {
        this.presenter = presenter;
        mockData();
    }

    private void mockData(){
        locationRows.add(new LocationRowViewModel(presenter.getContext().getString(R.string.location_nearby),""));
        locationRows.add(new LocationRowViewModel("Doug McDonell Build", "123456"));
        locationRows.add(new LocationRowViewModel("House of Cards", "09876544321"));
        locationRows.add(new LocationRowViewModel("Asssss ddd gfaofaou", "1231231 asdasdasd asdqqrqrewrds adas da dsdad ad ags ea d!! asdasdasdasdasdasadsdasdassaassasasas"));

        locationRows.add(new LocationRowViewModel(presenter.getContext().getString(R.string.location_recent), ""));
        locationRows.add(new LocationRowViewModel("Recent Address One", "1231231faf asda d faga fas asda a asdad  asda ad adasdas as a"));
        locationRows.add(new LocationRowViewModel("asdad", "123 "));
        locationRows.add(new LocationRowViewModel("asa", "asadsda"));
        locationRows.add(new LocationRowViewModel("asa", "asadsda"));


        searchedLocationRows.add(new LocationRowViewModel("Doug McDonell Build", "123456"));
        searchedLocationRows.add(new LocationRowViewModel("Asssss ddd gfaofaou", "1231231 asdasdasd asdqqrqrewrds adas da dsdad ad ags ea d!! asdasdasdasdasdasadsdasdassaassasasas"));
        searchedLocationRows.add(new LocationRowViewModel("asdad", "123 "));
        searchedLocationRows.add(new LocationRowViewModel("asa", "asadsda"));

    }

    public View.OnClickListener onClickClean(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation("");
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
        private String str1;
        private String str2;

        public LocationRowViewModel(String str1, String str2) {
            this.str1 = str1;
            this.str2 = str2;
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
