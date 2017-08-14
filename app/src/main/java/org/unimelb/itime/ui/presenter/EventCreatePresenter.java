package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.PhotoApi;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.unimelb.itime.ui.presenter.contact.ContactPresenter.TAST_ALL_CONTACT;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreatePresenter<V extends TaskBasedMvpView> extends ItimeBasePresenter<V> {
    public static final int TASK_EVENT_UPDATE = 1;
    public static final int TASK_EVENT_INSERT = 2;
    public static final int TASK_EVENT_DELETE = 3;
    public static final int TASK_EVENT_GET = 4;
    public static final int TASK_EVENT_CONFIRM = 5;
    public static final int TASK_EVENT_ACCEPT = 6;
    public static final int TASK_EVENT_REJECT = 7;
    public static final int TASK_TIMESLOT_ACCEPT = 8;
    public static final int TASK_TIMESLOT_REJECT = 9;
    public static final int TASK_BACK = 10;

    public static final int TASK_UPLOAD_IMAGE= 11;
    public static final int TASK_UPDATE_IMAGE= 12;
    public static final int TASK_DELETE_IMAGE= 13;
    public static final int TASK_SYN_IMAGE= 14;
    public static final int TASK_EVENT_FETCH = 15;
    public static final int TASK_EVENT_CREATE = 16;
    public static final int TASK_REFRESH_EVENT = 17;

    public static final String UPDATE_THIS = "this";
    public static final String UPDATE_ALL = "all";
    public static final String UPDATE_FOLLOWING = "following";


    private static final String TAG = "EventCreatePresenter";


    private EventApi eventApi;
    private PhotoApi photoApi;
    private Context context;

    public EventCreatePresenter(Context context) {
        super(context);
        eventApi = HttpUtil.createService(context, EventApi.class);
        photoApi = HttpUtil.createService(context, PhotoApi.class);
        this.context = context;
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
                if(getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }



    /**
     * Only for solo events can be
     * @param event
     */
    public void updateEvent(final Event event){

        Event orgEvent = DBManager.getInstance(getContext()).getEvent(event.getEventUid());
        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        EventApi eventApi = HttpUtil.createService(getContext(), EventApi.class);
        Observable<HttpResult<List<Event>>> observable = eventApi.update(
                orgEvent.getCalendarUid(),
                event.getEventUid(),
                event,
                orgEvent.getStart().getDateTime(),
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                updateEventToken(listHttpResult.getSyncToken());
                synchronizeLocal(listHttpResult.getData());
                if(getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_UPDATE, listHttpResult.getData());
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

    public void deleteEvent(Event event) {
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_DELETE);
        }
        String syncToken = getEventToken();
        Observable<HttpResult<List<Event>>> observable = eventApi.delete(
                event.getCalendarUid(),
                event.getEventUid(),
                syncToken);
        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_DELETE, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                updateEventToken(listHttpResult.getSyncToken());
                synchronizeLocal(listHttpResult.getData());
                //sync message
//                inboxPresenter.fetchMessages();
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_DELETE, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    /**
     * upload the photo file to a file server,
     * currently it will be uploaded to our server
     * @param event
     */
    public void uploadImage(final Event event) {
//        if (event.hasPhoto()){
//
//            final List<ImageUploadWrapper> wrappers = new ArrayList<>();
//
//            for (final PhotoUrl photoUrl : event.getPhoto()){
//                // create file
//                final String fileName = event.getEventUid() + "_" + photoUrl.getPhotoUid() + ".png";
//
//                File image = new File(photoUrl.getLocalPath());
//                Luban.get(context)
//                        .load(image)                     //传人要压缩的图片
//                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
//                        .asObservable()
//                        .subscribeOn(Schedulers.from(threadPoolExecutor))
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doOnError(new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable throwable) {
//                                throwable.printStackTrace();
//                            }
//                        })
//                        .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
//                            @Override
//                            public Observable<? extends File> call(Throwable throwable) {
//                                return Observable.empty();
//                            }
//                        })
//                        .subscribe(new Action1<File>() {
//                            @Override
//                            public void call(File file) {
//                                final ImageUploadWrapper wrapper = new ImageUploadWrapper(photoUrl,false);
//                                wrappers.add(wrapper);
//                                final AVFile avFile;
//                                try {
//                                    avFile = AVFile.withFile(fileName,file);
//                                    avFile.saveInBackground(new SaveCallback() {
//                                        @Override
//                                        public void done(AVException e) {
//                                            if (e==null){
//                                                wrapper.getPhoto().setUrl(avFile.getUrl());
//                                                wrapper.setUploaded(true);
//
//                                                if (imageUploadChecker(wrappers)){
//                                                    Toast.makeText(getContext(),"Image Uploaded to leanCloud",Toast.LENGTH_SHORT).show();
//                                                    //start to syn server
//                                                    for (PhotoUrl photoUrl:event.getPhoto()
//                                                            ) {
//                                                        updatePhotoToServer(event, photoUrl.getPhotoUid(), photoUrl.getUrl());
//                                                    }
//                                                }else{
//                                                    Log.i(TAG, "done: ");
//                                                }
//                                            }else{
//                                                if (getView() != null){
//                                                    getView().onTaskError(TASK_UPLOAD_IMAGE, null);
//                                                }
//                                            }
//                                        }
//                                    });
//                                } catch (IOException e1) {
//                                    e1.printStackTrace();
//                                }
//                            }
//                        });
//            }
//        }
    }

    /**
     * update the photo url to server
     * @param event
     * @param url
     */
    public void updatePhotoToServer(final Event event, String photoUid, String url){
        Observable<HttpResult<Event>> observable = photoApi.updatePhoto(event.getCalendarUid(), event.getEventUid(), photoUid, url);
        ItimeSubscriber<HttpResult<Event>> subscriber = new ItimeSubscriber<HttpResult<Event>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null){
                    getView().onTaskError(TASK_SYN_IMAGE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void updateReminder(final Event event){
        if(getView() != null){
            getView().onTaskStart(TASK_EVENT_UPDATE);
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("reminder", event.getReminder());
        // orgCalendarUid to get the previous org event in server link
        String orgCalendarUid = EventManager.getInstance(context).getCurrentEvent().getCalendarUid();
        final String syncToken = getEventToken();

        Observable<HttpResult<List<Event>>> observable = eventApi.reminderUpdate(
                orgCalendarUid,
                event.getEventUid(),
                params,
                syncToken);

        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_EVENT_UPDATE, e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                if(eventHttpResult.getStatus() != 1){
                    throw new RuntimeException(eventHttpResult.getInfo());
                }
                synchronizeLocal(eventHttpResult.getData());
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_UPDATE, eventHttpResult.getData());
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                //sync message
//                inboxPresenter.fetchMessages();
                updateEventToken(eventHttpResult.getSyncToken());
                updateImage(event);
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    public void quitEvent(String calendarUid, String eventUid, String type, long originalStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_REJECT);
        }
        String syncToken = getEventToken();
        Observable<HttpResult<List<Event>>> observable = eventApi.quitEvent(
                calendarUid,
                eventUid,
                type,
                originalStartTime,
                syncToken);

        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_REJECT, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_REJECT, listHttpResult.getData());
                }

                //sync message
//                inboxPresenter.fetchMessages();
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void acceptEvent(String calendarUid, String eventUid, String type, long orgStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_ACCEPT);
        }
//        recordScrollTime(orgStartTime);
        String syncToken = getEventToken();
        Observable<HttpResult<List<Event>>> observable = eventApi.acceptEvent(
                calendarUid,
                eventUid,
                type,
                orgStartTime,
                syncToken);
        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());
                updateEventToken(listHttpResult.getSyncToken());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                //sync message
//                inboxPresenter.fetchMessages();
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_ACCEPT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    /** call the api to accept timeSlots in a event,
     *  after this api called, it will automatically sync with db
     *
     * */
    public void acceptTimeslots(Event event, HashMap<String, Object> params, long firstAcceptTimeslot){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_ACCEPT);
        }

        // find timeslot start time to record scroll, for two people event only
        if (event.getInvitee().size()==2) {
//            recordScrollTime(firstAcceptTimeslot);
        }

        String eventUid = event.getEventUid();
        String calendarUid = event.getCalendarUid();

        String syncToken = getEventToken();
        Observable<HttpResult<List<Event>>> observable = eventApi.acceptTimeslot(calendarUid,
                eventUid,
                params,
                syncToken);

        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_TIMESLOT_ACCEPT, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                updateEventToken(eventHttpResult.getSyncToken());
                //sync message
//                inboxPresenter.fetchMessages();
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_TIMESLOT_ACCEPT, eventHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    /** call the api to confirm event to server,
     *  after this api called, it will automatically sync db
     *
     * @param calendarUid
     * @param eventUid
     * @param timeslotUid
     */
    public void confirmEvent(String calendarUid, String eventUid, String timeslotUid, long confirmStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_CONFIRM);
        }
        String syncToken = getEventToken();
