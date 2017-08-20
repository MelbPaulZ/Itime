package org.unimelb.itime.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestResult;
import org.unimelb.itime.bean.ItimeWebSocket;
import org.unimelb.itime.bean.Meeting;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.MessageGroup;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.messageevent.MessageNetwork;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.ITimeActivityApi;
import org.unimelb.itime.restfulapi.MeetingApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.WebSocketRes;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.presenter.MeetingPresenter;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.NetworkUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;


import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class RemoteService extends Service {
    private final static String TAG = "RemoteService";
    private EventApi eventApi;
    private MeetingApi meetingApi;
    private MessageApi msgApi;
    private CalendarApi calendarApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;
    private ITimeActivityApi iTimeActivityApi;
    private Boolean isStart = true;

    private Context context;
    private User user;

    private DBManager dbManager;
    private EventManager eventManager;
    private TokenUtil tokenUtil;
    private UserUtil userUtil;
    List<Event> visibleEventList = new ArrayList<>();
    List<Event> invisibleEventList = new ArrayList<>();

    private ItimeWebSocket itimeWebSocket;

//    private MainInboxPresenter inboxPresenter;
//    private EventPresenter eventPresenter;
    private UserPresenter userPresenter;
//    private SettingPresenter settingPresenter;

    private long breakTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        EventBus.getDefault().register(this);
        initPresenters();
        fetchDomains();
    }

    @Subscribe
    public void receiveMessage(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.LOGIN_SUCCESS){
            successLogin();
        }else if (messageEvent.task == MessageEvent.LOGIN_REGISTER_LEANCLOUD){
            // TODO: 27/7/17  registerToLeanCloud
//            registerToLeanCloud();
            Log.i(TAG, "receiveMessage: " + "registerToLeanCloud");
        }
    }

    @Subscribe
    public void getErrorToast(MessageNetwork messageNetwork){
        if (messageNetwork.task == MessageNetwork.NETWORK_ERROR){
            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
        }
    }



    private void successLogin(){
        context = getApplicationContext();
        dbManager = DBManager.getInstance(context);

        eventManager = EventManager.getInstance(context);
        tokenUtil = TokenUtil.getInstance(context);

        eventApi = HttpUtil.createService(context, EventApi.class);
        meetingApi = HttpUtil.createService(context, MeetingApi.class);
        msgApi = HttpUtil.createService(context, MessageApi.class);
        calendarApi = HttpUtil.createService(context, CalendarApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context, FriendRequestApi.class);
        iTimeActivityApi = HttpUtil.createService(context, ITimeActivityApi.class);

        user = UserUtil.getInstance(context).getUser();
        loadLocalEvents();
        loadRegions();
        initWebSocket();
    }


    /**
     * this method use for register this user in lean cloud
     */
//    private void registerToLeanCloud(){
//        userUtil = UserUtil.getInstance(this);
//        try{
//            stopService(new Intent(this, PushService.class));
//        } catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }
//        PushService.setDefaultPushCallback(getApplication(), MainActivity.class);
//        String userUid = userUtil.getUserUid();
//        AVInstallation.getCurrentInstallation().put("user_uid", userUid);
//        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
//            public void done(AVException e) {
//                if (e == null) {
//                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
//                }
//                //FOR recording last used user name
//                try {
//                    String userName = UserUtil.getInstance(context).getUser().getUserId();
//                    SharedPreferences sp = AppUtil.getSharedPreferences(context);
//                    sp.edit().putString(C.spkey.USER_ID, userName).apply();
//                }catch (Exception ex){
//                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    /**
     * when user logout, call this method to set user_uid in leancloud as -1, so no message will be pushed
     */
