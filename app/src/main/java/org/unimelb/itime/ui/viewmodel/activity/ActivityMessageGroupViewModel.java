package org.unimelb.itime.ui.viewmodel.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class ActivityMessageGroupViewModel extends BaseObservable {
    private boolean isSystem;
    private String user = "";
    private String message = "";
    private ObservableBoolean showDetail = new ObservableBoolean(false);

    public ActivityMessageGroupViewModel(boolean isSystem, String user, String message) {
        this.isSystem = isSystem;
        this.user = user;
        this.message = message;
        mockMessage();
    }

    private void mockMessage(){
        messageGroups.add(new ActivityMessageViewModel("aaaa", "aaaa"));
        messageGroups.add(new ActivityMessageViewModel("bbbb", "bbb"));
    }

    private List<ActivityMessageViewModel> messageGroups = new ArrayList<>();
    public final OnItemBind<ActivityMessageViewModel> onItemBind = new OnItemBind<ActivityMessageViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ActivityMessageViewModel item) {
            itemBinding.set(BR.messageViewGroup, position >= 3? R.layout.itime_activity_message_bottom : R.layout.itime_activity_meeting_message);
        }
    };


    public void onChangeHideShow(View v){
        if (!showDetail.get()){ // to show
            v.startAnimation(getAlphaAnimation());
            showDetail.set(!showDetail.get());
            setShowDetail(showDetail);
        }else{ //to close
           v.startAnimation(getCloseAnimation());
        }
    }

    private AlphaAnimation getAlphaAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(800);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }


    private AlphaAnimation getCloseAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setFillAfter(false);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showDetail.set(!showDetail.get());
                setShowDetail(showDetail);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return alphaAnimation;
    }

    public int getDetailVisibility(boolean showDetail){
        return showDetail? View.VISIBLE : View.GONE;
    }

    public Drawable getIcon(Context context, boolean showDetail){
        return showDetail? context.getResources().getDrawable(R.drawable.icon_activities_togglepanel_up) : context.getResources().getDrawable(R.drawable.icon_activities_togglepanel_down);
    }

    @Bindable
    public ObservableBoolean getShowDetail() {
        return showDetail;
    }

    public void setShowDetail(ObservableBoolean showDetail) {
        this.showDetail = showDetail;
        notifyPropertyChanged(BR.showDetail);
    }

    @Bindable
    public List<ActivityMessageViewModel> getMessageGroups() {
        return messageGroups;
    }

    public void setMessageGroups(List<ActivityMessageViewModel> messageGroups) {
        this.messageGroups = messageGroups;
        notifyPropertyChanged(BR.messageGroups);
    }

    @Bindable
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Bindable
    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class ActivityMessageViewModel extends BaseObservable{
        private String name;
        private String title;

        public ActivityMessageViewModel(String name, String title) {
            this.name = name;
            this.title = title;
        }

        @Bindable
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Bindable
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}