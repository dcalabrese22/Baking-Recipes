package com.example.dan.baking_app;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity
        implements PassRecipeDataHandler, StepClickHandler{

    public static final String DESC_STEP_EXTRA = "desc_extra";
    public static final String URL_STEP_EXTRA = "url_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        RecipeFragment recipeFragment = new RecipeFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.activity_recipe_detail, recipeFragment)
                .commit();

    }

    @Override
    public ArrayList<Ingredient> passIngredients() {
        return getIntent().getExtras().getParcelableArrayList(MainActivity.INGREDIENT_EXTRA);
    }

    @Override
    public ArrayList<Step> passSteps() {
        return getIntent().getExtras().getParcelableArrayList(MainActivity.STEP_EXTRA);
    }

    @Override
    public void onStepClick(Step step) {
        String desc = step.getDescription();
        String url = step.getVideoUrl();
        Bundle bundle = new Bundle();
        bundle.putString(DESC_STEP_EXTRA, desc);
        bundle.putString(URL_STEP_EXTRA, url);
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.activity_recipe_detail, stepFragment)
                .addToBackStack(null)
                .commit();

    }
}
