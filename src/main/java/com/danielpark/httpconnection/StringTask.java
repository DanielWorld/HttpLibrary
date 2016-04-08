package com.danielpark.httpconnection;

import com.danielpark.httpconnection.model.NameValue;
import com.danielpark.httpconnection.request.HttpRequest;
import com.danielpark.httpconnection.type.ContentType;
import com.danielpark.httpconnection.util.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Copyright (c) 2014-2015 daniel@bapul.net
 * Created by Daniel Park on 2015-12-22.
 */
public class StringTask extends HttpConnectionTask {

    private Logger LOG = Logger.getInstance();

    private OkHttpClient client;
    private HttpRequest httpRequest;
    private Callback callback;
    private Interceptor interceptor;

    StringTask(OkHttpClient client, HttpRequest httpRequest, Callback callback, Interceptor interceptor) {
        super(client);

        this.client = client;
        this.httpRequest = httpRequest;
        this.callback = callback;
        this.interceptor = interceptor;
    }

    @Override
    public void run(SyncType syncType) {
        super.run(syncType);

        if (interceptor != null)
            client.interceptors().add(interceptor);

        if (syncType == null)
            return;

        if (syncType == SyncType.Sync) {
            connectionSync();
        } else if (syncType == SyncType.Async) {
            connectionAsync();
        }


    }

    /**
     * Create URL with query parameters <br> Support only GET method
     *
     * @return URL with query parameters for GET method
     */
    private URL createGetURL() {
        /*
         * Example of create perfect URL
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("www.google.com")
                .addPathSegment("search")
                .addQueryParameter("q", "polar bears")
                .build();
                */

        // Let's create URL with query parameters ~~!!
        ArrayList<NameValue> parameters = httpRequest.getParameters();

        HttpUrl detailURL = HttpUrl.parse(httpRequest.getURL());
        HttpUrl.Builder builder = detailURL.newBuilder();

        for (NameValue n : parameters) {
            builder.setQueryParameter(n.getName(), n.getValue());
        }

        detailURL = builder.build();
        return detailURL.url();
    }

    /**
     * Create POST URL
     *
     * @return
     */
    private URL createPostURL() {
          /*
         * Example of create perfect URL
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("www.google.com")
                .addPathSegment("search")
                .addQueryParameter("q", "polar bears")
                .build();
                */

        // Let's create URL with query parameters ~~!!
//        ArrayList<NameValue> parameters = httpRequest.getParameters();

        HttpUrl detailURL = HttpUrl.parse(httpRequest.getURL());
        HttpUrl.Builder builder = detailURL.newBuilder();

//        for(NameValue n : parameters){
//            builder.setQueryParameter(n.getName(), n.getValue());
//        }

        detailURL = builder.build();
        return detailURL.url();
    }

    // Daniel (2016-04-07 12:04:10): Async 형태로 연결
    private void connectionAsync() {
        Request.Builder requestBuilder = new Request.Builder();

        // Parsing Headers
        ArrayList<NameValue> headers = httpRequest.getHeaders();

        for (NameValue n : headers) {
            requestBuilder.header(n.getName(), n.getValue());
            // if you want to add multiple values with same name then
            // use "requestBuilder.addheader(name, value);"
        }

        switch (httpRequest.getMethod()) {
            case GET:
                // set URL
                requestBuilder.url(createGetURL());
                // set GET method
                requestBuilder.get();
                break;
            case POST:
                // set URL
                requestBuilder.url(createPostURL());
                // set POST method
                requestBuilder.post(RequestBody.create(MediaType.parse(httpRequest.getContentType()), httpRequest.getBody()));
                break;
            case PUT:
                // set URL
                requestBuilder.url(createPostURL());
                // set POST method
                requestBuilder.put(RequestBody.create(MediaType.parse(httpRequest.getContentType()), httpRequest.getBody()));
                break;
            case DELETE:
                // set URL
                requestBuilder.url(createPostURL());
                // set DELETE method
                if (httpRequest.getBody() == null || httpRequest.getBody().isEmpty())
                    requestBuilder.delete();
                else
                    requestBuilder.delete(RequestBody.create(MediaType.parse(httpRequest.getContentType()), httpRequest.getBody()));
                break;
            default:
                break;
        }

        final Request request = requestBuilder.build();

        Call call = client.newCall(request);
        call.enqueue(callback); // Thread-safe execution, No need to create other thread...
    }

    // Daniel (2016-04-07 12:03:34): Sync 형태로 연결
    private void connectionSync() {
        Request.Builder requestBuilder = new Request.Builder();

        // Parsing Headers
        ArrayList<NameValue> headers = httpRequest.getHeaders();

        for (NameValue n : headers) {
            requestBuilder.header(n.getName(), n.getValue());
            // if you want to add multiple values with same name then
            // use "requestBuilder.addheader(name, value);"
        }

        switch (httpRequest.getMethod()) {
            case GET:
                // set URL
                requestBuilder.url(createGetURL());
                // set GET method
                requestBuilder.get();
                break;
            case POST:
                // set URL
                requestBuilder.url(createPostURL());
                // set POST method
                requestBuilder.post(createBody(httpRequest));
                break;
            case PUT:
                // set URL
                requestBuilder.url(createPostURL());
                // set POST method
                requestBuilder.put(createBody(httpRequest));
                break;
            case DELETE:
                // set URL
                requestBuilder.url(createPostURL());
                // set DELETE method
                requestBuilder.delete(createBody(httpRequest));
                break;
            default:
                break;
        }

        final Request request = requestBuilder.build();

        Call call = client.newCall(request);
//        call.enqueue(callback); // Thread-safe execution, No need to create other thread...
        try {
            Response response = call.execute();

            if (callback != null)
                callback.onResponse(call, response);
        } catch (IOException e) {
            LOG.e(e.getMessage());
        }

    }

    /**
     * Create body according to Content-Type
     * @param request
     */
    private RequestBody createBody(HttpRequest request){
        if(request == null)
            return RequestBody.create(null, "");

        if(request.getContentType().equals(ContentType.getApplicationJson())){
            // application/json
            return RequestBody.create(MediaType.parse(httpRequest.getContentType()), httpRequest.getBody());
        }
        else if(request.getContentType().equals(ContentType.getApplicationXWwwFormUrlencoded())){
            // application/x-www-form-urlencoded
            FormBody.Builder builder = new FormBody.Builder();

            for(NameValue nv : httpRequest.getParameters()){
                builder.add(nv.getName(), nv.getValue());
            }
            RequestBody requestBody = builder.build();

            return requestBody;
        }
        else{
            // etc
            return RequestBody.create(MediaType.parse(httpRequest.getContentType()), httpRequest.getBody());
        }
    }
}
