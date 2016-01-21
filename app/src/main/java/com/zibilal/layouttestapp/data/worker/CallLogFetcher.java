package com.zibilal.layouttestapp.data.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import com.google.gson.Gson;

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
public class CallLogFetcher {
    private static CallLogFetcher _instance;

    private ContentResolver mContentResolver;

    public interface Callback {
        void onError(Throwable t);
        void onUpdate(Object object);
    }

    private CallLogFetcher(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public static void init(Context context) {
        if (_instance == null) {
            _instance = new CallLogFetcher(context);
        }
    }

    public static CallLogFetcher getInstance(){
        if (_instance == null)
            throw new IllegalStateException("Please init this object first by calling CallLogFetcher.init(context)");

        return _instance;
    }

    public void fetchCallLog(Callback callback, String phoneNumber) {
        Observable<List<CallLogModel>> observable = Observable.create(subscriber -> {
            Cursor cursor = null;
            try {
                List<CallLogModel> result = new ArrayList<>();
                String whereClause = CallLog.Calls.NUMBER + "=?";
                String[] selectionArgs = {phoneNumber};
                cursor = mContentResolver.query(CallLog.Calls.CONTENT_URI, null, whereClause,
                            selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER
                        );
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        JSONObject jobj = new JSONObject();
                        String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                        String cacheNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
                        String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                        long dateTimeMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                        long durationMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                        int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                        jobj.put("name", name);
                        jobj.put("cache_number", cacheNumber);
                        jobj.put("number", number);
                        jobj.put("date_time_millis", dateTimeMillis);
                        jobj.put("duration_millis", durationMillis);
                        jobj.put("type", type);
                        Gson gson = new Gson();
                        CallLogModel model = gson.fromJson(jobj.toString(), CallLogModel.class);
                        result.add(model);
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
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callLogs -> callback.onUpdate(callLogs),
                        error -> callback.onError(error));
    }

    public void fetchCallLog(Callback callback) {
        Observable<List<CallLogModel>> observable = Observable.create(subscriber -> {
            Cursor cursor = null;
            try {
                List<CallLogModel> result = new ArrayList<>();
                cursor = mContentResolver.query(CallLog.Calls.CONTENT_URI,
                        null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        JSONObject jobj = new JSONObject();
                        String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                        String cacheNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
                        String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                        long dateTimeMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                        long durationMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                        int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                        jobj.put("name", name);
                        jobj.put("cache_number", cacheNumber);
                        jobj.put("number", number);
                        jobj.put("date_time_millis", dateTimeMillis);
                        jobj.put("duration_millis", durationMillis);
                        jobj.put("type", type);
                        Gson gson = new Gson();
                        CallLogModel model = gson.fromJson(jobj.toString(), CallLogModel.class);
                        result.add(model);
                    }
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            } catch (SecurityException e) {
                Log.e(CallLogFetcher.class.getSimpleName(), "Security exception is occured: " + e.getMessage());
                subscriber.onError(e);
            } catch (Exception e) {
                Log.e(CallLogFetcher.class.getSimpleName(), "Exception is occured: " + e.getMessage());
                subscriber.onError(e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(callLogs -> callback.onUpdate(callLogs),
                        error -> callback.onError(error));
    }
}
