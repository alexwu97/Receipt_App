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
import com.example.receipt_app.service.JsonDataExtractorService;
import com.example.receipt_app.view.LoadingDialog;
import com.example.receipt_app.R;
import com.example.receipt_app.model.ReceiptItem;
import com.example.receipt_app.model.ReceiptMain;
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
    private byte[] receiptImageByteArray = new byte[0];
    ReceiptViewModel viewModel;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        //Set up the actionbar
        Toolbar toolBar = findViewById(R.id.secondActivityToolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Submission");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView receiptImage = findViewById(R.id.selectedReceipt);

        if (getIntent().hasExtra("receiptPictureURI")){
            Uri receiptImageURI = Uri.parse(getIntent().getExtras().getString("receiptPictureURI"));
            receiptImage.setImageURI(receiptImageURI);
            receiptImage.invalidate();
            BitmapDrawable receiptImageDrawable = (BitmapDrawable) receiptImage.getDrawable();
            Bitmap receiptImageBitMap = receiptImageDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            receiptImageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            receiptImageByteArray = stream.toByteArray();
        }

        Button submitBtn = findViewById(R.id.submitBtn);
        loadingDialog = new LoadingDialog(this);

        //Get view model
        viewModel = ViewModelProviders.of(this).get(ReceiptViewModel.class);

        //Instantiate the request queue
        final RequestQueue uploadImageRequestQueue = Volley.newRequestQueue(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Start the loading screen UI while making request calls to APIs
                loadingDialog.startLoadingDialog();

                HashMap<String, String> uploadReceiptRequestHeader = new HashMap<>();
                uploadReceiptRequestHeader.put("Content-Type", "image/jpeg");
                uploadReceiptRequestHeader.put(RECEIPT_HEADER_SUBKEY_REQUEST, SUBSCRIPTION_KEY);

                //First API request: post binary data of receipt image to API
                ByteArrRequest uploadReceiptRequest = new ByteArrRequest(Request.Method.POST, UPLOAD_RECEIPT_REQUEST_URL, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    //get the endpoint from the response header for retrieving extracted data
                                    JSONObject uploadReceiptResponseHeader = response.getJSONObject("headers");
                                    String operationID = uploadReceiptResponseHeader.get("apim-request-id").toString();

                                    //on getting response, perform API call on the new endpoint
                                    //to get the extracted data
                                    getReceiptData(operationID, uploadImageRequestQueue);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }, uploadReceiptRequestHeader, receiptImageByteArray);
                uploadImageRequestQueue.add(uploadReceiptRequest);
            }
        });
    }

    void getReceiptData(String id, RequestQueue queue){

        final RequestQueue getReceiptDataRequestQueue = queue;
        final String operationID = id;
        String getReceiptResultURL = RECEIPT_RESULT_REQUEST_URL + operationID;
        HashMap<String, String> resultRequestHeaders = new HashMap<>();
        resultRequestHeaders.put(RECEIPT_HEADER_SUBKEY_REQUEST, SUBSCRIPTION_KEY);

        //Second API request: get data extracted from receipt image
        JsonRequest getReceiptDataRequest = new JsonRequest(Request.Method.GET, getReceiptResultURL, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject receiptData = response.getJSONObject("data");
                            String status = receiptData.get("status").toString();
                            if(!status.equals("succeeded")){
                                //wait 2 seconds and try again
                                Thread.sleep(2000);
                                getReceiptData(operationID, getReceiptDataRequestQueue);
                            }else{
                                //get receipt information from the extracted JSON data and save it to DB
                                ReceiptMain receipt = JsonDataExtractorService.getReceiptFromReceiptData(receiptData);
                                List<ReceiptItem> receiptItems = JsonDataExtractorService.getReceiptItems(receiptData, "Items");
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
                }, resultRequestHeaders
        );
        getReceiptDataRequestQueue.add(getReceiptDataRequest);
    }

    private void saveReceiptDataToDB(ReceiptMain receipt, List<ReceiptItem> receiptItems){
        viewModel.insertReceipt(receipt, receiptItems);
    }
}
