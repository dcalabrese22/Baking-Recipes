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

import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    public String mRecipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("updateAppWidgetMethod: ", "called");

        Intent intent = new Intent(context, WidgetAdapterService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        rv.setRemoteAdapter(R.id.widget_listview, intent);
        //rv.setEmptyView(R.id.widget_listview, R.id.widget_empty);

//        Intent update = new Intent();
//        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, update, PendingIntent.FLAG_UPDATE_CURRENT);
//        rv.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);

//        appWidgetManager.updateAppWidget(thisWidget, rv);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);

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

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), BakingWidgetProvider.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            updateAppWidget(context, appWidgetManager, appWidgetId);
//        try {
//            if (intent.getAction().equals(Constants.WIDGET_EXTRA_INTENT)) {
//                mRecipeName = intent.getStringExtra(Constants.RECIPE_NAME_EXTRA);
//                Log.d("WidgetProvider", "onReceive: " + Constants.WIDGET_EXTRA_INTENT);
//                Log.d("Recipe name: ", mRecipeName);
//                Intent newIntent = new Intent();
//                newIntent.setAction(Constants.WIDGET_INGREDIENT_DATA_ACTION);
//                newIntent.putExtra(Constants.RECIPE_NAME_EXTRA, mRecipeName);
//                newIntent.putParcelableArrayListExtra(Constants.INGREDIENT_EXTRA,
//                        intent.getParcelableArrayListExtra(Constants.INGREDIENT_EXTRA));
//                context.sendBroadcast(newIntent);
//          }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

            super.onReceive(context, intent);
        }
    }


}

