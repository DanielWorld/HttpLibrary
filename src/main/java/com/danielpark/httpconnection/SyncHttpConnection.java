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
 * Sync http protocol connection (https://github.com/square/okhttp)
 * <br><br>
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class SyncHttpConnection {

    private Logger LOG = Logger.getInstance();

    private static SyncHttpConnection sThis;

    private SyncHttpConnection(){}

    public final static synchronized SyncHttpConnection getInstance(Context ctx) {
        if (sThis == null)
            sThis = new SyncHttpConnection();
        return sThis;
    }

    private OkHttpClient client;

    HttpConnectionTask currentTask;

    /**
     * Start HTTP connection
     * <br>
     * Because we already took care of Thread issue.
     *
     * @param request
     * @throws Exception
     */
    public void start(HttpRequest request, JsonHttpResponseHandler mListener, Interceptor interceptor){

        // Make sure that everything is perfect!
        synchronized (this) {
            if (request == null) {
                LOG.e("HttpRequest parameter is null!");
                return;
            }

            // No need to check thread, because okhttp call method already run in another Thread!!
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                LOG.e("Do not execute this on main thread!!");
//                return;
//            }

            // 기본 read timeout 20초로 설정
            if(client == null){
                client = new OkHttpClient.Builder()
                        .connectTimeout(20000, TimeUnit.MILLISECONDS)
                        .writeTimeout(20000, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(false)
                        .readTimeout(20000, TimeUnit.MILLISECONDS)
                        .build();
            }

            if(mListener != null){
                mListener.setUseSynchronousMode(true);
            }

            if (request.getRequestType() == RequestType.Type.MULTI_PART) {
                currentTask = new MultipartTask(client, request, mListener, interceptor);
            } else if (request.getRequestType() == RequestType.Type.TEXT) {
                currentTask = new StringTask(client, request, mListener, interceptor);
            }
        }
        currentTask.run(HttpConnectionTask.SyncType.Sync);
    }

    /**
     * Restart HTTP connection using OkHttp Request
     *
     * @param request
     * @param callback
     */
    public void reStart(Request request, Callback callback) throws Exception {
        if (request == null)
            throw new Exception("Request is null!!");

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

}
