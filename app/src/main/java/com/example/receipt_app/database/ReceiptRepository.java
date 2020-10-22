package com.example.receipt_app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.receipt_app.model.ReceiptItem;
import com.example.receipt_app.model.ReceiptItemDao;
import com.example.receipt_app.model.ReceiptMain;
import com.example.receipt_app.model.ReceiptMainDao;

import java.util.List;

public class ReceiptRepository {
    private ReceiptMainDao receiptMainDao;
    private ReceiptItemDao receiptItemDao;
    private LiveData<List<ReceiptMain>> mAllReceipts;
    private LiveData<List<ReceiptItem>> mAllReceiptItems;

    public ReceiptRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        receiptMainDao = db.receiptLoggerDao();
        mAllReceipts = receiptMainDao.getAll();
        receiptItemDao = db.receiptItemDao();
        mAllReceiptItems = receiptItemDao.getAllReceiptItems();
    }

    public LiveData<List<ReceiptMain>> getAllReceipts(){
        return mAllReceipts;
    }

    public LiveData<List<ReceiptItem>> getAllReceiptItems(){
        return mAllReceiptItems;
    }

    public void insertReceiptLogger(final ReceiptMain receipt){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptMainDao.insert(receipt);
            }
        });
    }

    public void insert(final ReceiptMain receipt, final List<ReceiptItem> receiptItemList){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long generatedID = receiptMainDao.insert(receipt);
                for (int i = 0; i < receiptItemList.size(); i++){
                    ReceiptItem receiptItem = receiptItemList.get(i);
                    receiptItem.setId((int) generatedID);
                    receiptItem.setItemNo(i);

                    receiptItemDao.insert(receiptItem);
                }
            }
        });
    }

    public void insertReceiptItem(final ReceiptItem receiptItem){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptItemDao.insert(receiptItem);
            }
        });
    }

    public void deleteAll(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptMainDao.deleteAll();
                receiptItemDao.deleteAll();
            }
        });
    }
}
