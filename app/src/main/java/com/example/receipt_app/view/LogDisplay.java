package com.example.receipt_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.receipt_app.CustomListAdapter;
import com.example.receipt_app.R;
import com.example.receipt_app.database.AppDatabase;
import com.example.receipt_app.database.AppExecutors;
import com.example.receipt_app.model.ReceiptLogger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogDisplay extends AppCompatActivity {

    private final String filenameInternal = "receiptLogs";
    private AppDatabase db;
    private CustomListAdapter adapter;
    private ListView listView;
    List<ReceiptLogger> receiptArr= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_display);

        getReceiptDataToDB();
        listView = findViewById(R.id.list);
        adapter = new CustomListAdapter(this, receiptArr);
        listView.setAdapter(adapter);

        //getReceiptDataToDB();

        //Button readFileBtn = (Button) findViewById(R.id.readFileBtn);

        /*readFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


    }

    private void getReceiptDataToDB(){
        db = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptArr = (ArrayList<ReceiptLogger>) db.receiptLoggerDao().getAll();
                System.out.println(receiptArr);
                // notifying list adapter about data changes
                // so that it renders the list view with updated data
                adapter.notifyDataSetChanged();
            }
        });
    }

    /*public void readFileInternalStorage(View view) {
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
    }*/
}
