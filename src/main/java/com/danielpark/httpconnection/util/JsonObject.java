package com.danielpark.httpconnection.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * case insensitive class which extends {@link JSONObject}
 * <p>All get or put key name should be lowercase</p>
 * <br><br>
 * Copyright (C) 2014-2016 daniel@bapul.net
 * Created by Daniel on 2016-11-17.
 */

public class JsonObject extends JSONObject {

    @Override
    public Object get(String name) throws JSONException {
        return super.get(FieldNamingPolicy.translateName(name));
    }

    @Override
    public Object opt(String name) {
        return super.opt(FieldNamingPolicy.translateName(name));
    }

    @Override
    public JSONObject put(String name, boolean value) throws JSONException {
        return super.put(FieldNamingPolicy.translateName(name), value);
    }

    @Override
    public JSONObject put(String name, double value) throws JSONException {
        return super.put(FieldNamingPolicy.translateName(name), value);
    }

    @Override
    public JSONObject put(String name, int value) throws JSONException {
        return super.put(FieldNamingPolicy.translateName(name), value);
    }

    @Override
    public JSONObject put(String name, long value) throws JSONException {
        return super.put(FieldNamingPolicy.translateName(name), value);
    }

    @Override
    public JSONObject put(String name, Object value) throws JSONException {
        return super.put(FieldNamingPolicy.translateName(name), value);
    }

    @Override
    public JSONObject putOpt(String name, Object value) throws JSONException {
        return super.putOpt(FieldNamingPolicy.translateName(name), value);
    }
}
