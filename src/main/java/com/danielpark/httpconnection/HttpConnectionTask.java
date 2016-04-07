package com.danielpark.httpconnection;


import okhttp3.OkHttpClient;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
abstract class HttpConnectionTask {

    private OkHttpClient client;

    HttpConnectionTask(OkHttpClient client){
        this.client = client;
    }

    enum SyncType{
        Async, Sync
    }

    public void run(SyncType syncType){

    }
}
