package com.example.dan.baking_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dan.baking_app.contentprovider.RecipeContract;
import com.example.dan.baking_app.Interfaces.RecipeClickHandler;
import com.example.dan.baking_app.helpers.JsonResponseParser;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import org.json.JSONArray;

import java.util.ArrayList;

public class MasterListFragment extends Fragment {

    private MasterListAdapter mAdapter;

    public static final String INGREDIENT_EXTRA = "ingredient_extra";
    public static final String STEP_EXTRA = "step_extra";

    public MasterListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_recipe_list);

        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getContext(),3);
        } else {
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        }

        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MasterListAdapter((RecipeClickHandler) getActivity());
        recyclerView.setAdapter(mAdapter);

        getOnlineRecipes();



        return rootView;
    }

    public void getOnlineRecipes() {
        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    ArrayList<Recipe> recipes = new ArrayList<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        recipes = JsonResponseParser.parseTopLevelJsonRecipeData(response);
                        for (Recipe recipe : recipes) {
                            ContentValues recipeVals = new ContentValues();
                            recipeVals.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
                            Uri uriId = getContext().getContentResolver()
                                    .insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeVals);
                            long id = ContentUris.parseId(uriId);
                            for (Ingredient ingredient : recipe.getIngredients()) {
                                ContentValues ingredientVals = new ContentValues();
                                ingredientVals.put(RecipeContract.IngredientEntry.COLUMN_NAME,
                                        ingredient.getName());
                                ingredientVals.put(RecipeContract.IngredientEntry.COLUMN_QUANTITY,
                                        ingredient.getQuantity());
                                ingredientVals.put(RecipeContract.IngredientEntry.COLUMN_MEASURE,
                                        ingredient.getMeasure());
                                ingredientVals.put(RecipeContract.IngredientEntry.FOREIGN_KEY,
                                        id);
                                getContext().getContentResolver().
                                        insert(RecipeContract.IngredientEntry.CONTENT_URI, ingredientVals);
                            }
                            for (Step step : recipe.getSteps()) {
                                ContentValues stepVals = new ContentValues();
                                stepVals.put(RecipeContract.StepEntry.COLUMN_DESCRIPTION,
                                        step.getDescription());
                                stepVals.put(RecipeContract.StepEntry.COLUMN_SHORT_DESC,
                                        step.getShortDescription());
                                stepVals.put(RecipeContract.StepEntry.COLUMN_URL,
                                        step.getVideoUrl());
                                stepVals.put(RecipeContract.StepEntry.FOREIGN_KEY,
                                        id);
                                getContext().getContentResolver()
                                        .insert(RecipeContract.StepEntry.CONTENT_URI, stepVals);
                            }

                        }
                        mAdapter.setRecipeData(recipes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }
}
