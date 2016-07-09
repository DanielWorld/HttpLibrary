package com.danielpark.httpconnection;

import android.content.Context;
import android.os.Looper;

import com.danielpark.httpconnection.handler.AsyncHttpResponseHandler;
import com.danielpark.httpconnection.handler.JsonHttpResponseHandler;
import com.danielpark.httpconnection.request.HttpRequest;
import com.danielpark.httpconnection.type.RequestType;
import com.danielpark.httpconnection.util.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.danielpark.httpconnection.util.NetworkUtil;
import okhttp3.*;

/**
 * Async Http protocol connection (https://github.com/square/okhttp)
 * <br><br>
 * Copyright (c) 2014-2016 daniel@bapul.net
 * Created by Daniel Park on 2016-04-07.
 */
public class AsyncHttpConnection {

    private Logger LOG = Logger.getInstance();
    
    private static AsyncHttpConnection sThis;

    private AsyncHttpConnection(){}

	@Deprecated
    public final static synchronized AsyncHttpConnection getInstance(Context ctx) {
        if (sThis == null)
            sThis = new AsyncHttpConnection();
        return sThis;
    }

    public final static synchronized AsyncHttpConnection getInstance() {
        if (sThis == null)
            sThis = new AsyncHttpConnection();
        return sThis;
    }

    private OkHttpClient client;

    HttpConnectionTask currentTask;

	// Daniel (2016-07-08 18:06:24): Cache info
	private boolean initCacheConfiguration = false;
	private int cacheMaxExpireTime = 60 * 60 * 24 * 7;	// 7 Days
	private int cacheSize = 300 * 1024 * 1024;	// 300 MiB

	/**
	 * Called to init Cache configuration. You should call if you want to use cache mode <br>
	 *     Notice that cache mode use interceptor for each request. Do not use interceptor if you wanna use cache when connectivity is unavailable.
	 * Set Response cache mode for configuration <br>
	 *     if you set true, you can't use {@link Interceptor} for the request
	 *     <br><br>
	 *         default cache expired date is 7 days - It only works when network connectivity is unavailable
	 * @param result <code>true</code> use response cache mode when {@link HttpRequest#isCacheResponse()} true
	 */
	public void initCacheConfiguration(boolean result){
		initCacheConfiguration(result, 300 * 1024 * 1024, 7, TimeUnit.DAYS);
	}
	/**
	 * Called only one time <br>
	 * Set Response cache mode for configuration <br>
	 *     if you set true, you can't use {@link Interceptor} for the request
	 * @param result <code>true</code> use response cache mode when {@link HttpRequest#isCacheResponse()} true
	 * @param cacheSize default is 300 * 1024 * 1024 (300 MiB)
	 * @param time
	 * @param timeUnit
	 */
	public void initCacheConfiguration(boolean result, int cacheSize, int time, TimeUnit timeUnit){
		initCacheConfiguration = result;
		this.cacheSize = cacheSize;

		setCacheMaxExpireTime(time, timeUnit);
	}

	/**
	 * It only works when Network connectivity is not available. set cache expire date.
	 * @param maxStale
	 * @param timeUnit
	 */
	private void setCacheMaxExpireTime(int maxStale, TimeUnit timeUnit) {
		if (maxStale < 0) throw new IllegalArgumentException("maxStale < 0: " + maxStale);
		long maxStaleSecondsLong = timeUnit.toSeconds(maxStale);
		this.cacheMaxExpireTime = maxStaleSecondsLong > Integer.MAX_VALUE
				? Integer.MAX_VALUE
				: (int) maxStaleSecondsLong;
	}

    /**
     * Start Async HTTP connection with {@link JsonHttpResponseHandler}
     * @param request
     * @param mListener
     * @param interceptor
     */
	@Deprecated
    public void start(HttpRequest request, JsonHttpResponseHandler mListener, Interceptor interceptor){
		start(null, request, mListener);
    }

    /**
     * Start Async HTTP connection with {@link AsyncHttpResponseHandler}
     * @param request
     * @param mListener
     * @param interceptor
     */
	@Deprecated
    public void start(HttpRequest request, AsyncHttpResponseHandler mListener, Interceptor interceptor){
		start(null, request, mListener);
    }

    /**
     * Start Async HTTP connection with {@link JsonHttpResponseHandler}
     * @param request
     * @param mListener
     */
    public void start(HttpRequest request, JsonHttpResponseHandler mListener){
		start(null, request, mListener);
    }

    /**
     * Start Async HTTP connection with {@link AsyncHttpResponseHandler}
     * @param request
     * @param mListener
     */
    public void start(HttpRequest request, AsyncHttpResponseHandler mListener){
		start(null, request, mListener);
    }

