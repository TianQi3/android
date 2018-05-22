package com.humming.asc.sales.service;

import android.content.Context;
import android.widget.Toast;

import com.humming.asc.sales.R;


public class RESTException extends Exception {
	private static final long serialVersionUID = 5569499062635945277L;

	public static final int CODE_NETWORK_ERROR =R.string.error_network_unreachable ;
	public static final int CODE_SERVICE_NOT_FOUND = R.string.error_server;

	public static final int CODE_OTHER_ERROR = 1000000;

	private int code = CODE_OTHER_ERROR;
	private String text = null;
	
	
	public RESTException(Throwable throwable) {
		super(throwable);
	}

	public RESTException(int code, Throwable throwable) {
		super(throwable);
		this.code = code;
	}

	public void showMessage(Context context) {
		if (code != CODE_OTHER_ERROR) {
			Toast.makeText(context, code, 2000).show();
		}
	}

}
