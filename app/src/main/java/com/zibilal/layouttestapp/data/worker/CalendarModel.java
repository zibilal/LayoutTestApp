package com.zibilal.layouttestapp.data.worker;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bilal on 1/21/2016.
 */
public class CalendarModel {
    @SerializedName("id")
    public String Id;
    @SerializedName("name")
    public String Name;
    @SerializedName("account_name")
    public String AccountName;
    @SerializedName("account_type")
    public String AccountType;
    @SerializedName("events")
    public List<EventModel> Events = new ArrayList<>();
    public CalendarModel() {}
}
