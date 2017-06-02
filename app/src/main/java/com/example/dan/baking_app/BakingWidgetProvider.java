package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dan.baking_app.objects.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static ArrayList<Ingredient> sIngredients;
    public static final String WIDGET_INGREDIENT_DATA_ACTION
            = "com.example.dan.baking_app.GET_WIDGET_DATA";

    public static final String WIDGET_ROW_ACTION = "com.example.dan.baking_app.WIDGET_ROW_ACTION";
    public static final String WIDGET_ROW_EXTRA = "com.example.dan.baking_app.WIDGET_ROW_EXTRA";

    public static final String WIDGET_EXTRA_INTENT = "com.example.dan.baking_app.EXTRA_ITEM";

    public String mRecipeName;
    public static Intent mIntent;



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String name) {
        Log.d("updateAppWidgetMethod: ", "called");

        Intent intent = new Intent(context, WidgetAdapterService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object


        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        rv.setRemoteAdapter(R.id.widget_listview, intent);
        rv.setEmptyView(R.id.widget_listview, R.id.widget_empty);

//        Intent openStep = new Intent(context, RecipeDetailActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openStep, 0);
//        rv.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

//        appWidgetManager.updateAppWidget(thisWidget, rv);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("onUpdate", "Recipe name = " + mRecipeName);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mRecipeName);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getParcelableArrayListExtra("ingredients") != null) {
            mRecipeName = intent.getStringExtra(MainActivity.RECIPE_NAME_EXTRA);

            Log.d("Recipe name: ", mRecipeName);
            Intent newIntent = new Intent();
            newIntent.setAction(WIDGET_INGREDIENT_DATA_ACTION);
            newIntent.putExtra(MainActivity.RECIPE_NAME_EXTRA, mRecipeName);
            newIntent.putParcelableArrayListExtra("ingredients",
                    intent.getParcelableArrayListExtra("ingredients"));
            context.sendBroadcast(newIntent);
        }
        super.onReceive(context, intent);
    }

}

