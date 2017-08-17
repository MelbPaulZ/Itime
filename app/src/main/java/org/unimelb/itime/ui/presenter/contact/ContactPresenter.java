package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.RecomandContact;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Qiushuo Huang on 2017/6/21.
 */

public class ContactPresenter<T extends TaskBasedMvpView> extends MvpBasePresenter<T> {

    private static final String TAG = "AddFriend";
    public static final int TASK_SEARCH_USER = 1112;
    public static final int TASK_INVITE = 1113;
    public static final int TASK_MYSELF = 1114;
    public static final int TAST_ALL_CONTACT = 1115;
    public static final int TASK_RECOMMEND = 1116;
    public static final int TASK_ADD = 1117;
    public static final int ERROR_INVALID_EMAIL = 2224;
    private Context context;
    private UserApi userApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;

    public ContactPresenter(Context context){
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context, FriendRequestApi.class);
    }

    public Context getContext(){
        return context;
    }


    public void findFriend(String searchStr){

        if(searchStr==null || searchStr.equals("")){
            return;
        }

        if(UserUtil.getInstance(context).getUser().getUserId()
                .equals(searchStr)){
            getView().onTaskSuccess(TASK_MYSELF, null);
            return;
        }

        if(getView()!=null){
            getView().onTaskStart(TASK_SEARCH_USER);
        }

        DBManager dbManager = DBManager.getInstance(context);
        searchStr = searchStr.trim();
        final List<Contact> contacts = dbManager.getAllContact();
        for(Contact contact:contacts){
            if(contact.getUserDetail().getPhone().equals(searchStr)
                    || contact.getUserDetail().getEmail().equals(searchStr)){
                if(getView()!=null) {
                    getView().onTaskSuccess(TASK_SEARCH_USER, contact);
                }
                return;
            }
        }

        Observable<HttpResult<List<User>>> observable = userApi.search(searchStr);
        ItimeSubscriber<HttpResult<List<User>>> subscriber = new ItimeSubscriber<HttpResult<List<User>>>() {
            @Override
            public void onHttpError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<List<User>> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_SEARCH_USER, null);
                    }
                }else {
                    if(result.getData().isEmpty()){
                        if(getView()!=null) {
                            getView().onTaskError(TASK_SEARCH_USER, null);
                        }
                    }else {
                        User user = result.getData().get(0);
                        if(getView()!=null) {
                            getView().onTaskSuccess(TASK_SEARCH_USER, new Contact(user));
                        }
                    }
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
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

    public void loadContacts(){
        Observable<List<Contact>> observable = Observable.create(new Observable.OnSubscribe<List<Contact>>() {
            @Override
            public void call(Subscriber<? super List<Contact>> subscriber) {
                DBManager dbManager = DBManager.getInstance(context);
                subscriber.onNext(dbManager.getAllContact());
            }
        });
        ItimeSubscriber<List<Contact>> subscriber = new ItimeSubscriber<List<Contact>>() {
            @Override
            public void onHttpError(Throwable e) {
                getView().onTaskError(TASK_INVITE, null);
            }

            @Override
            public void onNext(List<Contact> result) {
                if(getView()!=null){
                    getView().onTaskSuccess(TAST_ALL_CONTACT, result);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void getRecommendContacts(){
        Observable<HttpResult<List<RecomandContact>>> observable = contactApi.recommend();
        ItimeSubscriber<HttpResult<List<RecomandContact>>> subscriber = new ItimeSubscriber<HttpResult<List<RecomandContact>>>() {
            @Override
            public void onHttpError(Throwable e) {
               e.printStackTrace();
                if(getView()!=null) {
                    getView().onTaskError(TASK_RECOMMEND, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<RecomandContact>> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_RECOMMEND, null);
                    }
                }else {
                    if(result.getData()==null){

                    }else {
                        if(getView()!=null) {
                            getView().onTaskSuccess(TASK_RECOMMEND, result.getData());
                        }
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void addUser(String userUid){
        if(getView()==null){
            return;
        }
        getView().onTaskStart(TASK_ADD);
        Observable<HttpResult<Void>> observable = requestApi.send(userUid, FriendRequest.SOURCE_ITIME);
        ItimeSubscriber<HttpResult<Void>> subscriber = new ItimeSubscriber<HttpResult<Void>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView()!=null) {
                    getView().onTaskError(TASK_ADD, null);
                }
            }

            @Override
            public void onNext(HttpResult<Void> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_ADD, null);
                    }
                }else {
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_ADD, userUid);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
