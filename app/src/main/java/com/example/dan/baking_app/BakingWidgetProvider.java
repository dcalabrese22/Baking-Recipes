package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dan.baking_app.helpers.Constants;

import java.util.Date;

/**
 *Widget that shows a user specified recipe's ingredients
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    /**
     * Updates the application's widget
     *
     * @param context Application context
     * @param appWidgetManager Application AppWidgetManager
     * @param appWidgetId Application Id of widget
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        //Create intent to call Widget service
        Intent intent = new Intent(context, WidgetAdapterService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        //get recipe name from sharedpreferences
        SharedPreferences preferences = context.
                getSharedPreferences(Constants.WIDGET_PREFERENCE, Context.MODE_PRIVATE);
        String recipeName = preferences.getString(Constants.PREFERENCE_INGREDIENT_NAME, null);
        // Construct the RemoteViews object, set adapter, recipe title, and empty view
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        rv.setRemoteAdapter(R.id.widget_listview, intent);
        rv.setEmptyView(R.id.widget_listview, R.id.widget_empty);
        rv.setTextViewText(R.id.widget_recipe_title, recipeName);

        //create intent that handles widget clicks
        Intent openDetail = new Intent(context, RecipeDetailActivity.class);
        //pendingIntent to call intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openDetail, 0);
        //add PendingIntent to remoteview
        rv.setPendingIntentTemplate(R.id.widget_listview, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        ComponentName componentName = new ComponentName(context, BakingWidgetProvider.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, rv);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    /**
     * Receives broadcasts sent to Widget
     *
     * @param context Application context
     * @param intent Intent that was broadcast
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), BakingWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
        if (intent.getAction().equals(Constants.UPDATE_MY_WIDGET) ||
                intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }

    }


}

