package com.example.dan.baking_app;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
                sendToService();
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
        Intent intent = new Intent();
        intent.putExtra(Constants.RECIPE_NAME_EXTRA, mRecipeName);
        intent.putParcelableArrayListExtra(Constants.INGREDIENT_EXTRA, mIngredients);
        intent.setAction(Constants.WIDGET_EXTRA_INTENT);
        getActivity().sendBroadcast(intent);
    }

    public void sendToService() {
        Intent sendToService = new Intent(getActivity(), WidgetAdapterService.class);
        sendToService.putExtra(Constants.RECIPE_NAME_EXTRA, mRecipeName);
        sendToService.putParcelableArrayListExtra(Constants.INGREDIENT_EXTRA, mIngredients);
        getActivity().startService(sendToService);
    }

    public void broadcastWidgetUpdate() {
        Intent intent = new Intent(getActivity(), WidgetAdapterService.class);
        intent.putExtra(Constants.RECIPE_NAME_EXTRA, mRecipeName);
        intent.putParcelableArrayListExtra("ingredients", mIngredients);
        intent.setAction(Constants.WIDGET_EXTRA_INTENT);
        getContext().sendBroadcast(intent);
        Intent update = new Intent(getActivity(), BakingWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getContext())
                .getAppWidgetIds(new ComponentName(getContext(), BakingWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        getContext().sendBroadcast(update);
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
