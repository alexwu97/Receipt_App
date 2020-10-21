package com.example.receipt_app.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.receipt_app.database.ReceiptRepository;

import java.util.List;

public class ReceiptViewModel extends AndroidViewModel {
    private ReceiptRepository repository;
    private LiveData<List<ReceiptLogger>> allReceipts;
    private LiveData<List<ReceiptItems>> allReceiptItems;

    public ReceiptViewModel(@NonNull Application application) {
        super(application);
        repository = new ReceiptRepository(application);
        allReceipts = repository.getAllReceipts();
        allReceiptItems = repository.getAllReceiptItems();
    }

    public LiveData<List<ReceiptLogger>> getAllReceipts(){
        return allReceipts;
    }

    public LiveData<List<ReceiptItems>> getAllReceiptItems(){
        return allReceiptItems;
    }


    public void insertReceiptLogger(ReceiptLogger receipt){
        repository.insertReceiptLogger(receipt);
    }

    public void insertReceiptItem(ReceiptItems receiptItem){
        repository.insertReceiptItem(receiptItem);
    }

    public void insertReceipt(ReceiptLogger receipt, List<ReceiptItems> receiptItemsList){
        repository.insert(receipt, receiptItemsList);
    }

    public void deleteAll(){
        repository.deleteAll();
    }
}
