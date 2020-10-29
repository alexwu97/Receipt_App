package com.example.receipt_app.request_model;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ByteArrRequest extends JsonObjectRequest {
    private static final String DATA = "data";
    private static final String HEADERS = "headers";
    private final Map<String, String> headers;
    private final byte[] imageData;

    public ByteArrRequest(int method, String url, JSONObject jsonRequest, Response.Listener
            <JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> headers, byte[] imageData) {
        super(method, url, jsonRequest, listener, errorListener);
        this.headers = headers;
        this.imageData = imageData;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            //Put response body and header into jsonResponse and return it as response
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put(DATA, ("".equals(jsonString))? new JSONObject(): new JSONObject(jsonString));
            jsonResponse.put(HEADERS, new JSONObject(response.headers));

            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    //Override method in order to provide custom request header
    @Override
    public Map<String,String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    //Override the method to use imageData as the request body
    @Override
    public byte[] getBody() {
        return imageData;
    }
}