package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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


    public static final String WIDGET_INTENT = "com.example.dan.baking_app.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, RemoteViews rv) {

        Intent intent = new Intent(context, WidgetAdapterService.class);
        // Construct the RemoteViews object

        rv.setRemoteAdapter(R.id.widget_listview, intent);
        rv.setEmptyView(R.id.widget_listview, R.id.widget_empty);

//        appWidgetManager.updateAppWidget(thisWidget, rv);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, rv);
        }
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra("key", appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
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
        super.onReceive(context, intent);
        if (intent.getParcelableArrayListExtra("ingredients") != null) {
            Intent newIntent = new Intent();
            newIntent.setAction(WIDGET_INGREDIENT_DATA_ACTION);
            newIntent.putParcelableArrayListExtra("ingredients",
                    intent.getParcelableArrayListExtra("ingredients"));
            context.sendBroadcast(newIntent);
        }

    }

}