//        recordScrollTime(confirmStartTime);
        Observable<HttpResult<List<Event>>> observable = eventApi.confirm(calendarUid,eventUid, timeslotUid, syncToken);
        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_CONFIRM, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                updateEventToken(eventHttpResult.getSyncToken());
                //sync message
//                inboxPresenter.fetchMessages();
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_CONFIRM, eventHttpResult.getData());
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }


    public void rejectTimeslots(String calendarUid, String eventUid){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_REJECT);
        }
        String syncToken = getEventToken();
        Observable<HttpResult<List<Event>>> observable = eventApi.rejectTimeslot(
                calendarUid,
                eventUid,
                syncToken);
        ItimeSubscriber<HttpResult<List<Event>>> subscriber = new ItimeSubscriber<HttpResult<List<Event>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_TIMESLOT_REJECT, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());
                //sync message
//                inboxPresenter.fetchMessages();
                updateEventToken(listHttpResult.getSyncToken());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_TIMESLOT_REJECT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private String getEventToken(){
        return TokenUtil.getInstance(context).getEventToken(UserUtil.getInstance(context).getUserUid());
    }

    private void updateImage(Event event){
        List<PhotoUrl> newImageSet = new ArrayList<>();
        for (PhotoUrl url: event.getPhoto()
                ) {
            if (url.getUrl().equals("") && !url.getLocalPath().equals("")){
                newImageSet.add(url);
            }
        }
        if (newImageSet.size()!=0){
            event.setPhoto(newImageSet);
            uploadImage(event);
        }else{

        }
    }

    public void refreshEvent(String eventUid){
        Observable<Event> observable = Observable.create(new Observable.OnSubscribe<Event>() {
            @Override
            public void call(Subscriber<? super Event> subscriber) {
                DBManager dbManager = DBManager.getInstance(context);
                subscriber.onNext(dbManager.getEvent(eventUid));
            }
        });
        ItimeSubscriber<Event> subscriber = new ItimeSubscriber<Event>() {
            @Override
            public void onHttpError(Throwable e) {
                getView().onTaskError(TASK_REFRESH_EVENT, null);
            }

            @Override
            public void onNext(Event result) {
                if(getView()!=null){
                    getView().onTaskSuccess(TASK_REFRESH_EVENT, result);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private class ImageUploadWrapper{
        PhotoUrl photo;
        boolean uploaded;

        ImageUploadWrapper(PhotoUrl photo, boolean uploaded) {
            this.photo = photo;
            this.uploaded = uploaded;
        }

        boolean isUploaded() {
            return uploaded;
        }

        void setUploaded(boolean uploaded) {
            this.uploaded = uploaded;
        }

        PhotoUrl getPhoto() {
            return photo;
        }

        void setPhoto(PhotoUrl photo) {
            this.photo = photo;
        }
    }


    public List<Contact> getContacts(){
        DBManager dbManager = DBManager.getInstance(getContext());
        return dbManager.getAllContact();
    }

    public List<Contact> getRecentContacts(){
        DBManager dbManager = DBManager.getInstance(getContext());
        return dbManager.getAllContact();
    }

    public void muteEvent(Event event, boolean isMute){

        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        int mute = isMute?EventApi.OPERATION_TRUE:EventApi.OPERATION_FALSE;

        Observable<HttpResult<List<Event>>> observable = eventApi.mute(event.getCalendarUid(), event.getEventUid(), mute, syncToken);
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
//                updateEventToken(result.getSyncToken());
//                synchronizeLocal(result.getData());
////                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                if(getView() != null){
//                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }

    public void pinEvent(Event event, boolean isPin){

        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        int pin = isPin?EventApi.OPERATION_TRUE:EventApi.OPERATION_FALSE;

        Observable<HttpResult<List<Event>>> observable = eventApi.pin(event.getCalendarUid(), event.getEventUid(), pin, syncToken);
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
//                updateEventToken(result.getSyncToken());
//                synchronizeLocal(result.getData());
////                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                if(getView() != null){
//                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }

    public void archiveEvent(Event event, boolean isArchive){

        String syncToken = TokenUtil.getInstance(getContext()).getEventToken(UserUtil.getInstance(getContext()).getUserUid());
        int archive = isArchive?EventApi.OPERATION_TRUE:EventApi.OPERATION_FALSE;

        Observable<HttpResult<List<Event>>> observable = eventApi.archive(event.getCalendarUid(), event.getEventUid(), archive, syncToken);
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
//                updateEventToken(result.getSyncToken());
//                synchronizeLocal(result.getData());
////                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                if(getView() != null){
//                    getView().onTaskSuccess(TASK_EVENT_CREATE, result.getData());
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);

    }

}
