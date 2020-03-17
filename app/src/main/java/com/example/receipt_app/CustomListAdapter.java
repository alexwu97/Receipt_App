package com.example.receipt_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.receipt_app.model.ReceiptLogger;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ReceiptLogger> loggers;

    public CustomListAdapter(Activity activity, List<ReceiptLogger> loggers) {
        this.activity = activity;
        this.loggers = loggers;
    }

    @Override
    public int getCount() {
        return loggers.size();
    }

    @Override
    public Object getItem(int location) {
        return loggers.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        ReceiptLogger logger = loggers.get(position);

        // title
        title.setText("title");

        // rating
        rating.setText("rating");

        // release year
        year.setText("year");

        return convertView;
    }

}