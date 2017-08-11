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
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.activity.ItimeActivitiesMvpView;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class ActivityMessageGroupViewModel extends BaseObservable {
    private MessageGroup messageGroup;
    private ItimeActivitiesMvpView mvpView;
    private Context context;

    private ObservableBoolean showDetail = new ObservableBoolean(false);

    public ActivityMessageGroupViewModel(MessageGroup messageGroup) {
        this.messageGroup = messageGroup;
        initMessages();
    }

    public void setMvpView(ItimeActivitiesMvpView mvpView) {
        this.mvpView = mvpView;
        updateMvpViewForSubviewmodels();
    }

    private void updateMvpViewForSubviewmodels(){
        for (ActivityMessageViewModel msgViewModel : messageGroups){
            msgViewModel.setMvpView(mvpView);
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void initMessages(){
        int size = messageGroup.getMessage().size();
        for (int i = 0 ; i < size ; i++){
            ActivityMessageViewModel v = new ActivityMessageViewModel(messageGroup.getMessage().get(i));
            v.setContext(context);
            v.setMvpView(mvpView);
            messageGroups.add(v);
        }

    }

//    private void mockMessage(){
//        for (int i = 0 ; i <= 3; i ++){
//            ActivityMessageViewModel v = new ActivityMessageViewModel(mockMessage(mockMsg[i]));
//            v.setMvpView(mvpView);
//            messageGroups.add(v);
//        }
//    }



    private List<ActivityMessageViewModel> messageGroups = new ArrayList<>(); // maximum size 4
    public final OnItemBind<ActivityMessageViewModel> onItemBind = new OnItemBind<ActivityMessageViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ActivityMessageViewModel item) {
            itemBinding.set(BR.messageViewGroup, position >= 3? R.layout.itime_activity_message_bottom : R.layout.itime_activity_meeting_message);
        }
    };

    private void readMessages(){
        for (ActivityMessageViewModel messageViewModel : messageGroups){
            messageViewModel.getMessage().setRead(true);
        }
        notifyPropertyChanged(BR.messageGroup);
    }


    public void onChangeHideShow(View v){
        if (!showDetail.get()){ // to show
            v.startAnimation(getAlphaAnimation());
            showDetail.set(!showDetail.get());
            setShowDetail(showDetail);
            mvpView.onDisplayMessages(messageGroup);
            readMessages(); // just
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

    public int getMuteVisibility(MessageGroup messageGroup){
        return messageGroup.isMute() ? View.VISIBLE : View.INVISIBLE;
    }

    public int getDetailVisibility(boolean showDetail){
        return showDetail? View.VISIBLE : View.GONE;
    }

    public int getUnNumberMuteDotVisibility(MessageGroup messageGroup){
        if (messageGroup.isMute()){
            for (ActivityMessageViewModel messageVM: messageGroups){
                if (messageVM.getMessage().isRead() == false){
                    return View.VISIBLE;
                }
            }
        }
        return View.GONE;
    }

    public int getNumberredMuteDotVisibility(MessageGroup messageGroup){
        if (!messageGroup.isMute()){
            for (ActivityMessageViewModel messageVM: messageGroups){
                if (messageVM.getMessage().isRead() == false){
                    return View.VISIBLE;
                }
            }
        }
        return View.GONE;
    }

    public String getUnReadMessageNumber(MessageGroup messageGroup){
        int count = 0;
        for (Message message: messageGroup.getMessage()){
            if (!message.isRead()){
                count ++;
            }
        }
        return count > 0 ? count + "" : "";
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
    public MessageGroup getMessageGroup() {
        return messageGroup;
    }

    public void setMessageGroup(MessageGroup messageGroup) {
        this.messageGroup = messageGroup;
        notifyPropertyChanged(BR.messageGroup);
    }

}