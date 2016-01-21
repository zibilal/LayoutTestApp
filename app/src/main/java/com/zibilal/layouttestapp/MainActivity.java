package com.zibilal.layouttestapp;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.zibilal.layouttestapp.customs.IcoMoonDrawable;
import com.zibilal.layouttestapp.data.worker.CalendarFetcher;
import com.zibilal.layouttestapp.data.worker.CalendarModel;
import com.zibilal.layouttestapp.data.worker.CallLogFetcher;
import com.zibilal.layouttestapp.data.worker.CallLogModel;
import com.zibilal.layouttestapp.model.Item;
import com.zibilal.layouttestapp.network.retrofit.BeecastleApiManager;
import com.zibilal.layouttestapp.network.retrofit.BeecastleRestrictedApiManager;
import com.zibilal.layouttestapp.network.retrofit.SeApiManager;
import com.zibilal.layouttestapp.network.retrofit.model.Preferences;
import com.zibilal.layouttestapp.network.retrofit.model.TokenModel;
import com.zibilal.layouttestapp.service.MyService;
import com.zibilal.volley.AuthRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CALL_LOG = 111;
    private static final int REQUEST_PERMISSION_CALENDAR = 112;

    private SeApiManager mSeApiManager;
    private BeecastleApiManager mBeecastleApiManager;
    private BeecastleRestrictedApiManager mBeecastleRestrictedApiManager;

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

    @OnClick(R.id.start_test_api) public void onStartTestApi(View view) {
        startTest();
    }

    @OnClick(R.id.start_fetch_calendar) public void onStartFetchCalendar(View view) {
        boolean fetch= true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheckReadCalendars = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
            if (permissionCheckReadCalendars != PackageManager.PERMISSION_GRANTED) {
                fetch = false;
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALENDAR}, REQUEST_PERMISSION_CALENDAR );
            }
        }

        if (fetch) {
            mProgressDialog = ProgressDialog.show(this, "Fetch Calendar", "Fetching calendar", true, false);
            fetchCalendar(mCalendarCallback);
        }
    }

    @OnClick(R.id.start_fetch_callog) public void onStartFetchCallog(View view) {
        boolean fetch=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheckReadCallLog = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);

            if (permissionCheckReadCallLog != PackageManager.PERMISSION_GRANTED) {
                fetch = false;
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, REQUEST_PERMISSION_CALL_LOG);
            }
        }

        if (fetch) {
            mProgressDialog = ProgressDialog.show(this, "Fetch Call Log", "Fetching call log", true, false);
            fetchCallog();
        }
    }

    private Subscriber<List<String>> mSubs = new Subscriber<List<String>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(MainActivity.this, "Exception is occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<String> strings) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            Toast.makeText(MainActivity.this, "Result: " + strings.size(), Toast.LENGTH_SHORT).show();
            for (String s : strings) {
                Log.d(MainActivity.class.getSimpleName(), "S: " + s );
            }
            Log.d(MainActivity.class.getSimpleName(), "Fetch is finished " + new Date());
        }
    };

    private CalendarFetcher.Callback mCalendarCallback = new CalendarFetcher.Callback() {
        @Override
        public void onUpdate(Object object) {
            List<CalendarModel> strings = (List<CalendarModel>) object;
            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            Toast.makeText(MainActivity.this, "Result: " + strings.size(), Toast.LENGTH_SHORT).show();
            for (CalendarModel s : strings) {
                Log.d(MainActivity.class.getSimpleName(), "S: " + s.Name );
            }
            Log.d(MainActivity.class.getSimpleName(), "Fetch is finished " + new Date());
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(MainActivity.this, "Exception is occured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private ProgressDialog mProgressDialog;

    private void fetchCalendar(CalendarFetcher.Callback subs) {
        Log.d(MainActivity.class.getSimpleName(), "Fetch is starting " + new Date());
        CalendarFetcher.getInstance().fetchCalendars(subs);
    }

    private CallLogFetcher.Callback callLogCallback = new CallLogFetcher.Callback() {
        @Override
        public void onError(Throwable t) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            Log.e(MainActivity.class.getSimpleName(), "Exception is occured: " + t.getMessage());
        }

        @Override
        public void onUpdate(Object object) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            List<CallLogModel> logs = (List<CallLogModel>) object;
            for (CallLogModel c : logs) {
                Log.d(MainActivity.class.getSimpleName(), c.toString());
            }
        }
    };

    private void fetchCallog() {
        Log.d(MainActivity.class.getSimpleName(), "Fetch is starting " + new Date());
        CallLogFetcher.getInstance().fetchCallLog(callLogCallback, "+61277488708973648");
    }

    private void startTest() {
        try {
            JSONObject params = new JSONObject();
            params.put("GroupId", 3914);
            params.put("Start", 0);
            params.put("PageSize", 42);

            String url = "https://beecastle.flight-speed.com/api/Contact/GetAllByParentGroup";

            AuthRequest request = new AuthRequest(Request.Method.POST, url, response ->
                    Log.d(MainActivity.class.getSimpleName(), "Response " + response.toString()),
                    error -> Log.d(MainActivity.class.getSimpleName(), "Error !!! " + error.toString()) );
            request.setToken("JlPDQ7fY4ngkzqiVwGENjwNPLCLOQ6P2veM7MwtL4zk1TJIClvFoaUKSTIBy4tUQjZ51Ayz8dD0B6nERiYPDPAXe-ijqaNHyyCvIzVnIsc0WKPPUdU77T3ykIfri1oTJW5mONJCRRalE8HiurILboM2Hi71x0X3JG5UOPLU9HBrXH_osZCzCf9eNI5c8rjM8-wFx3bPdQUt557JBc6Tpnjp8YJqH5PhpxcEaa1jiI12pkw7fvIvQ6uJZEDocZr8ccfNIleCTk_dqjLtE09fE2V8nkv7VduiKwkqdwEcB0jN_ffP1ikKEMhlFWS4t4t5uIbSk7ggOFJXgXG83Sl__I0XpfVf8jN9AsiyEjFdEpeoGXYWu4HkaDxYCb9oWMJACmMzZgKaaC0PiXom8Pt_QbBV9yVXjJjafCwzqjov9WvOH_baeCaCBPaQ4Tx-gC7yF9XmXokicZWT8MxLSTZVxYIzYmr28ESkbgC9mLN59ZzXgO-xbKy9Zh25nIMkdsd3O");
            request.setParams(params.toString());
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        new Thread(() -> {
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
            }).start();
    }

    @OnClick(R.id.get_data) public void onGetDataClick(View view) {
        mSeApiManager.getMostPopularSOusers(10)
                .subscribe(users -> {
                    for (Item item : users) {
                        Log.d(MainActivity.class.getSimpleName(), "The item: " + item.getDisplayName());
                    }
                    Toast.makeText(MainActivity.this, "Users data is gathered", Toast.LENGTH_SHORT).show();
                }, error -> Toast.makeText(MainActivity.this, "error is occured: " + error.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @OnClick(R.id.login_beecastle) public void onLoginBeecastle(View view) {
        mBeecastleApiManager.login("tt101@example.com", "111111")
                .subscribe(tokenModel -> {
                    try {
                        Preferences.getInstance().setTokenObject(tokenModel);
                        mBeecastleRestrictedApiManager = new BeecastleRestrictedApiManager(tokenModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Login is successfull --> token_access: " + tokenModel.getAccessToken(), Toast.LENGTH_SHORT).show();
                    Log.d(MainActivity.class.getSimpleName(), "Access token : " + tokenModel.getAccessToken());
                }, error -> Toast.makeText(MainActivity.this, "Login is failed dute to: " + error.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @OnClick(R.id.get_group) public void onGetGroupClick(View view) {
        if (mBeecastleRestrictedApiManager != null) {
            mBeecastleRestrictedApiManager.getGroup()
                    .subscribe(list -> {
                        Toast.makeText(MainActivity.this, "The list : " + list.size(), Toast.LENGTH_SHORT).show();
                        Log.d(MainActivity.class.getSimpleName(), "Group list : " + list.size());
                    }, error -> Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(MainActivity.this, "Restricted Api Manager is missing", Toast.LENGTH_SHORT).show();
        }
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

        mSeApiManager = new SeApiManager();
        mBeecastleApiManager = new BeecastleApiManager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = false;
        switch (requestCode) {
            case REQUEST_PERMISSION_CALL_LOG:
                if (grantResults != null) {
                    for (int grant : grantResults) {
                        granted = (grant == PackageManager.PERMISSION_GRANTED);
                    }
                    if (granted) {
                        Toast.makeText(this, "Permission is granted, start fetching call log...", Toast.LENGTH_SHORT).show();
                        mProgressDialog = ProgressDialog.show(this, "Fetch Call Log", "Fetching call log", true, false);
                        fetchCallog();
                    }
                }
                break;
            case REQUEST_PERMISSION_CALENDAR:
                if (grantResults != null) {
                    for (int grant : grantResults) {
                        granted = (grant == PackageManager.PERMISSION_GRANTED);
                    }
                    if (granted) {
                        Toast.makeText(this, "Permission is granted, start fetching calendar...", Toast.LENGTH_SHORT).show();
                        mProgressDialog = ProgressDialog.show(this, "Fetch Calendar", "Fetching calendar", true, false);
                        fetchCalendar(mCalendarCallback);
                    }
                }
                break;
        }
    }
}
