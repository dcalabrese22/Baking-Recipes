package com.example.dan.baking_app.objects;

import java.util.ArrayList;

/**
 * Class for representing a Recipe
 */
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
