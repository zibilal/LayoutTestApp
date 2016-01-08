package com.zibilal.layouttestapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Bilal on 1/8/2016.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Intent detected : " + intent.getStringExtra(MyService.SERVICE_MSG), Toast.LENGTH_SHORT).show();
    }
}
