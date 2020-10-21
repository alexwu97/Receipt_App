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
import com.example.receipt_app.adapters.ItemsListAdapter;
import com.example.receipt_app.database.AppDatabase;
import com.example.receipt_app.database.AppExecutors;
import com.example.receipt_app.model.ReceiptItems;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.model.ReceiptViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptDetail extends AppCompatActivity {
    private AppDatabase db;
    private ReceiptLogger receipt = new ReceiptLogger();
    List<ReceiptItems> itemsArr = new ArrayList<>();
    private ItemsListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        Intent retrieveIntent = getIntent();
        receipt = (ReceiptLogger) retrieveIntent.getSerializableExtra("com.example.receipt_app.RECEIPT");

        //Set up the actionbar
        Toolbar toolbar = findViewById(R.id.receiptDetailToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receipt.getMerchantName());

        //Get view model
        ReceiptViewModel model = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        listView = findViewById(R.id.itemList);
        adapter = new ItemsListAdapter(this, itemsArr);
        listView.setAdapter(adapter);

        model.getAllReceiptItems().observe(this, new Observer<List<ReceiptItems>>() {
            @Override
            public void onChanged(@Nullable List<ReceiptItems> receiptItemList) {
                itemsArr.clear();
                for(ReceiptItems item : receiptItemList){
                    if(item.getId() == receipt.getId()){
                        itemsArr.add(item);
                    }
                }
                adapter.setItems(itemsArr);
            }
        });

    }

    private void getReceiptDataToDB(){
        db = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                itemsArr = (ArrayList<ReceiptItems>) db.receiptItemsDao().getReceiptItems(receipt.getId());
            }
        });
    }
}
