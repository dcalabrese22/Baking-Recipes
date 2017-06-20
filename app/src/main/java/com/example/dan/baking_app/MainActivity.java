package com.example.dan.baking_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dan.baking_app.Interfaces.RecipeClickHandler;
import com.example.dan.baking_app.contentprovider.RecipeContract;
import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.helpers.JsonResponseParser;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Application's main activity
 */
public class MainActivity extends AppCompatActivity implements RecipeClickHandler {

    private MasterListAdapter mAdapter;
    private static final int LAND_GRID_COLUMNS = 3;
    private static final int PORT_GRID_COLUMNS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find and set the title bar
        TextView tv = (TextView) findViewById(R.id.textview_toolbar);
        tv.setText(getResources().getString(R.string.app_name));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipe_list);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this,LAND_GRID_COLUMNS);
        } else {
            gridLayoutManager = new GridLayoutManager(this, PORT_GRID_COLUMNS);
        }

        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MasterListAdapter(this);
        recyclerView.setAdapter(mAdapter);

        getOnlineRecipes();


    }

    /**
     * Gets recipes from online
     */
    public void getOnlineRecipes() {
        String url =
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        //user Volley to get data off main thread
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    ArrayList<Recipe> recipes = new ArrayList<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        recipes = JsonResponseParser.parseTopLevelJsonRecipeData(response);
                        for (Recipe recipe : recipes) {

                            ContentValues recipeVals = new ContentValues();
                            recipeVals.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipe.getName());
                            //check if recipe is already in database
                            Cursor cursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                                    null,
                                    RecipeContract.RecipeEntry.COLUMN_NAME + "=?",
                                    new String[] {recipe.getName()},
                                    null);
                            //move to the next recipe if it is
                            if (cursor.moveToNext()) {

                                continue;
                            //otherwise add the recipe to the database
                            } else {
                                Uri uriId = getContentResolver()
                                        .insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeVals);
                                long id = ContentUris.parseId(uriId);
                                //add the recipe's ingredients to the database
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
                                    getContentResolver().
                                            insert(RecipeContract.IngredientEntry.CONTENT_URI, ingredientVals);
                                }
                                //add the recipe's steps to the database
                                for (Step step : recipe.getSteps()) {
                                    ContentValues stepVals = new ContentValues();
                                    stepVals.put(RecipeContract.StepEntry.COLUMN_ID,
                                            step.getId());
                                    stepVals.put(RecipeContract.StepEntry.COLUMN_DESCRIPTION,
                                            step.getDescription());
                                    stepVals.put(RecipeContract.StepEntry.COLUMN_SHORT_DESC,
                                            step.getShortDescription());
                                    stepVals.put(RecipeContract.StepEntry.COLUMN_URL,
                                            step.getVideoUrl());
                                    stepVals.put(RecipeContract.StepEntry.COLUMN_THUMB_URL,
                                            step.getThumbnailUrl());
                                    stepVals.put(RecipeContract.StepEntry.FOREIGN_KEY,
                                            id);
                                    getContentResolver()
                                            .insert(RecipeContract.StepEntry.CONTENT_URI, stepVals);
                                }
                            }
                            cursor.close();
                        }
                        mAdapter.setRecipeData(recipes);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Handles Recipe clicks
     * @param recipe
     */
    @Override
    public void onRecipeClick(Recipe recipe) {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        ArrayList<Step> steps = recipe.getSteps();

        //start recipe detail activity and pass the recipes ingredients and steps via the intent
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        Bundle b = new Bundle();
        b.putString(Constants.RECIPE_NAME_EXTRA, recipe.getName());
        b.putParcelableArrayList(Constants.INGREDIENT_EXTRA, ingredients);
        b.putParcelableArrayList(Constants.STEP_EXTRA, steps);
        intent.putExtras(b);
        startActivity(intent);
    }
}
