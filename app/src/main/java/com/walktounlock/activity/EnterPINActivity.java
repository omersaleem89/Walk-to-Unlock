package com.walktounlock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.walktounlock.model.App;
import com.walktounlock.manager.DatabaseHandler;
import com.walktounlock.manager.LockManager;
import com.walktounlock.R;
import com.walktounlock.manager.SessionManager;
import com.walktounlock.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EnterPINActivity extends AppCompatActivity {
    EditText editTextPin;
    String pin="";
    int key=0;
    String packagename="";
    Button button;
    ImageView imageView;
    DatabaseHandler databaseHandler;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        TextView textViewAppDistance,textViewTotalDistance,textViewAppName;
        textViewAppDistance=(TextView) findViewById(R.id.textView_app_distance);
        textViewTotalDistance=(TextView) findViewById(R.id.textView_total_istance);
        textViewAppName=(TextView) findViewById(R.id.textView_name);
        imageView=(ImageView) findViewById(R.id.imageView_icon);
        databaseHandler=new DatabaseHandler(this);
//        manager=new LockManager(this);
        preferences = getSharedPreferences("chosen_apps", 0);
        editor = preferences.edit();

        sessionManager=new SessionManager(this);

        if (!sessionManager.isLoggedin()) {
            startActivity(new Intent(EnterPINActivity.this, LoginActivity.class));
            finish();
        }

        final Intent i=getIntent();
        if(i!=null){
            packagename=i.getStringExtra("app");
            key=i.getIntExtra("key",0);
            textViewAppDistance.setText("Distance Required: "+databaseHandler.getApp(packagename).getLimit()+" km");
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String d=databaseHandler.getDistance(date).getTotalDistance();
            if (!d.equals(null)) {
                textViewTotalDistance.setText("Distance Covered: " +d+" km");
            }
            else{
                textViewTotalDistance.setText("Distance Covered: 0.0 km");
            }
            textViewAppName.setText(getAppName(packagename));
            imageView.setImageDrawable(getAppIcon(packagename));
        }
        if(key!=1){
            textViewAppName.setText("Walk to Unlock");
            textViewAppDistance.setVisibility(View.GONE);
            imageView.setImageResource(R.mipmap.ic_launcher);

        }

        user=databaseHandler.getUser("kanwal");
        editTextPin=(EditText) findViewById(R.id.editText_pin);
        button=(Button) findViewById(R.id.button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pin=editTextPin.getText().toString();
                String pintemp = user.getPin();
                if(user.getPin().equals(pin)){
                    if(key==1) {
                        startActivity(getPackageManager().getLaunchIntentForPackage(packagename));
                        App app=new App(packagename,"0");
                        databaseHandler.updateAppCheck(app);
                        ExitActivity.exitApplication(getApplicationContext());
                    }else {
                        startActivity(new Intent(EnterPINActivity.this, MainActivity.class)
                        .putExtra("user",user));
                        finish();
                    }
                }
            }
        });
    }
    private Drawable getAppIcon(String packagename) {
        Drawable icon = null;
        try {
            icon = getPackageManager().getApplicationIcon(packagename);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }
    private String getAppName(String packagename) {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        String appName = null;
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packagename, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return appName;
        }
    }
}