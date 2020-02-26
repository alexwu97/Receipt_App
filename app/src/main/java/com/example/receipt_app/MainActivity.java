package com.example.receipt_app;

import android.content.Intent;
import android.net.Uri;
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
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = RECEIPT_SEND_REQUEST_URL;

                JsonRequest request = new JsonRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray data = response.getJSONArray("data");
                            JSONObject headers = response.getJSONObject("headers");
                                textView.append(headers.toString() + "\n\n");
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })

                {
                    /** Passing some request headers* */
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("Ocp-Apim-Subscription-Key", "f2b2a6bf17ff4e119dbdcda4a4ae3d94");
                        return headers;
                    }

                    @Override
                    public byte[] getBody() {
                        JSONObject jsonBodyObj = new JSONObject();
                        String requestBody = null;

                        try {
                            jsonBodyObj.put("url", "https://media-cdn.tripadvisor.com/media/photo-s/0e/4c/61/59/receipt-in-ec-approximatey.jpg");

                            requestBody = jsonBodyObj.toString();
                        } catch (JSONException e) {
                            VolleyLog.wtf(e.getMessage(), "utf-8");
                            return null;
                        }

                        try{
                            return requestBody.getBytes("utf-8");
                        }catch(UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                        return null;
                    }

                };
                    queue.add(request);
            }
        });


    }
}

