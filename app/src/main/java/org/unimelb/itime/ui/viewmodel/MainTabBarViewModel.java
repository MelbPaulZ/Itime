package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.Color;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.widget.ITimeShootMenu;

import java.util.ArrayList;
import java.util.List;


import org.unimelb.itime.R;
/**
 * Created by yinchuandong on 16/08/2016.
 */
public class MainTabBarViewModel extends ItimeBaseViewModel{

    private MainTabBarView mvpView;
    private String unReadNum;
    private int visible;
    private int unReadFriendRequest;
    private Context context;

    private List<ITimeShootMenu.Item> items;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean[] tabSelectedArr = {true, false, false, false};

    public List<ITimeShootMenu.Item> getShootMenuItems(){
        items = new ArrayList<>();
        ITimeShootMenu.Item inviteOthers = new ITimeShootMenu.Item(
                R.drawable.icon_event_inviteothers, getContext().getResources().getColor(R.color.white), getContext().getString(R.string.event_create_inviteothers) );
        ITimeShootMenu.Item onlyMyself = new ITimeShootMenu.Item(
                R.drawable.icon_event_onlyme, getContext().getResources().getColor(R.color.white), getContext().getString(R.string.event_create_onlymyself) );
        items.add(inviteOthers);
        items.add(onlyMyself);
        return items;
    }

    public ITimeShootMenu.OnItemClickListener getOnMenuItemClickListener(){
        return new ITimeShootMenu.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ITimeShootMenu.Item item) {
                if(mvpView==null){
                    return;
                }
                switch (position){
                    case 0:
                        mvpView.gotoCreateMeeting();
                        break;
                    case 1:
                        mvpView.gotoCreateEvent();
                        break;
                }
            }
        };
    }

    public MainTabBarViewModel(MainTabBarView view){
        this.mvpView = view;
    }

    public View.OnClickListener onTabBarClick(final int pageId){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < tabSelectedArr.length; i++){
                    if(pageId == i){
                        tabSelectedArr[i] = true;
                    }else{
                        tabSelectedArr[i] = false;
                    }
                }
                notifyPropertyChanged(BR.tabSelectedArr);
                mvpView.showFragmentById(pageId);
            }
        };
    }

    @Bindable
    public int getVisible(){
        return visible;
    }


    @Bindable
    public String getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(String unReadNum) {
        this.unReadNum = unReadNum;
        if (unReadNum.equals("0")){
            visible =  View.GONE;
        }else{
            visible = View.VISIBLE;
        }
        notifyPropertyChanged(BR.unReadNum);
        notifyPropertyChanged(BR.visible);
    }

    @Bindable
    public int getUnReadFriendRequest() {
        return unReadFriendRequest;
    }

    public void setUnReadFriendRequest(int unReadFriendRequest) {
        this.unReadFriendRequest = unReadFriendRequest;
        notifyPropertyChanged(BR.unReadFriendRequest);
    }


    @Bindable
    public boolean[] getTabSelectedArr() {
        return tabSelectedArr;
    }

    public void setTabSelectedArr(boolean[] tabSelectedArr) {
        this.tabSelectedArr = tabSelectedArr;
        notifyPropertyChanged(BR.tabSelectedArr);
    }
}
