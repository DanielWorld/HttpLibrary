package com.danielpark.httpconnection.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Copyright (c) 2014-2015 op7773hons@gmail.com
 * Created by Daniel Park on 2015-12-19.
 */
public class UploadService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }
}
