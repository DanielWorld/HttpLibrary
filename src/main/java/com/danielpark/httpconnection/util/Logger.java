package com.danielpark.httpconnection.util;

/**
 * Copyright (c) 2014-2016 daniel@bapul.net
 * Created by Daniel Park on 2016-04-07.
 */

import android.util.Log;

/**
 * Created by daniel on 15. 10. 29.
 */
public class Logger {

    private static Logger sThis;

    public static final Logger getInstance() {
        if (sThis == null)
            sThis = new Logger();

        return sThis;
    }

    private final String TAG = "DANIEL";
    private boolean mLogFlag = true;

    public void v(String msg) {
        if (mLogFlag) {
            Log.v(TAG, "" + msg);
        }
    }

    public void d(String msg) {
        if (mLogFlag) {
            Log.d(TAG, "" + msg);
        }
    }

    public void e(String msg) {
        if (mLogFlag) {
            Log.e(TAG, "" + msg);
        }
    }

    public void i(String msg) {
        if (mLogFlag) {
            Log.i(TAG, "" + msg);
        }
    }

    public void w(String msg) {
        if (mLogFlag) {
            Log.w(TAG, "" + msg);
        }
    }
}
