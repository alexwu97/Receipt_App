package com.example.receipt_app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.receipt_app.model.Receipt;
import com.example.receipt_app.model.ReceiptDao;

import java.util.List;

public class ReceiptRepository {
    private ReceiptDao receiptDao;
    private LiveData<List<Receipt>> mAllReceipts;

    public ReceiptRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        receiptDao = db.receiptDao();
        mAllReceipts = receiptDao.getAll();
    }

    public LiveData<List<Receipt>> getAllReceipts() {
        return mAllReceipts;
    }

    public void insertReceipt(final Receipt receipt) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptDao.insert(receipt);
            }
        });
    }

    public void deleteAll() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                receiptDao.deleteAll();
            }
        });
    }
}
