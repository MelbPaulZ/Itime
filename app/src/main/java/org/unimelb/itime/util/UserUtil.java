package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.Gson;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.manager.CalendarManager;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.manager.EventManager;
import org.unimelb.itime.manager.SettingManager;
import org.unimelb.itime.restfulresponse.UserLoginRes;

import java.util.Arrays;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class UserUtil {
    private static UserUtil instance;
    private boolean isLogin;
    private UserLoginRes userLoginRes;
    private Context context;

    private UserUtil(Context context){
        this.context = context.getApplicationContext();
    }

    public static UserUtil getInstance(Context context){
        if(instance == null){
            instance = new UserUtil(context);
            instance.init();
        }
        return instance;
    }

    /**
     * restore all persistent information to instance
     */
    private void init(){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        String userUid = sp.getString(C.spkey.USER_UID, "");
        this.userLoginRes = new UserLoginRes();
        User user = DBManager.getInstance(context).getUser(userUid);
//        Setting setting = DBManager.getInstance(context).getSetting(userUid);

//        if (userUid.equals("") || user==null || setting == null){
        if (userUid.equals("") || user==null){
            isLogin=false;
            return;
        }
        this.userLoginRes.setUser(DBManager.getInstance(context).getUser(userUid));
//        this.userLoginRes.setSetting(DBManager.getInstance(context).getSetting(userUid));
        isLogin=true;
    }

    public User copyUser(){
        Gson gson = new Gson();
        String cpyString= gson.toJson(getUser());
        User cpyUser = gson.fromJson(cpyString, User.class);
        return cpyUser;
    }

    /**
     *
     * @return true if user can be find in DB otherwise return false
     */
    public boolean isLogin() {
        return isLogin;
    }

    public boolean userDataComplete(){
        return this.userLoginRes != null && this.userLoginRes.getUser()!=null && this.userLoginRes.getSetting()!=null;
    }

    /** save the user info into shared preference, which can be used to fast signIn
     * **/
    public void saveLoginUser(UserLoginRes userLoginRes){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(C.spkey.USER_UID, userLoginRes.getUser().getUserUid());
        editor.apply();
//        DBManager.getInstance(context).updateSetting(userLoginRes.getSetting());
        DBManager.getInstance(context).insertOrReplace(Arrays.asList(userLoginRes.getUser()));
        this.userLoginRes = userLoginRes;
        isLogin=true;
    }

    /**
     *
     */
    public void logout(){
        instance = null;
    }

    public static void clean(){
        instance = null;
    }

    public String getUserUid(){
        return instance.getUser().getUserUid();
    }

    public Setting getSetting(){
        Setting setting = userLoginRes.getSetting();
        boolean bool = NotificationManagerCompat.from(context).areNotificationsEnabled();
        setting.setEnableNotification(bool);
        return setting;
    }

    public Setting copySetting(){
        Gson gson = new Gson();
        String cpyString = gson.toJson(getSetting());
        Setting cpySetting = gson.fromJson(cpyString, Setting.class);
        return cpySetting;
    }

    public void setSetting(Setting setting){
        this.userLoginRes.setSetting(setting);
    }

    public UserLoginRes getUserLoginRes() {
        return userLoginRes;
    }


    public User getUser(){
        return userLoginRes.getUser();
    }

    /**
     * not delete DB data
     */
    public void clearAccount(){
//        UserUtil user = UserUtil.getInstance(context);
//        user.logout();

        SettingManager.clear();

        CalendarUtil.getInstance(context).clear();
        CalendarManager.getInstance().clear();
        EventManager.getInstance(context).clear();
        clean();

        AuthUtil.clearJwtToken(context);
        SharedPreferences sp = AppUtil.getTokenSaver(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();

        SharedPreferences userSp = AppUtil.getSharedPreferences(context);
        userSp.edit().remove(C.calendar.LAST_USED_CAL).apply();
    }

    /**
     * delete everything
     */
    public void clearAccountWithDB(){

        SettingManager.clear();

        CalendarManager.getInstance().clear();
        clean();

        AuthUtil.clearJwtToken(context);
        SharedPreferences sp = AppUtil.getTokenSaver(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, "");
        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, "");
        editor.apply();

        SharedPreferences userSp = AppUtil.getSharedPreferences(context);
        userSp.edit().remove(C.calendar.LAST_USED_CAL).apply();

//        DBManager.getInstance(context).deleteAllMessages();
        DBManager.getInstance(context).clearDB();
        EventManager.getInstance(context).clear();
    }
}
