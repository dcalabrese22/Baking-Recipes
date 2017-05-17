package com.example.dan.baking_app.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe {

    private String mName;
    private int mServings;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;

    public Recipe(String name, int servings,
                  ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {

        mName = name;
        mServings = servings;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public Recipe(String name, int servings) {
        mName = name;
        mServings = servings;
    }

    public String getName() {
        return mName;
    }

    public int getServings() {
        return mServings;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }
}
