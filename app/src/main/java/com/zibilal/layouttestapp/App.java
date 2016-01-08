package com.zibilal.layouttestapp;

import android.app.Application;
import android.content.Intent;

import com.zibilal.layouttestapp.service.MyService;

/**
 * Created by Bilal on 1/8/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Starting service
        startService(new Intent(getBaseContext(), MyService.class));
    }
}
