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
import com.example.receipt_app.model.ReceiptMain;
import com.example.receipt_app.view.ReceiptDetail;

import java.util.List;

public class ReceiptListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ReceiptMain> receiptMainList;

    public ReceiptListAdapter(Activity activity, List<ReceiptMain> receiptMainList) {
        this.activity = activity;
        this.receiptMainList = receiptMainList;
    }

    public void setReceiptMainList(List<ReceiptMain> receiptMainList) {
        this.receiptMainList = receiptMainList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return receiptMainList.size();
    }

    @Override
    public Object getItem(int location) {
        return receiptMainList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        TextView receiptName = convertView.findViewById(R.id.name);
        TextView receiptTotal = convertView.findViewById(R.id.price);

        ReceiptMain receipt = receiptMainList.get(position);

        receiptName.setText(receipt.getMerchantName());
        receiptTotal.setText("$".concat(String.valueOf(receipt.getTotal())));

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
                ReceiptMain receipt = receiptMainList.get(position);
                Intent startIntent = new Intent(activity.getApplicationContext(), ReceiptDetail.class);
                startIntent.putExtra("receipt", receipt);
                activity.startActivity(startIntent);
            }
        }
    }

}