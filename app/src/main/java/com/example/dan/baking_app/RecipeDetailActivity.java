package com.example.dan.baking_app;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity
        implements PassRecipeDataHandler, StepClickHandler{

    public static final String DESC_STEP_EXTRA = "desc_extra";
    public static final String URL_STEP_EXTRA = "url_extra";
    public static final String ID_STEP_EXTRA = "id_extra";
    public static final String STEP_KEY = "step_fragment";
    public static final String RECIPE_KEY = "recipe_fragment";

    StepFragment mStepFragment;
    RecipeFragment mRecipeFragment;

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STEP_KEY)) {
                mStepFragment = (StepFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, STEP_KEY);
            } else if (savedInstanceState.containsKey(RECIPE_KEY)) {
                mRecipeFragment = (RecipeFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, RECIPE_KEY);
            }
        } else {

            mRecipeFragment = new RecipeFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.activity_recipe_detail, mRecipeFragment, RECIPE_KEY)
                    .commit();
        }
     }

    @Override
    public ArrayList<Ingredient> passIngredients() {
        mIngredients = getIntent().getExtras().getParcelableArrayList(MainActivity.INGREDIENT_EXTRA);
        return mIngredients;
    }

    @Override
    public ArrayList<Step> passSteps() {
        mSteps = getIntent().getExtras().getParcelableArrayList(MainActivity.STEP_EXTRA);
        return mSteps;
    }

    @Override
    public void onStepClick(Step step) {
        String desc = step.getDescription();
        String url = step.getVideoUrl();
        int id = step.getId();
        Bundle bundle = new Bundle();
        bundle.putString(DESC_STEP_EXTRA, desc);
        bundle.putString(URL_STEP_EXTRA, url);
        bundle.putInt(ID_STEP_EXTRA, id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mStepFragment = new StepFragment();
        mStepFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_recipe_detail, mStepFragment, STEP_KEY);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
