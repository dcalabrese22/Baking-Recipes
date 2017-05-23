package com.example.dan.baking_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.dan.baking_app.Interfaces.RecipeClickHandler;
import com.example.dan.baking_app.helpers.JsonResponseParser;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeClickHandler {

    public static final String INGREDIENT_EXTRA = "ingredient_extra";
    public static final String STEP_EXTRA = "step_extra";

    private MasterListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipe_list);

        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this,3);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 1);
        }

        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MasterListAdapter(this);
        recyclerView.setAdapter(mAdapter);

        getOnlineRecipes();


    }

    public void getOnlineRecipes() {
        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    ArrayList<Recipe> recipes = new ArrayList<>();

                    @Override
                    public void onResponse(JSONArray response) {
                        recipes = JsonResponseParser.parseTopLevelJsonRecipeData(response);
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

    @Override
    public void onRecipeClick(Recipe recipe) {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        ArrayList<Step> steps = recipe.getSteps();

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        Bundle b = new Bundle();

        b.putParcelableArrayList(INGREDIENT_EXTRA, ingredients);
        b.putParcelableArrayList(STEP_EXTRA, steps);
        intent.putExtras(b);
        startActivity(intent);
    }
}
