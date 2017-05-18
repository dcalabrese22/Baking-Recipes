package com.example.dan.baking_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dcalabrese on 5/15/2017.
 */

public class Step implements Parcelable {

    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;

    public Step(String shortDesc, String desc, String url) {
        mShortDescription = shortDesc;
        mDescription = desc;
        mVideoUrl = url;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    private Step(Parcel in) {
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
    }

    public static final Parcelable.Creator  CREATOR = new Parcelable.Creator<Step>() {

        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };


        @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
    }

    @Override
    public String toString() {
        return mShortDescription;
    }
}
