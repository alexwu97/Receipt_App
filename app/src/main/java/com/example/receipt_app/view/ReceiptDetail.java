package com.example.receipt_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.receipt_app.R;
import com.example.receipt_app.adapters.ReceiptItemListAdapter;
import com.example.receipt_app.model.ReceiptItem;
import com.example.receipt_app.model.ReceiptMain;
import com.example.receipt_app.model.ReceiptViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetail extends AppCompatActivity {
    private ReceiptMain receipt = new ReceiptMain();
    List<ReceiptItem> receiptItemsList = new ArrayList<>();
    private ReceiptItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        if (getIntent().hasExtra("receipt")){
            Intent retrieveIntent = getIntent();
            receipt = (ReceiptMain) retrieveIntent.getSerializableExtra("receipt");
        }else{
            Intent goBackToReceiptHistory = new Intent(getApplicationContext(), ReceiptHistory.class);
            startActivity(goBackToReceiptHistory);
        }

        //Set up the actionbar
        Toolbar toolbar = findViewById(R.id.receiptDetailToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receipt.getMerchantName());

        //Get view model
        ReceiptViewModel viewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        ListView listView = findViewById(R.id.itemList);
        adapter = new ReceiptItemListAdapter(this, receiptItemsList);
        listView.setAdapter(adapter);

        viewModel.getAllReceiptItems().observe(this, new Observer<List<ReceiptItem>>() {
            @Override
            public void onChanged(@Nullable List<ReceiptItem> itemList) {
                receiptItemsList.clear();
                if(itemList != null){
                    for(ReceiptItem item : itemList){
                        if(item.getId() == receipt.getId()){
                            receiptItemsList.add(item);
                        }
                    }
                }

                adapter.setReceiptItemList(receiptItemsList);
            }
        });
    }
}
