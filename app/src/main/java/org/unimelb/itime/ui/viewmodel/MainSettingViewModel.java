package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.MainSettingMvpView;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingViewModel extends BaseObservable{
    private MvpBasePresenter presenter;
    private MainSettingMvpView mvpView;
    private User user;

    public MainSettingViewModel(MvpBasePresenter presenter) {
        this.presenter = presenter;
        if (presenter.getView() instanceof MainSettingMvpView){
            this.mvpView = (MainSettingMvpView) presenter.getView();
        }
    }

    public View.OnClickListener onHelpClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView==null){
                    return;
                }
                mvpView.onHelpAndFeedback();
            }
        };
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    public View.OnClickListener onProfileClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.toProfilePage();
            }
        };
    }

    public View.OnClickListener onBlockUserClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.toBlockedUserPage();
            }
        };
    }

    public View.OnClickListener onNotificationClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.toNotificationPage();
            }
        };
    }

    public View.OnClickListener onLanguageClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toLanguagePage();
                }
            }
        };
    }

    public View.OnClickListener onCalendarPreferenceClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.toCalendarPreferencePage();
            }
        };
    }

    public View.OnClickListener onHelpFdClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.toHelpFdPage();
            }
        };
    }

    public View.OnClickListener onAboutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.toAboutPage();
            }
        };
    }

    public View.OnClickListener onLogOutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView==null){
                    return;
                }
                mvpView.onLogOut();
            }
        };
    }
}
