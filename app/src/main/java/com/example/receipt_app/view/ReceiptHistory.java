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
import com.example.receipt_app.adapters.ReceiptListAdapter;
import com.example.receipt_app.model.Receipt;
import com.example.receipt_app.model.ReceiptViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptHistory extends AppCompatActivity {

    private ReceiptListAdapter adapter;
    List<Receipt> receipts = new ArrayList<>();
    final Activity receiptHistoryActivity = this;
    ReceiptViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        //Set up the list with adapter
        adapter = new ReceiptListAdapter(receiptHistoryActivity, receipts);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        //Observe when the view model updates to the latest info
        viewModel.getAllReceipts().observe(this, new Observer<List<Receipt>>() {
            @Override
            public void onChanged(@Nullable List<Receipt> receiptList) {
                //Update the receipt history list
                receipts = receiptList;
                adapter.setReceipts(receipts);
                //Calculate all the receipts' costs
                double allReceiptsSum = 0.0;
                for (Receipt i : receipts) {
                    allReceiptsSum += i.getTotal();
                }
                TextView grandTotal = findViewById(R.id.totalText);
                grandTotal.setText("$".concat(String.valueOf(allReceiptsSum)));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                viewModel.deleteAll();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
