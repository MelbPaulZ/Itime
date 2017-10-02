package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Account;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Domain;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.BindApi;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class CalendarPresenter<V extends TaskBasedMvpView> extends MvpBasePresenter<V> {
    public static final String TAG = "CalendarPresenter";
    public static final int TASK_CALENDAR_UPDATE = 0;
    public static final int TASK_CALENDAR_DELETE = 1;
    public static final int TASK_CALENDAR_INSERT = 2;
    public static final int TASK_CALENDAR_BIND_UNIMELB = 3;
    public static final int TASK_CALENDAR_BIND_UNI = 4;
    public static final int TASK_CALENDAR_UNBIND_UNI = 5;


    private Context context;
    private CalendarApi calendarApi;
    private BindApi bindApi;
    private TokenUtil tokenUtil;

    public CalendarPresenter(Context context) {
        this.context = context;
        this.calendarApi = HttpUtil.createService(context, CalendarApi.class);
        this.bindApi = HttpUtil.createService(context, BindApi.class);
        tokenUtil = TokenUtil.getInstance(context);
    }

    public Context getContext() {
        return context;
    }

    private void reloadLocalCalendars(Calendar calendar) {
        Calendar oldCal = DBManager.getInstance(context).find(
                Calendar.class, "calendarUid", calendar.getCalendarUid()).get(0);
        oldCal.delete();
        DBManager.getInstance(context).insertOrReplace(Arrays.asList(calendar));

        EventManager.getInstance(context).refreshEventManager();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
    }

    private void deleteLocalCalendars(Calendar calendar) {
        Calendar oldCal = DBManager.getInstance(context).find(
                Calendar.class, "calendarUid", calendar.getCalendarUid()).get(0);
        oldCal.delete();
        EventManager.getInstance(context).refreshEventManager();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
    }

    public void update(final Calendar calendar) {
        if (getView() != null) {
            getView().onTaskStart(TASK_CALENDAR_UPDATE);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi
                .update(calendar.getCalendarUid(), calendar)
                .map(new Func1<HttpResult<Calendar>, HttpResult<Calendar>>() {
                    @Override
                    public HttpResult<Calendar> call(HttpResult<Calendar> calendarHttpResult) {
                        if (calendarHttpResult.getStatus() == 1) {
                            reloadLocalCalendars(calendarHttpResult.getData());
                        }

                        return calendarHttpResult;
                    }
                });

        ItimeSubscriber<HttpResult<Calendar>> subscriber = new ItimeSubscriber<HttpResult<Calendar>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_CALENDAR_UPDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                if (getView() != null) {
                    getView().onTaskSuccess(TASK_CALENDAR_UPDATE, calendarHttpResult.getData());
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void delete(final Calendar calendar) {
        if (getView() != null) {
            getView().onTaskStart(TASK_CALENDAR_DELETE);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi
                .delete(calendar.getCalendarUid())
                .map(new Func1<HttpResult<Calendar>, HttpResult<Calendar>>() {
                    @Override
                    public HttpResult<Calendar> call(HttpResult<Calendar> calendarHttpResult) {
                        Log.i(TAG, "call: " + calendarHttpResult.getStatus());
                        if (calendarHttpResult.getStatus() == 1) {
                            deleteLocalCalendars(calendarHttpResult.getData());
                        }
                        return calendarHttpResult;
                    }
                });

        ItimeSubscriber<HttpResult<Calendar>> subscriber = new ItimeSubscriber<HttpResult<Calendar>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_CALENDAR_DELETE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                if (getView() != null) {
                    getView().onTaskSuccess(TASK_CALENDAR_DELETE, calendarHttpResult.getData());
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void insert(final Calendar calendar) {
        if (getView() != null) {
            getView().onTaskStart(TASK_CALENDAR_INSERT);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi.insert(calendar);
        ItimeSubscriber<HttpResult<Calendar>> subscriber = new ItimeSubscriber<HttpResult<Calendar>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_CALENDAR_INSERT, null);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                DBManager.getInstance(context).insertOrReplace(Arrays.asList(calendarHttpResult.getData()));
                if (getView() != null) {
                    getView().onTaskSuccess(TASK_CALENDAR_INSERT, calendarHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    /**
     * Added by Qiushuo Huang
     *
     * @return
     */
    public List<Domain> getDomains() {
        return DBManager.getInstance(context).getAllDomains();
    }

    public List<Account> getAccounts() {
        return DBManager.getInstance(context).getAllAccounts();
    }

    /**
     * Added by Qiushuo Huang
     */
    public void bindUniversity(String userId, String password, String source) {
        if (getView() != null) {
            getView().onTaskStart(TASK_CALENDAR_BIND_UNI);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("password", password);
        String syncToken = tokenUtil.getAccountToken(UserUtil.getInstance(context).getUserUid());

        Observable<HttpResult<List<Account>>> observable = bindApi.bindUni(source, params, syncToken)
                .map(new Func1<HttpResult<List<Account>>, HttpResult<List<Account>>>() {
                    @Override
                    public HttpResult<List<Account>> call(HttpResult<List<Account>> result) {
                        if (result.getStatus() == 1) {
                            tokenUtil.setAccountToken(UserUtil.getInstance(context).getUserUid(),
                                    result.getSyncToken());
                            DBManager.getInstance(context).insertOrReplace(result.getData());
                        }
                        return result;
                    }
                });
        ItimeSubscriber<HttpResult<List<Account>>> subscriber = new ItimeSubscriber<HttpResult<List<Account>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_CALENDAR_BIND_UNI, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Account>> result) {
                if (result.getStatus() != 1) {
                    onHttpError(null);
                }

                if (getView() != null) {
                    getView().onTaskSuccess(TASK_CALENDAR_BIND_UNI, null);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    /**
     * Added by Qiushuo Huang
     */
    public void unbindUniversity(String accountUid) {
        if (getView() != null) {
            getView().onTaskStart(TASK_CALENDAR_UNBIND_UNI);
        }

        String syncToken = tokenUtil.getAccountToken(UserUtil.getInstance(context).getUserUid());
        Observable<HttpResult<List<Account>>> observable = bindApi.unbindUni(accountUid, syncToken)
                .map(new Func1<HttpResult<List<Account>>, HttpResult<List<Account>>>() {
                    @Override
                    public HttpResult<List<Account>> call(HttpResult<List<Account>> result) {
                        if (result.getStatus() == 1) {
                            tokenUtil.setAccountToken(UserUtil.getInstance(context).getUserUid(),
                                    result.getSyncToken());
                            DBManager.getInstance(context).insertOrReplace(result.getData());
                        }
                        return result;
                    }
                });
        ItimeSubscriber<HttpResult<List<Account>>> subscriber = new ItimeSubscriber<HttpResult<List<Account>>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null) {
                    getView().onTaskError(TASK_CALENDAR_UNBIND_UNI, null);
                }
            }

            @Override
            public void onNext(HttpResult<List<Account>> result) {
                if (result.getStatus() != 1) {
                    onHttpError(null);
                }

                if (getView() != null) {
                    getView().onTaskSuccess(TASK_CALENDAR_UNBIND_UNI, result.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

}
