package com.zibilal.layouttestapp.network.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sumanto on 8/7/15.
 */
public class PersonModel implements IDataAdapter, Parcelable {

    public int Id;
    public String DisplayName;
    public String FirstName;
    public String LastName;
    public String Company;
    public String JobTitle;
    public List<String> PhoneNumber =  new ArrayList<>();
    public List<String> EmailAddress =  new ArrayList<>();

    public PersonModel() {}

    public PersonModel(int Id, String EmailAddress) {
        this.Id = Id;
        this.EmailAddress.add(EmailAddress);
    }


    @Override
    public String toString() {
        return String.format("Id: %s Firstname: %s", Id, DisplayName);
    }

    public static PersonModel getThis(IDataAdapter iDataAdapter) throws Exception {
        if (PersonModel.class.isInstance(iDataAdapter)) {
            return (PersonModel) iDataAdapter;
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
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Company);
        dest.writeString(JobTitle);
        dest.writeStringList(PhoneNumber);
        dest.writeStringList(EmailAddress);
    }

    protected PersonModel(Parcel in) {
        Id = in.readInt();
        DisplayName = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        Company = in.readString();
        PhoneNumber = in.createStringArrayList();
        EmailAddress = in.createStringArrayList();
    }

    public static final Creator<PersonModel> CREATOR = new Creator<PersonModel>() {
        public PersonModel createFromParcel(Parcel source) {
            return new PersonModel(source);
        }

        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };

    public static PersonModel parse(JSONObject jo) throws JSONException {
        PersonModel person = new ContactModel();
        person.Id = jo.optInt("Id");
        person.DisplayName = jo.optString("FirstName");
        person.DisplayName = jo.optString("Name");
        person.FirstName = jo.optString("FirstName");
        person.LastName = jo.optString("LastName");
        person.Company = jo.optString("Company");
        person.JobTitle = jo.optString("JobTitle");
        JSONArray jaPhone = jo.optJSONArray("PhoneNumbers");
        if(jaPhone != null){
            for(int i=0; i<jaPhone.length() ;i++){
                JSONObject jPhone = jaPhone.optJSONObject(i);
                String phone = jPhone.optString("Number");
                person.PhoneNumber.add(phone);
            }
        }
        JSONArray jaEmail = jo.optJSONArray("EmailAddresses");
        if(jaEmail != null){
            for(int i=0; i<jaEmail.length() ;i++){
                JSONObject jEmail = jaEmail.optJSONObject(i);
                String email = jEmail.optString("Email");
                person.EmailAddress.add(email);
            }
        }
        return person;
    }

    public static JSONObject parseToJson(IDataAdapter data) {
        PersonModel person = (PersonModel) data;
        JSONObject jo = new JSONObject();
        try {
            jo.put("Id", person.Id);
            jo.put("FirstName", person.DisplayName);
            jo.put("LastName", person.LastName);
            jo.put("Company", person.Company);
            jo.put("JobTitle", person.JobTitle);
//            jo.put("PhoneNumbers", new JSONArray(person.PhoneNumber));
//            jo.put("EmailAddresses", new JSONArray(person.EmailAddress));

            JSONArray jaPhoneNumber = new JSONArray();
            for (String phone : person.PhoneNumber){
                JSONObject jNum = new JSONObject();
                jNum.put("Number", phone);
                jaPhoneNumber.put(jNum);
            }
            jo.put("PhoneNumbers", jaPhoneNumber);

            JSONArray jaEmailAddresses  = new JSONArray();
            for (String email : person.EmailAddress){
                JSONObject jEmail = new JSONObject();
                jEmail.put("Email", email);
                jaEmailAddresses.put(jEmail);
            }
            jo.put("EmailAddresses", jaEmailAddresses);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    @Override
    public boolean equals(Object o) {
        PersonModel p = (PersonModel) o;
        return this.DisplayName.equals(p.DisplayName);
    }
}
