package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestResult;
import org.unimelb.itime.bean.RecomandContact;
import org.unimelb.itime.bean.RequestReadBody;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.contact.AddFriendsMvpView;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Qiushuo Huang on 2017/6/21.
 */

public class ContactPresenter<T extends TaskBasedMvpView> extends MvpBasePresenter<T> {

    private static final String TAG = "ContactPresenter";
    public static final int TASK_SEARCH_USER = 1112;
    public static final int TASK_INVITE = 1113;
    public static final int TASK_MYSELF = 1114;
    public static final int TAST_ALL_CONTACT = 1115;
    public static final int TASK_RECOMMEND = 1116;
    public static final int TASK_ADD = 1117;
    public static final int TASK_REQUEST_LIST = 1118;
    public static final int TASK_REQUEST_ACCEPT = 1119;
    public static final int TASK_BLOCK_LIST = 1120;
    public static final int ERROR_INVALID_EMAIL = 2224;


    private Context context;
    private UserApi userApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;
    private DBManager dbManager;


    public ContactPresenter(Context context){
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context, FriendRequestApi.class);
        dbManager = DBManager.getInstance(context);
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

    public void acceptRequest(String requestUid){
        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.confirm(requestUid);
        ItimeSubscriber<HttpResult<List<FriendRequest>>> subscriber = new ItimeSubscriber<HttpResult<List<FriendRequest>>>() {
            @Override
            public void onHttpError(Throwable e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }

            @Override
            public void onNext(HttpResult<List<FriendRequest>> result) {
                if (result.getStatus()!=1){

                }else {
                    dbManager.insertFriendRequest(result.getData().get(0));
                    if(getView()!=null){
                        getView().onTaskSuccess(TASK_REQUEST_ACCEPT, requestUid);
                    }
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    // Set requests status to read
    public void setRead(String[] requestIds){
        Observable<HttpResult<Void>> observable = requestApi.read(new RequestReadBody(requestIds));
        ItimeSubscriber<HttpResult<Void>> subscriber = new ItimeSubscriber<HttpResult<Void>>() {
            @Override
            public void onHttpError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<Void> result) {
                if (result.getStatus()!=1){

                }else {
                    EventBus.getDefault().post(new MessageNewFriendRequest(0));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void getRequestFriendList() {
        List<FriendRequest> requests = dbManager.getAllFriendRequest();
        if(requests!=null){
            if(getView()!=null) {
                getView().onTaskSuccess(TASK_REQUEST_LIST, requests);
            }
        }
        getRequestFriendListFromServer();
    }

    public void getRequestFriendListFromServer(){
        String syncToken = TokenUtil.getInstance(context).getFriendRequestToken(
                UserUtil.getInstance(context).getUserUid());
        Observable<HttpResult<FriendRequestResult>> observable = requestApi.list(syncToken);
        Observable<List<FriendRequest>> dbObservable = observable.map(new Func1<HttpResult<FriendRequestResult>, List<FriendRequest>>() {
            @Override
            public List<FriendRequest> call(HttpResult<FriendRequestResult> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    return null;
                }else {
                    TokenUtil.getInstance(context).setFriendRequestToken(
                            UserUtil.getInstance(context).getUserUid(), result.getSyncToken());
                    for(FriendRequest request:result.getData().getSend()) {
                        request.setSender(true);
                    }
                    for(FriendRequest request:result.getData().getReceive()) {
                        request.setSender(false);
                    }
                    DBManager.getInstance(context).insertOrReplace(result.getData().getSend());
                    DBManager.getInstance(context).insertOrReplace(result.getData().getReceive());
                    return DBManager.getInstance(context).getAllFriendRequest();
                }
            }
        });

        ItimeSubscriber<List<FriendRequest>> subscriber = new ItimeSubscriber<List<FriendRequest>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView()!=null) {
                    getView().onTaskError(TASK_REQUEST_LIST, null);
                }
            }

            @Override
            public void onNext(List<FriendRequest> list) {
                if (list == null){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_REQUEST_LIST, null);
                    }
                }else {
                    List<FriendRequest> result = new ArrayList<>();
                    for(FriendRequest request:list) {
                        result.add(request);
                    }

                    EventBus.getDefault().post(new MessageNewFriendRequest(countUnread(list)));

                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_REQUEST_LIST, result);
                    }
                }
            }
        };

        HttpUtil.subscribe(dbObservable, subscriber);
    }

    private int countUnread(List<FriendRequest> list){
        int count=0;
        for(FriendRequest request:list){
            if(request.isSender()){
                if(!request.senderIsRead()){
                    count++;
                }
            }else{
                if(!request.receiverIsRead()){
                    count++;
                }
            }
        }
        return count;
    }

    public void getBlockList(){
        DBManager dbManager = DBManager.getInstance(context);
        List<Contact> list = blocksToContacts(dbManager.getBlockContacts());
        if(getView()!=null){
            getView().onTaskSuccess(TASK_BLOCK_LIST, list);
        }
        getBlockListFromServer();
    }

    public List<Contact> blocksToContacts(List<Block> blocks){
        List<Contact> result = new ArrayList<>();
        for(Block block: blocks){
            Contact contact = new Contact(block.getUserDetail());
            contact.setBlockLevel(block.getBlockLevel());
            contact.setRelationship(1);
            result.add(contact);
        }
        return result;
    }

    private void getBlockListFromServer() {
        final DBManager dbManager = DBManager.getInstance(context);
        String syncToken = TokenUtil.getInstance(context).getBlockToken(UserUtil.getInstance(context).getUserUid());
        Observable<HttpResult<List<Block>>> observable = userApi.listBlock(syncToken);
        Observable<List<Block>> dbObservable = observable.map(new Func1<HttpResult<List<Block>>, List<Block>>() {
            @Override
            public List<Block> call(HttpResult<List<Block>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus() != 1) {
                    return null;
                } else {
                    TokenUtil.getInstance(context)
                            .setBlockToken(UserUtil.getInstance(context).getUserUid(), result.getSyncToken());
                    for (Block block : result.getData()) {
                        dbManager.insertBlock(block);
                    }
                    return DBManager.getInstance(context).getBlockContacts();
                }
            }
        });

        ItimeSubscriber<List<Block>> subscriber = new ItimeSubscriber<List<Block>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_BLOCK_LIST, null);
                }
            }

            @Override
            public void onNext(List<Block> list) {
                if (list == null) {
                    if (getView() != null) {
                        getView().onTaskError(TASK_BLOCK_LIST, null);
                    }
                } else {
                    if (getView() != null) {
                        getView().onTaskSuccess(TASK_BLOCK_LIST, blocksToContacts(list));
                    }
                }
            }
        };
        HttpUtil.subscribe(dbObservable, subscriber);
    }

}
