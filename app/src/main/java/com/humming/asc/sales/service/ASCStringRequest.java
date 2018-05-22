package com.humming.asc.sales.service;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humming.asc.sales.Application;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhtq on 16/3/5.
 */
public class ASCStringRequest extends StringRequest {
    private Map<String, String> params;
    private Object requestDate;

    public ASCStringRequest(int method, String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.params = params;
    }

    public ASCStringRequest(String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.params = params;
    }

    public ASCStringRequest(int method, String url, Object requestDate, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.requestDate = requestDate;
    }

    public ASCStringRequest(int method, String url, String filePartName, File file, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.params = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerMap = new HashMap<String, String>();
        String token = Application.getToken();
        headerMap.put("Token", token);
        return headerMap;
    }

   /* @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Charset", "UTF-8");
        headers.put("Content-Type", "application/json");
        return headers;
    }*/

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params != null) {
            return params;
        }
        return super.getParams();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (requestDate != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonInString = mapper.writeValueAsString(requestDate);
                byte[] bytes = jsonInString.getBytes();
                return bytes;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.getBody();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

}

