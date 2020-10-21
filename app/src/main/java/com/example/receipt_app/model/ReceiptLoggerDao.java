package com.example.receipt_app.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptLoggerDao {
    @Query("SELECT * from receipt")
    LiveData<List<ReceiptLogger>> getAll();

    @Query("DELETE FROM receipt")
    void deleteAll();

    @Insert
    long insert(ReceiptLogger receipt);

    @Update
    void update(ReceiptLogger receipt);

    @Delete()
    void delete(ReceiptLogger receipt);

}
