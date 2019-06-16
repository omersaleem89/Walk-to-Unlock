package com.walktounlock.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.ApplicationInfo.DisplayNameComparator;
import android.content.pm.PackageManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.walktounlock.R;
import com.walktounlock.manager.DatabaseHandler;
import com.walktounlock.model.App;
import com.walktounlock.service.LockService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {
    List<String> allAppList = null;
    private List<ApplicationInfo> appsList = null;
    private Context context;
    DatabaseHandler databaseHandler;
    Editor editor;
    List<String> lockedAppList = null;
    private PackageManager packageManager;
    SharedPreferences preferences;

    public ApplicationAdapter(Context context, int textViewResourceId, List<ApplicationInfo> appList) {
        super(context, textViewResourceId, appList);
        this.context = context;
        allAppList = new ArrayList();
        lockedAppList = new ArrayList();
        appsList = appList;
        packageManager = context.getPackageManager();
        preferences = context.getSharedPreferences("chosen_apps", 0);
        Collections.sort(appsList, new DisplayNameComparator(packageManager));
        editor = preferences.edit();
        databaseHandler=new DatabaseHandler(context);
    }

    public int getCount() {
        return appsList != null ? appsList.size() : 0;
    }

    public ApplicationInfo getItem(int position) {
        return appsList != null ? (ApplicationInfo) appsList.get(position) : null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_list_item, null);
        }
        final ApplicationInfo data = (ApplicationInfo) appsList.get(position);
        if (data != null) {
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
            final EditText editText=(EditText) view.findViewById(R.id.editText_distance);
            final SwitchCompat lockApp = (SwitchCompat) view.findViewById(R.id.lockApp);
            lockApp.setText(data.loadLabel(packageManager));
            iconview.setImageDrawable(data.loadIcon(packageManager));
            editText.setText(databaseHandler.getApp(data.packageName).getLimit());
            if (databaseHandler.checkApp(data.packageName,"true")) {
                lockApp.setChecked(true);
                editText.setEnabled(false);
            } else {
                lockApp.setChecked(false);
                editText.setEnabled(true);
            }

            lockApp.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    stopLockService();
                    ApplicationInfo data = (ApplicationInfo) appsList.get(position);
                    if (lockApp.isChecked()) {
                        editor.putBoolean(data.packageName, true).apply();
                        String s=editText.getText().toString();
                        App app=new App(data.packageName,"true",s,"1");
                        databaseHandler.updateApp(app);
                        editText.setEnabled(false);
                    }
                    if (!lockApp.isChecked()) {
                        editor.putBoolean(data.packageName, false).apply();
                        App app=new App(data.packageName,"false","0","0");
                        databaseHandler.updateApp(app);
                        editText.setEnabled(true);
                    }
                    startLockService();
                }
            });
        }
        return view;
    }

    private void startLockService() {
        context.startService(new Intent(this.context, LockService.class));
    }

    private void stopLockService() {
        context.stopService(new Intent(this.context, LockService.class));
    }
}
