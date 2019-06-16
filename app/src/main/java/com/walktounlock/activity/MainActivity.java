package com.walktounlock.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walktounlock.BuildConfig;
import com.walktounlock.R;
import com.walktounlock.adapter.ApplicationAdapter;
import com.walktounlock.manager.DatabaseHandler;
import com.walktounlock.manager.SessionManager;
import com.walktounlock.model.App;
import com.walktounlock.model.Distance;
import com.walktounlock.model.User;
import com.walktounlock.service.GpsService;
import com.walktounlock.service.LockService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHandler databaseHandler;
    private List<ApplicationInfo> applist = null;
    ListView listApps;
    private ApplicationAdapter listadaptor = null;
    RelativeLayout mainLayout;
    private PackageManager packageManager = null;
    SessionManager sessionManager;


    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private LoadApplications() {
        }

        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadaptor = new ApplicationAdapter(MainActivity.this, R.layout.app_list_item, applist);
            if (!isMyServiceRunning(LockService.class) || !isMyServiceRunning(GpsService.class)) {
                startService();
            }
            return null;
        }

        protected void onCancelled() {
            super.onCancelled();
        }

        protected void onPostExecute(Void result) {
            listApps.setAdapter(listadaptor);
            super.onPostExecute(result);
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary)
        };

        ColorStateList myList = new ColorStateList(states, colors);

        navigationView.setItemTextColor(myList);
        navigationView.setItemIconTintList(myList);

        View headerView = navigationView.getHeaderView(0);
        View v = findViewById(R.id.app_bar_main);
        View mainView = v.findViewById(R.id.content_main);

        TextView name=headerView.findViewById(R.id.textView_header_name);
        TextView email=headerView.findViewById(R.id.textView_header_email);

        databaseHandler = new DatabaseHandler(this);
        sessionManager = new SessionManager(this);

        User user = databaseHandler.getUser(sessionManager.getName());
        name.setText(user.getName());
        email.setText(user.getEmail());

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        databaseHandler.addDistance(new Distance(date, "0.0"));

        mainLayout = (RelativeLayout) mainView.findViewById(R.id.mainLayout);
        packageManager = getPackageManager();
        listApps = (ListView) mainView.findViewById(R.id.listApps);
//        listApps.setItemsCanFocus(true);
        startService();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MainActivity.this.startReqUsageStat();
            }
        }, 3000);
        applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        for (int i = 0; i < applist.size(); i++) {
            ApplicationInfo data = (ApplicationInfo) applist.get(i);
            App app = new App(data.packageName, "false", "0", "0");
            databaseHandler.addApp(app);
        }
        listadaptor = new ApplicationAdapter(this, R.layout.app_list_item, applist);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                new MainActivity.LoadApplications().execute(new Void[0]);
            }
        }, 1500);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_nav_logout) {
            sessionManager.setLogout();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            stopService();
            finish();
        }
        if (id == R.id.menu_nav_history) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }
        if (id== R.id.menu_nav_share){
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Share Walk-to-Unlock App");
            intent.putExtra("android.intent.extra.TEXT", "Try this Awesome App 'Walk-to-Unlock' which helps you in controlling your kids physical activities ..! \nhttps://play.google.com/store/apps/details?id=com.bestwhatsappstatus.saver");
            startActivity(Intent.createChooser(intent, "Share Via"));
            return true;
        }
        if (id==R.id.menu_nav_help){
            View inflate = LayoutInflater.from(this).inflate(R.layout.tut, (ViewGroup) null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(inflate).setTitle("How To Use?").setPositiveButton("Ok!", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
        if (id==R.id.menu_nav_pin){
            startActivity(new Intent(getApplicationContext(),ChangePINActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startService() {
        if (!isMyServiceRunning(LockService.class) || !isMyServiceRunning(GpsService.class)) {
            startService(new Intent(getApplicationContext(), LockService.class));
            startService(new Intent(getApplicationContext(), GpsService.class));
        }
    }

    private void stopService() {
        stopService(new Intent(getApplicationContext(), LockService.class));
        stopService(new Intent(getApplicationContext(), GpsService.class));
    }

    private void startReqUsageStat() {
        if (Build.VERSION.SDK_INT >= 21 && !checkUsageStatsPermission()) {
            startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
        }
    }

    public boolean checkUsageStatsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (((UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE)).queryUsageStats(0, 0, System.currentTimeMillis()).isEmpty()) {
                return false;
            }
        }
        return true;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        for (ActivityManager.RunningServiceInfo service : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        stopService();
        startService();
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList();
        for (ApplicationInfo info : list) {
            try {
                if (!(info.packageName.equals("com.google.android.googlequicksearchbox") || info.packageName.equals(BuildConfig.APPLICATION_ID) || info.packageName.contains("launcher3") || info.packageName.contains("launcher") || info.packageName.contains("trebuchet") || this.packageManager.getLaunchIntentForPackage(info.packageName) == null)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applist;
    }
}
