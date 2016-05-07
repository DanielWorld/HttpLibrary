package com.danielpark.httpconnection;

import com.danielpark.httpconnection.model.MultipartFile;
import com.danielpark.httpconnection.model.NameValue;
import com.danielpark.httpconnection.request.HttpRequest;
import com.danielpark.httpconnection.type.ContentType;
import com.danielpark.httpconnection.util.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
class MultipartTask extends HttpConnectionTask {

    private Logger LOG = Logger.getInstance();

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

        if (interceptor != null)
            client.interceptors().add(interceptor);

        if(syncType == null)
            return;

		build(syncType);
    }

    /**
     * Create POST URL
     * @return
     */
    private URL createPostURL(){
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

    MultipartBody.Builder addMultiPart(MultipartBody.Builder builder, HttpRequest httpRequest) {

        ArrayList<NameValue> parameters = httpRequest.getParameters();

        for (NameValue n : parameters) {
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"" + n.getName() + "\""),
                    RequestBody.create(MediaType.parse(ContentType.getTextPlain()), n.getValue())
            );
        }

        ArrayList<MultipartFile> files = httpRequest.getFiles();

        for (MultipartFile m : files) {
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"" + m.getParamName() + "\""
                                    + "; filename=\"" + m.getFileName() + "\""
                    ),
                    RequestBody.create(MediaType.parse(ContentType.getApplicationOctetStream()), new File(m.getFilePath()))
            );
        }

        return builder;
    }

	private void build(final SyncType syncType) {
		Request.Builder requestBuilder = new Request.Builder();

		// Parsing Headers
		ArrayList<NameValue> headers = httpRequest.getHeaders();

		for(NameValue n : headers){
			requestBuilder.header(n.getName(), n.getValue());
			// if you want to add multiple values with same name then
			// use "requestBuilder.addheader(name, value);"
		}

		switch (httpRequest.getMethod()){
			case GET:
				LOG.e("Multipart request doesn't support GET method!!");
				return;
			case PUT:
				MultipartBody.Builder multipartBody = new MultipartBody.Builder();
				multipartBody.setType(MultipartBody.FORM);

				addMultiPart(multipartBody, httpRequest);
				MultipartBody requestBody = multipartBody.build();

				// set URL
				requestBuilder.url(createPostURL());
				// set PUT body
				requestBuilder.put(requestBody);
				break;
			case POST:
				MultipartBody.Builder multipartBody1 = new MultipartBody.Builder();
				multipartBody1.setType(MultipartBody.FORM);

				addMultiPart(multipartBody1, httpRequest);
				MultipartBody requestBody1 = multipartBody1.build();

				// set URL
				requestBuilder.url(createPostURL());
				// set POST body
				requestBuilder.post(requestBody1);
				break;
			default:
				break;
		}

		if(syncType == SyncType.Async) {
			connectionAsync(requestBuilder);
		} else if (syncType == SyncType.Sync) {
			connectionSync(requestBuilder);
		}
	}


    // Daniel (2016-04-07 17:47:12): Sync Connection
    private void connectionSync(Request.Builder requestBuilder){
        final Request request = requestBuilder.build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            if(callback != null)
                callback.onResponse(call, response);
        } catch (IOException e) {
            LOG.e(e.getMessage());
        }
    }

    // Daniel (2016-04-20 18:44:31): Async connection
    private void connectionAsync(Request.Builder requestBuilder){
        final Request request = requestBuilder.build();

        Call call = client.newCall(request);
        call.enqueue(callback); // Thread-safe execution, No need to create other thread...
    }
}
