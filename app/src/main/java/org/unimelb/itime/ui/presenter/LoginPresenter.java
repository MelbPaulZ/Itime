package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Domain;
import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.others.ItimeSubscriber;
import org.unimelb.itime.restfulapi.PasswordApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginPresenter<V extends TaskBasedMvpView> extends MvpBasePresenter<V> {
    private static final String TAG = "LoginPresenter";

    public static int TASK_LOGIN = 1000;
    public static int TASK_SIGNUP = 1001;
    public static int TASK_SEND_LINK = 1002;
    public static int TASK_VALIDATE = 1003;
    public static int TASK_UPLOAD_PHOTO = 1004;


    private Context context;
    private UserApi userApi;
    private PasswordApi passwordApi;

    public LoginPresenter(Context context) {
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
        passwordApi = HttpUtil.createService(context, PasswordApi.class);
    }

    public Context getContext(){
        return context;
    }

    public void loginByEmail(String email, String password) {

        if (getView() != null) {
            // call retrofit
            getView().onTaskStart(TASK_LOGIN);
        }
        AuthUtil.clearJwtToken(context);
        Observable<HttpResult<UserLoginRes>> observable = userApi.login(email, password);
        ItimeSubscriber<HttpResult<UserLoginRes>> subscriber = new ItimeSubscriber<HttpResult<UserLoginRes>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView() != null){
                    e.printStackTrace();
                    getView().onTaskError(TASK_LOGIN,e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<UserLoginRes> result) {
                if (result.getStatus() != 1){
                    if(getView() != null){
                        getView().onTaskError(TASK_LOGIN, result.getInfo());
                    }
                    return;
                }

                AuthUtil.saveJwtToken(context, result.getData().getToken());
                UserUtil.getInstance(context).saveLoginUser(result.getData());

                if(getView() != null){
                    getView().onTaskSuccess(TASK_LOGIN, result.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    @Deprecated
    public void refreshToken() {
        String authToken = AuthUtil.getJwtToken(context);
        Call<JwtToken> call = userApi.refreshToken(authToken);
        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                JwtToken jwt = response.body();
                AuthUtil.saveJwtToken(context, jwt.getToken());
                Log.d(TAG, "refreshEventManager: onResponse: " + jwt.getToken());
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.d(TAG, "refreshEventManager: onFailure: " + t.getMessage());
            }
        });
    }


    public void sendResetLink(String contact){
        if(getView() != null){
            getView().onTaskStart(TASK_SEND_LINK);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("contact", contact);
        Observable<HttpResult<Void>> observable = passwordApi.sendResetLink(hashMap);
        ItimeSubscriber<HttpResult<Void>> subscriber = new ItimeSubscriber<HttpResult<Void>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_SEND_LINK, null);
                }
            }

            @Override
            public void onNext(HttpResult<Void> ret) {
                if (getView() == null) {
                    return;
                }
                if (ret.getStatus() != 1) {
                    getView().onTaskError(TASK_SEND_LINK, ret.getInfo());
                }else{
                    getView().onTaskSuccess(TASK_SEND_LINK, 0);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void signUp(HashMap<String, Object> params){

        Observable<HttpResult<UserLoginRes>> observable = userApi.signup(params);
        ItimeSubscriber<HttpResult<UserLoginRes>> subscriber = new ItimeSubscriber<HttpResult<UserLoginRes>>() {
            @Override
            public void onHttpError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_SIGNUP, null);
                }
            }

            @Override
            public void onNext(HttpResult<UserLoginRes> ret) {
                if (ret.getStatus() != 1 ){
                    if(getView() != null){
                        getView().onTaskError(TASK_SIGNUP, ret.getInfo());
                    }
                }else{
                    AuthUtil.saveJwtToken(context, ret.getData().getToken());
                    UserUtil.getInstance(context).saveLoginUser(ret.getData());
                    if(getView() != null){
                        getView().onTaskSuccess(TASK_SIGNUP, ret.getData());
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    private void prepareSignup(User loginUser){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", loginUser.getEmail()); // might need to change later
        hashMap.put("password", loginUser.getPassword());
        hashMap.put("email", loginUser.getEmail());
        hashMap.put("personalAlias", loginUser.getPersonalAlias());
        hashMap.put("phone", loginUser.getPhone());
        hashMap.put("photo", loginUser.getPhoto());
        hashMap.put("gender", "2");
        hashMap.put("source", User.SOURCE_EMAIL);
        signUp(hashMap);
    }

    /**
     *
     * @param loginUser
     * @param localPath the photo that user picked for them
     */
    public void uploadImageToLeanCloud(final User loginUser, String localPath){
        if (getView() != null){
            getView().onTaskStart(TASK_UPLOAD_PHOTO);
        }
        String fileName = loginUser.getEmail() + "_" + "avatar.png";
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(fileName, localPath);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e != null){
                        if(getView() != null){
                            getView().onTaskError(TASK_UPLOAD_PHOTO, e.getMessage());
                        }
                        return;
                    }
                    loginUser.setPhoto(file.getUrl());
                    prepareSignup(loginUser);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param loginUser the user to be signed up
     * @param bitmap the default avatar that given to user
     */
    public void uploadImageToLeanCloud(final User loginUser, Bitmap bitmap){
        if (getView() != null){
            getView().onTaskStart(TASK_UPLOAD_PHOTO);
        }
        String fileName = loginUser.getEmail() + "_" + "avatar.png";
        final AVFile file = new AVFile(fileName, convertBitmapToByte(bitmap));
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e != null){
                    if(getView() != null){
                        getView().onTaskError(TASK_UPLOAD_PHOTO, e.getMessage());
                    }
                    return;
                }
                loginUser.setPhoto(file.getUrl());
                prepareSignup(loginUser);
                if(getView() != null){
                    getView().onTaskSuccess(TASK_UPLOAD_PHOTO, file);
                }
            }
        });
    }

    private byte[] convertBitmapToByte(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void validate(HashMap<String, String> params){
        if(getView() != null){
            getView().onTaskStart(TASK_VALIDATE);
        }
        ItimeSubscriber<HttpResult<ValidateRes>> subscriber = new ItimeSubscriber<HttpResult<ValidateRes>>() {
            @Override
            public void onHttpError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_VALIDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<ValidateRes> ret) {
                if (ret.getStatus()==1){
                    if (getView()!=null){
                        getView().onTaskSuccess(TASK_VALIDATE, null);
                    }
                }else{
                    // invalidate
                    if (getView()!=null){
                        getView().onTaskError(TASK_VALIDATE, ret.getData());
                    }
                }
            }
        };
        HttpUtil.subscribe(userApi.validate(params), subscriber);
    }

    public List<String> getEmailDomains(){
        List<Domain> list = DBManager.getInstance(context).getAllDomains();
        List<String> result = new ArrayList<>();
        if(list!=null){
            for(Domain domain:list){
                result.add(domain.getRule());
            }
        }
        return result;
    }

}
