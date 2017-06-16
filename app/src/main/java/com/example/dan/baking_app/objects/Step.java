package com.example.dan.baking_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for representing a Step of a Recipe. Parcelable for passing data with intents.
 */
public class Step implements Parcelable {

    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private int mId;

    public Step(int id, String shortDesc, String desc, String url, String thumbUrl) {
        mId = id;
        mShortDescription = shortDesc;
        mDescription = desc;
        mVideoUrl = url;
        mThumbnailUrl = thumbUrl;
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

    public int getId() {
        return mId;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    private Step(Parcel in) {
        mId = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
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
        dest.writeInt(mId);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
    }

    @Override
    public String toString() {
        return mShortDescription;
    }
}
