package com.humming.asc.sales.service;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humming.asc.dp.presentation.vo.cp.AbsResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.LoginActivity;
import com.humming.asc.sales.activity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by PuTi(编程即菩提) on 1/4/16.
 */
public abstract class AbstractService {
    static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public <T> void get(String url, Map<String, String> params, final ICallback callback, final Class<T> resultVOClass, StringRequest stringRequest) {
        int method = Request.Method.GET;
        String newUrl = urlEncodeUTF8(url, params);
        requestData(method, newUrl, params, callback, resultVOClass, stringRequest);
    }

    public <T> void get(String url, Map<String, String> params, final ICallback callback, final TypeReference typeReference, StringRequest stringRequest) {
        int method = Request.Method.GET;
        String newUrl = urlEncodeUTF8(url, params);
        requestData(method, newUrl, params, callback, typeReference, stringRequest);
    }

    public <T> void post(String url, Map<String, String> params, final ICallback callback, final Class<T> resultVOClass, StringRequest stringRequest) {
        int method = Request.Method.POST;
        requestData(method, url, params, callback, resultVOClass, stringRequest);
    }

    public <T> void post(String url, String filePartName, File file, Map<String, String> params, final ICallback callback, final Class<T> resultVOClass, StringRequest stringRequest) {
        int method = Request.Method.POST;
        requestData(method, url, filePartName, file, params, callback, resultVOClass, stringRequest);
    }

    public <T> void post(String url, Object params, final ICallback callback, final Class<T> resultVOClass, StringRequest stringRequest) {
        int method = Request.Method.POST;
        requestData(method, url, params, callback, resultVOClass, stringRequest);
    }

    private String urlEncodeUTF8(String url, Map<?, ?> map) {
        if (map == null || map.size() == 0) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }

        return sb.toString();
    }


    private <T> void requestData(int method, String url, Map<String, String> params, final ICallback callback, final Class<T> resultVOClass, StringRequest req) {
        if (req == null) {
            req = new ASCStringRequest(method, url, params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parseJSON(response, resultVOClass, callback);
                        }

                    }, new ErrorListener(callback));
        }
        Application.getInstance().addToRequestQueue(req);
    }

    private <T> void requestData(int method, String url, Object params, final ICallback callback, final Class<T> resultVOClass, StringRequest req) {
        if (req == null) {
            req = new ASCStringRequest(method, url, params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parseJSON(response, resultVOClass, callback);
                        }

                    }, new ErrorListener(callback));
        }
        Application.getInstance().addToRequestQueue(req);
    }

    private <T> void requestData(int method, String url, Map<String, String> params, final ICallback callback, final TypeReference typeReference, StringRequest req) {
        if (req == null) {
            req = new ASCStringRequest(method, url, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parseJSON(response, typeReference, callback);
                        }

                    }, new ErrorListener(callback));
            // req.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 1, 1.0f));
        }
        Application.getInstance().addToRequestQueue(req);
    }

    private <T> void requestData(int method, String url, String filePartName, File file, Map<String, String> params, final ICallback callback, final Class<T> resultVOClass, StringRequest req) {
        if (req == null) {
            req = new MultipartRequest(method, url, filePartName, file, params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parseJSON(response, resultVOClass, callback);
                        }

                    }, new ErrorListener(callback));
            //req.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 1, 1.0f));
        }
        Application.getInstance().addToRequestQueue(req);
    }

    protected <T> void parseJSON(String response, final Class<T> resultVOClass, final ICallback callback) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            T result = mapper.readValue(response, resultVOClass);
            callback.onDataReady(result);
            /*if (result instanceof AbsResultVO) {
                AbsResultVO absResultVO = (AbsResultVO) result;
                if (absResultVO.getState() == -10 || absResultVO.getState() == 10) {
                    backLogin();
                }
            }*/
        } catch (IOException e) {
            handleException(callback, e);
        }
    }

    private void backLogin() {
        MainActivity.activity.finish();
        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
        Application.getInstance().getCurrentActivity().startActivity(intent);
        Application.getInstance().getCurrentActivity().finish();
    }

    protected <T> void parseJSON(String response, final TypeReference typeReference, final ICallback callback) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            T result = mapper.readValue(response, typeReference);
            callback.onDataReady(result);
            if (result instanceof AbsResultVO) {
                AbsResultVO absResultVO = (AbsResultVO) result;
                if (absResultVO.getState() == -10 || absResultVO.getState() == 10) {
                    backLogin();
                }
            }
        } catch (IOException e) {
            handleException(callback, e);
        }
    }

    private void handleException(ICallback callback, IOException e) {
        e.printStackTrace();
        Context context = Application.getInstance().getBaseContext();
        String msg = context.getString(R.string.error_data_format);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        ServiceError error = new ServiceError();
        callback.onError(error);
    }

}
