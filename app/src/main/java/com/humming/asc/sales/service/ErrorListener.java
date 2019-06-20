package com.humming.asc.sales.service;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;

/**
 * Created by Zhtq on 1/4/16.
 */
public class ErrorListener implements Response.ErrorListener {
    protected ICallback callback;

    public ErrorListener(ICallback callback) {
        this.callback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Context context = Application.getInstance().getBaseContext();
        String msg = context.getString(R.string.error_network_unreachable);
        if (error instanceof TimeoutError) {
            msg = context.getString(R.string.error_network_timeout);
        }else if(error instanceof NoConnectionError){
            msg = context.getString(R.string.error_network_unreachable);
        } else if (error instanceof AuthFailureError) {
            msg = context.getString(R.string.error_auth_failure);
        } else if (error instanceof ServerError) {
            msg = context.getString(R.string.error_server);
        } else if (error instanceof NetworkError) {
            msg = context.getString(R.string.error_network_unreachable);
        } else if (error instanceof ParseError) {
            msg = context.getString(R.string.error_server);
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        callback.onError(error);
    }
}
