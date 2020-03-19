package com.example.receipt_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.receipt_app.adapters.CustomListAdapter;
import com.example.receipt_app.R;
import com.example.receipt_app.database.AppDatabase;
import com.example.receipt_app.database.AppExecutors;
import com.example.receipt_app.model.ReceiptLogger;

import java.util.ArrayList;
import java.util.List;

public class LogDisplay extends AppCompatActivity {

    private AppDatabase db;
    private CustomListAdapter adapter;
    private ListView listView;
    List<ReceiptLogger> receiptArr= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_display);

        getReceiptDataToDB();
        try{
            Thread.sleep(100);
        }catch(Exception e){
            e.printStackTrace();
        }

        listView = findViewById(R.id.list);
        adapter = new CustomListAdapter(this, receiptArr);
        listView.setAdapter(adapter);

        TextView grandTotal = (TextView) findViewById(R.id.totalText);
        double sum = 0.0;
        for (ReceiptLogger i : receiptArr){
            sum += i.getTotal();
        }

        grandTotal.setText("$" + sum);
    }

    private void getReceiptDataToDB(){


        db = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptArr = db.receiptLoggerDao().getAll();
            }
        });
    }
}
