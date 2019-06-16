package com.walktounlock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
//import com.google.gson.Gson;
import com.walktounlock.R;
import com.walktounlock.manager.AppController;
import com.walktounlock.manager.SessionManager;
import com.walktounlock.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText editText_email;
    EditText editText_name;
    EditText editText_pass;
    EditText editText_pin;
    Button button;
    ProgressBar progressBar;
    SessionManager sessionManager;
    SharedPreferences preferences;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register User");
        sessionManager=new SessionManager(this);
        preferences = this.getSharedPreferences("register",0);

        editText_email=(EditText) findViewById(R.id.editText_reg_email);
        editText_name=(EditText) findViewById(R.id.editText_reg_name);
        editText_pass=(EditText) findViewById(R.id.editText_reg_pass);
        editText_pin =(EditText) findViewById(R.id.editText_pin);
        progressBar=(ProgressBar) findViewById(R.id.progressbar_register);
        button=(Button) findViewById(R.id.button_register);


        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                User user = new User();
                user.setName(editText_name.getText().toString());
                user.setEmail(editText_email.getText().toString());
                user.setPassword(editText_pass.getText().toString());
                user.setPin(editText_pin.getText().toString());
                sessionManager.setLogin(true, "register");

                SharedPreferences.Editor prefsEditor = preferences.edit();

                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                finish();

//                Gson gson = new Gson();
//                String json = gson.toJson(user);
//                prefsEditor.putString(user.getName(), json);
//                prefsEditor.commit();

//                String  tag_string_req = "string_req";
//
//                String url = "https://www.zainfabricspk.com/fyp/reg.php";
//                StringRequest strReq = new StringRequest(Request.Method.GET,
//                        url+"?username="+editText_name.getText().toString()
//                                +"&email="+editText_email.getText().toString()
//                                +"&pass="+editText_pass.getText().toString()
//                                +"&pin="+editText_pin.getText().toString(), new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                            if(response.equals("1")){
//                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                                finish();
//                            }
//                        progressBar.setVisibility(View.GONE);
//
//
//                        Log.d("Login response: ", response.toString());
////                        pDialog.hide();
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d("Error: " , error.getMessage());
//                        progressBar.setVisibility(View.GONE);
////                        pDialog.hide();
//                    }
//                });
//
//// Adding request to request queue
//                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
                    button.setEnabled(false);
                    return;
                }
                else
                    editText_pass.setError(null);
                button.setEnabled(true);

            }

        });

        editText_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText_pin.getText().toString().trim().equals("")){
                    editText_pin.setError("Don't leave empty");
                    button.setEnabled(false);
                    return;
                }
                else
                    editText_pin.setError(null);
                button.setEnabled(true);
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
                    button.setEnabled(false);
                    return;
                }
                else
                    editText_name.setError(null);
                button.setEnabled(true);
            }

        });


        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isValidEmail(editText_email.getText().toString())){
                    editText_email.setError("Enter a valid address");
                    button.setEnabled(false);
                    return;
                }
                else
                    editText_email.setError(null);
                button.setEnabled(true);

            }
        });






    }
    public final boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}