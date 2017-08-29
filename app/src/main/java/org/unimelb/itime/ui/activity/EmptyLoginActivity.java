package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.mvpview.login.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.util.UserUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import david.itimecalendar.calendar.ui.monthview.MonthView;

/**
 * Created by yuhaoliu on 27/7/17.
 */

public class EmptyLoginActivity extends ItimeBaseActivity<LoginMvpView,LoginPresenter<LoginMvpView>> implements LoginMvpView
{

    private static final String TAG = "EmptyLoginActivity";

    private String username = "liuyuhao2test@gmail.com";
    private String password = "123456";
    private EditText usernameET;
    private EditText pswET;
    private Button loginBtn;
    private Button loginMainBtn;
    private Button loginTestBtn;
    private Spinner spinner;

    private Map<String, String[]> accountMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidget();
    }

    private void initWidget(){
        List<String> admins = new ArrayList<>();

        String david = "David";
        String paul = "Paul";
        String qiu = "Qiu Shuo";

        admins.add(david);
        accountMap.put(david, new String[]{"liuyuhao@gmail.com", "liuyuhao2test@gmail.com"});
        admins.add(paul);
        accountMap.put(paul, new String[]{"zhaopu@gmail.com", "zhaopu2test@gmail.com"});
        admins.add(qiu);
        accountMap.put(qiu, new String[]{"qiushuoh@student.unimelb.edu.au", "huangqiushuo2test@gmail.com"});


        usernameET = (EditText) findViewById(R.id.et_username);
        pswET = (EditText) findViewById(R.id.et_psw);
        loginBtn = (Button) findViewById(R.id.bt_login);
        loginMainBtn = (Button) findViewById(R.id.bt_login_main);
        loginTestBtn = (Button) findViewById(R.id.bt_login_test);

        spinner = (Spinner) findViewById(R.id.login_spinner);
        spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,admins));

        loginBtn.setOnClickListener(v -> {
            String inputUsername = usernameET.getText().toString();
            String inputPsw = pswET.getText().toString();
            username = inputUsername.isEmpty() ? username:inputUsername;
            password = inputUsername.isEmpty() ? password:inputPsw;
            presenter.loginByEmail(username,password);
        });

        loginMainBtn.setOnClickListener(v -> {
            username = accountMap.get(spinner.getSelectedItem())[0];
            presenter.loginByEmail(username,password);
        });

        loginTestBtn.setOnClickListener(v -> {
            username = accountMap.get(spinner.getSelectedItem())[1];
            presenter.loginByEmail(username,password);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserUtil.getInstance(getApplicationContext()).clearAccountWithDB();
        // start remote service
        Intent intent = new Intent(this,RemoteService.class);
        startService(intent);

//        getPresenter().loginByEmail(username,password);
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
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        Toast.makeText(getApplicationContext(),"Login as: " + username ,Toast.LENGTH_LONG).show();

        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_SUCCESS));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        Toast.makeText(getApplicationContext(),"Login Failed /(ㄒoㄒ)/~~",Toast.LENGTH_LONG).show();
    }
}
