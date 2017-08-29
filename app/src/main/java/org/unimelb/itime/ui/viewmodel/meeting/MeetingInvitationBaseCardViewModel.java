package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.CardTemplate;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.ui.mvpview.MeetingMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static org.unimelb.itime.util.MeetingUtil.getCardTypeInvitation;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingInvitationBaseCardViewModel extends MeetingBaseCardViewModel {
    private static Map<Integer, CardTemplate> cardsMap = new HashMap<>();

    static {
        cardsMap.put(1,new CardTemplate(
                1, View.VISIBLE, R.string.meeting_status_vote, R.color.white, R.drawable.icon_meetings_vote, R.color.brand_main, R.color.black
        ));
        cardsMap.put(2,new CardTemplate(
                2, View.GONE, R.string.meeting_status_voted, R.color.brand_main, R.drawable.icon_meetings_voted, R.color.transparent, R.color.brand_main
        ));
        cardsMap.put(3,new CardTemplate(
                3, View.GONE, R.string.meeting_status_going, R.color.brand_main, R.drawable.icon_meetings_going, R.color.transparent, R.color.brand_main
        ));
        cardsMap.put(4,new CardTemplate(
                4, View.VISIBLE, R.string.meeting_status_not_going, R.color.red, R.drawable.icon_meetings_notgoing, R.color.transparent, R.color.black
        ));
        cardsMap.put(5,new CardTemplate(
                5, View.GONE, R.string.meeting_status_revote, R.color.white, R.drawable.icon_meetings_vote, R.color.brand_main, R.color.brand_main
        ));
        cardsMap.put(6,new CardTemplate(
                6, View.GONE, R.string.meeting_status_decide, R.color.white, R.drawable.icon_meetings_decide, R.color.brand_main, R.color.brand_main
        ));
        cardsMap.put(7,new CardTemplate(
                7, View.GONE, R.string.meeting_status_going, R.color.brand_main, R.drawable.icon_meetings_going, R.color.transparent, R.color.brand_main
        ));
        cardsMap.put(8,new CardTemplate(
                8, View.GONE, R.string.meeting_status_not_going, R.color.red, R.drawable.icon_meetings_notgoing, R.color.transparent, R.color.brand_main
        ));
        cardsMap.put(9,new CardTemplate(
                9, View.GONE, R.string.meeting_status_voted, R.color.brand_main, R.drawable.icon_meetings_voted, R.color.transparent, R.color.brand_main
        ));
        cardsMap.put(10,new CardTemplate(
                10, View.GONE, R.string.meeting_status_not_going, R.color.red, R.drawable.icon_meetings_notgoing, R.color.transparent, R.color.brand_main
        ));
        cardsMap.put(11,new CardTemplate(
                11, View.GONE, R.string.meeting_status_cancelled, R.color.text_indication, R.drawable.icon_meetings_cancelled, R.color.white, R.color.transparent
        ));
        cardsMap.put(12,new CardTemplate(
                12, View.GONE, R.string.meeting_status_cancelled, R.color.text_indication, R.drawable.icon_meetings_cancelled, R.color.white, R.color.transparent
        ));
        cardsMap.put(13,new CardTemplate(
                13, View.VISIBLE, R.string.meeting_status_decide, R.color.white, R.drawable.icon_meetings_decide, R.color.brand_main, R.color.black
        ));
    }

    public enum PictureMode{
        CIRCLE, DEFAULT
    }

    protected CardTemplate cardTemplate;

    public MeetingInvitationBaseCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode,  MeetingPresenter<MeetingMvpView> meetingPresenter) {
        super(context,mode,meetingPresenter);
    }

    @Bindable
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
        this.cardTemplate = getCardInvitation(meeting);
        notifyPropertyChanged(BR.meeting);
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    /*********** Invitation *********** Start of card content configuration **********************************************/
    public String getSysMsg(){
        return cardTemplate.getSysMsg(context, meeting);
    }

    public int getSysTextColor(){
        return context.getResources().getColor(cardTemplate.sysMsgTextColor);
    }

    public Drawable getIconSrc(Context context){
        return context.getResources().getDrawable(cardTemplate.iconSrc);
    }

    public int getIconTextColor(){
        return context.getResources().getColor(cardTemplate.iconTextColor);
    }

    public String getIconText(){
        return cardTemplate.getIconText(context);
    }

    public String getTitle(){
        return cardTemplate.getTitle(meeting);
    }

    public String getGreeting(){
        return cardTemplate.getSubTitle(meeting);
    }

    public String getProfilePhotoUrl(){
        return cardTemplate.getPhotoUrl(meeting);
    }

    public int getSideBarColor(){
        return cardTemplate.sidebarColor;
    }

    /************ Invitation ********** End of card content configuration **********************************************/

    public int getPhotoVisibility(){
        return cardTemplate.photoVisibility;
    }

    @BindingAdapter( value={"imageUrl", "pictureMode"}, requireAll=false)
    public static void loadImage(ImageView view, String imageUrl, @Nullable PictureMode pictureMode) {
        RequestCreator creator = Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_image);

        if (pictureMode == null || pictureMode == PictureMode.DEFAULT){
            // no transform
        }else if (pictureMode == PictureMode.CIRCLE){
            creator.transform(new CropCircleTransformation());
        }

        creator.into(view);
    }

    protected CardTemplate getCardInvitation(Meeting meeting) {
        int type = getCardTypeInvitation(meeting);
        return cardsMap.get(type);
    }
}
