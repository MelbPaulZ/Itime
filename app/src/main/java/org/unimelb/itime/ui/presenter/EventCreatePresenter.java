package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateAddInviteeMvpView;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreatePresenter<V extends TaskBasedMvpView> extends ItimeBasePresenter<V> {
    public static final int TASK_EVENT_CONFIRM = 1;
    public static final int TASK_EVENT_CREATE = 2;
    private static final String TAG = "EventCreatePresenter";
    public EventCreatePresenter(Context context) {
        super(context);
    }

    public void confirmEvent(Event event){
        //TODO
        if(getView() !=null){
            getView().onTaskSuccess(TASK_EVENT_CONFIRM, null);
        }
    }

    public void createEvent(Event event){
        EventUtil.generateNeededHostAttributes(getContext(), event);
        if (event.getEventType().equals(Event.TYPE_GROUP)){
            EventUtil.generateGroupEventAttributes(getContext(), event);
        }
        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        EventApi eventApi = HttpUtil.createService(getContext(), EventApi.class);
        Observable<HttpResult<List<Event>>> observable = eventApi.insert(event, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Event>> result) {
                updateEventToken(result.getSyncToken());
                synchronizeLocal(result.getData());
//                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                if(getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }

    private void updateEventToken(String token){
        TokenUtil.getInstance(getContext()).setEventToken(UserUtil.getInstance(getContext()).getUserUid(), token);
    }

    public void synchronizeLocal(List<Event> events){
        for (Event ev: events){
            synchronizeLocal(ev);
        }
    }

    private void synchronizeLocal(Event ev){
        boolean visibility = EventUtil.isEventInVisibleCalendar(ev, getContext());

        if (!visibility){
            //remove event from event manager
            EventManager.getInstance(getContext()).removeEvent(ev);
            Toast.makeText(getContext(),"Created/Updated to an INVISIBLE calendar.",Toast.LENGTH_LONG).show();
        }else {
            // if visible, update EventManager
            EventManager.getInstance(getContext()).insertOrUpdate(ev);
            Toast.makeText(getContext(),"insert to event manager",Toast.LENGTH_LONG).show();
        }
        //need update DB after update Eventmanager
        DBManager.getInstance(getContext()).insertOrReplace(Arrays.asList(ev));
    }



    public List<Contact> getContacts(){
        DBManager dbManager = DBManager.getInstance(getContext());
        return dbManager.getAllContact();

//        List<Contact> contacts = new ArrayList<>();
//        Contact contact1 = new Contact();
//        contact1.setAliasName("a");
//        contact1.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact1.setContactUid("1");
//
//        Contact contact2 = new Contact();
//        contact2.setAliasName("b");
//        contact2.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact2.setContactUid("2");
//
//        Contact contact3 = new Contact();
//        contact3.setAliasName("c");
//        contact3.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact3.setContactUid("3");
//
//        Contact contact4 = new Contact();
//        contact4.setAliasName("d");
//        contact4.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact4.setContactUid("4");
//
//        Contact contact5 = new Contact();
//        contact5.setAliasName("ad");
//        contact5.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact5.setContactUid("5");
//
//        contacts.add(contact1);
//        contacts.add(contact5);
//        contacts.add(contact2);
//        contacts.add(contact3);
//        contacts.add(contact4);
//
//        int i=0;
//        for(Contact contact:contacts){
//            User user = new User();
//            user.setEmail("123456@unimelb.edu.au");
//            user.setUserId("123456@unimelb.edu.au");
//            user.setUserUid(i+"");
//            i++;
//            contact.setUserUid(i+"");
//            contact.setUserDetail(user);
//        }
//
//        return contacts;
    }

    public List<Contact> getRecentContacts(){
        DBManager dbManager = DBManager.getInstance(getContext());
        return dbManager.getAllContact();

//        List<Contact> contacts = new ArrayList<>();
//        Contact contact1 = new Contact();
//        contact1.setAliasName("a");
//        contact1.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact1.setContactUid("1");
//
//        Contact contact2 = new Contact();
//        contact2.setAliasName("b");
//        contact2.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact2.setContactUid("2");
//
//        Contact contact3 = new Contact();
//        contact3.setAliasName("c");
//        contact3.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact3.setContactUid("3");
//
//        Contact contact4 = new Contact();
//        contact4.setAliasName("d");
//        contact4.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact4.setContactUid("4");
//
//        Contact contact5 = new Contact();
//        contact5.setAliasName("ad");
//        contact5.setAliasPhoto("http://avatar.csdn.net/A/9/C/1_waldmer.jpg");
//        contact5.setContactUid("5");
//
//        contacts.add(contact1);
//        contacts.add(contact5);
//        contacts.add(contact2);
//        contacts.add(contact3);
//        contacts.add(contact4);
//
//        int i=0;
//        for(Contact contact:contacts){
//            User user = new User();
//            user.setEmail("123456@unimelb.edu.au");
//            user.setUserId("123456@unimelb.edu.au");
//            user.setUserUid(i+"");
//            i++;
//            contact.setUserUid(i+"");
//            contact.setUserDetail(user);
//        }
//
//        return contacts;
    }
}
