package com.walktounlock.activity;

import android.Manifest;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager=new SessionManager(this);
        editText_name=(EditText) findViewById(R.id.editText_name);
        editText_pass=(EditText) findViewById(R.id.editText_password);
        progressBar=(ProgressBar) findViewById(R.id.progressbar);
        textView=(TextView) findViewById(R.id.textView_login_forgot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        final DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        if (sessionManager.isLoggedin()) {
            startActivity(new Intent(getApplicationContext(), EnterPINActivity.class));
            finish();
        }

        mTextViewRegister=(TextView) findViewById(R.id.textView_register);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassActivity.class));
            }
        });

        button=(Button) findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String  tag_string_req = "string_req";

                String url = "https://www.zainfabricspk.com/fyp/getLogin.php";
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url+"?username="+editText_name.getText().toString()
                                +"&pass="+editText_pass.getText().toString(), new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Invalid")) {
                            Toast.makeText(LoginActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {
                                JSONObject object = new JSONObject(response);
                                boolean error = object.getBoolean("error");
                                if (!error) {
                                    User user = new User();
                                    JSONArray array = object.getJSONArray("user_data");
                                    user.setName(array.getString(0));
                                    user.setEmail(array.getString(1));
                                    user.setPassword(array.getString(2));
                                    user.setPin(array.getString(3));
                                    sessionManager.setLogin(true, array.getString(0));
                                    handler.addUser(user);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                        mTextViewRegister.setEnabled(true);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error: " , error.getMessage());
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                        mTextViewRegister.setEnabled(true);
                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                strReq.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
                if(editText_name.getText().toString().trim().equals("")){
                    editText_name.setError("Don't leave emplty");
                    return;
                }
                else
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
                if(editText_pass.getText().toString().trim().equals("")){
                    editText_pass.setError("Don't leave empty");
                    return;
                }
                else
                    editText_pass.setError(null);
            }
        });
    }

}
