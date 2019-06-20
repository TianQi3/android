package com.humming.asc.sales.service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.google.gson.Gson;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;


/**
 * Created by Zhtq on 2016/12/2.
 * 网络请求
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    public static final MediaType JSON = MediaType.parse("application/json;CharSet=UTF-8");

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass) {
        getInstance()._postAsyn(url, callback, requestMainDataData, resultVOClass);
    }

    public static <T> void postAsyn(String url, final ResultCallback callback, IRequestMainData requestMainDataData, final TypeReference typeReference) {
        getInstance()._postAsyn(url, callback, requestMainDataData, typeReference);
    }

    //异步请求 类(返回普通类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final Class<T> resultVOClass) {
        RequestData requestData = new RequestData();
        requestData.setCmd(cmd);
        SharedPreferences preferences = Application.getInstance().getCurrentActivity().getSharedPreferences("language", Activity.MODE_PRIVATE);
        String currentLanguage = preferences.getString("currentLanguage", "");
        if (currentLanguage == null || "".equals(currentLanguage) || "english".equals(currentLanguage)) {
            requestData.setLanguage("en");
        } else {
            requestData.setLanguage("cn");
        }
        requestData.setParameters(requestMainDataData);
        String url = Config.URL_SERVICE_ECATALOG;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, resultVOClass);
    }


    //异步请求 类(返回集合类)
    private <T> void _postAsyn(String cmd, final ResultCallback callback, IRequestMainData requestMainDataData, final TypeReference typeReference) {
        RequestData requestData = new RequestData();
        SharedPreferences preferences = Application.getInstance().getCurrentActivity().getSharedPreferences("language", Activity.MODE_PRIVATE);
        String currentLanguage = preferences.getString("currentLanguage", "");
        if (currentLanguage == null || "".equals(currentLanguage) || "english".equals(currentLanguage)) {
            requestData.setLanguage("en");
        } else {
            requestData.setLanguage("cn");
        }
        //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
        requestData.setCmd(cmd);
        requestData.setParameters(requestMainDataData);
        String url = Config.URL_SERVICE_ECATALOG;
        Request request = buildPostRequest(url, requestData);
        deliveryResult(callback, request, typeReference);
    }


    private <T> Request buildPostRequest(String url, RequestData requestData) {

        String json = new Gson().toJson(requestData);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Log.v("xxxxxx", json);
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Token", Application.getToken())
                .build();
    }

    public static class Param {
        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }

    private <T> void deliveryResult(final ResultCallback callback, final Request request, final Class<T> resultVOClass) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        if (resultVOClass == ResponseData.class) {//只判断状态(只确定返回是否成功)
                            ResponseData responseData = new ResponseData();
                            responseData.setStatusCode(0);
                            sendSuccessResultCallback(responseData, callback);
                        } else {
                            JsonNode responseNode = node.get("response");
                            T data = mapper.treeToValue(responseNode, resultVOClass);
                            sendSuccessResultCallback(data, callback);
                        }
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });
    }


    private <T> void deliveryResult(final ResultCallback callback, final Request request, final TypeReference typeReference) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    final String string = response.body().string();
                    Log.v("xxxxxx", "data:" + string);
                    JsonNode node = mapper.readValue(string, JsonNode.class);
                    int statusCode = ((IntNode) node.get("statusCode")).intValue();
                    if (statusCode == 0) {
                        JsonNode responseNode = node.get("response");
                        T result = mapper.readValue(responseNode.toString(), typeReference);
                        sendSuccessResultCallback(result, callback);
                    } else {
                        JsonNode errorNode = node.get("error");
                        Error error = mapper.treeToValue(errorNode, Error.class);
                        sendErrorStringCallback(request, error, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onOtherError(request, e);
            }
        });
    }

    private void sendErrorStringCallback(final Request request, final Error e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }


    public static abstract class ResultCallback<T> {

        public abstract void onError(Request request, Error info);

        public abstract void onResponse(T response);

        public abstract void onOtherError(Request request, Exception exception);
    }

}