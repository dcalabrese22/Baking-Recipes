package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.objects.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WidgetRemoteViewsFactoryReceiver extends BroadcastReceiver
        implements RemoteViewsService.RemoteViewsFactory{

    private final String TAG = "Factory Receiver";
    private Context mContext;
    private static ArrayList<Ingredient> sIngredients;
    ArrayList<String> strings = new ArrayList<>();

    public WidgetRemoteViewsFactoryReceiver(){}

    public WidgetRemoteViewsFactoryReceiver(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Called");
        initData();
        if (sIngredients != null) {
            Log.d(TAG, "onCreate: " + sIngredients.toString());
        }
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
//        rv.setTextViewText(R.id.widget_quantity, strings.get(position));
//        rv.setTextViewText(R.id.widget_ingredient_name, strings.get(position));
        Ingredient ingredient = sIngredients.get(position);
        Log.d(TAG, "getViewAt: " + ingredient.getName());
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

    public void initData() {
        for (int i = 0; i < 10; i++) {
            strings.add(Integer.toString(i));
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Called");
        if (intent.getAction().equals(BakingWidgetProvider.WIDGET_INGREDIENT_DATA_ACTION)) {
            sIngredients = intent.getParcelableArrayListExtra("ingredients");
        }
        if (sIngredients != null) {
            Log.d(TAG, "onReceive: " + sIngredients.toString());
        }
    }
}
