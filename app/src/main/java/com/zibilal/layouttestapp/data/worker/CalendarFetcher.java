package com.zibilal.layouttestapp.data.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal on 1/14/2016.
 */
public class CalendarFetcher {

    private static CalendarFetcher _instance;
    private ContentResolver mContentResolver;

    private CalendarFetcher(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public static void init(Context context) {
        if (_instance == null) {
            _instance = new CalendarFetcher(context);
        }
    }

    public static CalendarFetcher getInstance() {
        if (_instance == null)
            throw new IllegalStateException("Uninitialized ...");
        return _instance;
    }

    public void fetchCalendars(Subscriber<List<String>> subs) {
        Observable<List<String>> observable = Observable.create(subscriber -> {
            Cursor cursor = null;
            try {
                String[] projection = new String[] {
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE
                };
                cursor = mContentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                        projection, CalendarContract.Calendars.VISIBLE + " = 1",
                        null, CalendarContract.Calendars._ID + " ASC");
                List<String> result = new ArrayList<>();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars._ID));
                        String name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME));
                        String accountName = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));
                        int accountType = cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE));
                        JSONObject jobj = new JSONObject();
                        jobj.put("id", id);
                        jobj.put("name", name);
                        jobj.put("account_name", accountName);
                        jobj.put("account_type", accountType);
                        result.add(jobj.toString());
                    }
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (SecurityException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subs);
    }
}
