package com.danielpark.httpconnection.handler;

import com.danielpark.httpconnection.network.HttpStatus;
import com.danielpark.httpconnection.util.CustomJsonTokenizer;
import com.danielpark.httpconnection.util.JsonObject;
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
import okhttp3.ResponseBody;

/**
 * Copyright (c) 2014-2016 daniel@bapul.net
 * Created by Daniel Park on 2016-04-07.
 */
public class JsonHttpResponseHandler extends AsyncHttpResponseHandler {

    public void onSuccess(int statusCode, Headers headers, JSONObject response) {
        // Daniel (2016-04-08 15:16:21): convert response body to JSONObject and then return it
    }

    public void onSuccess(int statusCode, Headers headers, JSONArray response) {
        // Daniel (2016-04-08 15:17:00): convert response body to JSONArray and then return it
    }

	@Override
    public void onSuccess(int statusCode, Headers headers, String response) {
        // Daniel (2016-04-08 15:17:54): None of JSONObject and JSONArray should return string format
    }

	@Override
	public void onFailure(int statusCode, Headers headers, String response) {

	}

	@Override
    public void onResponse(final Call call, final Response response) throws IOException {
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
                                    else if (jsonResponse instanceof JsonObject)
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
                                else if (jsonResponse instanceof JsonObject)
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
    public void onFailure(Call call, IOException io) {
        Runnable parser = new Runnable() {
            @Override
            public void run() {
                try {
                    postRunnable(new Runnable() {

                        @Override
                        public void run() {
                            onFailure(0, new Headers.Builder().build(), "");
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

    /**
     * Returns Object of type {@link JSONObject}, {@link JSONArray}, String, Boolean, Integer, Long,
     * Double or {@link JSONObject#NULL}, see {@link org.json.JSONTokener#nextValue()}
     *
     * @param responseBody response bytes to be assembled in String and parsed as JSON
     * @return Object parsedResponse
     * @throws org.json.JSONException exception if thrown while parsing JSON
     */
    private Object parseResponse(byte[] responseBody) throws JSONException {
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
//                result = new JSONTokener(jsonString).nextValue();
                result = new CustomJsonTokenizer(jsonString).nextValue();
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

    /**
     * Attempts to encode response bytes as string of set encoding
     *
     * @param charset     charset to create string with
     * @param stringBytes response bytes
     * @return String of set encoding or null
     */
    private String getResponseString(byte[] stringBytes, String charset) {
        try {
            String toReturn = (stringBytes == null) ? null : new String(stringBytes, charset);
            if (toReturn != null && toReturn.startsWith(UTF8_BOM)) {
                return toReturn.substring(1);
            }
            return toReturn;
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
            return null;
        }
    }
}
