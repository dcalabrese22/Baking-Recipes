package com.example.dan.baking_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;

    RecyclerView mRecyclerview;
    PassRecipeDataHandler mHandler;

    StepClickHandler mStepClickHandler;


    public RecipeFragment() {}

    @Override
    public void setArguments(Bundle args) {
        args.getParcelableArrayList(MasterListFragment.INGREDIENT_EXTRA);
        args.getParcelableArrayList(MasterListFragment.STEP_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        Bundle b = new Bundle();

        mRecyclerview = (RecyclerView) rootview.findViewById(R.id.recyclerview_single_recipe);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(linearLayoutManager);

        RecipeAdapter adapter = new RecipeAdapter(mStepClickHandler);
        adapter.setData(mIngredients, mSteps);

        mRecyclerview.setAdapter(adapter);

        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepClickHandler = (StepClickHandler) getActivity();
        mHandler = (PassRecipeDataHandler) context;
        mIngredients = mHandler.passIngredients();
        mSteps = mHandler.passSteps();
    }
}
