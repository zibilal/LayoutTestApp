package com.zibilal.layouttestapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Bilal on 1/7/2016.
 */
public class HelloService extends Service {

    // indicates how to behave if the service is killed
    private int mStartMode;
    // interface for clients
    private IBinder mBinder;
    // indicates whether onRebind should be used
    private boolean mAllowRebind;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // The service is starting, due to a call to startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return mStartMode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
