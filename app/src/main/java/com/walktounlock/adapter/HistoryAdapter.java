package com.walktounlock.adapter;

/**
 * Created by umer on 08-Jun-18.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.walktounlock.R;
import com.walktounlock.model.Distance;
import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private List<Distance> historyList = null;
    private Context context;

    public HistoryAdapter(Context context, List<Distance> historyList) {
        this.context = context;
        this.historyList=historyList;
    }

    public int getCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public Distance getItem(int position) {
        return  historyList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.history_list_item, null);
        }
        TextView textViewDate=view.findViewById(R.id.textView_history_date);
        TextView textViewDistance=view.findViewById(R.id.textView_history_distance);
        textViewDate.setText(historyList.get(position).getDate());
        textViewDistance.setText(historyList.get(position).getTotalDistance());
        return view;
    }


}
