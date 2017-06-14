package com.example.dan.baking_app.Interfaces;

import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

/**
 * Interface for passing data from Activities to Fragments
 */
public interface PassRecipeDataHandler {
    ArrayList<Ingredient> passIngredients();
    ArrayList<Step> passSteps();
    String passRecipeName();
}