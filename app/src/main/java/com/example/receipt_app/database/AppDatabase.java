package com.example.receipt_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.receipt_app.model.ReceiptItem;
import com.example.receipt_app.model.ReceiptItemDao;
import com.example.receipt_app.model.ReceiptMain;
import com.example.receipt_app.model.ReceiptMainDao;

@Database(entities = {ReceiptMain.class, ReceiptItem.class}, exportSchema = false, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "Receipt_DB";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ReceiptMainDao receiptLoggerDao();
    public abstract ReceiptItemDao receiptItemDao();
}