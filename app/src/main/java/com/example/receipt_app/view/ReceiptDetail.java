package com.example.receipt_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.receipt_app.R;
import com.example.receipt_app.adapters.ItemsListAdapter;
import com.example.receipt_app.database.AppDatabase;
import com.example.receipt_app.database.AppExecutors;
import com.example.receipt_app.model.ReceiptItems;
import com.example.receipt_app.model.ReceiptLogger;

import java.util.ArrayList;

public class ReceiptDetail extends AppCompatActivity {
    private AppDatabase db;
    private ReceiptLogger receipt = new ReceiptLogger();
    ArrayList<ReceiptItems> itemsArr = new ArrayList<>();
    private ItemsListAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        Intent retrieveIntent = getIntent();
        receipt = (ReceiptLogger) retrieveIntent.getSerializableExtra("com.example.receipt_app.RECEIPT");
        getReceiptDataToDB();

        try{
            Thread.sleep(100);
        }catch(Exception e){
            e.printStackTrace();
        }

        listView = findViewById(R.id.itemList);
        adapter = new ItemsListAdapter(this, itemsArr);
        listView.setAdapter(adapter);

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
