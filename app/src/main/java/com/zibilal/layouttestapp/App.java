package com.zibilal.layouttestapp;

import android.app.Application;

import com.zibilal.layouttestapp.data.worker.CalendarFetcher;
import com.zibilal.layouttestapp.data.worker.CallLogFetcher;
import com.zibilal.layouttestapp.data.worker.ContactsFetcher;
import com.zibilal.layouttestapp.network.retrofit.model.Preferences;

/**
 * Created by Bilal on 1/8/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CallLogFetcher.init(getBaseContext());
        CalendarFetcher.init(getBaseContext());
        ContactsFetcher.init(getBaseContext());
        Preferences.init(getBaseContext());
    }
}
