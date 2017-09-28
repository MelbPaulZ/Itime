package org.unimelb.itime.ui.viewmodel.setting;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Region;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.SettingRegionMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.util.UserUtil;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * Created by Paul on 30/1/17.
 */

public class SettingRegionViewModel extends BaseObservable {

    private Context context;
    private UserPresenter<SettingRegionMvpView> presenter;

    private ObservableList<RegionWrapper> items = new ObservableArrayList<>();
    private ItemBinding itemView = ItemBinding.of(BR.regionWrapper, R.layout.listview_region);

    private String selectLocationStr="";
    private User user;


    /**
     *
     * @param presenter
     * @param locationId {-1,locationId}  -1 refers to get all countries,
     *                   locationId refers to get cities where belongs to this locationId
     */
    public SettingRegionViewModel(UserPresenter<SettingRegionMvpView> presenter, long locationId) {
        this.presenter = presenter;
        this.context = presenter.getContext();
        user = UserUtil.getInstance(context).copyUser();
        if (locationId == -1) {
            init();
        }else{
            init(locationId);
        }
    }

    @Bindable
    public ObservableList<RegionWrapper> getItems() {
        return items;
    }

    @Bindable
    public ItemBinding getItemView() {
        return itemView;
    }

    private void init(long locationId){
        items.clear();
        List<Region> cityList = DBManager.getInstance(context).getChildRegionList(locationId);
        for (Region region: cityList){
            items.add(new RegionWrapper(region, region.getHasChild()));
        }
    }

    private void init(){
        if (items.size() == 0) {
            for (Region region : DBManager.getInstance(context).getCountryList()) {
                items.add(new RegionWrapper(region, region.getHasChild()));
            }
        }

    }

    public ListView.OnItemClickListener onClickRegion(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    return;
                }
                // the position needs to be position-1, because the
                RegionWrapper selectedWrapper = items.get(position-1);
                int currentSelection = selectedWrapper.getIsSelected();
                unselectAllWrapper(items);
                selectedWrapper.setSelected(currentSelection == 0 ? 1 : 0);

                if (presenter.getView()==null){
                    return;
                }


                if (selectedWrapper.hasChildRegion==0){
                    // finish pick location
                    String parentLocationStr = "";
                    if (selectedWrapper.getRegion().getParentId()!=-1){
                        Region parentRegion = DBManager.getInstance(context).findCountry(selectedWrapper.getRegion().getParentId());
                        parentLocationStr = parentRegion.getName();
                        if (parentRegion.getParentId()!=-1){
                            parentLocationStr = DBManager.getInstance(context).findCountry(parentRegion.getParentId()).getName();
                        }
                    }

                    if (parentLocationStr.equals("")){
                        selectLocationStr = selectedWrapper.getRegion().getName();
                    }else{
                        selectLocationStr = selectedWrapper.getRegion().getName() + ", " + parentLocationStr;
                    }
                    user.setRegion(selectLocationStr);
                    presenter.getView().finishSelect(user);
                }else if (selectedWrapper.getIsSelected() == 1) {
                    // select children location
                    presenter.getView().toSelectChildRegion(selectedWrapper.getRegion().getLocationId());
                }
            }
        };
    }


    /**
     * call thie method to save current region selection and update server
     * @return
     */
    public User getUser() {
        return user;
    }


    /**
     * this method is for counter-selecting all countries
     */
    private void unselectAllWrapper(List<RegionWrapper> wrapperList){
        for (RegionWrapper wrapper: wrapperList){
            wrapper.setSelected(0);
        }
    }


    public static class RegionWrapper extends BaseObservable {
        private int isSelected; // 0 = false, 1 = true
        private Region region;
        private int hasChildRegion; // 0 = false, 1 = true

        public RegionWrapper(Region region, int hasChildRegion) {
            this.region = region;
            this.isSelected = 0;
            this.hasChildRegion = hasChildRegion;
        }

        @Bindable
        public int getIsSelected() {
            return isSelected;
        }


        public void setSelected(int selected) {
            isSelected = selected;
            notifyPropertyChanged(BR.isSelected);
        }

        @Bindable
        public Region getRegion() {
            return region;
        }

        public void setRegion(Region region) {
            this.region = region;
            notifyPropertyChanged(BR.region);
        }

        @Bindable
        public int getHasChildRegion() {
            return hasChildRegion;
        }

        public void setHasChildRegion(int hasChildRegion) {
            this.hasChildRegion = hasChildRegion;
            notifyPropertyChanged(BR.hasChildRegion);
        }
    }
}
