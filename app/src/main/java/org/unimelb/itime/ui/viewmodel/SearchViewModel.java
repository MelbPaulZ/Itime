package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
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

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchViewModel extends BaseObservable {

    private SearchMvpView mvpView;
    private SearchPresenter<SearchMvpView> presenter;

    private List<Meeting> meetingDataSet = new ArrayList<>();
    private List<MeetingInfoViewModel> meetingVMs = new ArrayList<>();
    private ObservableList<MeetingInfoViewModel> meetingVMsResult = new ObservableArrayList<>();

    private List<Event> soloEventDataSet = new ArrayList<>();
    private List<EventInfoViewModel> soloEventVMs = new ArrayList<>();
    private ObservableList<EventInfoViewModel> soloEventVMsResult = new ObservableArrayList<>();

    private List<Contact> contactDataSet = new ArrayList<>();
    private List<ContactInfoViewModel> contactsVMs = new ArrayList<>();
    private ObservableList<ContactInfoViewModel> contactsVMsResult = new ObservableArrayList<>();

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

    public View.OnClickListener onMeetingShowMoreClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView == null)return;
                mvpView.onMeetingShowMoreClick(searchStr, meetingDataSet);
            }
        };
    }

    public View.OnClickListener onEventShowMoreClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView == null)return;
                mvpView.onEventShowMoreClick(searchStr, soloEventDataSet);
            }
        };
    }

    public View.OnClickListener onContactShowMoreClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView == null)return;
                mvpView.onContactShowMoreClick(searchStr, contactDataSet);
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
                search();
            }
        };
    }

    /**
     * data filter functions
     */
    public void search(){
        this.clearResults();

        if (searchStr.isEmpty()){
            return;
        }

        String tmp = searchStr.toLowerCase();

        for(MeetingInfoViewModel vm: meetingVMs){
            if(vm.tryMatch(tmp)){
                meetingVMsResult.add(vm);
            }
        }
        notifyPropertyChanged(BR.meetingVMsResult);

        for(EventInfoViewModel vm: soloEventVMs){
            if(vm.tryMatch(tmp)){
                soloEventVMsResult.add(vm);
            }
        }
        notifyPropertyChanged(BR.soloEventVMsResult);

        for(ContactInfoViewModel vm: contactsVMs){
            if(vm.tryMatch(tmp)){
                contactsVMsResult.add(vm);
            }
        }
        notifyPropertyChanged(BR.contactsVMsResult);

        refreshResultHint();
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

    /**
     * Search Scope DataSet setter & getter
     * @return
     */
    public List<Meeting> getMeetingDataSet() {
        return meetingDataSet;
    }

    public void setMeetingDataSet(List<Meeting> meetingDataSet) {
        this.meetingDataSet = meetingDataSet;
        this.setMeetingVMs(initMeetingsVM(meetingDataSet));
    }

    public List<Event> getSoloEventDataSet() {
        return soloEventDataSet;
    }

    public void setSoloEventDataSet(List<Event> soloEventDataSet) {
        this.soloEventDataSet = soloEventDataSet;
        this.setSoloEventVMs(initSoloEventsVM(soloEventDataSet));
    }

    public List<Contact> getContactDataSet() {
        return contactDataSet;
    }

    public void setContactDataSet(List<Contact> contactDataSet) {
        this.contactDataSet = contactDataSet;
        this.setContactsVMs(initContactsVM(contactDataSet));
    }

    /**
     * VM data set setter
     * @param meetingVMs
     */
    private void setMeetingVMs(List<MeetingInfoViewModel> meetingVMs) {
        this.meetingVMs = meetingVMs;
        search();
    }

    private void setSoloEventVMs(List<EventInfoViewModel> soloEventVMs) {
        this.soloEventVMs = soloEventVMs;
        search();
    }

    private void setContactsVMs(List<ContactInfoViewModel> contactsVMs) {
        this.contactsVMs = contactsVMs;
        search();
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

    public void setMeetingVMsResult(ObservableArrayList<MeetingInfoViewModel> meetingVMsResult) {
        this.meetingVMsResult = meetingVMsResult;
        notifyPropertyChanged(BR.meetingVMsResult);
    }

    @Bindable
    public List<EventInfoViewModel> getSoloEventVMsResult() {
        return soloEventVMsResult;
    }

    public void setSoloEventVMsResult(ObservableArrayList<EventInfoViewModel> soloEventVMsResult) {
        this.soloEventVMsResult = soloEventVMsResult;
        notifyPropertyChanged(BR.soloEventVMsResult);
    }

    @Bindable
    public List<ContactInfoViewModel> getContactsVMsResult() {
        return contactsVMsResult;
    }

    public void setContactsVMsResult(ObservableArrayList<ContactInfoViewModel> contactsVMsResult) {
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
