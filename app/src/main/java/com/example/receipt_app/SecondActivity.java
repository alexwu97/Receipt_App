package com.example.receipt_app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {

    private final static String RECEIPT_SEND_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyze";
    private final static String RECEIPT_RECEIVE_REQUEST_URL = "https://receiptrecognize.cognitiveservices.azure.com/formrecognizer/v2.0-preview/prebuilt/receipt/analyzeResults/";
    private final static String SUB_KEY = "f2b2a6bf17ff4e119dbdcda4a4ae3d94";
    private JSONObject result = null;
    byte[] byteArray = {};
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        if (getIntent().hasExtra("com.example.receipt_app.SOMETHING")){

            //TextView tv = (TextView) findViewById(R.id.textView);
            String text = getIntent().getExtras().getString("com.example.receipt_app.SOMETHING");
            //tv.setText(text);
            Uri texts = Uri.parse(text);
            System.out.println(text);
            imageView = (ImageView) findViewById(R.id.imageView5);
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

            String operationID;
            @Override
            public void onClick(View v) {

                String url = RECEIPT_SEND_REQUEST_URL;
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "image/jpeg");
                headers.put("Ocp-Apim-Subscription-Key", SUB_KEY);
                JSONObject requestBody = new JSONObject();
                try{
                    requestBody.put("url", "byteArray");
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
                                    headersGet.put("Ocp-Apim-Subscription-Key", SUB_KEY);

                                    JsonRequest requestGet = new JsonRequest(Request.Method.GET, urlGet, null,
                                            new Response.Listener<JSONObject>()
                                            {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try{
                                                        JSONObject data = response.getJSONObject("data");
                                                        JSONObject headers = response.getJSONObject("headers");
                                                        getReceiptData(operationID, queue);
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
                                Thread.sleep(2000);
                                getReceiptData(operationID, queuer);
                            }else{

                                result = data;
                                System.out.println(result);

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
