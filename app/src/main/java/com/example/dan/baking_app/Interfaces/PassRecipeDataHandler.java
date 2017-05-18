package com.example.dan.baking_app.Interfaces;

import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public interface PassRecipeDataHandler {
    ArrayList<Ingredient> passIngredients();
    ArrayList<Step> passSteps();
}