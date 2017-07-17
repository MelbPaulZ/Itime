package org.unimelb.itime.widget.listview;

import android.databinding.Bindable;
import android.text.SpannableString;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class ContactInfoViewModel extends SpannableInfoViewModel {
    private Contact contact;
    private SpannableString nameStr;
    private SpannableString email;

    private String personalAlias = "";
    private String alias = "";

    public ContactInfoViewModel(Contact contact) {
        setContact(contact);
    }

    @Bindable
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        this.personalAlias = contact.getUserDetail().getPersonalAlias();
        this.alias = contact.getAliasName();

        this.nameStr = new SpannableString(alias.isEmpty()?personalAlias:alias);
        notifyPropertyChanged(BR.contact);
    }

    public SpannableString getNameStr(String matchStr) {
        if (alias.contains(matchStr)){
            return changeMatchColor(alias, matchStr);
        }

        if (personalAlias.contains(matchStr)){
            return changeMatchColor(personalAlias, matchStr);
        }

        return this.nameStr;
    }

    @Bindable
    public SpannableString getEmail() {
        return changeMatchColor(contact.getUserDetail().getEmail(), getMatchStr());
    }

    public void setEmail(SpannableString email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public SpannableString getTitleStr() {
        return changeMatchColor(contact.getAliasName(), getMatchStr());
    }
}
