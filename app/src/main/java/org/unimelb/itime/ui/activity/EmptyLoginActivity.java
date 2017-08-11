package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.util.UserUtil;

import david.itimecalendar.calendar.ui.monthview.MonthView;

/**
 * Created by yuhaoliu on 27/7/17.
 */

public class EmptyLoginActivity extends ItimeBaseActivity<LoginMvpView,LoginPresenter> implements LoginMvpView
{

    private final static String username = "qiushuoh@student.unimelb.edu.au";
    private final static String password = "123456";
    private static final String TAG = "EmptyLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserUtil.getInstance(getApplicationContext()).clearAccountWithDB();

        // start remote service
        Intent intent = new Intent(this,RemoteService.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getPresenter().loginByEmail(username,password);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_fragment_container;
    }



    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(getApplicationContext());
    }

    @Override
    public void onPageChange(int task) {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_SUCCESS));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        Toast.makeText(getApplicationContext(),"Login Failed /(ㄒoㄒ)/~~",Toast.LENGTH_LONG).show();
    }
}
