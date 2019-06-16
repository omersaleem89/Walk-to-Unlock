package com.walktounlock.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
//import com.google.gson.Gson;
import com.walktounlock.manager.AppController;
import com.walktounlock.manager.DatabaseHandler;
import com.walktounlock.R;
import com.walktounlock.manager.SessionManager;
import com.walktounlock.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText editText_name;
    EditText editText_pass;
    TextView mTextViewRegister;
    Button button;
    SessionManager sessionManager;
    ProgressBar progressBar;
    TextView textView;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        preferences = this.getSharedPreferences("register", 0);

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_pass = (EditText) findViewById(R.id.editText_password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textView_login_forgot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        final DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        if (sessionManager.isLoggedin()) {
            startActivity(new Intent(getApplicationContext(), EnterPINActivity.class));
            finish();
        }

        mTextViewRegister = (TextView) findViewById(R.id.textView_register);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassActivity.class));
            }
        });

        button = (Button) findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String temp = editText_name.getText().toString();
                if (editText_name.getText().toString().trim().equals("kanwal") && editText_pass.getText().toString().trim().equals("umer")) {
                    User user = new User();
                    user.setName("kanwal");
                    user.setEmail("kanwal123@gmail.com");
                    user.setPassword("umer");
                    user.setPin("1122");
                    sessionManager.setLogin(true, user.getName());
                    handler.addUser(user);
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    button.setEnabled(true);
                    mTextViewRegister.setEnabled(true);
                }
            }
        });

        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText_name.getText().toString().trim().equals("")) {
                    editText_name.setError("Don't leave emplty");
                    return;
                } else
                    editText_name.setError(null);

            }

        });

        editText_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText_pass.getText().toString().trim().equals("")) {
                    editText_pass.setError("Don't leave empty");
                    return;
                } else
                    editText_pass.setError(null);
            }
        });
    }

}
