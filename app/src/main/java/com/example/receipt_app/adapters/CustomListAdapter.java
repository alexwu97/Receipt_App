package com.example.receipt_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.receipt_app.R;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.view.ReceiptDetail;

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

        TextView receiptName = (TextView) convertView.findViewById(R.id.name);
        TextView receiptTotal = (TextView) convertView.findViewById(R.id.price);

        ReceiptLogger logger = loggers.get(position);

        receiptName.setText(logger.getMerchantName());


        receiptTotal.setText("$" + logger.getTotal());

        convertView.setOnClickListener(new imageViewClickListener(position));


        return convertView;
    }

    class imageViewClickListener implements View.OnClickListener {
        int position;

        public imageViewClickListener(int pos) {
            this.position = pos;
        }

        public void onClick(View v) {
            {
                ReceiptLogger logger = loggers.get(position);
                Intent startIntent = new Intent(activity.getApplicationContext(), ReceiptDetail.class);
                startIntent.putExtra("com.example.receipt_app.RECEIPT", logger);
                activity.startActivity(startIntent);

            }
        }
    }

}