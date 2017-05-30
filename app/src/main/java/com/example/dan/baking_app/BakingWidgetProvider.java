package com.example.dan.baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.dan.baking_app.objects.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "com.example.dan.baking_app.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, WidgetAdapterService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        rv.setRemoteAdapter(appWidgetId,intent);
        rv.setEmptyView(R.id.widget_listview, R.id.widget_empty);
        Intent startActivityIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0,
                startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.widget_listview, startActivityPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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
        super.onReceive(context, intent);
        if (intent.getAction().equals(EXTRA_ITEM)) {
            ArrayList<Ingredient> ingredient = intent.getParcelableArrayListExtra("ingredients");
        }
    }
}

