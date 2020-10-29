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
import com.example.receipt_app.model.Receipt;
import com.example.receipt_app.view.ReceiptDetail;

import java.util.List;

public class ReceiptListAdapter extends BaseAdapter {
    private static final String INTENT_RECEIPT_KEY = "receipt";
    private Activity activity;
    private LayoutInflater inflater;
    private List<Receipt> receipts;

    public ReceiptListAdapter(Activity activity, List<Receipt> receipts) {
        this.activity = activity;
        this.receipts = receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int location) {
        return receipts.get(location);
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

        Receipt receipt = receipts.get(position);

        receiptName.setText(receipt.getMerchantName());

        StringBuilder sbTotal = new StringBuilder("$");
        sbTotal.append(receipt.getTotal());
        receiptTotal.setText(sbTotal.toString());

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
                Receipt receipt = receipts.get(position);
                Intent startIntent = new Intent(activity.getApplicationContext(), ReceiptDetail.class);
                startIntent.putExtra(INTENT_RECEIPT_KEY, receipt);
                activity.startActivity(startIntent);
            }
        }
    }

}