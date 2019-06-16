package com.walktounlock.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.walktounlock.service.LockService;

public class ScreenReciever extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            Log.i("applocker17", "Screen went OFF");
            stopLockService(context);
        } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            Log.i("applocker17", "Screen went ON");
            startLockService(context);
        }
    }

    private void startLockService(Context context) {
        context.startService(new Intent(context, LockService.class));
    }

    private void stopLockService(Context context) {
        context.stopService(new Intent(context, LockService.class));
    }
}
