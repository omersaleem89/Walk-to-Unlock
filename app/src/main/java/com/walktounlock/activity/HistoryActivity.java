package com.walktounlock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.walktounlock.R;
import com.walktounlock.adapter.HistoryAdapter;
import com.walktounlock.manager.DatabaseHandler;

public class HistoryActivity extends AppCompatActivity {
    ListView listView;
    HistoryAdapter adapter;
    DatabaseHandler databaseHandler;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView=(ListView) findViewById(R.id.listView_history);
        databaseHandler=new DatabaseHandler(this);

        adapter=new HistoryAdapter(getApplicationContext(),databaseHandler.getAllDistance());
        listView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
