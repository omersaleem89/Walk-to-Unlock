package com.walktounlock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.walktounlock.R;

public class ForgotPassActivity extends AppCompatActivity {
    EditText editText;
    Button button;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        editText=(EditText)findViewById(R.id.editText_forgot_username);
        button=(Button) findViewById(R.id.button_forgot_submit);

    }
}
