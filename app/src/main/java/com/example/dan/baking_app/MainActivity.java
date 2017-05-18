package com.example.dan.baking_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.dan.baking_app.ClickHandlers.RecipeClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeClickHandler {

    public static final String INGREDIENT_EXTRA = "ingredient_extra";
    public static final String STEP_EXTRA = "step_extra";

    private RecipeClickHandler mRecipeClickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipe_list);

    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        ArrayList<Step> steps = recipe.getSteps();

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putParcelableArrayListExtra(INGREDIENT_EXTRA, ingredients);
        intent.putParcelableArrayListExtra(STEP_EXTRA, steps);
        startActivity(intent);
    }
}
