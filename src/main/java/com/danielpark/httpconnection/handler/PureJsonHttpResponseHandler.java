package com.danielpark.httpconnection.handler;

import org.json.JSONException;
import org.json.JSONTokener;

/**
 * JsonHttpResponseHandler which {@link com.danielpark.httpconnection.util.FieldNamingPolicy} is not applied.
 * <br><br>
 * Copyright (C) 2014-2017 daniel@bapul.net
 * Created by Daniel on 2017-02-27.
 */

public class PureJsonHttpResponseHandler extends JsonHttpResponseHandler {

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
