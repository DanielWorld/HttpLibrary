package com.danielpark.httpconnection;

import com.danielpark.httpconnection.model.MultipartFile;
import com.danielpark.httpconnection.model.NameValue;
import com.danielpark.httpconnection.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
class MultipartTask extends HttpConnectionTask {


    private OkHttpClient client;
    private HttpRequest httpRequest;
    private Callback callback;
    private Interceptor interceptor;

    MultipartTask(OkHttpClient client, HttpRequest httpRequest, Callback callback, Interceptor interceptor) {
        super(client);

        this.client = client;
        this.httpRequest = httpRequest;
        this.callback = callback;
        this.interceptor = interceptor;
    }

    @Override
    public void run(SyncType syncType) {
        super.run(syncType);

//        client.setConnectTimeout(httpRequest.getConnectTimeout(), TimeUnit.MILLISECONDS);
//        client.setReadTimeout(httpRequest.getReadTimeout(), TimeUnit.MILLISECONDS);

        if (interceptor != null)
            client.interceptors().add(interceptor);

        if(syncType == null)
            return;

        if(syncType == SyncType.Sync){
            connectionSync();
        }
        else if(syncType == SyncType.Async){
            connectionAsync();
        }
    }


//    MultipartBuilder getBuilder() {
//        return new MultipartBuilder().type(MultipartBuilder.FORM);
//    }
//
//    MultipartBuilder addMultiPart(MultipartBuilder builder, HttpRequest httpRequest) {
//
//        ArrayList<NameValue> parameters = httpRequest.getParameters();
//
//        for (NameValue n : parameters) {
//            builder.addPart(
//                    Headers.of("Content-Disposition", "form-data; name=\"" + n.getName() + "\""),
//                    RequestBody.create(null, n.getValue())
//            );
//        }
//
//        ArrayList<MultipartFile> files = httpRequest.getFiles();
//
//        for (MultipartFile m : files) {
//            builder.addPart(
//                    Headers.of("Content-Disposition", "form-data; name=\"" + m.getParamName() + "\""
//                                    + "; filename=\"" + m.getFileName() + "\""
//                    ),
//                    RequestBody.create(MediaType.parse("application/octet-stream"), new File(m.getFilePath()))
//            );
//        }
//
//        return builder;
//    }


    private void connectionSync(){

    }

    private void connectionAsync(){
//        RequestBody requestBody = addMultiPart(getBuilder(), httpRequest).build();
//
//        Request.Builder requestBuilder = new Request.Builder();
//
//        ArrayList<NameValue> headers = httpRequest.getHeaders();
//
//        for (NameValue n : headers) {
//            requestBuilder.header(n.getName(), n.getValue());
//            // if you want to add multiple values with same name then
//            // use "requestBuilder.addheader(name, value);"
//        }
//
//        requestBuilder.url(httpRequest.getURL());  // set URL
//
//        switch (httpRequest.getMethod()) {
//            case GET:
//                requestBuilder.get();
//                break;
//            case POST:
//                requestBuilder.post(requestBody);
//                break;
//            default:
//                break;
//        }
//
//        final Request request = requestBuilder.build();
//
//        Call call = client.newCall(request);
//        call.enqueue(callback); // Thread-safe execution, No need to create other thread...
    }
}
