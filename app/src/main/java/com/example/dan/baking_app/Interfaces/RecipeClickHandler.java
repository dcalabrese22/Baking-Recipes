package com.example.dan.baking_app.Interfaces;

import com.example.dan.baking_app.objects.Recipe;

/**
 * Interface for handling when a user clicks on a recipe
 */
public interface RecipeClickHandler {

    void onRecipeClick(Recipe recipe);
}
