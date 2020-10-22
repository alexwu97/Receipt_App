package com.example.receipt_app.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// QUERY METHODS

@Dao
public interface ReceiptDao {
    @Query("SELECT * from Receipt")
    LiveData<List<Receipt>> getAll();

    @Query("DELETE FROM Receipt")
    void deleteAll();

    @Insert
    long insert(Receipt receipt);

    @Update
    void update(Receipt receipt);

    @Delete()
    void delete(Receipt receipt);
}
