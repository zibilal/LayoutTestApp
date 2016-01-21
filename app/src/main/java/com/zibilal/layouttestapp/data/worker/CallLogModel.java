package com.zibilal.layouttestapp.data.worker;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bilal on 1/21/2016.
 */
public class CallLogModel {
    @SerializedName("name")
    public String Name;
    @SerializedName("number")
    public String Number;
    @SerializedName("date_time_millis")
    public long DateTimeMillis;
    @SerializedName("duration_millis")
    public String DurationMillis;
    @SerializedName("type")
    public String Type;

    public CallLogModel(){}

    @Override
    public String toString() {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("Name", Name);
            jobj.put("Number", Number);
            jobj.put("DateTimeMillis", DateTimeMillis);
            jobj.put("DurationMillis", DurationMillis);
            jobj.put("Type", Type);
            return jobj.toString();
        } catch (JSONException e) {

        }
        return "";
    }
}
