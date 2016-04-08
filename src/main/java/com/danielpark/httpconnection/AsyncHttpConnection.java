package com.danielpark.httpconnection;

import android.content.Context;
import android.os.Looper;

import com.danielpark.httpconnection.handler.JsonHttpResponseHandler;
import com.danielpark.httpconnection.request.HttpRequest;
import com.danielpark.httpconnection.type.RequestType;
import com.danielpark.httpconnection.util.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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

    public final static synchronized AsyncHttpConnection getInstance(Context ctx) {
        if (sThis == null)
            sThis = new AsyncHttpConnection();
        return sThis;
    }

    private OkHttpClient client;

    HttpConnectionTask currentTask;

    public void start(HttpRequest request, JsonHttpResponseHandler mListener, Interceptor interceptor){

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

            // 기본 read timeout 20초로 설정
            if(client == null){
                client = new OkHttpClient.Builder()
                        .connectTimeout(20000, TimeUnit.MILLISECONDS)
                        .writeTimeout(20000, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(false)
                        .readTimeout(20000, TimeUnit.MILLISECONDS)
                        .build();
            }

            // Async 모드
            if(mListener != null)
                mListener.setUseSynchronousMode(false);

            if (request.getRequestType() == RequestType.Type.MULTI_PART) {
                currentTask = new MultipartTask(client, request,(mListener), interceptor);
            } else if (request.getRequestType() == RequestType.Type.TEXT) {
                currentTask = new StringTask(client, request, (mListener), interceptor);
            }
        }
        currentTask.run(HttpConnectionTask.SyncType.Async);
    }
}
