package com.example.receipt_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LogDisplay extends AppCompatActivity {

    private final String filenameInternal = "receiptLogs";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_display);

        tv= (TextView) findViewById(R.id.tv);

        Button readFileBtn = (Button) findViewById(R.id.readFileBtn);

        readFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFileInternalStorage(v);
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
