package com.example.receipt_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.receipt_app.R;
import com.example.receipt_app.model.ReceiptItem;

import java.util.List;

public class ReceiptItemListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ReceiptItem> receiptItemList;

    public ReceiptItemListAdapter(Activity activity, List<ReceiptItem> receiptItemList) {
        this.activity = activity;
        this.receiptItemList = receiptItemList;
    }

    @Override
    public int getCount() {
        return receiptItemList.size();
    }

    @Override
    public Object getItem(int location) {
        return receiptItemList.get(location);
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

        TextView itemName = convertView.findViewById(R.id.name);
        TextView itemPrice = convertView.findViewById(R.id.price);

        ReceiptItem receiptItem = receiptItemList.get(position);

        StringBuilder sbQuantityAndName = new StringBuilder(receiptItem.getQuantity());
        sbQuantityAndName.append("x ").append(receiptItem.getItemName());
        String quantityAndName = sbQuantityAndName.toString();
        itemName.setText(quantityAndName);

        StringBuilder sbPrice = new StringBuilder("$");
        sbPrice.append(receiptItem.getPrice());
        itemPrice.setText(sbPrice.toString());

        return convertView;
    }

}