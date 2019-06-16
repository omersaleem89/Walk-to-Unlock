package com.walktounlock.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.walktounlock.service.GpsService;
import com.walktounlock.service.LockService;

public class BootReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, LockService.class));
        context.startService(new Intent(context, GpsService.class));
    }
}
