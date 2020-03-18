package com.example.receipt_app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.receipt_app.JsonSearcher;
import com.example.receipt_app.R;
import com.example.receipt_app.database.AppDatabase;
import com.example.receipt_app.database.AppExecutors;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.request_model.ByteArrRequest;
import com.example.receipt_app.request_model.JsonRequest;
import com.example.receipt_app.view.LogDisplay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class SecondActivity extends AppCompatActivity {

    private final static String RECEIPT_SEND_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyze";
    private final static String RECEIPT_RECEIVE_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyzeResults/";
    private final static String SUB_KEY = "f2b2a6bf17ff4e119dbdcda4a4ae3d94";
    private JSONObject result = null;
    byte[] byteArray = {};
    private ImageView imageView;
    private AppDatabase db;

    private final String filenameInternal = "receiptLogs";

    private double total = 0.0;
    private String merchantName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (getIntent().hasExtra("com.example.receipt_app.PICTURE")){

            String text = getIntent().getExtras().getString("com.example.receipt_app.PICTURE");
            Uri texts = Uri.parse(text);
            System.out.println(text);
            imageView = (ImageView) findViewById(R.id.selectedReceipt);
            imageView.setImageURI(texts);
            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap photo = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
            String byteString = byteArray.toString();
            System.out.println(byteString);
        }

        Button backButton = (Button) findViewById(R.id.backBtn);
        Button buttonParse = (Button) findViewById(R.id.submitBtn);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {

            String operationID = "15128ce3-a62e-4db5-a5a3-df42a0fe859a";
            @Override
            public void onClick(View v) {

                getReceiptData(operationID, queue);

                /*String url = RECEIPT_SEND_REQUEST_URL;
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "image/jpeg");
                headers.put("Ocp-Apim-Subscription-Key", SUB_KEY);

                ByteArrRequest request = new ByteArrRequest(Request.Method.POST, url, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject headers = response.getJSONObject("headers");
                                    operationID = headers.get("apim-request-id").toString();
                                    System.out.println(operationID);

                                    getReceiptData(operationID, queue);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }, headers, byteArray);
                queue.add(request);*/
            }
        });
    }

    void getReceiptData(String id, RequestQueue queue){

        final RequestQueue queuer = queue;
        final String operationID = id;
        String urlGet = RECEIPT_RECEIVE_REQUEST_URL + operationID;
        HashMap<String, String> headersGet = new HashMap<>();
        headersGet.put("Ocp-Apim-Subscription-Key", SUB_KEY);

        // onSuccess, call second request
        JsonRequest requestGet = new JsonRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject data = response.getJSONObject("data");
                            String status = data.get("status").toString();
                            System.out.println(status);
                            if(!status.equals("succeeded")){
                                Thread.sleep(8000);
                                getReceiptData(operationID, queuer);
                            }else{

                                result = data;
                                total = JsonSearcher.getReceiptNumber(result, "Total");
                                merchantName = JsonSearcher.getReceiptString(result, "MerchantName");
                                System.out.println(total);
                                System.out.println(merchantName);
                                // createUpdateFile(filenameInternal, result.toString(), false);
                                saveReceiptDataToDB();

                                Intent gotoLogDisplay = new Intent(getApplicationContext(), LogDisplay.class);
                                startActivity(gotoLogDisplay);

                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }, headersGet
        );
        queuer.add(requestGet);
    }

    private void saveReceiptDataToDB(){
        db = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ReceiptLogger receipt = new ReceiptLogger();
                receipt.setMerchantName(merchantName);
                receipt.setTotal(total);
                db.receiptLoggerDao().insert(receipt);
                //db.receiptLoggerDao().deleteAll();
            }
        });
    }

    private void createUpdateFile(String fileName, String content, boolean update) {
        FileOutputStream outputStream;

        try {
            if (update) {
                outputStream = openFileOutput(fileName, Context.MODE_APPEND);
            } else {
                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            }
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
