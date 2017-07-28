package org.unimelb.itime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.unimelb.itime.service.AlarmService;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class BootReceiver extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT.equals(intent.getAction())){
            Toast.makeText(context, "BootReceiver worked.", Toast.LENGTH_SHORT).show();
            // start alarm service when device is launched
            Intent alarmService = new Intent(context, AlarmService.class);
            context.startService(alarmService);
        }
    }
}
