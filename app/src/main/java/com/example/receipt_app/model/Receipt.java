package com.example.receipt_app.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.receipt_app.database.Converters;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Receipt")
@TypeConverters(Converters.class)
public class Receipt implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "merchantName")
    private String merchantName;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "items")
    private List<ReceiptItem> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReceiptItem> getItems() {
        return this.items;
    }

    public void setItems(List<ReceiptItem> items) {
        this.items = items;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}