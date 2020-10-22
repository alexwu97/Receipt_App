package com.example.receipt_app.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptItemDao {
    @Query("SELECT * from ReceiptItem where receiptNumber = :receiptNo")
    List<ReceiptItem> getReceiptItems(int receiptNo);

    @Query("SELECT * from ReceiptItem")
    LiveData<List<ReceiptItem>> getAllReceiptItems();

    @Query("DELETE FROM ReceiptItem")
    void deleteAll();

    @Insert
    void insert(ReceiptItem receiptItem);

    @Update
    void update(ReceiptItem receiptItem);

    @Delete()
    void delete(ReceiptItem receiptItem);

}
