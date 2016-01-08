package com.zibilal.layouttestapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zibilal.layouttestapp.customs.IcoMoonDrawable;
import com.zibilal.layouttestapp.service.MyService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @OnClick(R.id.home) public void onHomeClick(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab) public void onFabClick(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bubbles) public void onBubblesClick(View view) {
        Intent intent = new Intent(this, CollapsingToolbarLayoutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.pie_chart) public void onPieChartClick(View view) {
        Intent intent = new Intent(this, PieChartActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.download_page) public void onDownloadPageClick(View view) {
        Intent intent = new Intent(this, VolleyTestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.start_service) public void onStartServiceClick(View view) {
        startService(new Intent(getBaseContext(), MyService.class));
    }

    @OnClick(R.id.stop_service) public void onStopServiceClick(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    @OnClick(R.id.create_notification) public void onCreateNotificationClick(View view) {
        final int id=1;
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder =  new NotificationCompat.Builder(this)
        .setContentText("Picture Download")
                .setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent showIntent = new Intent(this, MainActivity.class);
        showIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i;
                for (i=0; i <= 100; i+=5) {
                    builder.setProgress(100, i, false);
                    notificationManager.notify(id, builder.build());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                builder.setContentText("Download complete").setProgress(0, 0, false);
                notificationManager.notify(id, builder.build());
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        IcoMoonDrawable checkDrawable = new IcoMoonDrawable(getApplicationContext(), R.string.icon_checkmark);
        checkDrawable.setDPSize(12);
        checkDrawable.setColor(Color.CYAN);
        mFab.setImageDrawable(checkDrawable);
    }
}
