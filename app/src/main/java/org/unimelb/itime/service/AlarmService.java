package org.unimelb.itime.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.unimelb.itime.receiver.AlarmReceiver;
import org.unimelb.itime.util.AlarmUtil;

/**
 * Created by yuhaoliu on 5/03/2017.
 */

public class AlarmService extends Service {

    private AlarmManager alarmManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(sender);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmUtil.synchronizeSysAlarm(getApplicationContext());

        return START_STICKY;
    }
}

