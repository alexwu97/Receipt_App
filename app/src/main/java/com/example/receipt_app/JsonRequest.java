package com.example.receipt_app;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonRequest extends JsonObjectRequest {
    private final Map<String, String> headers;
    // private final JSONObject jsonBodyObj;

    public JsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener
            <JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, jsonRequest, listener, errorListener);
        this.headers = headers;
        // this.jsonBodyObj = jsonRequest;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("data", ("".equals(jsonString))? new JSONObject(): new JSONObject(jsonString));
            jsonResponse.put("headers", new JSONObject(response.headers));

            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Map<String,String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

//    @Override
//    public byte[] getBody() {
//        String requestBody = jsonBodyObj.toString();
//        try{
//            return requestBody.getBytes("utf-8");
//        }catch(UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}