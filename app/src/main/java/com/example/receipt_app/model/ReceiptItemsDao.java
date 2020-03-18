package com.example.receipt_app.model;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptItemsDao {
    @Query("SELECT * from items where receiptNumber = receiptNo")
    List<ReceiptItems> getAll(int receiptNo);

    @Query("DELETE FROM items")
    void deleteAll();

    @Insert
    void insert(ReceiptItems receiptItems);

    @Update
    void update(ReceiptItems receiptItems);

    @Delete()
    void delete(ReceiptItems receiptItems);

}
