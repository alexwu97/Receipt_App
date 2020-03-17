package com.example.receipt_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.receipt_app.R;
import com.example.receipt_app.database.AppDatabase;
import com.example.receipt_app.database.AppExecutors;
import com.example.receipt_app.model.ReceiptLogger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LogDisplay extends AppCompatActivity {

    private final String filenameInternal = "receiptLogs";
    private TextView tv;
    private AppDatabase db;
    ArrayList<ReceiptLogger> receiptArr= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_display);

        getReceiptDataToDB();

        tv= (TextView) findViewById(R.id.tv);

        Button readFileBtn = (Button) findViewById(R.id.readFileBtn);

        readFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReceiptDataToDB();
                for (ReceiptLogger i : receiptArr){
                    tv.append(i.getMerchantName() + "\n");
                }
            }
        });


    }

    private void getReceiptDataToDB(){
        db = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptArr = (ArrayList<ReceiptLogger>) db.receiptLoggerDao().getAll();

            }
        });
    }

    public void readFileInternalStorage(View view) {
        try {
            FileInputStream fileInputStream = openFileInput(filenameInternal);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuffer sb = new StringBuffer();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            tv.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