//    private void unregisterFromLeanCloud(){
//        AVInstallation.getCurrentInstallation().put("user_uid", "-1");
//        AVInstallation.getCurrentInstallation().saveInBackground();
//    }

    private void fetchDomains(){
        userPresenter.fetchDomainsFromServer();
    }

    private void firstFetchData(){
        fetchCalendar();
        fetchContact();
//        fetchFriendRequest();
//        fetchMessages();
        fetchMeetings();
        fetchITimeActivities();
    }

    private void initPresenters(){
//        inboxPresenter = new MainInboxPresenter(context);
//        eventPresenter = new EventPresenter(context);
        userPresenter = new UserPresenter(context);
//        settingPresenter = new SettingPresenter(context);
    }


    private void initWebSocket() {
        itimeWebSocket = ItimeWebSocket.getInstance(context);
        itimeWebSocket.setUserUid(user.getUserUid());
        itimeWebSocket.setDeviceUid(AppUtil.getUidForDevice());
        itimeWebSocket.setJwsToken(AppUtil.getTokenSaver(context).getString(C.spkey.ITIME_JWT_TOKEN, ""));
        itimeWebSocket.setOnConnectInterface(new ItimeWebSocket.OnConnectInterface() {
            @Override
            public void onConnect() {
                firstFetchData();
                breakTime = System.currentTimeMillis();
                Log.i(TAG, "websocket connect: " + breakTime);
                NetworkUtil.getInstance(context).setAvailable(true);
            }

            @Override
            public void onConnectError() {
                NetworkUtil.getInstance(context).setAvailable(false);
            }

            @Override
            public void onDisconnect() {
                Log.i(TAG, "websocket disconnect: " + (System.currentTimeMillis() - breakTime));
            }
        });

        itimeWebSocket.connect();
        itimeWebSocket.setOnItimeMessage(new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                String s = objects[0].toString();
                Gson gson = new Gson();
                WebSocketRes webSocketRes = gson.fromJson(s, WebSocketRes.class);
                runTask(webSocketRes.getType());
            }
        });
    }

    private void runTask(String task) {
        switch (task) {
            case WebSocketRes.SYNC_EVENT:
                fetchEvents();
                break;
            case WebSocketRes.SYNC_MEETING:
                fetchMeetings();
                break;
            case WebSocketRes.SYNC_INBOX:
                fetchMessages();
                break;
            case WebSocketRes.SYNC_USER:
                Log.i(TAG, "runTask:  user" + AppUtil.getUidForDevice());
                // TODO: 1/3/17 fetch user info
                break;
            case WebSocketRes.SYNC_CONTACT:
                fetchContact();
                break;
            case WebSocketRes.SYNC_SETTING:
                Log.i(TAG, "runTask: setting");
                // TODO: 1/3/17 fetch setting info
                break;
            case WebSocketRes.SYNC_CALENDAR:
                Log.i(TAG, "runTask: ");
                fetchCalendar();
                break;
            case WebSocketRes.SYNC_FRIEND_REQUEST:
                Log.i(TAG, "runTask: friend");
                fetchFriendRequest();
                break;
        }
    }

    @Override
    public void onDestroy() {
        itimeWebSocket.disconnect();
//        unregisterFromLeanCloud();
        isStart = false;
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGOUT));
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadLocalEvents() {
        new Thread() {
            @Override
            public void run() {
                eventManager.refreshEventManager();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        }.start();
    }

    /**
     * if haven't load regions, then init regions
     */
    private void loadRegions() {
//        new Thread() {
//            @Override
//            public void run() {
//                if (DBManager.getInstance(context).getCountryList().size() == 0) {
//                    DBManager.getInstance(context).initRegion();
//                }
//            }
//        }.start();
    }

    private void fetchCalendar() {
        // here to list calendar;
        String token = tokenUtil.getCalendarToken(user.getUserUid());
        Observable<HttpResult<List<Calendar>>> observable = calendarApi.list(token)
                .map(new Func1<HttpResult<List<Calendar>>, HttpResult<List<Calendar>>>() {
                    @Override
                    public HttpResult<List<Calendar>> call(HttpResult<List<Calendar>> ret) {
                        if (isStart && ret.getStatus() == 1 && ret.getData().size() > 0) {
                            tokenUtil.setCalendarToken(user.getUserUid(), ret.getSyncToken());
                            dbManager.insertOrReplace(ret.getData());
                            tokenUtil.setContactToken(user.getUserUid(), ret.getSyncToken());
                        }
                        return ret;
                    }
                });
        Subscriber<HttpResult<List<Calendar>>> subscriber = new Subscriber<HttpResult<List<Calendar>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Calendar>> httpResult) {
                if (!isStart) {
                    return;
                }
                fetchEvents();
                //todo need to notify ui changes
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchMessages() {
        final String token = tokenUtil.getMessageToken(user.getUserUid());
        Observable<HttpResult<List<Message>>> observable = msgApi.get(token)
                .map(new Func1<HttpResult<List<Message>>, HttpResult<List<Message>>>() {
                    @Override
                    public HttpResult<List<Message>> call(HttpResult<List<Message>> ret) {
                        if (isStart && ret.getStatus() == 1) {
                            List<Message> msgs = ret.getData();
                            dbManager.insertOrReplace(msgs);
                            tokenUtil.setMessageToken(user.getUserUid(), ret.getSyncToken());
                        }
                        return ret;
                    }
                });
        Subscriber<HttpResult<List<Message>>> subscriber = new Subscriber<HttpResult<List<Message>>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpResult<List<Message>> ret) {
                if (!isStart) {
                    return;
                }
                if (ret.getStatus() == 1 && ret.getData().size() > 0) {
                    EventBus.getDefault().post(new MessageInboxMessage(ret.getData()));
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvents() {
        final String synToken = TokenUtil.getInstance(context).getEventToken(user.getUserUid());
        Observable<HttpResult<List<Event>>> observable = eventApi.list("-1", synToken) // -1 will fetch all events
                .map(new Func1<HttpResult<List<Event>>, HttpResult<List<Event>>>() {
                    @Override
                    public HttpResult<List<Event>> call(HttpResult<List<Event>> ret) {
                        if (ret.getData().size() > 0) {
                            //update db
                            dbManager.insertOrReplace(ret.getData());

                            //if calendar not shown
                            visibleEventList.clear();
                            invisibleEventList.clear();
                            for (Event ev : ret.getData()) {
                                if (EventUtil.isEventInVisibleCalendar(ev, context)) {
                                    visibleEventList.add(ev);
                                } else {
                                    invisibleEventList.add(ev);
                                }
                            }
                            //update syncToken
                            tokenUtil.setEventToken(user.getUserUid(), ret.getSyncToken());
                        }

                        return ret;
                    }
                });
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(final HttpResult<List<Event>> result) {
                if (!isStart) {
                    return;
                }
                //sync event manager
                for (Event event : visibleEventList
                        ) {
                    eventManager.insertOrUpdate(event);
                }

                if (result.getData().size() > 0 && result.getStatus() == 1 && result.getStatus() > 0) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void fetchContact() {
        final String synToken = TokenUtil.getInstance(context).getContactToken(user.getUserUid());
        Observable<HttpResult<List<Contact>>> observable = contactApi.list(synToken).map(new Func1<HttpResult<List<Contact>>, HttpResult<List<Contact>>>() {
            @Override
            public HttpResult<List<Contact>> call(HttpResult<List<Contact>> ret) {
                if (ret.getStatus() == 1 && ret.getData().size() > 0) {
                    List<Contact> contactList = ret.getData();
                    // need to uncomment
                    dbManager.insertOrReplace(contactList);
                    EventBus.getDefault().post(new MessageEditContact(null));
                    TokenUtil.getInstance(context).setContactToken(user.getUserUid(), ret.getSyncToken());
                }
                return ret;
            }
        });
        Subscriber<HttpResult<List<Contact>>> subscriber = new Subscriber<HttpResult<List<Contact>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpResult<List<Contact>> ret) {
                if (!isStart) {
                    return;
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    // Added by Qiushuo Huang
    private void fetchFriendRequest() {
        String syncToken = TokenUtil.getInstance(context).getFriendRequestToken(
                UserUtil.getInstance(context).getUserUid());

        Observable<HttpResult<FriendRequestResult>> observable = requestApi.list(syncToken).map(new Func1<HttpResult<FriendRequestResult>, HttpResult<FriendRequestResult>>() {
            @Override
            public HttpResult<FriendRequestResult> call(HttpResult<FriendRequestResult> result) {
                if (result.getStatus() != 1) {

                } else {
                    TokenUtil.getInstance(context).setFriendRequestToken(
                            UserUtil.getInstance(context).getUserUid(), result.getSyncToken());
                    for (FriendRequest request : result.getData().getSend()) {
                        dbManager.insertFriendRequest(request);
                    }
                    for (FriendRequest request : result.getData().getReceive()) {
                        dbManager.insertFriendRequest(request);
                    }
                }
                return result;
            }
        });

        Subscriber<HttpResult<FriendRequestResult>> subscriber = new Subscriber<HttpResult<FriendRequestResult>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<FriendRequestResult> result) {
                if (result.getStatus() != 1) {

                } else {
                    int count = 0;
                    for (FriendRequest request : result.getData().getSend()) {
                        if (!request.isRead()) {
                            count++;
                        }
                    }
                    for (FriendRequest request : result.getData().getReceive()) {
                        if (!request.isRead()) {
                            count++;
                        }
                    }
                    EventBus.getDefault().post(new MessageNewFriendRequest(count));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void fetchMeetings(){
        final String synToken = TokenUtil.getInstance(context).getMeetingToken(user.getUserUid());

        Observable<HttpResult<List<Meeting>>> observable = meetingApi.list(synToken) // -1 will fetch all events
                .map(ret -> {
                    if (ret.getData().size() > 0) {
                        //update db
                        dbManager.insertOrReplace(ret.getData());

                        //update syncToken
                        tokenUtil.setMeetingToken(user.getUserUid(), ret.getSyncToken());
                    }

                    return ret;
                });
        Subscriber<HttpResult<List<Meeting>>> subscriber = new Subscriber<HttpResult<List<Meeting>>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(final HttpResult<List<Meeting>> result) {
                MeetingPresenter.outOfDate = true;
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_MEETING));
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void fetchITimeActivities(){
        final String synToken = TokenUtil.getInstance(context).getITimeActivitiesToken(user.getUserUid());
        Observable<HttpResult<List<MessageGroup>>> observable = iTimeActivityApi.list(synToken).map(ret -> {
            if (ret.getData().size() > 0) {
                dbManager.insertMessageGroups(ret.getData());
                tokenUtil.setITimeActivitiesToken(user.getUserUid(), ret.getSyncToken());
            }
            return ret;
        });
        Subscriber<HttpResult<List<MessageGroup>>> subscriber = new Subscriber<HttpResult<List<MessageGroup>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<MessageGroup>> listHttpResult) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_ITIME_ACTIVITIES));
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


}
