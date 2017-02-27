package com.danielpark.httpconnection.handler;

import com.danielpark.httpconnection.network.HttpStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * JsonHttpResponseHandler which {@link com.danielpark.httpconnection.util.FieldNamingPolicy} is not applied.
 * <br><br>
 * Copyright (C) 2014-2017 daniel@bapul.net
 * Created by Daniel on 2017-02-27.
 */

public class PureJsonHttpResponseHandler extends JsonHttpResponseHandler {

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
//        super.onResponse(call, response);
        if (response.isSuccessful()) {
            if (response.code() != HttpStatus.SC_NO_CONTENT) {
                Runnable parser = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Object jsonResponse = parseResponse(response.body().bytes());
                            postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (jsonResponse == null)
                                        onSuccess(response.code(), response.headers(), "");
                                    else if (jsonResponse instanceof JSONObject)
                                        onSuccess(response.code(), response.headers(), (JSONObject) jsonResponse);
                                    else if (jsonResponse instanceof JSONArray)
                                        onSuccess(response.code(), response.headers(), (JSONArray) jsonResponse);
                                    else if (jsonResponse instanceof String)
                                        onSuccess(response.code(), response.headers(), (String) jsonResponse);
                                }
                            });
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                try {
                    if (!getUseSynchronousMode()) {
                        // proceed in Async Http connection mode
                        new Thread(parser).start();
                    } else {
                        // proceed in Sync Http connection mode
                        parser.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Runnable parser = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            postRunnable(new Runnable() {

                                @Override
                                public void run() {
                                    onSuccess(response.code(), response.headers(), "");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                // Daniel (2016-06-20 18:30:42): https://square.github.io/okhttp/3.x/okhttp/okhttp3/ResponseBody.html
                                // After calling response.body().bytes(), it automatically calls body().close()
                                response.close();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                };

                try {
                    if (!getUseSynchronousMode()) {
                        // proceed in Async Http connection mode
                        new Thread(parser).start();
                    } else {
                        // proceed in Sync Http connection mode
                        parser.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Runnable parser = new Runnable() {
                @Override
                public void run() {
                    try {
                        final Object jsonResponse = parseResponse(response.body().bytes());
                        postRunnable(new Runnable() {

                            @Override
                            public void run() {
                                if (jsonResponse == null)
                                    onFailure(response.code(), response.headers(), "");
                                else if (jsonResponse instanceof JSONObject)
                                    onFailure(response.code(), response.headers(), jsonResponse.toString());
                                else if (jsonResponse instanceof JSONArray)
                                    onFailure(response.code(), response.headers(), jsonResponse.toString());
                                else if (jsonResponse instanceof String)
                                    onFailure(response.code(), response.headers(), (String) jsonResponse);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            try {
                if (!getUseSynchronousMode()) {
                    // proceed in Async Http connection mode
                    new Thread(parser).start();
                } else {
                    // proceed in Sync Http connection mode
                    parser.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Object parseResponse(byte[] responseBody) throws JSONException {
//        return super.parseResponse(responseBody);
        if (responseBody == null)
            return null;
        Object result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If JSON is not valid this will return null
        String jsonString = getResponseString(responseBody, "UTF-8");
        if (jsonString != null) {
            jsonString = jsonString.trim();
            // Check if the string is an JSONObject style {} or JSONArray style []
            // If not we consider this as a string
            if ((jsonString.startsWith("{") && jsonString.endsWith("}"))
                    || jsonString.startsWith("[") && jsonString.endsWith("]")) {
                result = new JSONTokener(jsonString).nextValue();
//                result = new CustomJsonTokenizer(jsonString).nextValue();
            }
            // Check if this is a String "my String value" and remove quote
            // Other value type (numerical, boolean) should be without quote
            else if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                result = jsonString.substring(1, jsonString.length() - 1);
            }
        }
        if (result == null) {
            result = jsonString;
        }
        return result;
    }
}
