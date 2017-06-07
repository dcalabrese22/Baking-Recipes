package com.example.dan.baking_app;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.contentprovider.RecipeContract;
import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;
    String mRecipeName;

    RecyclerView mRecyclerview;
    PassRecipeDataHandler mHandler;
    Button mButton;

    StepClickHandler mStepClickHandler;

    public RecipeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mRecyclerview = (RecyclerView) rootview.findViewById(R.id.recyclerview_single_recipe);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mButton = (Button) rootview.findViewById(R.id.widget_update_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                broadcast();
            }
        });

        RecipeAdapter adapter = new RecipeAdapter(mStepClickHandler);

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(Constants.SAVED_STATE_STEPS);
            mIngredients = savedInstanceState.getParcelableArrayList(Constants.SAVED_STATE_INGREDIENTS);
        }

        adapter.setData(mIngredients, mSteps);

        mRecyclerview.setAdapter(adapter);

        return rootview;

    }

    public void broadcast() {
        Intent intent = new Intent(getContext(), BakingWidgetProvider.class);
        SharedPreferences widgetSetting = getActivity()
                .getSharedPreferences(Constants.WIDGET_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = widgetSetting.edit();
        editor.putString(Constants.PREFERENCE_INGREDIENT_NAME, mRecipeName);
        editor.commit();
        intent.setAction(Constants.UPDATE_MY_WIDGET);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.SAVED_STATE_INGREDIENTS, mIngredients);
        outState.putParcelableArrayList(Constants.SAVED_STATE_STEPS, mSteps);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStepClickHandler = (StepClickHandler) context;
        mHandler = (PassRecipeDataHandler) context;
        mRecipeName = mHandler.passRecipeName();
        mIngredients = mHandler.passIngredients();
        mSteps = mHandler.passSteps();
    }
}
