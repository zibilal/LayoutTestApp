package com.zibilal.layouttestapp.data.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal on 1/22/2016.
 */
public class ContactsFetcher {

    private static ContactsFetcher _instance;
    private ContentResolver mContentResolver;

    public interface Callback {
        void onError(Throwable t);
        void onUpgrade(Object object);
    }

    private ContactsFetcher(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public static void init(Context context) {
        if (_instance == null)
            _instance = new ContactsFetcher(context);
    }

    public static ContactsFetcher getInstance() {
        if (_instance == null)
            throw new IllegalStateException("Please init this object first by calling ContactsFetcher.init(context)");

        return _instance;
    }

    public void fetchContacts(Callback callback) {
        Observable<List<String>> observable = Observable.create(subscriber -> {
            Cursor cursor = null;
            try {
                String[] projections = {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.PHOTO_URI
                };
                cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, projections, null, null, null);
                List<String> strings = new ArrayList<>();

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        try {
                            JSONObject jobj = new JSONObject();
                            String id =  cursor.getString(cursor.getColumnIndex(projections[0]));
                            jobj.put("contact_id", id);
                            jobj.put("display_name", cursor.getString(cursor.getColumnIndex(projections[1])));
                            jobj.put("photo_url", cursor.getString(cursor.getColumnIndex(projections[2])));
                            //jobj.put("companies", getOrganizations(id));
                            //jobj.put("phones", getPhones(id));
                            //jobj.put("emails", getEmails(id));
                            Log.d(ContactsFetcher.class.getSimpleName(), "[" + id + "]" + jobj.toString());
                            strings.add(jobj.toString());
                        } catch (JSONException e) {
                            Log.e(ContactsFetcher.class.getSimpleName(), "Exception is occured: " + e.getMessage());
                        }
                    }
                }
                subscriber.onNext(strings);
                subscriber.onCompleted();
            } catch (Exception e) {
                Log.e(ContactsFetcher.class.getSimpleName(), "Exception: " + e.getMessage());
                subscriber.onError(e);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> callback.onUpgrade(contacts),
                        error-> callback.onError(error));
    }

    public JSONArray getOrganizations(String id) {
        Cursor cursor = null;
        JSONArray arry = null;
        try {
            String[] projections = {
                    ContactsContract.CommonDataKinds.Organization.TITLE,
                    ContactsContract.CommonDataKinds.Organization.COMPANY
            };
            String selection = ContactsContract.CommonDataKinds.Organization.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "=?";
            String[] selectionArgs = {id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            cursor = mContentResolver.query(ContactsContract.Data.CONTENT_URI, projections, selection, selectionArgs, null);
            if (cursor != null && cursor.getCount() > 0) {
                arry = new JSONArray();
                while (cursor.moveToNext()) {
                    try {
                        JSONObject jobj = new JSONObject();
                        jobj.put("job_title", cursor.getString(cursor.getColumnIndex(projections[0])));
                        jobj.put("company", cursor.getString(cursor.getColumnIndex(projections[1])));
                        arry.put(jobj);
                    } catch (JSONException e) {
                        Log.e(ContactsFetcher.class.getSimpleName(), "Exception is occured: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return arry;
    }

    public JSONArray getPhones(String id) {
        Cursor cursor = null;
        JSONArray arry = null;
        try {
            String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
            String[] selectionArgs = {id};
            String[] projections = {
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.LABEL
            };
            cursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projections, selection, selectionArgs, null);

            if (cursor != null && cursor.getCount() > 0) {
                arry = new JSONArray();
                while (cursor.moveToNext()) {
                    try {
                        JSONObject jobj = new JSONObject();
                        jobj.put("phone_label", cursor.getString(cursor.getColumnIndex(projections[2])));
                        jobj.put("phone_type", cursor.getString(cursor.getColumnIndex(projections[1])));
                        jobj.put("phone_number", cursor.getString(cursor.getColumnIndex(projections[0])));
                        arry.put(jobj);
                    } catch (JSONException e) {
                        Log.d(ContactsFetcher.class.getSimpleName(), "Exception is occured: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor!=null)
                cursor.close();
        }
        return arry;
    }

    public JSONArray getEmails(String id) {
        Cursor cursor = null;
        JSONArray arry = null;

        try {
            String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
            String[] selectionArgs = {id};
            String[] projections = {
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.TYPE,
                    ContactsContract.CommonDataKinds.Email.LABEL
            };
            cursor = mContentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projections,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.getCount() > 0) {
                arry = new JSONArray();
                while (cursor.moveToNext()) {
                    try {
                        JSONObject jobj = new JSONObject();
                        jobj.put("email_label", cursor.getString(cursor.getColumnIndex(projections[2])));
                        jobj.put("email_address", cursor.getString(cursor.getColumnIndex(projections[0])));
                        jobj.put("email_type", cursor.getString(cursor.getColumnIndex(projections[1])));
                        arry.put(jobj);
                    } catch (JSONException e) { }
                }
            }
        } catch (Exception e) {
            Log.e(ContactsFetcher.class.getSimpleName(), "Exception: " + e.getMessage());
        } finally {
            if (cursor!=null)
                cursor.close();
        }
        return arry;
    }
}
