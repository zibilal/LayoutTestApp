package com.zibilal.layouttestapp.data.worker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
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

    public interface Callback {
        void onUpdate(Object object);
        void onError(Throwable t);
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

    private JSONArray fetchEventAttendees(String eventId) throws Exception {
        Cursor cursorAttendees = null;
        JSONArray arryAttendees = null;

        try {
            String selection = CalendarContract.Attendees.EVENT_ID +"=?";
            String[] selectionArgs = {eventId};
            cursorAttendees = mContentResolver.query(CalendarContract.Attendees.CONTENT_URI, null, selection, selectionArgs, null);
            if (cursorAttendees != null) {
                arryAttendees = new JSONArray();
                while(cursorAttendees.moveToNext()) {
                    JSONObject temp = new JSONObject();
                    temp.put("event_id", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.EVENT_ID)));
                    temp.put("attendee_name", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.ATTENDEE_NAME)));
                    temp.put("attendee_email", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.ATTENDEE_EMAIL)));
                    temp.put("attendee_relationship", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP)));
                    temp.put("attendee_type", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.ATTENDEE_TYPE)));
                    temp.put("attendee_status", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.ATTENDEE_STATUS)));
                    temp.put("attendee_identitiy", cursorAttendees.getString(cursorAttendees.getColumnIndex(CalendarContract.Attendees.ATTENDEE_IDENTITY)));
                    arryAttendees.put(temp);
                }
            }
        } catch (SecurityException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (cursorAttendees != null)
                cursorAttendees.close();
        }

        return arryAttendees;
    }

    private JSONArray fetchEvents(String calendarId) throws Exception{
        Cursor cursorEvents = null;
        JSONArray arryEvents = null;
        try {
            String selection = CalendarContract.Events.CALENDAR_ID +"=?";
            String[] selectionArgs = {calendarId};
            cursorEvents = mContentResolver.query(CalendarContract.Events.CONTENT_URI, null, selection, selectionArgs, null );

            if (cursorEvents != null) {
                arryEvents = new JSONArray();
                while (cursorEvents.moveToNext()) {
                    JSONObject temp = new JSONObject();
                    try {
                        String eventId = cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events._ID));
                        temp.put("event_location", cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)));
                        temp.put("title", cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events.TITLE)));
                        temp.put("description", cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events.DESCRIPTION)));
                        temp.put("duration", cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events.DURATION)));
                        temp.put("dtstart", cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events.DTSTART)));
                        temp.put("dtend", cursorEvents.getString(cursorEvents.getColumnIndex(CalendarContract.Events.DTEND)));
                        JSONArray attendees = fetchEventAttendees(eventId);
                        if (attendees != null && attendees.length() > 0) {
                            temp.put("attendees", attendees);
                        }

                    } catch (JSONException e) {}

                    arryEvents.put(temp);
                }
            }
        } catch (SecurityException e) {
            throw new Exception(e.getMessage());
        } finally {
            if (cursorEvents != null)
                cursorEvents.close();
        }
        return arryEvents;
    }

    public void fetchCalendars(Callback callback) {
        Observable<List<CalendarModel>> observable = Observable.create(subscriber -> {
            Cursor cursorCalendar = null;
            try {
                String[] projection = new String[] {
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE
                };
                cursorCalendar = mContentResolver.query(CalendarContract.Calendars.CONTENT_URI,
                        projection, CalendarContract.Calendars.VISIBLE + " = 1",
                        null, CalendarContract.Calendars._ID + " ASC");
                List<CalendarModel> result = new ArrayList<>();
                if (cursorCalendar != null) {
                    while (cursorCalendar.moveToNext()) {
                        String id = cursorCalendar.getString(cursorCalendar.getColumnIndex(CalendarContract.Calendars._ID));
                        String name = cursorCalendar.getString(cursorCalendar.getColumnIndex(CalendarContract.Calendars.NAME));
                        String accountName = cursorCalendar.getString(cursorCalendar.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));
                        int accountType = cursorCalendar.getInt(cursorCalendar.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE));
                        JSONObject jobj = new JSONObject();
                        jobj.put("id", id);
                        jobj.put("name", name);
                        jobj.put("account_name", accountName);
                        jobj.put("account_type", accountType);
                        JSONArray events = fetchEvents(id);
                        if (events != null && events.length() > 0) {
                            jobj.put("events", events);
                        }
                        Gson gson = new Gson();
                        CalendarModel model = gson.fromJson(jobj.toString(), CalendarModel.class);
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
                if (cursorCalendar != null)
                    cursorCalendar.close();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendars -> callback.onUpdate(calendars),
                        error -> callback.onError(error));
    }
}
