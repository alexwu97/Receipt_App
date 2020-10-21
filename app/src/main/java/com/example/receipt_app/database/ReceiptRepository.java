package com.example.receipt_app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.receipt_app.model.ReceiptItems;
import com.example.receipt_app.model.ReceiptItemsDao;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.model.ReceiptLoggerDao;

import java.util.List;

public class ReceiptRepository {
    private ReceiptLoggerDao receiptLoggerDao;
    private ReceiptItemsDao receiptItemsDao;
    private LiveData<List<ReceiptLogger>> mAllReceipts;
    private LiveData<List<ReceiptItems>> mAllReceiptItems;

    public ReceiptRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        receiptLoggerDao = db.receiptLoggerDao();
        mAllReceipts = receiptLoggerDao.getAll();
        receiptItemsDao = db.receiptItemsDao();
        mAllReceiptItems = receiptItemsDao.getAllReceiptItems();
    }

    public LiveData<List<ReceiptLogger>> getAllReceipts(){
        return mAllReceipts;
    }

    public LiveData<List<ReceiptItems>> getAllReceiptItems(){
        return mAllReceiptItems;
    }

    public void insertReceiptLogger(final ReceiptLogger receipt){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptLoggerDao.insert(receipt);
            }
        });
    }

    public void insert(final ReceiptLogger receipt, final List<ReceiptItems> receiptItemsList){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long generatedID = receiptLoggerDao.insert(receipt);
                for (int i = 0; i < receiptItemsList.size(); i++){
                    ReceiptItems receiptItems = receiptItemsList.get(i);
                    receiptItems.setId((int) generatedID);
                    receiptItems.setItemNo(i);

                    receiptItemsDao.insert(receiptItems);
                }
            }
        });
    }

    public void insertReceiptItem(final ReceiptItems receiptItem){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptItemsDao.insert(receiptItem);
            }
        });
    }

    public void deleteAll(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptLoggerDao.deleteAll();
                receiptItemsDao.deleteAll();
            }
        });
    }
}
