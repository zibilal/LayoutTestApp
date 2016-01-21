package com.zibilal.layouttestapp.data.worker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bilal on 1/21/2016.
 */
public class AttendeeModel {

    @SerializedName("event_id")
    public String EventId;
    @SerializedName("attendee_name")
    public String Name;
    @SerializedName("attendee_email")
    public String Email;
    @SerializedName("attendee_relationship")
    public String Relationship;
    @SerializedName("attendee_type")
    public String Type;
    @SerializedName("attendee_status")
    public String Status;
    @SerializedName("attendee_identity")
    public String Identity;

    public AttendeeModel() {}
}
