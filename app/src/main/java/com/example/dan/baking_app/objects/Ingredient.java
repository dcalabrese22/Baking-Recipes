package com.example.dan.baking_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private int mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(int quantity, String measure, String name) {
        mQuantity = quantity;
        mMeasure = measure;
        mName = name;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getName() {
        return mName;
    }

    private Ingredient(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mName = in.readString();
    }

    public static final Parcelable.Creator  CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mName);
    }
}
