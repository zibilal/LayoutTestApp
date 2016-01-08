package com.zibilal.layouttestapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.zibilal.layouttestapp.MainActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.schedulers.Schedulers;

public class MyService extends Service {

    public static final String SERVICE_MSG = "service message";
    public static final String SERVICE_FINISHED = "finished";
    public static final String SERVICE_ALREADY_RUN = "already run";

    public static final String MY_SERVICE_ACTION = "com.zibilal.layouttestapp.MY_SERVICE_ACTION";

    private MyWorker mMyWorker;

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d(MyService.class.getSimpleName(), "On create is called");
        super.onCreate();
        mMyWorker = new MyWorker(getBaseContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MyService.class.getSimpleName(), "On start command is called");
        Toast.makeText(this, "MyService is started", Toast.LENGTH_SHORT).show();
        if (!mMyWorker.isRunning()) {
            mMyWorker.fetch();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MyService.class.getSimpleName(), "On destroy is called");
        Toast.makeText(this, "MyService is destroyed", Toast.LENGTH_SHORT).show();

        if (mMyWorker.isRunning()) {
            mMyWorker.stop();
        }
    }

    public static class MyWorker {
        private AtomicBoolean mRunning = new AtomicBoolean(false);
        private Context mContext;

        public MyWorker(Context context) {
            mContext = context;
        }

        public boolean isRunning() {
            return mRunning.get();
        }

        public void stop() {
            mRunning.set(false);
            Intent i = new Intent();
            i.setAction(MY_SERVICE_ACTION);
            i.putExtra(SERVICE_MSG, SERVICE_FINISHED);
            mContext.sendBroadcast(i);
        }

        public void fetch() {
            Schedulers.io().createWorker().schedule(() -> {

                if (mRunning.compareAndSet(false, true)) {
                    final int id = 2;
                    final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                            .setContentText("Picture Download")
                            .setSmallIcon(android.R.drawable.ic_dialog_alert);
                    Intent showIntent = new Intent(mContext, MainActivity.class);
                    showIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, showIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);
                    builder.setProgress(0, 0, true);
                    notificationManager.notify(id, builder.build());
                    while (mRunning.get()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                        Log.d(MyWorker.class.getSimpleName(), "Fetch data still running -->");
                    }
                    builder.setContentText("Process complete").setProgress(0, 0, false);
                    notificationManager.notify(id, builder.build());
                } else {
                    Intent i = new Intent();
                    i.setAction(MY_SERVICE_ACTION);
                    i.putExtra(SERVICE_MSG, SERVICE_ALREADY_RUN);
                    mContext.sendBroadcast(i);
                }
            });
        }
    }
}
