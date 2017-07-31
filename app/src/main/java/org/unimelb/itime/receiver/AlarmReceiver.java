package org.unimelb.itime.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;


import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.manager.DBManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.util.AlarmUtil;
import org.unimelb.itime.util.EventUtil;

/**
 * Created by yuhaoliu on 4/03/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public final static String TAG = "Receiver";

    public static final int ERROR = -1;
    public static final int NORMAL = 0;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.showNotification(intent);
        AlarmUtil.synchronizeSysAlarm(context);
    }

    /**
     * 通知栏提醒
     */
    private void showNotification(Intent intent) {
        Bundle extras = intent.getExtras();
        String uid = extras.getString(AlarmUtil.EVENT_UID, null);
        long startTime = extras.getLong(AlarmUtil.START_TIME, -1);

        /**
         * Event bundle error, may cause by synchronization error bet server and local db.
         */
        if (uid == null || startTime == -1){
            showMsg(context.getResources().getString(R.string.alarm_error_msg_bundle_error),ERROR);
            return;
        }
        /**
         * find event from local db.
         */
        Event event = DBManager.getInstance(context).getEvent(uid);

        /**
         * Event cannot be found, show error text msg.
         */
        if (event == null){
            showMsg(context.getResources().getString(R.string.alarm_error_msg_event_error),ERROR);
            return;
        }

        this.createNotification(event, startTime);
    }

    private void createNotification(Event event, long startTime){
        long duration = event.getDuration();
        event.setStartTime(startTime);
        event.setEndTime(startTime + duration);

        String startTimeStr = EventUtil.getSuggestTimeStringFromLong(context,event.getStartTime(),event.getEndTime());
        // 定义通知栏展现的内容信息
        int icon = R.drawable.ic_launcher_blue;// 提示图片
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setSmallIcon(icon)
                .setContentTitle(event.getSummary())
                .setContentText(startTimeStr)
                .setWhen(System.currentTimeMillis());

        // 点击消息后跳转页面
        Intent notificationIntent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        notificationIntent.putExtras(bundle);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        int uniCode = (int) (System.currentTimeMillis() & 0xfffffff);
        mNotificationManager.notify(uniCode, notification);
    }

    private void showMsg(String msg, int type){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);

        switch (type){
            case NORMAL:{
                v.setTextColor(Color.WHITE);
            }
            case ERROR:{
                v.setTextColor(Color.RED);
            }
            default:{
                v.setTextColor(Color.BLACK);
            }
        }
        toast.show();
    }
}
