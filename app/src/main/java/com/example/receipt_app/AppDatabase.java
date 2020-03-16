package com.example.receipt_app;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ReceiptLogger.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReceiptLoggerDao receiptLoggerDao();
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
}