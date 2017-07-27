package org.unimelb.itime.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVOSCloud;
import com.google.gson.Gson;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.NotificationBean;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.util.AppUtil;

/**
 * Created by yuhaoliu on 1/12/16.
 * Reconstructed by Qiushuo Huang on 2/11/2017
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String DATA_KEY = "com.avos.avoscloud.Data";
    private Bitmap largeIcon;

    @Override
    public void onReceive(Context context, Intent intent) {

            try {
                String data = intent.getExtras().getString(DATA_KEY);
                NotificationBean bean = new Gson().fromJson(data, NotificationBean.class);
                if(bean==null){
                    Log.e("Message Receiver", "null data");
                    return;
                }
                Notification notification = getMessageNotification(bean);
                int mNotificationId = (int) System.currentTimeMillis();
                NotificationManager mNotifyMgr = (NotificationManager) AVOSCloud.applicationContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public Notification getMessageNotification(NotificationBean bean){
        return getNotificationBuilder(bean, MainActivity.MODE_MESSAGE);
    }

    public Notification getContactNotification(NotificationBean bean){
        return getNotificationBuilder(bean, MainActivity.MODE_CONTACT);
    }

    public Notification getNotificationBuilder(NotificationBean bean, int mode){
        Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
        resultIntent.putExtra(MainActivity.START_MODE, mode);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.addFlags(Intent.FILL_IN_DATA);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(AVOSCloud.applicationContext)
                        .setSmallIcon(R.drawable.ic_launcher_blue)
                        .setContentTitle(AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                        .setContentText(bean.getAlert())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        Notification notification = mBuilder.build();

        boolean isAlert = bean.isAppAlertSound();
        boolean isFore = AppUtil.isForeground(AVOSCloud.applicationContext);

        if(isAlert || !isFore){
            notification.defaults |= Notification.DEFAULT_SOUND;
        }

        if(bean.isSystemVibrate()){
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        return notification;
    }

    public Bitmap getLargeIcon(){
        if(largeIcon==null) {
            ImageView view = new ImageView(AVOSCloud.applicationContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setImageResource(R.drawable.ic_launcher_blue);
            largeIcon = convertViewToBitmap(view);
        }
        return largeIcon;
    }

    private Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
