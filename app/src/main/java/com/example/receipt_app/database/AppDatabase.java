package com.example.receipt_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.receipt_app.model.ReceiptItems;
import com.example.receipt_app.model.ReceiptItemsDao;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.model.ReceiptLoggerDao;

@Database(entities = {ReceiptLogger.class, ReceiptItems.class}, exportSchema = false, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "ReceiptLogger_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ReceiptLoggerDao receiptLoggerDao();
    public abstract ReceiptItemsDao receiptItemsDao();
}