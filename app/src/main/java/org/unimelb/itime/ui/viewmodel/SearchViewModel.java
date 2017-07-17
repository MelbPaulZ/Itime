package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.ui.mvpview.activity.SearchMvpView;
import org.unimelb.itime.ui.presenter.SearchPresenter;
import org.unimelb.itime.widget.listview.ContactInfoViewModel;
import org.unimelb.itime.widget.listview.EventInfoViewModel;
import org.unimelb.itime.widget.listview.MeetingInfoViewModel;
import org.unimelb.itime.widget.listview.SpannableInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchViewModel extends BaseObservable {

    private SearchMvpView mvpView;
    private SearchPresenter<SearchMvpView> presenter;

    private List<MeetingInfoViewModel> meetingVMsResult = new ArrayList<>();
    private List<EventInfoViewModel> soloEventVMsResult = new ArrayList<>();
    private List<ContactInfoViewModel> contactsVMsResult = new ArrayList<>();

    private String resultHint = "";
    private String searchStr = "";

    public ObservableField<Boolean> titleEnabled = new ObservableField<>(true);
    public ObservableField<Boolean> showMoreEnabled = new ObservableField<>(true);

    public SearchViewModel(SearchPresenter<SearchMvpView> presenter) {
        this.presenter = presenter;
        this.mvpView = presenter.getView();
        refreshResultHint();
    }

    /**
     * listeners
     * @return
     */
    public View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.onBack();
                }
            }
        };
    }

    public AdapterView.OnItemClickListener onMeetingResultItemClick(){
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mvpView == null) return;
                    Meeting data = meetingVMsResult.get(i).getMeeting();
                    mvpView.onMeetingClick(data);
            }
        };
    }

    public AdapterView.OnItemClickListener onEventResultItemClick(){
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mvpView == null) return;
                Event data = soloEventVMsResult.get(i).getEvent();
                mvpView.onEventClick(data);
            }
        };
    }

    public AdapterView.OnItemClickListener onContactResultItemClick(){
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mvpView == null) return;
                Contact data = contactsVMsResult.get(i).getContact();
                mvpView.onContactClick(data);
            }
        };
    }

    public View.OnClickListener onShowMoreClick(final int type){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView == null)return;
                mvpView.onShowMoreClick(type, searchStr);
            }
        };
    }

    public View.OnClickListener onClearClick(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchStr("");
            }
        };
    }

    public TextWatcher onEdit(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshResultHint();
                clearResults();
                presenter.search(searchStr);
            }
        };
    }

    /**
     * Change matched spannable string color
     * @param vms ViewModel of results
     */
    private void changeResultTextColor(List<? extends SpannableInfoViewModel> vms){
        for (SpannableInfoViewModel vm:vms
             ) {
            vm.setMatchStr(searchStr);
        }
    }

    private void refreshResultHint(){
        if (searchStr.isEmpty()){
            setResultHint("Try searching for keywords");
        }else{
            setResultHint("");
        }
    }

    private void clearResults(){
        meetingVMsResult.clear();
        soloEventVMsResult.clear();
        contactsVMsResult.clear();
    }

    /**
     * Item binding part
     */
    public ItemBinding getMeetingItemBinding(){
        return ItemBinding.of(BR.meetingVM, R.layout.search_item_meeting);
    }

    public ItemBinding getSoloEventItemBinding(){
        return ItemBinding.of(BR.eventVM, R.layout.search_item_soloevent);
    }

    public ItemBinding getContactItemBinding(){
        return ItemBinding.of(BR.contactVM, R.layout.search_item_contact);
    }

    public void setMeetingSearchResult(List<Meeting> meetingSearchResult) {
        this.setMeetingVMsResult(initMeetingsVM(meetingSearchResult));
    }

    public void setEventSearchResult(List<Event> eventSearchResult) {
        this.setSoloEventVMsResult(initSoloEventsVM(eventSearchResult));
    }

    public void setContactSearchResult(List<Contact> contactSearchResult) {
        this.setContactsVMsResult(initContactsVM(contactSearchResult));
    }

    /**
     * VM converter
     * @param meetings
     * @return
     */
    private List<MeetingInfoViewModel> initMeetingsVM(List<Meeting> meetings){
        List<MeetingInfoViewModel> meetingsVM = new ArrayList<>();
        for (Meeting meeting:meetings
                ) {
            meetingsVM.add(new MeetingInfoViewModel(meeting));
        }
        return meetingsVM;
    }

    private List<ContactInfoViewModel> initContactsVM(List<Contact> contacts){
        List<ContactInfoViewModel> contactsVM = new ArrayList<>();
        for (Contact contact:contacts
                ) {
            contactsVM.add(new ContactInfoViewModel(contact));
        }
        return contactsVM;
    }

    private List<EventInfoViewModel> initSoloEventsVM(List<Event> events){
        List<EventInfoViewModel> eventsVM = new ArrayList<>();
        for (Event event :events
                ) {
            eventsVM.add(new EventInfoViewModel(event));
        }
        return eventsVM;
    }

    /**
     * Bindable setter & getter
     * @return
     */
    @Bindable
    public List<MeetingInfoViewModel> getMeetingVMsResult() {
        return meetingVMsResult;
    }

    public void setMeetingVMsResult(List<MeetingInfoViewModel> meetingVMsResult) {
        this.changeResultTextColor(meetingVMsResult);
        this.meetingVMsResult = meetingVMsResult;
        notifyPropertyChanged(BR.meetingVMsResult);
    }

    @Bindable
    public List<EventInfoViewModel> getSoloEventVMsResult() {
        return soloEventVMsResult;
    }

    public void setSoloEventVMsResult(List<EventInfoViewModel> soloEventVMsResult) {
        this.changeResultTextColor(soloEventVMsResult);
        this.soloEventVMsResult = soloEventVMsResult;
        notifyPropertyChanged(BR.soloEventVMsResult);
    }

    @Bindable
    public List<ContactInfoViewModel> getContactsVMsResult() {
        return contactsVMsResult;
    }

    public void setContactsVMsResult(List<ContactInfoViewModel> contactsVMsResult) {
        this.changeResultTextColor(contactsVMsResult);
        this.contactsVMsResult = contactsVMsResult;
        notifyPropertyChanged(BR.contactsVMsResult);
    }

    @Bindable
    public String getResultHint() {
        return resultHint;
    }

    public void setResultHint(String resultHint) {
        this.resultHint = resultHint;
        notifyPropertyChanged(BR.resultHint);
    }

    @Bindable
    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr){
        this.searchStr = searchStr;
        notifyPropertyChanged(BR.searchStr);
    }

}
