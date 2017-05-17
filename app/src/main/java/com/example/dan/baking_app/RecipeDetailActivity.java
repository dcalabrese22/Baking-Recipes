package com.example.dan.baking_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe);

        ArrayList<Ingredient> ingredients = getIntent()
                .getParcelableArrayListExtra(MasterListFragment.INGREDIENT_EXTRA);
        ArrayList<Step> steps = getIntent()
                .getParcelableArrayListExtra(MasterListFragment.STEP_EXTRA);

        Bundle b = new Bundle();

        b.putParcelableArrayList(MasterListFragment.INGREDIENT_EXTRA, ingredients);
        b.putParcelableArrayList(MasterListFragment.STEP_EXTRA, steps);

        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(b);

    }
}