	/**
	 * Start Async HTTP connection with {@link JsonHttpResponseHandler} for Response Cache
	 * @param request
	 * @param mListener
	 */
	public void start(final Context context, final HttpRequest request, JsonHttpResponseHandler mListener){

		// Make sure that everything is perfect!
		synchronized (this) {
			if (request == null) {
				LOG.e("HttpRequest parameter is null!");
				return;
			}

			// No need to check thread, because okhttp call method already run in another Thread!!
			if (Looper.myLooper() != Looper.getMainLooper()) {
				LOG.e("Do execute this on main thread!!");
				return;
			}

			// set read timeout to 30 seconds
			if(client == null){
				client = new OkHttpClient.Builder()
						.connectTimeout(30, TimeUnit.SECONDS)
						.writeTimeout(30, TimeUnit.SECONDS)
						.readTimeout(30, TimeUnit.SECONDS)
						.build();

				if (request.isCacheResponse())
					initCacheConfiguration = true;	// initialize initCacheConfiguration
			}

			// Daniel (2016-07-08 12:04:51): is response cache available ?
			try {
				if (request.isCacheResponse() && initCacheConfiguration) {
					initCacheConfiguration = false;
					Cache cache = new Cache(context.getCacheDir(), cacheSize);

					client = client.newBuilder()
							.connectTimeout(30, TimeUnit.SECONDS)
							.writeTimeout(30, TimeUnit.SECONDS)
							.readTimeout(30, TimeUnit.SECONDS)
							.cache(cache)
							.addInterceptor(new Interceptor() {
								@Override
								public Response intercept(Chain chain) throws IOException {
									Request interRequest = chain.request();

									if (interRequest.cacheControl().noCache() || !interRequest.method().equals("GET")) {
										return chain.proceed(interRequest);
									} else {
										LOG.d("current request is cached !!");

//										// Daniel (2016-07-08 15:42:06): default time unit is SECOND
										if (NetworkUtil.isNetworkAvailable(context)) {
											interRequest = interRequest.newBuilder().header("Cache-Control", "public, max-age=" + interRequest.cacheControl().maxAgeSeconds() + ", max-stale=" + interRequest.cacheControl().maxStaleSeconds()).build();
										} else {
//											request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
											interRequest = interRequest.newBuilder().header("Cache-Control", "public, max-stale=" + cacheMaxExpireTime).build();
										}
									}
									return chain.proceed(interRequest);
								}
							})
							.build();
				}
			} catch (Exception e){
				e.printStackTrace();
			}

			// Async mode
			if(mListener != null)
				mListener.setUseSynchronousMode(false);

			if (request.getRequestType() == RequestType.Type.MULTI_PART) {
				currentTask = new MultipartTask(client, request, mListener);
			} else if (request.getRequestType() == RequestType.Type.STRING) {
				currentTask = new StringTask(client, request, mListener);
			}
		}
		currentTask.run(HttpConnectionTask.SyncType.Async);
	}

	/**
	 * Start Async HTTP connection with {@link AsyncHttpResponseHandler}
	 * @param request
	 * @param mListener
	 */
	public void start(final Context context, final HttpRequest request, AsyncHttpResponseHandler mListener){

		// Make sure that everything is perfect!
		synchronized (this) {
			if (request == null) {
				LOG.e("HttpRequest parameter is null!");
				return;
			}

			// No need to check thread, because okhttp call method already run in another Thread!!
			if (Looper.myLooper() != Looper.getMainLooper()) {
				LOG.e("Do execute this on main thread!!");
				return;
			}

			// set read timeout to 30 seconds
			if(client == null){
				client = new OkHttpClient.Builder()
						.connectTimeout(30, TimeUnit.SECONDS)
						.writeTimeout(30, TimeUnit.SECONDS)
						.readTimeout(30, TimeUnit.SECONDS)
						.build();

				if (request.isCacheResponse())
					initCacheConfiguration = true;	// initialize initCacheConfiguration
			}

			// Daniel (2016-07-08 12:04:51): is response cache available ?
			try {
				if (request.isCacheResponse() && initCacheConfiguration) {
					initCacheConfiguration = false;
					Cache cache = new Cache(context.getCacheDir(), cacheSize);

					client = client.newBuilder()
							.connectTimeout(30, TimeUnit.SECONDS)
							.writeTimeout(30, TimeUnit.SECONDS)
							.readTimeout(30, TimeUnit.SECONDS)
							.cache(cache)
							.addInterceptor(new Interceptor() {
								@Override
								public Response intercept(Chain chain) throws IOException {
									Request interRequest = chain.request();

									if (interRequest.cacheControl().noCache() || !interRequest.method().equals("GET")) {
										return chain.proceed(interRequest);
									} else {
										LOG.d("current request is cached !!");

//										// Daniel (2016-07-08 15:42:06): default time unit is SECOND
										if (NetworkUtil.isNetworkAvailable(context)) {
											interRequest = interRequest.newBuilder().header("Cache-Control", "public, max-age=" + interRequest.cacheControl().maxAgeSeconds() + ", max-stale=" + interRequest.cacheControl().maxStaleSeconds()).build();
										} else {
//											request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
											interRequest = interRequest.newBuilder().header("Cache-Control", "public, max-stale=" + cacheMaxExpireTime).build();
										}
									}
									return chain.proceed(interRequest);
								}
							})
							.build();
				}
			} catch (Exception e){
				e.printStackTrace();
			}

			// Async mode
			if(mListener != null)
				mListener.setUseSynchronousMode(false);

			if (request.getRequestType() == RequestType.Type.MULTI_PART) {
				currentTask = new MultipartTask(client, request, mListener);
			} else if (request.getRequestType() == RequestType.Type.STRING) {
				currentTask = new StringTask(client, request, mListener);
			}
		}
		currentTask.run(HttpConnectionTask.SyncType.Async);
	}
}
