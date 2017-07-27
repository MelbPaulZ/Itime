package org.unimelb.itime.util;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.unimelb.itime.base.C;

import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by yinchuandong on 1/07/16.
 */
public class AppUtil {

    private static ProgressDialog pgBar;

//    public static SharedPreferences getSharedPreferences(Context ctx){
//        return ctx.getSharedPreferences(C.sp.DEFAULT, Context.MODE_PRIVATE);
//    }

    /**
     * Added by Qiushuo Huang
     * @return
     */
    public static String getCurrentTimeZone()
    {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(false,true,tz.getRawOffset());
    }
    private static String createGmtOffsetString(boolean includeGmt,
                                                boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }


    public static SharedPreferences getSharedPreferences(Context ctx){
        return ctx.getSharedPreferences(C.sp.DEFAULT, Context.MODE_PRIVATE);
    }

    /**
     * all tokens are saved in this shared preference
     * @param ctx
     * @return
     */
    public static SharedPreferences getTokenSaver(Context ctx){
        return ctx.getSharedPreferences(C.sp.TOKEN, Context.MODE_PRIVATE);
    }


    /**
     * to generate a uuid (random uuid of any)
     * @return
     */
    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }

    @Deprecated
    public static void showProgressBar(Context context, String title, String subtitle){
        if (pgBar==null || !pgBar.isShowing()){
            pgBar = ProgressDialog.show(context, title, subtitle);
        }
    }

    @Deprecated
    public static void hideProgressBar(){
        if(pgBar != null){
            pgBar.dismiss();
        }
    }

//    /**
//     * read event sync token into
//     * @param context
//     * @return
//     */
//    public static String getEventSyncToken(Context context){
//        String token = TokenUtil.getInstance(context).getEventToken(UserUtil.getInstance(context).getUserUid());
//        return AppUtil.getTokenSaver(context).getString(C.spkey.EVENT_LIST_SYNC_TOKEN,"");
//    }

//    /**
//     * save the event sync token into sp
//     * @param context
//     * @param syncToken
//     */
//    public static void saveEventSyncToken(Context context, String syncToken){
//        TokenUtil.getInstance(context).setEventToken(UserUtil.getInstance(context).getUserUid(), syncToken);
////        SharedPreferences sp = AppUtil.getTokenSaver(context);
////        SharedPreferences.Editor editor = sp.edit();
////        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, syncToken);
////        editor.apply();
//    }

    public static void stopRemoteService(Context context){
//        Intent serviceI = new Intent(context, RemoteService.class);
//        context.stopService(serviceI);
    }

    public static String[] getAllAlertStr(Context context){
        return new String[]{
//                context.getString(R.string.none),
//                context.getString(R.string.at_the_time_of_the_event),
//                context.getString(R.string.five_mintues_before),
//                context.getString(R.string.fifteen_minutes_before),
//                context.getString(R.string.thirty_minutes_before),
//                context.getString(R.string.one_hour_before),
//                context.getString(R.string.two_hours_before),
//                context.getString(R.string.one_day_before),
//                context.getString(R.string.two_days_before),
//                context.getString(R.string.one_week_before)
        };
    }

    public static String getDefaultAlertStr(int key){
        switch (key){
            case -1:
                return "None";
            case 0:
                return "At the time of the event";
            case 5:
                return "5 minutes before";
            case 15:
                return "15 minutes before";
            case 30:
                return "30 minutes before";
            case 60:
                return "1 hour before";
            case 120:
                return "2 hours before";
            case 24*60:
                return "1 day before";
            case 2*24*60:
                return "2 days before";
            case 7*24*60:
                return "1 week before";
            default:
                return "N/A";

        }
    }

    public static int[] getDefaultAlertMins(){
        return new int[]{-1,0,5,15,30,60,120,24*60,2*24*60,7*24*60};
    }

    public static String getGenderStr(String gCode){
        switch (gCode){
            case "0":
                return "Female";
            case "1":
                return "Male";
            case "2":
                return "Undefined";
            default:
                return "N/A";
        }
    }

    /**
     * Added by Qiushuo Huang
     * @param context
     * @return
     */
    public static boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * this method is alternatively getting deviceUid without permission check
     * @return
     */
    public static String getUidForDevice(){
        String id = "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits
        return id;
    }
}
