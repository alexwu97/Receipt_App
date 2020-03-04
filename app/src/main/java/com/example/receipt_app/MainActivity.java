package com.example.receipt_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final static String RECEIPT_SEND_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyze";
    private final static String RECEIPT_RECEIVE_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyzeResults/";
    private String result = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button secondActivityBtn = (Button) findViewById(R.id.secondActivityBtn);
        secondActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                startIntent.putExtra("com.example.receipt_app.SOMETHING", "HELLO WORLD!");
                startActivity(startIntent);
            }
        });

        Button googleBtn = (Button) findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "http://old.reddit.com/r/leagueoflegends";
                Uri webaddress = Uri.parse(google);

                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webaddress);
                if (gotoGoogle.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoGoogle);
                }
            }
        });

        final TextView textView = (TextView) findViewById(R.id.text);
        Button buttonParse = findViewById(R.id.button_parse);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        // local class



        buttonParse.setOnClickListener(new View.OnClickListener() {
            String operationID;

            @Override
            public void onClick(View v) {

                String url = RECEIPT_SEND_REQUEST_URL;
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", "f2b2a6bf17ff4e119dbdcda4a4ae3d94");
                JSONObject requestBody = new JSONObject();
                try{
                    requestBody.put("url", "https://media-cdn.tripadvisor.com/media/photo-s/0e/4c/61/59/receipt-in-ec-approximatey.jpg");
                }catch(Exception e){
                  e.printStackTrace();
                }

                JsonRequest request = new JsonRequest(Request.Method.POST, url, requestBody,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject headers = response.getJSONObject("headers");
                                    operationID = headers.get("apim-request-id").toString();


                                    String urlGet = RECEIPT_RECEIVE_REQUEST_URL + operationID;
                                    HashMap<String, String> headersGet = new HashMap<>();
                                    headersGet.put("Ocp-Apim-Subscription-Key", "f2b2a6bf17ff4e119dbdcda4a4ae3d94");

                                    JsonRequest requestGet = new JsonRequest(Request.Method.GET, urlGet, null,
                                            new Response.Listener<JSONObject>()
                                            {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try{
                                                        JSONObject data = response.getJSONObject("data");
                                                        JSONObject headers = response.getJSONObject("headers");
                                                        getReceiptData(operationID, queue);


                                                        textView.append(result);
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
                                    queue.add(requestGet);



                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }, headers
                );
                queue.add(request);
            }
        });
    }

    void getReceiptData(String id, RequestQueue queue){

        final RequestQueue queuer = queue;
        final String operationID = id;
        String urlGet = RECEIPT_RECEIVE_REQUEST_URL + operationID;
        HashMap<String, String> headersGet = new HashMap<>();
        headersGet.put("Ocp-Apim-Subscription-Key", "f2b2a6bf17ff4e119dbdcda4a4ae3d94");

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
                                Thread.sleep(2000);
                                getReceiptData(operationID, queuer);
                            }else{

                                result = status;

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
}

