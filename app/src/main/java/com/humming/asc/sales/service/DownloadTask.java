package com.humming.asc.sales.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<Void, Integer, Void> {

	private Context context;
	private String downloadPath;
	private String fileName;
	private RESTException restException;
	private IDataReadyCallback<?> dataRadyCallback;

	public DownloadTask(Context context, String downloadPath, String fileName,
			IDataReadyCallback<?> dataReadyCallback) {
		this.context = context;
		this.downloadPath = downloadPath;
		this.fileName = fileName;
		this.dataRadyCallback = dataReadyCallback;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		wl.acquire();

		try {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(this.downloadPath);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
					this.restException = new RESTException(
							RESTException.CODE_NETWORK_ERROR, null);

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();

				output = context.openFileOutput(fileName, Context.MODE_PRIVATE);

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled())
						return null;
					total += count;
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				this.restException = new RESTException(e);
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
		} finally {
			wl.release();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		dataRadyCallback.onDataReady(null, restException);
	}

}
