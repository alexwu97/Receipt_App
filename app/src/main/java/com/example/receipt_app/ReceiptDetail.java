package com.example.receipt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.receipt_app.model.ReceiptLogger;

public class ReceiptDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        Intent i = getIntent();
        ReceiptLogger receipt = (ReceiptLogger) i.getSerializableExtra("com.example.receipt_app.RECEIPT");

        TextView totalView = (TextView) findViewById(R.id.totalView);
        TextView nameView = (TextView) findViewById(R.id.nameView);

        totalView.setText("$" + receipt.getTotal());
        nameView.setText(receipt.getMerchantName());

    }
}
