package com.example.receipt_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.receipt_app.JsonDataExtractorService;
import com.example.receipt_app.LoadingDialog;
import com.example.receipt_app.R;
import com.example.receipt_app.model.ReceiptItems;
import com.example.receipt_app.model.ReceiptLogger;
import com.example.receipt_app.model.ReceiptViewModel;
import com.example.receipt_app.request_model.ByteArrRequest;
import com.example.receipt_app.request_model.JsonRequest;
import com.example.receipt_app.view.ReceiptHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class SubmissionActivity extends AppCompatActivity {

    private final static String RECEIPT_HEADER_SUBKEY_REQUEST = "Ocp-Apim-Subscription-Key";
    private final static String UPLOAD_RECEIPT_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyze";
    private final static String RECEIPT_RESULT_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyzeResults/";
    private final static String SUBSCRIPTION_KEY = "f2b2a6bf17ff4e119dbdcda4a4ae3d94";
    byte[] byteArray = {};
    private ImageView imageView;
    ReceiptViewModel model;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        if (getIntent().hasExtra("com.example.receipt_app.PICTURE")){

            String text = getIntent().getExtras().getString("com.example.receipt_app.PICTURE");
            Uri texts = Uri.parse(text);
            System.out.println(text);
            imageView = findViewById(R.id.selectedReceipt);
            imageView.setImageURI(texts);
            imageView.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap photo = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
        }

        //Set up the actionbar
        Toolbar toolBar = findViewById(R.id.secondActivityToolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Submission");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button buttonParse = findViewById(R.id.submitBtn);
        loadingDialog = new LoadingDialog(this);

        //Get view model
        model = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        buttonParse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Start the loading screen UI while making request calls to APIs
                loadingDialog.startLoadingDialog();

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "image/jpeg");
                headers.put(RECEIPT_HEADER_SUBKEY_REQUEST, SUBSCRIPTION_KEY);

                ByteArrRequest request = new ByteArrRequest(Request.Method.POST, UPLOAD_RECEIPT_REQUEST_URL, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject headers = response.getJSONObject("headers");
                                    String operationID = headers.get("apim-request-id").toString();

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
                queue.add(request);
            }
        });
    }

    void getReceiptData(String id, RequestQueue queue){

        final RequestQueue queuer = queue;
        final String operationID = id;
        String retrieveReceiptResultURL = RECEIPT_RESULT_REQUEST_URL + operationID;
        HashMap<String, String> headersGet = new HashMap<>();
        headersGet.put(RECEIPT_HEADER_SUBKEY_REQUEST, SUBSCRIPTION_KEY);

        // onSuccess, call second request
        JsonRequest requestGet = new JsonRequest(Request.Method.GET, retrieveReceiptResultURL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject data = response.getJSONObject("data");
                            String status = data.get("status").toString();
                            if(!status.equals("succeeded")){
                                Thread.sleep(2000);
                                getReceiptData(operationID, queuer);
                            }else{
                                ReceiptLogger receipt = new ReceiptLogger();
                                receipt.setTotal(JsonDataExtractorService.getReceiptNumber(data, "Total"));
                                receipt.setMerchantName(JsonDataExtractorService.getReceiptString(data, "MerchantName"));
                                List<ReceiptItems> receiptItems = JsonDataExtractorService.getReceiptItems(data, "Items");

                                saveReceiptDataToDB(receipt, receiptItems);

                                loadingDialog.dismissDialog();
                                Intent gotoReceiptHistory = new Intent(getApplicationContext(), ReceiptHistory.class);
                                startActivity(gotoReceiptHistory);
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

    private void saveReceiptDataToDB(ReceiptLogger receipt, List<ReceiptItems> receiptItems){
        model.insertReceipt(receipt, receiptItems);
    }
}
