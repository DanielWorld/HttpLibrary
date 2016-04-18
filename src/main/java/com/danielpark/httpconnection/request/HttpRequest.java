package com.danielpark.httpconnection.request;

import android.content.Context;

import com.danielpark.httpconnection.model.MultipartFile;
import com.danielpark.httpconnection.model.NameValue;
import com.danielpark.httpconnection.type.ContentType;
import com.danielpark.httpconnection.type.RequestType;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MediaType;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class HttpRequest extends RequestType {

    private String URL;

    private ArrayList<NameValue> headers;       // Daniel (2016-04-07 22:52:45): Header
    private ArrayList<NameValue> parameters;    // Daniel (2016-04-07 22:52:54): URL 뒤 파라미터들
    private String body;                        // Daniel (2016-04-07 22:53:07): 전송할 body
    private String contentType;                 // Daniel (2016-04-07 22:53:24): 전송할 body 의 content-type
    private ArrayList<MultipartFile> files;     // Daniel (2016-04-07 22:53:56): Multi-part request 에 사용되는 File

    public enum Method {
        POST, GET, PUT, DELETE
    }

    private Method httpMethod = Method.GET;  // http type. default value is POST

    public HttpRequest() {
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
     * Remove certain header by Header name
     * @param headerName
     */
    public void removeHeader(final String headerName){
        if(headers == null) return;

        for(NameValue nv : headers){
            if(nv.getName().equals(headerName)){
                headers.remove(nv);
                return;
            }
        }
    }

    /**
     * Remove certain header by Header name & Header value
     * @param headerName
     * @param headerValue
     */
    public void removeHeader(final String headerName, final String headerValue){
        if(headers == null) return;

        for(NameValue nv : headers){
            if(nv.getName().equals(headerName) && nv.getValue().equals(headerValue)){
                headers.remove(nv);
                return;
            }
        }
    }

    /**
     * Adds a parameter to this request (Not body but parameters)
     *
     * @param paramName  parameter name
     * @param paramValue parameter value
     * @return {@link HttpRequest}
     */
    public HttpRequest addParameter(final String paramName, final Object paramValue) {
        try{
            parameters.add(new NameValue(paramName, String.valueOf(paramValue)));
        }catch (Exception ignored){
        }
        return this;
    }

    /**
     * @return Get parameters
     */
    public ArrayList<NameValue> getParameters() {
        return parameters;
    }

    /**
     * Add body String to send
     * @param body
     */
    public void addBody(String body){
        this.body = body;
    }

    /**
     * Get body
     * @return
     */
    public String getBody(){
        return body;
    }

    /**
     * Set contentType;
     * @param contentType
     */
    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    /**
     * Get contentType <br>
     *     default Content-Type is application/x-www-form-urlencoded
     * @return
     */
    public String getContentType(){
        if(contentType == null)
            return ContentType.getApplicationXWwwFormUrlencoded();
        return contentType;
    }

	/**
	 * Add file to send to server
	 * @param file
	 * @param paramName
	 * @param contentType
	 * @return
	 * @throws FileNotFoundException
	 */
	public HttpRequest addFile(File file, String paramName, String contentType) throws FileNotFoundException {
		if (files == null)
			files = new ArrayList<>();

		files.add(new MultipartFile(file.getAbsolutePath(), file.getName(), paramName, contentType));
		return this;
	}

    public ArrayList<MultipartFile> getFiles() {
        if (files == null)
            files = new ArrayList<>();
        return files;
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
