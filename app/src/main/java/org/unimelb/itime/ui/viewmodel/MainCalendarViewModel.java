package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;


import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.SpinnerWrapper;
import org.unimelb.itime.ui.mvpview.calendar.CalendarMvpView;

import java.util.ArrayList;
import java.util.Calendar;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends ItimeBaseViewModel{
    public final static String TAG = "MainCalendarViewModel";

    private String toolbarTitle;
    private CalendarMvpView mvpView;
    private boolean showSpinnerMenu;

    private ArrayList<SpinnerWrapper> menuItems = new ArrayList<>();
    private ItemView menuItemView = ItemView.of(BR.wrapper, R.layout.listview_simple_menu_dropdown_item);
    private AdapterView.OnItemClickListener onMenuSpinnerClicked;


    @Bindable
    public AdapterView.OnItemClickListener getOnMenuSpinnerClicked() {
        return onMenuSpinnerClicked;
    }

    public void setOnMenuSpinnerClicked(AdapterView.OnItemClickListener onMenuSpinnerClicked) {
        this.onMenuSpinnerClicked = onMenuSpinnerClicked;
        notifyPropertyChanged(BR.onMenuSpinnerClicked);
    }

    @Bindable
    public ArrayList<SpinnerWrapper> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<SpinnerWrapper> menuItems) {
        this.menuItems = menuItems;
        notifyPropertyChanged(BR.menuItems);
    }

    public View.OnClickListener getThreeLineOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowSpinnerMenu(!showSpinnerMenu);
            }
        };
    }

    /**
     * hide the spinner when outside area clicked
     * @return
     */
    public View.OnClickListener onOutsideClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowSpinnerMenu(false);
            }
        };
    }

    @Bindable
    public ItemView getMenuItemView() {
        return menuItemView;
    }

    @Bindable
    public boolean isShowSpinnerMenu() {
        return showSpinnerMenu;
    }

    public void setShowSpinnerMenu(boolean showSpinnerMenu) {
        this.showSpinnerMenu = showSpinnerMenu;
        notifyPropertyChanged(BR.showSpinnerMenu);
    }

    public void resetOtherWrappers(int position){
        for (int i = 0 ; i < menuItems.size() ; i++){
            menuItems.get(i).setIsSelecte(i==position? 1 : 0);
        }
    }

    public String initToolBarTitle(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int delta = -(calendar.get(Calendar.DAY_OF_WEEK)-1);
        calendar.add(Calendar.DATE,delta);

//        return EventUtil.getMonth(getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
        return "";
    }

    @Bindable
    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        notifyPropertyChanged(BR.toolbarTitle);
    }
}
