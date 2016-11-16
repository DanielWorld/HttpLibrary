package com.danielpark.httpconnection;

import com.danielpark.httpconnection.model.NameValue;
import com.danielpark.httpconnection.request.HttpRequest;
import com.danielpark.httpconnection.type.ContentType;
import com.danielpark.httpconnection.util.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

/**
 * Copyright (c) 2014-2015 daniel@bapul.net
 * Created by Daniel Park on 2015-12-22.
 */
public class StringTask extends HttpConnectionTask {

    private Logger LOG = Logger.getInstance();

    private OkHttpClient client;
    private HttpRequest httpRequest;
    private Callback callback;

    StringTask(OkHttpClient client, HttpRequest httpRequest, Callback callback) {
        super(client);

        this.client = client;
        this.httpRequest = httpRequest;
        this.callback = callback;
    }

    @Override
    public void run(SyncType syncType) {
        super.run(syncType);

        if (syncType == null)
            return;

        build(syncType);
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
//            builder.setQueryParameter(n.getName(), n.getValue());
            builder.addQueryParameter(n.getName(), n.getValue());
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

    private void build(final SyncType syncType) {
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
//                requestBuilder.url(createPostURL());
                requestBuilder.url(createGetURL());
                // set DELETE method
                requestBuilder.delete(createBody(httpRequest));
                break;
            default:
                break;
        }

        if (syncType == SyncType.Async) {
            connectionAsync(requestBuilder);
        } else if (syncType == SyncType.Sync) {
            connectionSync(requestBuilder);
        }
    }

    // Daniel (2016-04-07 12:04:10): Async connection
    private void connectionAsync(Request.Builder requestBuilder) {
        final Request request = requestBuilder.build();

        Call call = client.newCall(request);
        call.enqueue(callback); // Thread-safe execution, No need to create other thread...
    }

    // Daniel (2016-04-07 12:03:34): Sync connection
    private void connectionSync(Request.Builder requestBuilder) {
        final Request request = requestBuilder.build();

        Call call = client.newCall(request);
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
     *
     * @param request
     */
    private RequestBody createBody(HttpRequest request) {
        if (request == null)
            return null;

        // Content-Type is not NULL
        if (request.getContentType() != null) {
            // application/x-www-form-urlencoded
            if (request.getContentType().equals(ContentType.getApplicationXWwwFormUrlencoded())) {
                FormBody.Builder builder = new FormBody.Builder();

                for (NameValue nv : request.getParameters()) {
                    builder.add(nv.getName(), nv.getValue());
                }
                return builder.build();
            }
            // application/json
            else if (request.getContentType().equals(ContentType.getApplicationJson())) {
                return RequestBody.create(MediaType.parse(request.getContentType()), request.getBody());
            }
            // Content-Type isn't NULL
            else {
                if (request.getBody() != null && !request.getBody().trim().isEmpty())   // If body exists
                    return RequestBody.create(MediaType.parse(request.getContentType()), request.getBody());
            }
        }

        // Content-Type is NULL
        if (request.getBody() == null || request.getBody().trim().isEmpty())
            return RequestBody.create(null, new byte[0]);
        else {
            return RequestBody.create(null, request.getBody());
        }
    }
}
