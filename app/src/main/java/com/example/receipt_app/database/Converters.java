package com.example.receipt_app.database;

import androidx.room.TypeConverter;

import com.example.receipt_app.model.ReceiptItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<ReceiptItem> stringToReceiptItems(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReceiptItem>>() {
        }.getType();
        List<ReceiptItem> items = gson.fromJson(json, type);
        return items;
    }

    @TypeConverter
    public static String receiptItemsToString(List<ReceiptItem> items) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReceiptItem>>() {
        }.getType();
        String json = gson.toJson(items, type);
        return json;
    }
}
