package com.walktounlock.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.walktounlock.R;
import com.walktounlock.activity.EnterPINActivity;
import com.walktounlock.activity.MainActivity;
import com.walktounlock.manager.DatabaseHandler;
import com.walktounlock.model.App;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

public class LockService extends Service {
    static Context context;
    private static String current_app = "";
    private static String locked_app = "";
    private static Handler mHandler;
    private static int mInterval = 700;
    static String date;
    static Double totalDisance=0.0;
    static Double appDistance=0.0;
    static DatabaseHandler databaseHandler;
    static App app;
    static String TAG="lockservice";
    static UsageStatsManager mUsageStatsManager;
    static List<UsageStats> stats;
    static SortedMap<Long, UsageStats> mySortedMap;
    Runnable mStatusChecker = new Runnable() {

        public void run() {

            try {
                context = LockService.this;
                databaseHandler=new DatabaseHandler(context);
                current_app = getRecentApps(context);
                if(current_app!=null) {
                    if (!current_app.equals(locked_app)) {
                        app = new App(locked_app, "1");
                        databaseHandler.updateAppCheck(app);
                    }
                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    totalDisance = Double.parseDouble(databaseHandler.getDistance(date).getTotalDistance());

                    appDistance = Double.parseDouble(databaseHandler.getApp(current_app).getLimit());
                    if (totalDisance <= appDistance) {
                        if (databaseHandler.checkApp(current_app, "true")) {
                            locked_app = getRecentApps(context);
                            if (databaseHandler.isCheck(current_app, "1")) {
                                startUnlockActivity(getRecentApps(context));
                            }
                        }
                        if (!current_app.equals(locked_app)) {
                            app = new App(locked_app, "1");
                            databaseHandler.updateAppCheck(app);
                        }
                        locked_app = getRecentApps(context);
                    }
                }

            } catch (Exception e) {
                Log.i(TAG,e.getMessage());

            } catch (Throwable th) {
                mHandler.postDelayed(mStatusChecker, (long) mInterval);
            }
            mHandler.postDelayed(mStatusChecker, (long) mInterval);

        }

    };



    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mHandler = new Handler();
        startRepeatingTask();
        Log.i(TAG,"start");
        return Service.START_STICKY;
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public String getRecentApps(Context context) {
        if (VERSION.SDK_INT < 21) {
            return ((RunningTaskInfo) ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0)).topActivity.getPackageName();
        }
        mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        stats = mUsageStatsManager.queryUsageStats(0, time - 10000, time);
        if (stats == null) {
            return null;
        }
        mySortedMap = new TreeMap();
        for (UsageStats usageStats : stats) {
            mySortedMap.put(Long.valueOf(usageStats.getLastTimeUsed()), usageStats);
        }
        if (mySortedMap.isEmpty()) {
            return null;
        }
        return ((UsageStats) mySortedMap.get(mySortedMap.lastKey())).getPackageName();
    }

    public void startUnlockActivity(String packagename) {
        Intent i = new Intent(this, EnterPINActivity.class);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        i.putExtra("app",packagename);
        i.putExtra("key",1);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, EnterPINActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_02";// The id of the channel.
            CharSequence name = "Walk-to-Unlock";// The user-visible name of the channel.
            int importance = 0;
            importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
            notification = new Notification.Builder(this,CHANNEL_ID)
                    .setContentTitle("Walk To Unlock")
                    .setContentText("Measuring Distance...")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
            mNotificationManager.notify(0 , notification);
        }
        else{
            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Walk To Unlock")
                    .setContentText("Measuring Distance...")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent).build();
        }
        startForeground(1337, notification);
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
        Log.i(TAG,"onDestroy");
    }
}
