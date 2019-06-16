package com.walktounlock.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.walktounlock.R;
import com.walktounlock.activity.EnterPINActivity;
import com.walktounlock.activity.MainActivity;
import com.walktounlock.manager.DatabaseHandler;
import com.walktounlock.model.Distance;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GpsService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private static LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    static DatabaseHandler databaseHandler;
    static Location location;
    static String date;
    static DecimalFormat df;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        private Location previousLocation = null;
        private double totalDistance = 0.0;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location newLocation) {
            if (previousLocation != null) {
                location = new Location(newLocation);
                int speed=(int) ((location.getSpeed()*3600)/1000);
                if(speed<=5) {
                    databaseHandler = new DatabaseHandler(getApplicationContext());
                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    totalDistance=Double.valueOf(databaseHandler.getDistance(date).getTotalDistance());
                    totalDistance = totalDistance + ((double) location.distanceTo(previousLocation) / 1000);
                    Toast.makeText(GpsService.this, String.valueOf((double) location.distanceTo(previousLocation) / 1000), Toast.LENGTH_SHORT).show();
                    totalDistance = Double.valueOf(df.format(totalDistance));
                    databaseHandler.updateDistance(new Distance(date, String.valueOf(totalDistance)));
                }
            }
            // Update stored location
            previousLocation = newLocation;
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        initializeLocationManager();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        df = new DecimalFormat("#.##");

        Intent notificationIntent = new Intent(this, EnterPINActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
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

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }


    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
}