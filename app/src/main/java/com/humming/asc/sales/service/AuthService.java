package com.humming.asc.sales.service;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.humming.asc.dp.presentation.vo.AuthVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PuTi(编程即菩提) on 12/22/15.
 */
public class AuthService extends AbstractService implements IAuthService {
    @Override
    public void login(final ICallback callback, final String username, final String password) {
        AuthRequest req = new AuthRequest(Request.Method.POST, Config.URL_SERVICE_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJSON(response, AuthVO.class, callback);
            }
        }, new ErrorListener(callback));
        req.setUsername(username);
        req.setPassword(password);
        Application.getInstance().addToRequestQueue(req);
    }

    public class AuthRequest extends StringRequest {
        private String username;
        private String password;

        public AuthRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return createBasicAuthHeader(this.username, this.password);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            Map<String, String> responseHeaders = response.headers;
            String token = responseHeaders.get(Application.HEADER_TOKEN);
            Application.setToken(token);
            return super.parseNetworkResponse(response);
        }

        Map<String, String> createBasicAuthHeader(String username, String password) {
            Map<String, String> headerMap = new HashMap<String, String>();

            String credentials = username + ":" + password;
            String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headerMap.put("Authorization", "Basic " + encodedCredentials);
            String androidId = Settings.Secure.getString(Application.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.v("xxxxx", "---" + androidId);
            //   Toast.makeText(Application.getInstance().getBaseContext(),androidId, Toast.LENGTH_LONG).show();
            headerMap.put("Divice", androidId);
            return headerMap;
        }
    }
}
