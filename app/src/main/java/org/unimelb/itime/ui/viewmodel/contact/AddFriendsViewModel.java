package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;

import org.unimelb.itime.util.EmailUtil;
import org.unimelb.itime.widget.listview.UserInfoViewModel;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * Created by 37925 on 2016/12/10.
 */

public class AddFriendsViewModel extends BaseObservable {
    private ContactPresenter presenter;
    private AddFriendsMvpView mvpView;
    private boolean showSearch = false;
    private boolean resetSearchBar = true;
    private boolean showNotFound = false;
    private boolean showButtons = true;
    private String searchText = "";
    private boolean searching = false;
    private boolean showCancel = false;
    private boolean showSearchButton = false;
    private boolean showAutoComplete = false;
    private SpannableStringBuilder inviteText;
    private EmailUtil emailUtil;
    private List<String> autoEmails = new ArrayList<>();
    private ObservableList<UserInfoViewModel> autoCompleteItems = new ObservableArrayList<>();


    private void generateInviteeItems(List<String> autoEmails){
        autoCompleteItems.clear();

        for(String email: autoEmails){
           User user = new User();
            user.setPersonalAlias(email);
            UserInfoViewModel<User> vm = new UserInfoViewModel<>();
            vm.setData(user);
            autoCompleteItems.add(vm);
            vm.setMatchStr(searchText);
            }
    }

    @Bindable
    public List<String> getAutoEmails() {
        return autoEmails;
    }

    public void setAutoEmails(List<String> autoEmails) {
        this.autoEmails = autoEmails;
        notifyPropertyChanged(BR.autoEmails);
    }

    @Bindable
    public ObservableList<UserInfoViewModel> getAutoCompleteItems() {
        return autoCompleteItems;
    }

    public void setAutoCompleteItems(ObservableList<UserInfoViewModel> autoCompleteItems) {
        this.autoCompleteItems = autoCompleteItems;
        notifyPropertyChanged(BR.autoCompleteItems);
    }

    public AdapterView.OnItemClickListener onItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String email = autoEmails.get(i);
                setSearchText(email);
                setShowAutoComplete(false);
                setShowSearchButton(true);
            }
        };
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(com.android.databinding.library.baseAdapters.BR.viewModel, R.layout.listview_user_item);
    }

    public AddFriendsMvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(AddFriendsMvpView mvpView) {
        this.mvpView = mvpView;
    }

    public void showAlert() {
        presenter.showEmailAlert();
    }

    public View.OnClickListener onSearchClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearching(true);
            }
        };
    }

    @Bindable
    public boolean getResetSearchBar() {
        return resetSearchBar;
    }

    public void setResetSearchBar(boolean resetSearchBar) {
        this.resetSearchBar = resetSearchBar;
        notifyPropertyChanged(BR.resetSearchBar);
    }

    @Bindable
    public boolean getIsValidEmail(){
        return emailUtil.isValidDomain(getPureSearchText());
    }

    public AddFriendsViewModel(ContactPresenter presenter){
        this.presenter = presenter;
        emailUtil = EmailUtil.getInstance(presenter.getContext());
    }

    @Bindable
    public boolean getShowSearch() {
        return showSearch;
    }

    public void showSearch() {
        setShowSearch(true);
        setShowNotFound(false);
        setShowButtons(false);
        setSearching(false);
        setShowCancel(true);
    }

    @Bindable
    public boolean getShowNotFound() {
        return showNotFound;
    }

    public void setShowNotFound(boolean bool){
        this.showNotFound = bool;
        notifyPropertyChanged(BR.showNotFound);
    }

    public void setShowSearch(boolean bool){
        this.showSearch = bool;
        notifyPropertyChanged(BR.showSearch);
    }

    public void setShowButtons(boolean bool){
        this.showButtons = bool;
        notifyPropertyChanged(BR.showButtons);
    }

    @Bindable
    public boolean isShowSearchButton() {
        return showSearchButton;
    }

    public void setShowSearchButton(boolean showSearchButton) {
        this.showSearchButton = showSearchButton;
        notifyPropertyChanged(BR.showSearchButton);
    }

    @Bindable
    public boolean isShowAutoComplete() {
        return showAutoComplete;
    }

    public void setShowAutoComplete(boolean showAutoComplete) {
        this.showAutoComplete = showAutoComplete;
        notifyPropertyChanged(BR.showAutoComplete);
    }

    public void showNotFound() {
        setShowNotFound(true);
        setShowButtons(false);
        setShowSearch(false);
        notifyPropertyChanged(BR.isValidEmail);
//        String emailtext = ContactCheckUtil.getInsstance().ellipsizeEmail(searchText);
        String emailtext = searchText;
        String str = "Invite "+ emailtext +" to use Timegenii";

        SpannableStringBuilder text = new SpannableStringBuilder(str);
        int begin = str.indexOf(emailtext);
        int end = begin+emailtext.length();
        text.setSpan(new StyleSpan(Typeface.ITALIC),
                begin,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setInviteText(text);
    }

    @Bindable
    public SpannableStringBuilder getInviteText(){
        return inviteText;
    }

    public void setInviteText(SpannableStringBuilder inviteText){
        this.inviteText = inviteText;
        notifyPropertyChanged(BR.inviteText);
    }

    @Bindable
    public boolean getShowButtons() {
        return showButtons;
    }

    public void showButtons() {
//        setShowButtons(true);
//        setShowNotFound(false);
//        setShowSearch(false);
//        setSearching(true);
//        setShowCancel(false);
//        setResetSearchBar(true);
    }

    @Bindable
    public View.OnClickListener getQdListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.goToScanQRCode();
            }
        };
    }


    public View.OnClickListener getSearchButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = getPureSearchText();
                presenter.findFriend(searchStr);
            }
        };
    }



    public String getPureSearchText(){
        return searchText;
    }

    @Bindable
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        notifyPropertyChanged(BR.searchText);
    }

    public View.OnClickListener getCancelListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showButtons();
            }
        };
    }

    public void sendInvite(){
        presenter.sendInvite(searchText);
    }

    @Bindable
    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean showTitle) {
        this.searching = showTitle;
        notifyPropertyChanged(BR.searching);
    }

    public View.OnClickListener onQuitSearchClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchText("");
                setSearching(false);
            }
        };
    }

    public View.OnClickListener onClearClick(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchText("");
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
                if (searchText.isEmpty()) {
                    setShowSearchButton(false);
                    setShowAutoComplete(false);
                } else {
                    autoEmails = EmailUtil.getInstance(presenter.getContext()).getAutoCompleteLists(editable.toString());
                    if (autoEmails.isEmpty()) {
                    } else {
                        if (autoEmails.get(0).equals(editable.toString())) {
                            setShowSearchButton(true);
                            setShowAutoComplete(false);
                        } else {
                            generateInviteeItems(autoEmails);
                            setShowSearchButton(false);
                            setShowAutoComplete(true);
                        }
                    }
                }
            }
        };
    }

    @Bindable
    public boolean getShowCancel(){
        return showCancel;
    }

    public void setShowCancel(boolean showCancel) {
        this.showCancel = showCancel;
        notifyPropertyChanged(BR.showCancel);
    }

    public View.OnClickListener getSearchClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch();
            }
        };
    }

//    public .OnEditListener getOnEditListener(){
//        return new SearchBar.OnEditListener() {
//            @Override
//            public void onEditing(View view, String text) {
//                if ("".equals(text)) {
//
//                } else {
//                    showSearch();
//                }
//                setSearchText(text);
//            }
//        };
//    }
}
