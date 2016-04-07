package com.danielpark.httpconnection.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class NameValue implements Parcelable {

    private final String name;
    private final String value;

    public NameValue(final String name, final String value){
        this.name = name;
        this.value = value;
    }

    public final String getName(){
        return this.name;
    }

    public final String getValue(){
        return this.value;
    }

    protected NameValue(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    public static final Creator<NameValue> CREATOR = new Creator<NameValue>() {
        @Override
        public NameValue createFromParcel(Parcel in) {
            return new NameValue(in);
        }

        @Override
        public NameValue[] newArray(int size) {
            return new NameValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }
}
