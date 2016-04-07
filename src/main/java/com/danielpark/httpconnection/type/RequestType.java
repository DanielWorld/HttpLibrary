package com.danielpark.httpconnection.type;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class RequestType {

    public enum Type {
        TEXT, MULTI_PART
    }

    private Type requestType = Type.TEXT;

    /**
     * Set request type
     *
     * @param type
     */
    public void setRequestType(Type type) {
        if (type != null)
            this.requestType = type;
    }

    /**
     * Get request type
     *
     * @return
     */
    public Type getRequestType() {
        return requestType;
    }
}
