package org.unimelb.itime.ui.viewmodel.meeting;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.fragment.meeting.CardTemplate;
import org.unimelb.itime.ui.fragment.meeting.RecyclerViewAdapterMeetings;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static org.unimelb.itime.util.EventUtil.getCardTypeInvitation;

/**
 * Created by yuhaoliu on 27/06/2017.
 */

public class MeetingHostingBaseCardViewModel extends MeetingBaseCardViewModel {

    public MeetingHostingBaseCardViewModel(Context context, RecyclerViewAdapterMeetings.Mode mode) {
        super(context,mode);
    }

    public String getTitle(){
        return meeting.getEvent().getSummary();
    }

    public String getGreeting(){
        return meeting.getEvent().getGreeting();
    }

}
