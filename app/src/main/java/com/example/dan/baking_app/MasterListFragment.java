package com.example.dan.baking_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dan.baking_app.ClickHandlers.RecipeClickHandler;
import com.example.dan.baking_app.helpers.JsonResponseParser;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Recipe;
import com.example.dan.baking_app.objects.Step;

import org.json.JSONArray;
import org.json.JSONObject;

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
