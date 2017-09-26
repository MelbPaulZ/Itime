package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.SettingApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Paul on 3/10/16.
 */
public class SettingPresenter <V extends TaskBasedMvpView> extends MvpBasePresenter<V> {
    public static final int TASK_SETTING_UPDATE = 0;
    public static final int TASK_SEARCH_USER = 44444;
    public static final int TASK_TO_SETTING = 44445;
    public static final int ERROR_USER_NOT_FOUND = 44446;
    public static final int TAST_SEND_REPORT = 44447;
    public static final int TAST_GET_HELP_LIST = 44448;

    private static final String TAG = "SettingPresenter";
    private Context context;
    private SettingApi settingApi;
    private UserApi userApi;

    public SettingPresenter(Context context) {
        this.context = context;
        this.settingApi = HttpUtil.createService(context, SettingApi.class);
        this.userApi = HttpUtil.createService(context, UserApi.class);
    }

    public Context getContext(){
        return context;
    }

    public void update(final Setting setting){
        if(getView() != null){
            getView().onTaskStart(TASK_SETTING_UPDATE);
        }

        Observable<HttpResult<Setting>> observable = settingApi.update(setting);
        ItimeSubscriber<HttpResult<Setting>> subscriber = new ItimeSubscriber<HttpResult<Setting>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_SETTING_UPDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Setting> settingHttpResult) {
                DBManager.getInstance(context).updateSetting(settingHttpResult.getData());
                UserUtil.getInstance(getContext()).setSetting(settingHttpResult.getData());

                Log.i(TAG, "onNext: ");
                if(getView() != null){
                    getView().onTaskSuccess(TASK_SETTING_UPDATE, settingHttpResult.getData());
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void findFriend(String searchStr){
        if(searchStr==null || searchStr.equals("")){
            return;
        }

        if(UserUtil.getInstance(context).getUser().getUserId()
                .equals(searchStr.trim())){
            return;
        }

        if(getView()!=null) {
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

    public void gotoSetting(){
        if(getView()!=null){
            getView().onTaskSuccess(TASK_TO_SETTING, null);
        }
    }

    public void sendReport(String reportText){
        if(getView()!=null){
            getView().onTaskStart(TAST_SEND_REPORT);
        }
        Observable<HttpResult<String>> observable = settingApi.feedback(reportText);
        ItimeSubscriber<HttpResult<String>> subscriber = new ItimeSubscriber<HttpResult<String>>() {
            @Override
            public void onHttpError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<String> result) {
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TAST_SEND_REPORT, null);
                    }
                }else {
                    if(getView()!=null){
                        getView().onTaskSuccess(TAST_SEND_REPORT, null);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
