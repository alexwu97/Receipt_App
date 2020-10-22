package com.example.receipt_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.receipt_app.R;
import com.example.receipt_app.adapters.ReceiptItemListAdapter;
import com.example.receipt_app.model.Receipt;
import com.example.receipt_app.model.ReceiptItem;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetail extends AppCompatActivity {
    private Receipt receipt = new Receipt();
    List<ReceiptItem> receiptItemsList = new ArrayList<>();
    private ReceiptItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        if (getIntent().hasExtra("receipt")) {
            Intent retrieveIntent = getIntent();
            receipt = (Receipt) retrieveIntent.getSerializableExtra("receipt");

        } else {
            Intent goBackToReceiptHistory = new Intent(getApplicationContext(), ReceiptHistory.class);
            startActivity(goBackToReceiptHistory);
        }

        receiptItemsList = receipt.getItems();

        //Set up the actionbar
        Toolbar toolbar = findViewById(R.id.receiptDetailToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receipt.getMerchantName());

        ListView listView = findViewById(R.id.itemList);
        adapter = new ReceiptItemListAdapter(this, receiptItemsList);
        listView.setAdapter(adapter);
    }
}
