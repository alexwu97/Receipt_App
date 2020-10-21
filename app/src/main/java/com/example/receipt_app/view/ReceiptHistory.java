package com.example.receipt_app.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.receipt_app.R;
import com.example.receipt_app.adapters.CustomListAdapter;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.model.ReceiptViewModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReceiptHistory extends AppCompatActivity {

    private CustomListAdapter adapter;
    private ListView listView;
    List<ReceiptLogger> receiptArr= new ArrayList<>();
    final Activity logDisplayActivity = this;
    ReceiptViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_history);

        //Set up the actionbar
        Toolbar toolbar = findViewById(R.id.logDisplayToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Receipt History");
        toolbar.showOverflowMenu();

        //Get view model
        model = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        //Set up the list with adapter
        listView = findViewById(R.id.list);
        adapter = new CustomListAdapter(logDisplayActivity, receiptArr);
        listView.setAdapter(adapter);

        //observer on when the view model updates to the latest info
        model.getAllReceipts().observe(this, new Observer<List<ReceiptLogger>>() {
            @Override
            public void onChanged(@Nullable List<ReceiptLogger> receiptLoggerList) {

                receiptArr = receiptLoggerList;
                adapter.setLoggers(receiptLoggerList);

                double sum = 0.0;
                for (ReceiptLogger i : receiptArr) {
                    sum += i.getTotal();
                }
                TextView grandTotal = findViewById(R.id.totalText);
                grandTotal.setText("$" + sum);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                model.deleteAll();
                break;
            } default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
