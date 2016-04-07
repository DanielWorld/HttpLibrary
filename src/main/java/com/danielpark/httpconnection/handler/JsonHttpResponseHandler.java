package com.danielpark.httpconnection.handler;

import com.danielpark.httpconnection.network.HttpStatus;
import com.danielpark.httpconnection.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Copyright (c) 2014-2016 daniel@bapul.net
 * Created by Daniel Park on 2016-04-07.
 */
public class JsonHttpResponseHandler extends AsyncHttpResponseHandler implements Callback {

    private static Logger LOG = Logger.getInstance();

    public void onSuccess(int statusCode, Headers headers, String response) {

    }

    public void onSuccess(int statusCode, Headers headers, JSONObject response) {

    }

    public void onSuccess(int statusCode, Headers headers, JSONArray response) {

    }

    public void onFailure(int statusCode, Headers headers, JSONObject response) {

    }

    public void onFailure(int statusCode, Headers headers, JSONArray response) {

    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            if (response.code() != HttpStatus.SC_NO_CONTENT) {
                Runnable parser = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Object jsonResponse = parseResponse(response.body().bytes());
//                            if(mListener != null) {
                            postRunnable(new Runnable() {

                                @Override
                                public void run() {
                                    if (jsonResponse == null)
                                        onSuccess(response.code(), response.headers(), "");
                                    else if (jsonResponse instanceof JSONObject)
                                        onSuccess(response.code(), response.headers(), (JSONObject) jsonResponse);
                                    else if (jsonResponse instanceof JSONArray)
                                        onSuccess(response.code(), response.headers(), (JSONArray) jsonResponse);
                                    else if (jsonResponse instanceof String) {
                                        onSuccess(response.code(), response.headers(), (String) jsonResponse);
                                    }
                                }
//                            }
                            });
                        } catch (final JSONException ex) {
                            LOG.e(ex.getMessage());
                        } catch (IOException e) {
                            LOG.e(e.getMessage());
                        }
                    }
                };

                try {
                    if (!getUseSynchronousMode() && !getUsePoolThread()) {
                        // Async Http connection 의 경우 thread 로 실행
                        new Thread(parser).start();
                    } else {
                        // Sync Http connection 에서 proceed
                        parser.run();
                    }
                } catch (Exception e) {
                    LOG.e("Thread(parser).start() error!");
                    e.printStackTrace();

                    try {
                        // Sync Http connection 의 경우 should be run on one thread
                        parser.run();
                    } catch (Exception e1) {
                        LOG.e("parser.run() error!");
                        e1.printStackTrace();
                    }
                }

            } else {
                onSuccess(response.code(), response.headers(), new JSONObject());
            }
        } else {
            onFailure(0, null, new JSONObject());
        }

    }

    @Override
    public void onFailure(Call call, IOException e) {
        try{
            onFailure(0, null, new JSONObject());
            LOG.e(e.getMessage());
        }catch (Exception ignored){}
    }

    /**
     * Returns Object of type {@link JSONObject}, {@link JSONArray}, String, Boolean, Integer, Long,
     * Double or {@link JSONObject#NULL}, see {@link org.json.JSONTokener#nextValue()}
     *
     * @param responseBody response bytes to be assembled in String and parsed as JSON
     * @return Object parsedResponse
     * @throws org.json.JSONException exception if thrown while parsing JSON
     */
    protected Object parseResponse(byte[] responseBody) throws JSONException {
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
            }
            // Check if this is a String "my String value" and remove quote
            // Other value type (numerical, boolean) should be without quote
            else if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                result = jsonString.substring(1, jsonString.length() - 1);
            }
//            }
        }
        if (result == null) {
            result = jsonString;
        }
        return result;
    }

    /**
     * Attempts to encode response bytes as string of set encoding
     *
     * @param charset     charset to create string with
     * @param stringBytes response bytes
     * @return String of set encoding or null
     */
    public static String getResponseString(byte[] stringBytes, String charset) {
        try {
            String toReturn = (stringBytes == null) ? null : new String(stringBytes, charset);
            if (toReturn != null && toReturn.startsWith(UTF8_BOM)) {
                return toReturn.substring(1);
            }
            return toReturn;
        } catch (UnsupportedEncodingException e) {
            LOG.e("Encoding response into string failed" + e.getMessage());
            return null;
        }
    }
}
