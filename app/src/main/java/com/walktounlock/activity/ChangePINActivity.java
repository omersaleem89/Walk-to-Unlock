package com.walktounlock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.walktounlock.R;

public class ChangePINActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        getSupportActionBar().setTitle("Change PIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
