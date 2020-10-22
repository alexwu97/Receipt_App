package com.example.receipt_app.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.receipt_app.database.ReceiptRepository;

import java.util.List;

public class ReceiptViewModel extends AndroidViewModel {
    private ReceiptRepository repository;
    private LiveData<List<ReceiptMain>> allReceipts;
    private LiveData<List<ReceiptItem>> allReceiptItems;

    public ReceiptViewModel(@NonNull Application application) {
        super(application);
        repository = new ReceiptRepository(application);
        allReceipts = repository.getAllReceipts();
        allReceiptItems = repository.getAllReceiptItems();
    }

    public LiveData<List<ReceiptMain>> getAllReceipts(){
        return allReceipts;
    }

    public LiveData<List<ReceiptItem>> getAllReceiptItems(){
        return allReceiptItems;
    }


    public void insertReceiptLogger(ReceiptMain receipt){
        repository.insertReceiptLogger(receipt);
    }

    public void insertReceiptItem(ReceiptItem receiptItem){
        repository.insertReceiptItem(receiptItem);
    }

    public void insertReceipt(ReceiptMain receipt, List<ReceiptItem> receiptItemList){
        repository.insert(receipt, receiptItemList);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
