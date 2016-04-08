package com.danielpark.httpconnection.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.danielpark.httpconnection.type.ContentType;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class MultipartFile implements Parcelable {

    private String filePath;
    private String fileName;
    private String paramName;
    private String contentType;

    public MultipartFile(final String filePath, final String fileName, final String parameterName
            , final String contentType) throws FileNotFoundException {

        if (filePath == null || "".equals(filePath)) {
            throw new IllegalArgumentException("Please specify a file path! Passed path value is: " + filePath);
        }
        File file = new File(filePath);
        if (!file.exists())
            throw new FileNotFoundException("Could not find file at path: " + filePath);

        if (parameterName == null || "".equals(parameterName)) {
            throw new IllegalArgumentException("Please specify parameterName value for file: " + filePath);
        }

        this.filePath = filePath;
        this.fileName = fileName;
        this.paramName = parameterName;

        if (contentType == null || contentType.isEmpty())
            this.contentType = ContentType.getApplicationOctetStream();
        else
            this.contentType = contentType;
    }

    protected MultipartFile(Parcel in) {
        filePath = in.readString();
        fileName = in.readString();
        paramName = in.readString();
        contentType = in.readString();

    }

    public static final Creator<MultipartFile> CREATOR = new Creator<MultipartFile>() {
        @Override
        public MultipartFile createFromParcel(Parcel in) {
            return new MultipartFile(in);
        }

        @Override
        public MultipartFile[] newArray(int size) {
            return new MultipartFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeString(fileName);
        dest.writeString(paramName);
        dest.writeString(contentType);
    }

    public String getFilePath(){
        return filePath;
    }
    public String getFileName(){
        return fileName;
    }
    public String getParamName(){
        return paramName;
    }
    public String getContentType(){
        return contentType;
    }
}
