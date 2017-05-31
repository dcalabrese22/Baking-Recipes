package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.objects.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WidgetRemoteViewsFactoryReceiver extends BroadcastReceiver
        implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private static ArrayList<Ingredient> sIngredients;
    ArrayList<String> strings = new ArrayList<>();

    public WidgetRemoteViewsFactoryReceiver(){}

    public WidgetRemoteViewsFactoryReceiver(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
        if (sIngredients != null) {
            Log.d("Ingredients", sIngredients.toString());
        }
    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public void onDataSetChanged() {
        strings.clear();
        initData();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget);
        rv.setTextViewText(R.id.widget_quantity, strings.get(position));
        rv.setTextViewText(R.id.widget_ingredient_name, strings.get(position));
//        Ingredient ingredient = sIngredients.get(position);
//        DecimalFormat df = new DecimalFormat("####.#");
//        String quantityStr = df.format(ingredient.getQuantity());
//        if (ingredient.getMeasure().equals("UNIT")) {
//            rv.setTextViewText(R.id.textview_recipe_quantity, quantityStr);
//        } else {
//            String qAndMeasure = quantityStr + " " + ingredient.getMeasure().toLowerCase();
//            rv.setTextViewText(R.id.textview_recipe_quantity, qAndMeasure);
//        }
//        rv.setTextViewText(R.id.textview_recipe_name, ingredient.getName());

//            Bundle extras = new Bundle();
//            extras.putInt(BakingWidgetProvider.WIDGET_INTENT, position);
//            Intent fillInIntent = new Intent();
//            fillInIntent.putExtras(extras);
//            rv.setOnClickFillInIntent(R.id.recipe_ingredient, fillInIntent);
        Log.d("getViewAt", "CALLED");
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onDestroy() {
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
        sIngredients = intent.getParcelableArrayListExtra("ingredients");
        if (sIngredients != null) {
            Log.d("getRecipeIngredients", "Got Ingredients " + sIngredients.toString());
        }
    }
}
