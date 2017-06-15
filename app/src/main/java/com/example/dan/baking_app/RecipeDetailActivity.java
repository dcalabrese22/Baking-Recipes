package com.example.dan.baking_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.contentprovider.RecipeContract;
import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;
import com.example.dan.baking_app.objects.Step;

import java.util.ArrayList;

/**
 * Activity that displays a recipe's details
 */
public class RecipeDetailActivity extends AppCompatActivity
        implements PassRecipeDataHandler, StepClickHandler{


    StepFragment mStepFragment;
    RecipeFragment mRecipeFragment;

    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;

    private boolean mTwoPane;//for determining if the device should show the fragments side by side
    Toolbar mToolbar;

    String mRecipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        //find and set the toolbar title appropriately
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.WIDGET_PREFERENCE,
                Context.MODE_PRIVATE);
        mRecipeName = sharedPreferences.getString(Constants.PREFERENCE_INGREDIENT_NAME, null);


        FragmentManager fragmentManager = getFragmentManager();
        //if the device is a tablet its layout will have R.id.two_pane
        if (findViewById(R.id.two_pane) != null) {
            mTwoPane = true;
            //find the fragment if coming from a onSavedInstanceState call
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(Constants.STEP_KEY)) {
                    mStepFragment = (StepFragment) getFragmentManager()
                            .getFragment(savedInstanceState, Constants.STEP_KEY);
                } else {
                    mRecipeFragment = (RecipeFragment) getFragmentManager()
                            .getFragment(savedInstanceState, Constants.RECIPE_KEY);
                }
            } else {
                //create a new RecipeFragment
                mRecipeFragment = new RecipeFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_list_container, mRecipeFragment, Constants.RECIPE_KEY)
                        .commit();
            }
        //if R.id.two_pane wasn't found, the device isn't large enough to support side by side
        //fragments
        } else {
            mTwoPane = false;
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(Constants.STEP_KEY)) {
                    mStepFragment = (StepFragment) getFragmentManager()
                            .getFragment(savedInstanceState, Constants.STEP_KEY);
                } else if (savedInstanceState.containsKey(Constants.RECIPE_KEY)) {
                    mRecipeFragment = (RecipeFragment) getFragmentManager()
                            .getFragment(savedInstanceState, Constants.RECIPE_KEY);
                }
            } else {

                mRecipeFragment = new RecipeFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_recipe_detail, mRecipeFragment, Constants.RECIPE_KEY)
                        .commit();
            }
        }
        setMyTitle();
        setSupportActionBar(mToolbar);

        //hide toolbar if video is displaying fullscreen
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.VISIBLE) {
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        });
    }

    /**
     * Sets the toolbar title appropriately
     */
    public void setMyTitle() {
        //two pane mode sets generic title
        if (mTwoPane) {
            mToolbar.setTitle(getResources().getString(R.string.app_name));
        //single pane sets specific title
        } else if (getIntent().hasExtra(Constants.RECIPE_NAME_EXTRA)) {
            mToolbar.setTitle(getIntent().getExtras().getString(Constants.RECIPE_NAME_EXTRA));
        //if activity is launched from widget, get correct title
        } else {
            mToolbar.setTitle(mRecipeName);
        }
    }

    /**
     * Method for passing Ingredient ArrayList to fragment
     * @return ArrayList of Ingredients
     */
    @Override
    public ArrayList<Ingredient> passIngredients() {
        //if we came from main activity, the data was passed in the intent
        if (getIntent().hasExtra(Constants.INGREDIENT_EXTRA)) {
            mIngredients = getIntent().getExtras()
                    .getParcelableArrayList(Constants.INGREDIENT_EXTRA);
        //if we came from the widget, the data needs to be queried from the database
        } else {
            mIngredients = new ArrayList<>();
            Log.d("Came from", "widget");
            Cursor cursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                    null,
                    RecipeContract.RecipeEntry.COLUMN_NAME + "=?",
                    new String[]{mRecipeName},
                    null);
            int idIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry._ID);
            if (cursor.moveToFirst()) {
                String selection = RecipeContract.IngredientEntry.FOREIGN_KEY + "=?";
                String sortOrder = RecipeContract.IngredientEntry._ID;
                cursor = getContentResolver().query(RecipeContract.IngredientEntry.CONTENT_URI,
                        null, selection, new String[]{Integer.toString(cursor.getInt(idIndex))}, sortOrder);
                makeIngredientsList(cursor);
            }

            cursor.close();
        }

        return mIngredients;
    }

    /**
     * Creates an ArrayList of Ingredients from a database query
     *
     * @param cursor Cursor pointing to database query of ingredients
     */
    public void makeIngredientsList(Cursor cursor) {
        int ingNameIndex = cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_NAME);
        int ingQuantityIndex = cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_QUANTITY);
        int ingMeasureIndex = cursor.getColumnIndex(RecipeContract.IngredientEntry.COLUMN_MEASURE);
        while (cursor.moveToNext()) {
            String ingName = cursor.getString(ingNameIndex);
            String ingMeasure = cursor.getString(ingMeasureIndex);
            Double ingQuantity = cursor.getDouble(ingQuantityIndex);
            Ingredient ingredient = new Ingredient(ingQuantity, ingMeasure, ingName);
            mIngredients.add(ingredient);
        }

    }

    /**
     * Method for passing Step ArrayList to fragment
     *
     * @return ArrayList of Steps
     */
    @Override
    public ArrayList<Step> passSteps() {
        //if we came from main activity, the step data was passed as an extra
        if (getIntent().hasExtra(Constants.STEP_EXTRA)) {
            mSteps = getIntent().getExtras().getParcelableArrayList(Constants.STEP_EXTRA);
        //if we came from the widget, the step data needs to be queried
        } else {
            mSteps = new ArrayList<>();
            Cursor cursor = getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                    null,
                    RecipeContract.RecipeEntry.COLUMN_NAME + "=?",
                    new String[]{mRecipeName},
                    null);
            int idIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry._ID);
            if (cursor.moveToFirst()) {
                String selection = RecipeContract.StepEntry.FOREIGN_KEY + "=?";
                String sortOrder = RecipeContract.StepEntry._ID;
                String[] selectionArgs = {Integer.toString(cursor.getInt(idIndex))};
                cursor = getContentResolver().query(RecipeContract.StepEntry.CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        sortOrder);
                makeStepsList(cursor);
            }
        }
        return mSteps;
    }

    /**
     * Creates ArrayList of Steps from database query
     *
     * @param cursor Cursor pointing to database query of steps
     */
    public void makeStepsList(Cursor cursor) {
        int stepIdIndex = cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_ID);
        int descIndex = cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_DESCRIPTION);
        int shortDescIndex = cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_SHORT_DESC);
        int urlIndex = cursor.getColumnIndex(RecipeContract.StepEntry.COLUMN_URL);
        while (cursor.moveToNext()) {
            int stepId = cursor.getInt(stepIdIndex);
            String desc = cursor.getString(descIndex);
            String shortDesc = cursor.getString(shortDescIndex);
            String url = cursor.getString(urlIndex);
            Step step = new Step(stepId, shortDesc, desc, url);
            mSteps.add(step);
        }
    }

    /**
     * Method for passing the Recipe name to fragment
     *
     * @return String recipe name
     */
    @Override
    public String passRecipeName() {
        //if we came from main activity, the recipe name was passed in the intent
        if (getIntent().hasExtra(Constants.RECIPE_NAME_EXTRA)) {
            return getIntent().getStringExtra(Constants.RECIPE_NAME_EXTRA);
        //if we came from the widget, the recipe name came from shared preferences
        } else {
            return mRecipeName;

        }
    }

    /**
     * Handles when recipe steps are clicked
     *
     * @param step The Step clicked
     */
    @Override
    public void onStepClick(Step step) {
        FragmentManager fragmentManager = getFragmentManager();
        String desc = step.getDescription();
        String url = step.getVideoUrl();
        int id = step.getId();
        //bundle recipe details to pass to StepFragment
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DESC_STEP_EXTRA, desc);
        bundle.putString(Constants.URL_STEP_EXTRA, url);
        bundle.putInt(Constants.ID_STEP_EXTRA, id);
        bundle.putBoolean(Constants.TWO_PANE_EXTRA, mTwoPane);
        mStepFragment = new StepFragment();
        mStepFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //add the fragment to the appropriate container depending on if the device is a tablet
        if (mTwoPane) {
            fragmentTransaction.add(R.id.step_container, mStepFragment, Constants.STEP_KEY);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.activity_recipe_detail, mStepFragment, Constants.STEP_KEY);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
