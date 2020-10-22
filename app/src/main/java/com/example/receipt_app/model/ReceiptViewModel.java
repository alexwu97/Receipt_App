package com.example.receipt_app.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.receipt_app.database.ReceiptRepository;

import java.util.List;

public class ReceiptViewModel extends AndroidViewModel {
    private ReceiptRepository repository;
    private LiveData<List<Receipt>> allReceipts;

    public ReceiptViewModel(@NonNull Application application) {
        super(application);
        repository = new ReceiptRepository(application);
        allReceipts = repository.getAllReceipts();
    }

    public LiveData<List<Receipt>> getAllReceipts() {
        return allReceipts;
    }

    public void insertReceipt(Receipt receipt) {
        repository.insertReceipt(receipt);
    }


    public void deleteAll() {
        repository.deleteAll();
    }
}
