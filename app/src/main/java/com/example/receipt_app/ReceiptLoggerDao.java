package com.example.receipt_app;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptLoggerDao {
    @Query("SELECT * from receipt")
    List<ReceiptLogger> getAll();

    @Insert
    void insert(ReceiptLogger receipt);

    @Update
    void update(ReceiptLogger receipt);

    @Delete
    void delete(ReceiptLogger receipt);

}
