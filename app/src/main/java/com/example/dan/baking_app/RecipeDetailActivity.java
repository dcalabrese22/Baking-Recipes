package com.example.dan.baking_app;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_single_recipe);

        RecipeAdapter adapter = new RecipeAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Ingredient> ingredients = getIntent()
                .getParcelableArrayListExtra(MasterListFragment.INGREDIENT_EXTRA);
        ArrayList<Step> steps = getIntent()
                .getParcelableArrayListExtra(MasterListFragment.STEP_EXTRA);

        adapter.setData(ingredients, steps);
        recyclerView.setAdapter(adapter);

//        Bundle b = new Bundle();
//
//        b.putParcelableArrayList(MasterListFragment.INGREDIENT_EXTRA, ingredients);
//        b.putParcelableArrayList(MasterListFragment.STEP_EXTRA, steps);
//
        RecipeFragment recipeFragment = new RecipeFragment();
//        recipeFragment.setArguments(b);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recyclerview_single_recipe, recipeFragment)
                .commit();

    }
}
