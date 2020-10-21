package com.example.receipt_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.receipt_app.R;
import com.example.receipt_app.model.ReceiptItems;

import java.util.List;

public class ItemsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ReceiptItems> items;

    public ItemsListAdapter(Activity activity, List<ReceiptItems> items) {
        this.activity = activity;
        this.items = items;
    }

    public void setItems(List<ReceiptItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
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

        TextView itemName = convertView.findViewById(R.id.name);
        TextView itemPrice = convertView.findViewById(R.id.price);

        ReceiptItems item = items.get(position);

        String quantityAndName = item.getQuantity() + "x " + item.getItemName();
        itemName.setText(quantityAndName);

        itemPrice.setText("$" + item.getPrice());

        return convertView;
    }

}