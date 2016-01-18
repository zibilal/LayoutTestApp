package com.zibilal.layouttestapp.network.retrofit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sumanto on 8/10/15.
 */
public class GroupModel extends AbstractDataProvider.Data implements GenericOptionsModel {
    public static final String FIREBASE_MODEL = "Group";

    private int mViewType;
    private int mSwipeReaction;
    private boolean mPinnedToSwipeLeft;

    public int Id;
    public int ParentId;
    public int PersonId;
    public String Name;
    public int Level = 0;
    public long ContactPeriod;
    public int Color = 0xff000000;// default color is black

    @SerializedName("Children")
    public List<GroupModel> Childs = new ArrayList<>();

    public GroupModel(){
    }

    public GroupModel(int PersonId, String Name){
        this.PersonId = PersonId;
        this.Name = Name;
        this.Level = 1;
        this.ContactPeriod = 1;
        this.mViewType = 0;
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    @Override
    public String toString() {
        return Name;
    }

    @Override
    public int getSwipeReactionType() {
        return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_LEFT | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_RIGHT;
    }

    @Override
    public String getText() {
        return Name;
    }

    @Override
    public boolean isPinnedToSwipeLeft() {
        return mPinnedToSwipeLeft;
    }

    @Override
    public void setPinnedToSwipeLeft(boolean pinedToSwipeLeft) {
        mPinnedToSwipeLeft = pinedToSwipeLeft;
    }

    public static GroupModel getThis(IDataAdapter iDataAdapter) throws Exception {
        if (GroupModel.class.isInstance(iDataAdapter)) {
            return (GroupModel) iDataAdapter;
        } else
            throw new Exception("This is object is not of the type ContactGroupModel");

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeInt(this.ParentId);
        dest.writeInt(this.PersonId);
        dest.writeString(this.Name);
        dest.writeInt(this.Level);
        dest.writeLong(this.ContactPeriod);
        dest.writeInt(this.Color);
        dest.writeTypedList(this.Childs);
    }

    protected GroupModel(Parcel in) {
        this.Id = in.readInt();
        this.ParentId = in.readInt();
        this.PersonId = in.readInt();
        this.Name = in.readString();
        this.Level = in.readInt();
        this.ContactPeriod = in.readLong();
        this.Color = in.readInt();
        in.readTypedList(this.Childs, GroupModel.CREATOR);
    }

    public static final Parcelable.Creator<GroupModel> CREATOR = new Parcelable.Creator<GroupModel>() {
        public GroupModel createFromParcel(Parcel source) {
            return new GroupModel(source);
        }

        public GroupModel[] newArray(int size) {
            return new GroupModel[size];
        }
    };

    public static GroupModel parse(JSONObject jo){
        GroupModel group = new GroupModel();
        group.Id = jo.optInt("Id");
        group.ParentId = jo.optInt("ParentId");
        group.PersonId = jo.optInt("PersonId");
        group.Name = jo.optString("Name");
        group.Level = jo.optInt("Level");
        group.ContactPeriod = jo.optLong("ContactPeriod");
        group.Color = jo.optInt("Color");
        JSONArray jaGroups = jo.optJSONArray("Children");
        if(jaGroups!=null){
            for (int i=0; i<jaGroups.length(); i++){
                JSONObject jGroup = jaGroups.optJSONObject(i);
                group.Childs.add(GroupModel.parse(jGroup));
            }
        }
        return group;
    }

    public static JSONObject parseToJson(IDataAdapter data) {
        GroupModel group = (GroupModel) data;
        JSONObject jo = new JSONObject();
        try {
            jo.put("Id", group.Id);
            jo.put("ParentId", group.ParentId);
            jo.put("Name", group.Name);
            jo.put("Level", group.Level);
            jo.put("ContactPeriod", group.ContactPeriod);
            jo.put("Color", group.Color);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

}
