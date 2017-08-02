package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;


import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Contact;

import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.User;

import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.messageevent.contact.MessageUnblockContact;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.contact.ProfileMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by 37925 on 2016/12/14.
 */

public class ProfilePresenter<T extends TaskBasedMvpView> extends MvpBasePresenter<T> {
    private static final String TAG = "profile";
    public static final int TASK_ADD = 0;
    public static final int TASK_DELETE = 1;
    public static final int TASK_BLOCK = 2;
    public static final int TASK_UNBLOCK = 3;
    public static final int TASK_ACCEPT = 4;
    public static final int TASK_CONTACT = 5;
    public static final int TASK_STRANGER = 6;
    public static final int TASK_REQUEST = 7;

    private Context context;
    private UserApi userApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;
    private DBManager dbManager;
    private TokenUtil tokenUtil;


    public ProfilePresenter(Context context) {
        this.context = context;
        dbManager = DBManager.getInstance(getContext());
        userApi = HttpUtil.createService(context, UserApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context, FriendRequestApi.class);
        tokenUtil = TokenUtil.getInstance(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void acceptRequest(final String requestId, final User user){
//        Observable<HttpResult<List<FriendRequest>>> observable = requestApi.confirm(requestId);
//        ItimeSubscriber<HttpResult<List<FriendRequest>>> subscriber = new ItimeSubscriber<HttpResult<List<FriendRequest>>>() {
//            @Override
//            public void onHttpError(Throwable e) {
//                if(getView()!=null) {
//                    getView().onTaskError(TASK_ACCEPT, null);
//                }
//            }
//
//            @Override
//            public void onNext(HttpResult<List<FriendRequest>> result) {
//                if (result.getStatus()!=1){
//
//                }else {
//                    FriendRequest resultRequest = result.getData().get(0);
//                    resultRequest.setUserDetail(user);
//                    dbManager.insertFriendRequest(resultRequest);
//                    if(getView()!=null) {
//                        getView().onTaskSuccess(TASK_ACCEPT, null);
//                    }
//                }
//            }
//        };
//        HttpUtil.subscribe(observable, subscriber);
    }

    public void blockUser(final Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Block>> observable = userApi.block(Integer.parseInt(user.getContactUid()))
                .map(new Func1<HttpResult<Block>, HttpResult<Block>>() {
                    @Override
                    public HttpResult<Block> call(HttpResult<Block> result) {
                        if(result.getStatus()==1) {
                            TokenUtil.getInstance(context).setBlockToken(UserUtil.getInstance(context).getUserUid(),
                                    result.getSyncToken());
                            user.setBlockLevel(result.getData().getBlockLevel());
                            dbManager.insertOrReplace(Arrays.asList(user));
                            Block block = result.getData();
                            block.setUserDetail(user.getUserDetail());
                            dbManager.insertBlock(block);
                        }
                        return result;
                    }
                });
        ItimeSubscriber<HttpResult<Block>> subscriber = new ItimeSubscriber<HttpResult<Block>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView()!=null) {
                    getView().onTaskError(TASK_BLOCK, null);
                }
            }

            @Override
            public void onNext(HttpResult<Block> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_BLOCK, null);
                    }
                }else {
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_BLOCK, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void unblockUser(final Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Block>> observable = userApi.unblock(Integer.parseInt(user.getContactUid()))
                .map(new Func1<HttpResult<Block>, HttpResult<Block>>() {
                    @Override
                    public HttpResult<Block> call(HttpResult<Block> result) {
                        TokenUtil.getInstance(context).setBlockToken(UserUtil.getInstance(context).getUserUid(),
                                result.getSyncToken());
                        user.setBlockLevel(result.getData().getBlockLevel());
                        dbManager.insertOrReplace(Arrays.asList(user));
                        dbManager.deleteBlock(result.getData());
                        return result;
                    }
                });
        ItimeSubscriber<HttpResult<Block>> subscriber = new ItimeSubscriber<HttpResult<Block>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView()!=null) {
                    getView().onTaskError(TASK_UNBLOCK, null);
                }
            }

            @Override
            public void onNext(HttpResult<Block> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_UNBLOCK, null);
                    }
                }else {
                    EventBus.getDefault().post(new MessageAddContact(user));
                    EventBus.getDefault().post(new MessageUnblockContact(user));
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_UNBLOCK, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void deleteUser(final Contact user){
        if(getView()!=null) {
            getView().onTaskStart(TASK_DELETE);
        }
        if(getView()==null){
            return;
        }
        String syncToken = tokenUtil.getContactToken(UserUtil.getInstance(context).getUserUid());
        Observable<HttpResult<Contact>> observable = contactApi.delete(user.getContactUid(),syncToken).map(new Func1<HttpResult<Contact>, HttpResult<Contact>>() {
            @Override
            public HttpResult<Contact> call(HttpResult<Contact> result) {
                if(result.getStatus()==1){
                    user.setStatus(result.getData().getStatus());
                    user.setRelationship(user.getRelationship()-1);
                    dbManager.insertOrReplace(Arrays.asList(user));
                    tokenUtil.setContactToken(UserUtil.getInstance(context).getUserUid(),result.getSyncToken());
                }
                return result;
            }
        });
        ItimeSubscriber<HttpResult<Contact>> subscriber = new ItimeSubscriber<HttpResult<Contact>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView()!=null) {
                    getView().onTaskError(TASK_DELETE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Contact> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_DELETE, null);
                    }
                }else {
                    EventBus.getDefault().post(new MessageRemoveContact(result.getData()));
                    if(getView()!=null) {
                        getView().onTaskSuccess(TASK_DELETE, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void addUser(Contact user){
        if(getView()==null){
            return;
        }
        Observable<HttpResult<Void>> observable = requestApi.send(user.getContactUid(), FriendRequest.SOURCE_ITIME);
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
                        getView().onTaskSuccess(TASK_ADD, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void inviteUser(){
//        if(getView()!=null) {
//            getView().goToInviteFragment();
//        }
    }

    public void gotoEditAlias() {
//        if(getView()!=null){
//            getView().goToEditAlias();
//        }
    }

    public void getRequest(String userUid){
//        List<FriendRequest> requests = dbManager.getAllFriendRequest();
//        for(FriendRequest request: requests){
//            if (request.getUserDetail().getUserUid().equals(userUid)){
//                getView().onTaskSuccess(TASK_REQUEST, new Contact(request.getUserDetail()));
//                return;
//            }
//        }
    }

    public void getContact(String userUid){
        if(userUid==null || userUid.equals("")){
            return;
        }

        userUid = userUid.trim();
        if(getView()!=null) {
            getView().onTaskStart(TASK_CONTACT);
            List<Contact> contacts = dbManager.getAllContact();
            for(Contact contact: contacts){
                if (contact.getUserDetail().getUserUid().equals(userUid)){
                    getView().onTaskSuccess(TASK_CONTACT, contact);
                    return;
                }
            }

            List<Block> blocks = dbManager.getBlockContacts();
            for(Block block: blocks){
                if (block.getUserDetail().getUserUid().equals(userUid)){
                    Contact contact = new Contact(block.getUserDetail());
                    contact.setUserUid(UserUtil.getInstance(context).getUserUid());
                    contact.setBlockLevel(1);
                    getView().onTaskSuccess(TASK_CONTACT, contact);
                    return;
                }
            }

            User user = dbManager.getUser(userUid);
            if(user!=null){
                getView().onTaskSuccess(TASK_STRANGER, new Contact(user));
                return;
            }

            findUser(userUid);
        }
    }

    private void findUser(String userUid){
        if(userUid==null || userUid.equals("")){
            return;
        }

        Observable<HttpResult<User>> observable = userApi.get(userUid);
        ItimeSubscriber<HttpResult<User>> subscriber = new ItimeSubscriber<HttpResult<User>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView()!=null) {
                    getView().onTaskError(TASK_STRANGER, null);
                }
            }

            @Override
            public void onNext(HttpResult<User> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_STRANGER, null);
                    }
                }else {
                    if(result.getData()==null){
                        if(getView()!=null) {
                            getView().onTaskError(TASK_STRANGER, null);
                        }
                    }else {
                        User user = result.getData();
                        dbManager.insertOrReplace(Arrays.asList(user));
                        if(getView()!=null) {
                            getView().onTaskSuccess(TASK_STRANGER, new Contact(user));
                        }
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void editAlias(Contact contact){
        if(getView()!=null){
            EventBus.getDefault().post(new MessageEditContact(contact));
            updateAlias(contact);
        }
    }

    private void updateAlias(final Contact contact){
        Observable<HttpResult<Void>> observable = contactApi.updateAlias(contact.getContactUid(), contact.getAliasName());

        ItimeSubscriber<HttpResult<Void>> subscriber = new ItimeSubscriber<HttpResult<Void>>() {
            @Override
            public void onHttpError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<Void> result) {
                if (result.getStatus()!=1){

                }else {
                    DBManager.getInstance(context).insertOrReplace(Arrays.asList(contact));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
