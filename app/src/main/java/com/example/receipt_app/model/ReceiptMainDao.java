package com.example.receipt_app.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ReceiptMainDao {
    @Query("SELECT * from ReceiptMain")
    LiveData<List<ReceiptMain>> getAll();

    @Query("DELETE FROM ReceiptMain")
    void deleteAll();

    @Insert
    long insert(ReceiptMain receipt);

    @Update
    void update(ReceiptMain receipt);

    @Delete()
    void delete(ReceiptMain receipt);

}
