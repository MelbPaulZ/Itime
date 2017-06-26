package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddInviteeMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreatePresenter<V extends ItimeBaseMvpView> extends ItimeBasePresenter<V> {

    public EventCreatePresenter(Context context) {
        super(context);
    }

    public List<Contact> getContacts(){


        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setAliasName("a");
        contact1.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact1.setContactUid("1");

        Contact contact2 = new Contact();
        contact2.setAliasName("b");
        contact2.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact2.setContactUid("2");

        Contact contact3 = new Contact();
        contact3.setAliasName("c");
        contact3.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact3.setContactUid("3");

        Contact contact4 = new Contact();
        contact4.setAliasName("d");
        contact4.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact4.setContactUid("4");

        Contact contact5 = new Contact();
        contact5.setAliasName("ad");
        contact5.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact5.setContactUid("5");

        contacts.add(contact1);
        contacts.add(contact5);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);

        int i=0;
        for(Contact contact:contacts){
            User user = new User();
            user.setEmail("123456@unimelb.edu.au");
            user.setUserId("123456@unimelb.edu.au");
            user.setUserUid(i+"");
            i++;
            contact.setUserUid(i+"");
            contact.setUserDetail(user);
        }

        return contacts;
    }

    public List<Contact> getRecentContacts(){
        User user = new User();
        user.setEmail("123456@unimelb.edu.au");
        user.setUserId("123456@unimelb.edu.au");
        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setAliasName("a");
        contact1.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact1.setUserDetail(user);
        contact1.setContactUid("1");

        Contact contact2 = new Contact();
        contact2.setAliasName("b");
        contact2.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact2.setUserDetail(user);
        contact2.setContactUid("2");

        Contact contact3 = new Contact();
        contact3.setAliasName("c");
        contact3.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact3.setUserDetail(user);
        contact3.setContactUid("3");

        Contact contact4 = new Contact();
        contact4.setAliasName("d");
        contact4.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact4.setUserDetail(user);
        contact4.setContactUid("4");

        Contact contact5 = new Contact();
        contact5.setAliasName("ad");
        contact5.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
        contact5.setUserDetail(user);
        contact5.setContactUid("5");

        contacts.add(contact1);
        contacts.add(contact5);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);

        return contacts;
    }
}
