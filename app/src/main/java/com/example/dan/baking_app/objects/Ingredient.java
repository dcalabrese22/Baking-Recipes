package com.example.dan.baking_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private double mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(double quantity, String measure, String name) {
        mQuantity = quantity;
        mMeasure = measure;
        mName = name;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getName() {
        return mName;
    }

    private Ingredient(Parcel in) {
        mQuantity = in.readDouble();
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
        dest.writeDouble(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mName);
    }

    @Override
    public String toString() {
        return mName;
    }
}
