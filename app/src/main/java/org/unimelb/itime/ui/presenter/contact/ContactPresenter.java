package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Qiushuo Huang on 2017/6/21.
 */

public class ContactPresenter<T extends ItimeBaseMvpView> extends MvpBasePresenter<T> {

    private static final String TAG = "AddFriend";
    public static final int TASK_SEARCH_USER = 1112;
    public static final int TASK_INVITE = 1113;
    public static final int TASK_MYSELF = 1114;
    public static final int ERROR_INVALID_EMAIL = 2224;
    private Context context;
//    private UserApi userApi;
//    private ContactApi contactApi;

    public ContactPresenter(Context context){
        this.context = context;
    }

    public Context getContext(){
        return context;
//        userApi = HttpUtil.createService(context, UserApi.class);
//        contactApi = HttpUtil.createService(context, ContactApi.class);
    }


    public void findFriend(String searchStr){
//
//        if(searchStr==null || searchStr.equals("")){
//            return;
//        }
//
//        if(UserUtil.getInstance(context).getUser().getUserId()
//                .equals(searchStr)){
//            getView().onTaskSuccess(TASK_MYSELF, null);
//            return;
//        }
//
//        if(getView()!=null){
//            getView().onTaskStart(TASK_SEARCH_USER);
//        }
//
//        DBManager dbManager = DBManager.getInstance(context);
//        searchStr = searchStr.trim();
//        final List<Contact> contacts = dbManager.getAllContact();
//        for(Contact contact:contacts){
//            if(contact.getUserDetail().getPhone().equals(searchStr)
//                    || contact.getUserDetail().getEmail().equals(searchStr)){
//                if(getView()!=null) {
//                    getView().onTaskSuccess(TASK_SEARCH_USER, contact);
//                }
//                return;
//            }
//        }
//
//        Observable<HttpResult<List<User>>> observable = userApi.search(searchStr);
//        ItimeSubscriber<HttpResult<List<User>>> subscriber = new ItimeSubscriber<HttpResult<List<User>>>() {
//            @Override
//            public void onHttpError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(HttpResult<List<User>> result) {
//                if (result.getStatus()!=1){
//                    if(getView()!=null) {
//                        getView().onTaskError(TASK_SEARCH_USER, null);
//                    }
//                }else {
//                    if(result.getData().isEmpty()){
//                        if(getView()!=null) {
//                            getView().onTaskError(TASK_SEARCH_USER, null);
//                        }
//                    }else {
//                        User user = result.getData().get(0);
//                        if(getView()!=null) {
//                            getView().onTaskSuccess(TASK_SEARCH_USER, new Contact(user));
//                        }
//                    }
//                }
//            }
//        };
//
//        HttpUtil.subscribe(observable, subscriber);
    }

    public void sendInvite(String email) {
//        Observable<HttpResult<String>> observable = contactApi.invite(email, Contact.SOURCE_EMAIL);
//        ItimeSubscriber<HttpResult<String>> subscriber = new ItimeSubscriber<HttpResult<String>>() {
//            @Override
//            public void onHttpError(Throwable e) {
//                getView().onTaskError(TASK_INVITE, null);
//            }
//
//            @Override
//            public void onNext(HttpResult<String> result) {
//                if (result.getStatus()!=1){
//                    if(getView()!=null) {
//                        getView().onTaskError(TASK_INVITE, null);
//                    }
//                }else {
//                    getView().onTaskSuccess(TASK_INVITE, null);
//                }
//            }
//        };
//        HttpUtil.subscribe(observable, subscriber);
    }

    public void showEmailAlert() {
//        if(getView()!=null){
//            getView().onTaskError(ERROR_INVALID_EMAIL, null);
//        }
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
}
