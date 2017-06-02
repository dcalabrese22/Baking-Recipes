package com.example.dan.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.contentprovider.RecipeContract;
import com.example.dan.baking_app.objects.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WidgetRemoteViewsFactoryReceiver extends BroadcastReceiver
        implements RemoteViewsService.RemoteViewsFactory{

    private final String TAG = "Factory Receiver";
    private Context mContext;
    private static ArrayList<Ingredient> sIngredients;
    ArrayList<String> strings = new ArrayList<>();
    private static String mRecipeName;

    public WidgetRemoteViewsFactoryReceiver(){}

    public WidgetRemoteViewsFactoryReceiver(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public int getCount() {
        if (sIngredients != null) {
            return sIngredients.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: Called");

    }

    @Override
    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount: Called");
        return 2;
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
        Log.d(TAG, "mRecipeName: " + mRecipeName);
        if (position == 0) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_provider);
            rv.setTextViewText(R.id.widget_recipe_title, mRecipeName);
            return rv;
        } else {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget);
            Ingredient ingredient = sIngredients.get(position);
            rv.setTextViewText(R.id.widget_quantity, getIngredientQuantity(ingredient));
            rv.setTextViewText(R.id.widget_ingredient_name, ingredient.getName());

            Bundle extras = new Bundle();
            extras.putInt(BakingWidgetProvider.WIDGET_ROW_EXTRA, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            fillInIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            rv.setOnClickFillInIntent(R.id.recipe_ingredient, fillInIntent);
            return rv;
        }
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId: Called");
        return position;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        if (sIngredients != null) {
            sIngredients.clear();
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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BakingWidgetProvider.WIDGET_INGREDIENT_DATA_ACTION)) {
            sIngredients = intent.getParcelableArrayListExtra("ingredients");
            mRecipeName = intent.getStringExtra(MainActivity.RECIPE_NAME_EXTRA);
        }
    }
}
