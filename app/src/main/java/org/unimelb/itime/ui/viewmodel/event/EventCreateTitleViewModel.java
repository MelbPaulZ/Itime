package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.RecentEventTitle;
import org.unimelb.itime.bean.RecentLocation;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.mvpview.event.EventCreateTitleMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTitlePresenter;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by Paul on 9/6/17.
 */

public class EventCreateTitleViewModel extends ItimeBaseViewModel {

    private Event event;
    private List<TitleRowViewModel> titleRows = new ArrayList<>();
    private OnTitleChangeInterface onTitleChangeInterface;

    public void setOnTitleChangeInterface(OnTitleChangeInterface onTitleChangeInterface) {
        this.onTitleChangeInterface = onTitleChangeInterface;
    }

    public OnItemBind<TitleRowViewModel> onItemBind = new OnItemBind<TitleRowViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, TitleRowViewModel item) {
            if (titleRows.get(position).isTitle){
                itemBinding.set(BR.titleRowVM,R.layout.row_title_title);
            }else{
                itemBinding.set(BR.titleRowVM, R.layout.row_title_content);
            }
        }
    };

    public int getSuggestedRecentVisibility(Event event){
        if (event.getSummary().length()==0){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }


    private EventCreateTitlePresenter<EventCreateTitleMvpView> presenter;

    public static int TITLE_COUNT_LIMIT = 50;
    public static int TITLE_WARNING_THRESHOLD = 5;

    public EventCreateTitleViewModel(EventCreateTitlePresenter<EventCreateTitleMvpView> presenter) {
        this.presenter = presenter;

    }

    private String getTitleString(Event event){
        String suggestTitle = "Meeting with ";
        int count = 0;
        int totalInviteeNumber = event.getInvitee().size();
        for(Invitee invitee : event.getInvitee().values()){
            if (count == 3){
                suggestTitle += "...";
                break;
            }
            if (!invitee.getUserUid().equals(UserUtil.getInstance(presenter.getContext()).getUserUid())) {
                count ++;
                if (count < 3 && count < totalInviteeNumber) {
                    suggestTitle += invitee.getAliasName() + ", ";
                }else{
                    suggestTitle +=  invitee.getAliasName();
                }
            }
        }
        return suggestTitle;
    }

    private void loadTitleData(Event event){
        titleRows.clear();
        if (event.getInvitee().values().size()>0) {
            titleRows.add(new TitleRowViewModel(presenter.getContext().getString(R.string.event_create_title_suggested), true));
            titleRows.add(new TitleRowViewModel(getTitleString(event)));
        }
//        List<RecentLocation>
        List<RecentEventTitle> recentEventTitles = DBManager.getInstance(presenter.getContext()).getRecentTitles();
        if (recentEventTitles.size()>0){
            titleRows.add(new TitleRowViewModel(presenter.getContext().getString(R.string.event_create_title_recent),true));
            for (RecentEventTitle recentEventTitle: recentEventTitles){
                titleRows.add(new TitleRowViewModel(recentEventTitle.getTitle()));
            }
        }
//        titleRows.add(new TitleRowViewModel(presenter.getContext().getString(R.string.event_create_title_recent),true));
//        titleRows.add(new TitleRowViewModel("Lunch with David"));
//        titleRows.add(new TitleRowViewModel("Design Team Discussion"));
//        titleRows.add(new TitleRowViewModel("Meeting at Google"));
//        titleRows.add(new TitleRowViewModel("Coffee with Sarah"));
    }

    public TextWatcher onTitleChangeListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setEvent(event);
                if (onTitleChangeInterface!=null){
                    onTitleChangeInterface.onTitleChange(s.length() <= TITLE_COUNT_LIMIT);
                }
            }
        };
    }

    public int getWordCountTextColor(Event event){
        if(event.getSummary().length()>TITLE_COUNT_LIMIT - TITLE_WARNING_THRESHOLD){
            return presenter.getContext().getResources().getColor(R.color.warmPink);
        }else{
            return presenter.getContext().getResources().getColor(R.color.pinkishGrey);
        }
    }

    public String getWordCountText(Event event){
        return (TITLE_COUNT_LIMIT - event.getSummary().length()) + "";
    }

    @Bindable
    public List<TitleRowViewModel> getTitleRows() {
        return titleRows;
    }

    public void setTitleRows(List<TitleRowViewModel> titleRows) {
        this.titleRows = titleRows;
        notifyPropertyChanged(BR.titleRows);
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        loadTitleData(event);
        notifyPropertyChanged(BR.event);
    }

    public class TitleRowViewModel extends BaseObservable{
        private String title;
        private boolean isTitle = false;

        public TitleRowViewModel(String title, boolean isTitle) {
            this.title = title;
            this.isTitle = isTitle;
        }

        public boolean isTitle() {
            return isTitle;
        }

        public void setTitle(boolean title) {
            isTitle = title;
        }

        public View.OnClickListener onTitleClick(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.setSummary(title);
                    setEvent(event);
                }
            };
        }

        public TitleRowViewModel(String title) {
            this.title = title;
        }

        @Bindable
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
            notifyPropertyChanged(BR.title);
        }
    }

    public interface OnTitleChangeInterface{
        void onTitleChange(boolean b);
    }
}
