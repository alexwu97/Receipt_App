package com.example.receipt_app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Button;

import com.example.receipt_app.R;

public class NotReceiptDialog {
    private Activity activity;
    private AlertDialog dialog;

    public NotReceiptDialog(Activity activity){
        this.activity = activity;
    }

    public void startNotReceiptDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false)
                .setTitle("Invalid Image")
                .setMessage("Data could not be collected from the submitted image.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissDialog();
                        Intent gotoReceiptHistory = new Intent(activity.getApplicationContext(), ReceiptHistory.class);
                        activity.startActivity(gotoReceiptHistory);
                    }
                });

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
