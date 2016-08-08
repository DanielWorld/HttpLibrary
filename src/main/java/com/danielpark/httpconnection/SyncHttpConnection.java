package com.danielpark.httpconnection;

import android.os.Looper;

import com.danielpark.httpconnection.handler.AsyncHttpResponseHandler;
import com.danielpark.httpconnection.handler.JsonHttpResponseHandler;
import com.danielpark.httpconnection.request.HttpRequest;
import com.danielpark.httpconnection.type.RequestType;
import com.danielpark.httpconnection.util.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.*;

/**
 * Sync http protocol connection (https://github.com/square/okhttp)
 * <br><br>
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class SyncHttpConnection {

	private Logger LOG = Logger.getInstance();

	private static SyncHttpConnection sThis;

	private SyncHttpConnection(){}

	public final static synchronized SyncHttpConnection getInstance() {
		if (sThis == null)
			sThis = new SyncHttpConnection();
		return sThis;
	}

	private OkHttpClient client;

	HttpConnectionTask currentTask;

	/**
	 * Start Async HTTP connection with {@link JsonHttpResponseHandler}
	 * @param request
	 * @param mListener
	 */
	public void start(HttpRequest request, JsonHttpResponseHandler mListener){
		start(request, mListener, null);
	}

	/**
	 * Start Async HTTP connection with {@link AsyncHttpResponseHandler}
	 * @param request
	 * @param mListener
	 */
	public void start(HttpRequest request, AsyncHttpResponseHandler mListener){
		start(request, mListener, null);
	}

	/**
	 * Start Sync HTTP connection with {@link JsonHttpResponseHandler}
	 *
	 * @param request
	 */
	public void start(HttpRequest request, JsonHttpResponseHandler mListener, Interceptor interceptor){

		// Make sure that everything is perfect!
		synchronized (this) {
			if (request == null) {
				LOG.e("HttpRequest parameter is null!");
				return;
			}

			// No need to check thread, because okhttp call method already run in another Thread!!
			if (Looper.myLooper() == Looper.getMainLooper()) {
				LOG.e("Do not execute this on main thread!!");
				return;
			}

			if(client == null){
				client = new OkHttpClient.Builder()
						.connectTimeout(30, TimeUnit.SECONDS)
						.writeTimeout(30, TimeUnit.SECONDS)
						.readTimeout(30, TimeUnit.SECONDS)
						.build();
			}

			// Daniel (2016-07-08 12:04:51): if interceptor exists!
			try {
				if (interceptor != null) {
					client = client.newBuilder()
							.connectTimeout(30, TimeUnit.SECONDS)
							.writeTimeout(30, TimeUnit.SECONDS)
							.readTimeout(30, TimeUnit.SECONDS)
							.addInterceptor(interceptor)
							.build();
				}
			} catch (Exception e){
				e.printStackTrace();
			}

			// Sync mode
			if(mListener != null)
				mListener.setUseSynchronousMode(true);

			if (request.getRequestType() == RequestType.Type.MULTI_PART) {
				currentTask = new MultipartTask(client, request, mListener);
			} else if (request.getRequestType() == RequestType.Type.STRING) {
				currentTask = new StringTask(client, request, mListener);
			}
		}
		currentTask.run(HttpConnectionTask.SyncType.Sync);
	}

	/**
	 * Start Sync HTTP connection with {@link AsyncHttpResponseHandler}
	 *
	 * @param request
	 */
	public void start(HttpRequest request, AsyncHttpResponseHandler mListener, Interceptor interceptor){

		// Make sure that everything is perfect!
		synchronized (this) {
			if (request == null) {
				LOG.e("HttpRequest parameter is null!");
				return;
			}

			// No need to check thread, because okhttp call method already run in another Thread!!
			if (Looper.myLooper() == Looper.getMainLooper()) {
				LOG.e("Do not execute this on main thread!!");
				return;
			}

			if(client == null){
				client = new OkHttpClient.Builder()
						.connectTimeout(30, TimeUnit.SECONDS)
						.writeTimeout(30, TimeUnit.SECONDS)
						.readTimeout(30, TimeUnit.SECONDS)
						.build();
			}

			// Daniel (2016-07-08 12:04:51): if interceptor exists!
			try {
				if (interceptor != null) {
					client = client.newBuilder()
							.connectTimeout(30, TimeUnit.SECONDS)
							.writeTimeout(30, TimeUnit.SECONDS)
							.readTimeout(30, TimeUnit.SECONDS)
							.addInterceptor(interceptor)
							.build();
				}
			} catch (Exception e){
				e.printStackTrace();
			}

			// Sync mode
			if(mListener != null)
				mListener.setUseSynchronousMode(true);

			if (request.getRequestType() == RequestType.Type.MULTI_PART) {
				currentTask = new MultipartTask(client, request, mListener);
			} else if (request.getRequestType() == RequestType.Type.STRING) {
				currentTask = new StringTask(client, request, mListener);
			}
		}
		currentTask.run(HttpConnectionTask.SyncType.Sync);
	}
}
