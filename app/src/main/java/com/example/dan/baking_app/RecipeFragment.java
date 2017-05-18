package com.example.dan.baking_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;

    public RecipeFragment() {}

    @Override
    public void setArguments(Bundle args) {
        args.getParcelableArrayList(MasterListFragment.INGREDIENT_EXTRA);
        args.getParcelableArrayList(MasterListFragment.STEP_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_recipe_detail, container, false);

        //RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerview_single_recipe);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        //recyclerView.setLayoutManager(linearLayoutManager);

        //mIngredients = getArguments().getParcelableArrayList(MasterListFragment.INGREDIENT_EXTRA);
        //mSteps = getArguments().getParcelableArrayList(MasterListFragment.STEP_EXTRA);

        //RecipeAdapter adapter = new RecipeAdapter();

        //adapter.setData(mIngredients, mSteps);
        //recyclerView.setAdapter(adapter);

        return rootview;
    }


}
