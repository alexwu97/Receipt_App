package com.example.receipt_app.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"receiptNumber", "itemNumber"}, tableName = "items",
        foreignKeys = @ForeignKey(entity = ReceiptLogger.class,
        parentColumns = "id",
        childColumns = "receiptNumber",
        onDelete = ForeignKey.CASCADE)
)
public class ReceiptItems {
    @ColumnInfo(name = "receiptNumber")
    private int id;

    @ColumnInfo(name = "itemNumber")
    private int itemNo;

    @ColumnInfo(name = "itemName")
    private String itemName;

    @ColumnInfo(name = "Price")
    private double price;

    @ColumnInfo(name = "Quantity")
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemNo(){
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}