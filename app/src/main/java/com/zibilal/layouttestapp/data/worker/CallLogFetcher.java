package com.zibilal.layouttestapp.data.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
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

    public List<CallLogModel> queryCallLog(String selectionClause, String[] selectionArgs) throws Exception {
        Uri callLogUri = CallLog.Calls.CONTENT_URI;
        List<CallLogModel> result = null;
        try {
            Cursor cursor = mContentResolver.query(callLogUri, null, selectionClause, selectionArgs, CallLog.Calls.DATE + " DESC" + " LIMIT 100");
            if (cursor != null && cursor.getCount() > 0) {
                result = new ArrayList<>();
                while (cursor.moveToNext()) {
                    JSONObject jobj = new JSONObject();
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String cacheNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long dateTimeMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    long durationMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    int id=getContactId(number);
                    jobj.put("id", id);
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

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        return result;
    }

    public int getContactId(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = mContentResolver.query(uri, new String[]{
            ContactsContract.PhoneLookup._ID}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int[] id = new int[cursor.getCount()];
            for (int i=0; i < cursor.getCount()&& cursor.moveToNext(); i++) {
                Log.d("Test", "index " + i);
                id[i] = cursor.getInt(0);
            }
            return id[0];
        }
        return 0;
    }

    public void fetchCallLogs(Callback callback) {
        Observable<List<CallLogModel>> observable = Observable.create(subscriber -> {
            try {
                subscriber.onNext(queryCallLog(null, null));
                subscriber.onCompleted();
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            }
        });

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> callback.onUpdate(list), error -> callback.onError(error));
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

    public static void main(String[] args) {
        String str = "[{\"Id\":77,\"PersonId\":1054,\"ContactId\":86530,\"DeviceContactId\":11668,\"Type\":\"Event\",\"Body\":\"Test event_history _ 1\",\"Date\":null,\"Duration\":3,\"Contact\":{\"Group\":{\"Id\":2630,\"PersonId\":1054,\"ParentId\":0,\"Name\":\"Other\",\"ContactPeriod\":5000,\"Level\":0,\"Color\":-12887656,\"ColorStr\":null},\"EmailAddresses\":[{\"Id\":76817,\"Email\":\"jaimie.tippetts@gmail.com\",\"ContactId\":86530}],\"PhoneNumbers\":[{\"Id\":35742,\"Number\":\"+61419839552789796\",\"ContactId\":86530}],\"Id\":86530,\"PersonId\":1054,\"GroupId\":2630,\"FirstName\":\"Jaimie Tippetts\",\"LastName\":null,\"Company\":\"Beecastle\",\"JobTitle\":null,\"ContactPeriod\":5000,\"LastContactDate\":\"2016-02-25 07:24:19 +07:00\",\"IsChecked\":false,\"Ranking\":4995,\"DeviceContactId\":11668,\"Classification\":false,\"Address\":null},\"Person\":{\"ContactHistories\":[],\"Answers\":[],\"Id\":1054,\"AspNetUserId\":\"6386047f-1f30-4d5a-8f60-43c2cd37b0a9\",\"FirstName\":null,\"LastName\":null,\"Company\":null,\"JobTitle\":null,\"PhoneNumber\":null,\"EmailAddress\":\"bilal.muhammad@sijasolutions.com.au\"}}]";

        Gson gson = new Gson();
    }
}
