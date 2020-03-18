package com.example.receipt_app;

public class Item {
    private int receiptID;
    private String itemName;
    private double price;

    public Item(String itemName, double price){
        this.itemName = itemName;
        this.price = price;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
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
}
