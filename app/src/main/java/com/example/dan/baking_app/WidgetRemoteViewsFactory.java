package com.example.dan.baking_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.contentprovider.RecipeContract;
import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private ArrayList<Ingredient> mIngredients;
    private String mRecipeName;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIngredients = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        SharedPreferences preferences = mContext.
                getSharedPreferences(Constants.WIDGET_PREFERENCE, Context.MODE_PRIVATE);
        mRecipeName = preferences.getString(Constants.PREFERENCE_INGREDIENT_NAME, null);
        if (mRecipeName != null) {
            getData(mRecipeName);
        }
    }

    @Override
    public int getCount() {
        if (mIngredients != null) {
            return mIngredients.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onDataSetChanged() {
        if (mIngredients != null) {
            mIngredients.clear();
        }
        SharedPreferences preferences = mContext.
                getSharedPreferences(Constants.WIDGET_PREFERENCE, Context.MODE_PRIVATE);
        mRecipeName = preferences.getString(Constants.PREFERENCE_INGREDIENT_NAME, null);
        if (mRecipeName != null) {
            getData(mRecipeName);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public String getIngredientQuantity(Ingredient ingredient) {
        DecimalFormat df = new DecimalFormat("####.#");
        String quantityStr = df.format(ingredient.getQuantity());
        if (ingredient.getMeasure().equals("UNIT")) {
            return quantityStr;
        } else {
            String qAndMeasure = quantityStr + " " + ingredient.getMeasure().toLowerCase();
            return qAndMeasure;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget);
            Ingredient ingredient = mIngredients.get(position);
            rv.setTextViewText(R.id.widget_quantity, getIngredientQuantity(ingredient));
            rv.setTextViewText(R.id.widget_ingredient_name, ingredient.getName());

            Intent fillInIntent = new Intent();
            fillInIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            rv.setOnClickFillInIntent(R.id.widget_ingredient_name, fillInIntent);
            return rv;
    }

    public void getData(String recipeName) {
        String selection = RecipeContract.RecipeEntry.COLUMN_NAME + "=?";
        Cursor cursor = mContext.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                null, selection, new String[] {recipeName}, null);
        int idIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry._ID);
        if (cursor.moveToFirst()) {
            selection = RecipeContract.IngredientEntry.FOREIGN_KEY + "=?";
            String[] selectionArgs = {Integer.toString(cursor.getInt(idIndex))};
            String sortOrder = RecipeContract.IngredientEntry._ID;
            cursor = mContext.getContentResolver().query(RecipeContract.IngredientEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    sortOrder);
            makeIngredientsList(cursor);

        }

        cursor.close();
    }

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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onDestroy() {
        mRecipeName = null;
        if (mIngredients != null) {
            mIngredients.clear();
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
