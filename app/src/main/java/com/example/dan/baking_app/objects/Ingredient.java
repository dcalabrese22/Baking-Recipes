package com.example.dan.baking_app.objects;

/**
 * Created by dcalabrese on 5/15/2017.
 */

public class Ingredient {

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
}
