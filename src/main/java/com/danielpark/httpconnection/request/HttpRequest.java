package com.danielpark.httpconnection.request;

import android.content.Context;

import com.danielpark.httpconnection.model.MultipartFile;
import com.danielpark.httpconnection.model.NameValue;
import com.danielpark.httpconnection.type.RequestType;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class HttpRequest extends RequestType {

    private Context context;
    private String URL;
    private ArrayList<NameValue> headers;
    private ArrayList<NameValue> parameters;
    private ArrayList<MultipartFile> files;
    private int maxRetries;
    private long connectTimeout = 5000;
    private long readTimeout = 7000;

    public enum Method {
        POST, GET, PUT, DELETE
    }

    private Method httpMethod = Method.POST;  // http type. default value is POST

    public HttpRequest(final Context ctx) {
        this.context = ctx;
        this.headers = new ArrayList<>();
        this.parameters = new ArrayList<>();
//        this.files = new ArrayList<>();
    }

    /**
     * Set url
     *
     * @param URL
     * @return
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     */
    public HttpRequest setURL(String URL) throws IllegalArgumentException, MalformedURLException {
        if(validate(URL))
            this.URL = URL;
        return this;
    }

    /**
     * Get url
     *
     * @return
     */
    public String getURL() {
        return URL;
    }

    /**
     * Validates the upload request and throws exceptions if one or more parameters are
     * not properly set.
     *
     * @throws IllegalArgumentException if request protocol or URL are not correctly set
     * @throws MalformedURLException    if the provided server URL is not valid
     */
    private boolean validate(String url) throws IllegalArgumentException, MalformedURLException {
        if (url == null || "".equals(url)) {
            throw new IllegalArgumentException("Request URL cannot be either null or empty");
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("Specify either http:// or https:// as protocol");
        }

        // Check if the URL is valid
        new URL(url);

        return true;
    }

    /**
     * Sets the HTTP method to use. By default it's set to POST
     *
     * @param method new HTTP method to use
     * @return {@link HttpRequest}
     */
    public HttpRequest setMethod(Method method) {
        if (method != null)
            httpMethod = method;
        return this;
    }

    /**
     * Get the HTTP method
     *
     * @return
     */
    public Method getMethod() {
        return httpMethod;
    }

    /**
     * Adds a header to this request
     *
     * @param headerName  header name
     * @param headerValue header value
     * @return {@link HttpRequest}
     */
    public HttpRequest addHeader(final String headerName, final String headerValue) {
        headers.add(new NameValue(headerName, headerValue));
        return this;
    }

    /**
     * Get headers
     *
     * @return
     */
    public ArrayList<NameValue> getHeaders() {
        return headers;
    }

    /**
     * Adds a parameter to this request <br>
     * Not file or other things.. but only string text
     *
     * @param paramName  parameter name
     * @param paramValue parameter value
     * @return {@link HttpRequest}
     */
    public HttpRequest addParameter(final String paramName, final String paramValue) {
        parameters.add(new NameValue(paramName, paramValue));
        return this;
    }

    public HttpRequest addParameter(final String paramName, final Object paramValue) {
        try{
            parameters.add(new NameValue(paramName, paramValue == null ? "" : String.valueOf(paramValue)));
        }catch (Exception ignored){}
        return this;
    }

    /**
     * @return Get parameters
     */
    public ArrayList<NameValue> getParameters() {
        return parameters;
    }

    /**
     * Sets the maximum number of retries that the library will do if an error occurs,
     * before returning an error
     *
     * @param maxRetries number of maximum retries on error
     * @return {@link HttpRequest}
     */
    public HttpRequest setMaxRetries(int maxRetries) {
        if (maxRetries < 1)
            this.maxRetries = 0;
        else
            this.maxRetries = maxRetries;
        return this;
    }

    /**
     * @return Get the maximum number of retries that the library will do if an error occurs,
     * before returning error
     */
    public final int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Add file to send to server
     *
     * @param filePath
     * @param fileName
     * @param paramName
     * @param contentType
     * @return
     * @throws FileNotFoundException
     */
    public HttpRequest addFile(String filePath, String fileName, String paramName, String contentType) throws FileNotFoundException {
        if (files == null)
            files = new ArrayList<>();

        files.add(new MultipartFile(filePath, fileName, paramName, contentType));
        return this;
    }

    public ArrayList<MultipartFile> getFiles() {
        if (files == null)
            files = new ArrayList<>();
        return files;
    }

    /**
     * Set connection timeout (milliseconds)
     * @param time
     * @return
     */
    public HttpRequest setConnectionTimeout(long time){
        connectTimeout = time;
        return this;
    }

    /**
     * Get connection timeout (milliseconds)
     * @return
     */
    public long getConnectTimeout(){
        return connectTimeout;
    }

    /**
     * Set read timeout (milliseconds)
     * @param time
     * @return
     */
    public HttpRequest setReadTimeout(long time){
        readTimeout = time;
        return this;
    }

    /**
     * Get read timeout (milliseconds)
     * @return
     */
    public long getReadTimeout(){
        return readTimeout;
    }

    @Override
    public void setRequestType(Type type) {
        super.setRequestType(type);
    }

    @Override
    public RequestType.Type getRequestType() {
        return super.getRequestType();
    }
}
