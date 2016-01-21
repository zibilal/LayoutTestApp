package com.zibilal.layouttestapp.data.worker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bilal on 1/21/2016.
 */
public class EventModel {
    @SerializedName("event_location")
    public String Location;
    @SerializedName("title")
    public String Title;
    @SerializedName("description")
    public String Description;
    @SerializedName("duration")
    public String Duration;
    @SerializedName("dtstart")
    public String DtStart;
    @SerializedName("dtend")
    public String DtEnd;
    @SerializedName("attendees")
    public List<AttendeeModel> Attendees;
    public EventModel() {}
}
