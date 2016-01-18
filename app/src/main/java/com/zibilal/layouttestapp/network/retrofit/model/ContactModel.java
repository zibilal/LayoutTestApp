package com.zibilal.layouttestapp.network.retrofit.model;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sumanto on 8/7/2015.
 */

//@JsonIgnoreProperties({"IsChecked", "group"})
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactModel extends PersonModel implements Comparable<ContactModel> {
    public static final String FIREBASE_MODEL = "Contact";

    //    @JsonProperty("DeviceContactId")
    public long DeviceContactId;
    public String PersonId;
    public int GroupId;
    public String BaseGroupId;
    public long ContactPeriod = 0; //contact period in days; default is 0 day, default would be from web service
    public long Ranking;
    public Date LastContactDate;
    public long LastContactDuration;

    //    @JsonIgnore
    public boolean IsChecked = false;
//    @JsonIgnore
    public GroupModel group;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

    public ContactModel() {
//        this.PersonId = personId;
//        this.ContactPeriod = 0;
//        this.LastContactDate = new Date();
//        this.Ranking = 1;
    }

    public static ContactModel getThis(IDataAdapter iDataAdapter) throws Exception {
        if (ContactModel.class.isInstance(iDataAdapter)) {
            return (ContactModel) iDataAdapter;
        } else
            throw new Exception("This is object is not of the type ProfileModel");

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(DisplayName);
        dest.writeString(LastName);
        dest.writeString(Company);
        dest.writeString(JobTitle);
        dest.writeStringList(PhoneNumber);
        dest.writeStringList(EmailAddress);

        dest.writeLong(DeviceContactId);
        dest.writeString(PersonId);
        dest.writeInt(GroupId);
        dest.writeString(BaseGroupId);
        dest.writeLong(ContactPeriod);
        dest.writeLong(Ranking);
        dest.writeLong(LastContactDate.getTime());
        dest.writeByte((byte) (IsChecked ? 1 : 0));
        dest.writeParcelable(group, flags);
    }

    protected ContactModel(Parcel in) {
        Id = in.readInt();
        DisplayName = in.readString();
        LastName = in.readString();
        Company = in.readString();
        JobTitle = in.readString();
        PhoneNumber = in.createStringArrayList();
        EmailAddress = in.createStringArrayList();

        DeviceContactId = in.readLong();
        PersonId = in.readString();
        GroupId = in.readInt();
        BaseGroupId = in.readString();
        ContactPeriod = in.readLong();
        Ranking = in.readLong();
        LastContactDate = new Date(in.readLong());
        IsChecked = in.readByte() != 0;
        group = in.readParcelable(GroupModel.class.getClassLoader());
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        public ContactModel createFromParcel(Parcel source) {
            return new ContactModel(source);
        }

        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };

    public static ContactModel parse(JSONObject jo) throws JSONException {
        ContactModel contact = (ContactModel) PersonModel.parse(jo);
        contact.DeviceContactId = jo.optLong("DeviceContactId");
        contact.PersonId = jo.optString("PersonId");
        contact.GroupId = jo.optInt("GroupId");
        contact.ContactPeriod = jo.optLong("ContactPeriod");
        contact.Ranking = jo.optLong("Ranking");
        contact.Company = jo.optString("Company");
        contact.JobTitle = jo.optString("JobTitle");
        if (!jo.isNull("Group"))
            contact.group = GroupModel.parse(jo.optJSONObject("Group"));
        try {
            contact.LastContactDate = sdf.parse(jo.optString("LastContactDate"));
        } catch (ParseException e) {
            contact.LastContactDate = new Date();
            e.printStackTrace();
        }
        return contact;
    }

    public static JSONObject parseToJson(IDataAdapter data) {
        ContactModel contact = (ContactModel) data;
        JSONObject jo = PersonModel.parseToJson(contact);
        try {
            jo.put("DeviceContactId", contact.DeviceContactId);
            jo.put("PersonId", contact.PersonId);
            jo.put("GroupId", contact.GroupId);
            jo.put("ContactPeriod", contact.ContactPeriod);
            jo.put("Ranking", contact.Ranking);
            jo.put("Company", contact.Company);
            jo.put("JobTitle", contact.JobTitle);
            if(contact.LastContactDate!=null){
                String dateString = sdf.format(contact.LastContactDate);
                jo.put("LastContactDate", dateString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public static JSONObject parseToJsonAnswer(IDataAdapter data) {
        ContactModel contact = (ContactModel) data;
        JSONObject jo = new JSONObject();
        try {
            jo.put("ContactId", contact.Id);
            jo.put("DeviceContactId", contact.DeviceContactId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    @Override
    public int compareTo(ContactModel another) {
        if (Ranking != another.Ranking)
            return Ranking < another.Ranking ? -1 : 1;
        else
            return compareTo(another);
    }

    @Override
    public String toString() {
        return DisplayName;
    }
}
