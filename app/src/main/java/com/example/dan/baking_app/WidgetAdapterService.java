package com.example.dan.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.R;
import com.example.dan.baking_app.objects.Ingredient;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by dcalabrese on 5/30/2017.
 */

public class WidgetAdapterService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoveViewsFactory(getApplicationContext(), intent);
    }

    private class WidgetRemoveViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<Ingredient> mIngredients;
        private PassRecipeDataHandler mHandler;
        private int mAppWidgetId;

        public WidgetRemoveViewsFactory(Context context, Intent intent) {
            mContext = context;
            mHandler = (PassRecipeDataHandler) mContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            mIngredients = mHandler.passIngredients();
        }

        @Override
        public int getCount() {
            return mIngredients.size();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipe_ingredient);
            Ingredient ingredient = mIngredients.get(position);
            DecimalFormat df = new DecimalFormat("####.#");
            String quantityStr = df.format(ingredient.getQuantity());
            if (ingredient.getMeasure().equals("UNIT")) {
                rv.setTextViewText(R.id.textview_recipe_quantity, quantityStr);
            } else {
                String qAndMeasure = quantityStr + " " + ingredient.getMeasure().toLowerCase();
                rv.setTextViewText(R.id.textview_recipe_ingredient, qAndMeasure);
            }

            Bundle extras = new Bundle();
            extras.putInt(BakingWidgetProvider.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.recipe_ingredient, fillInIntent);
            return rv;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onDestroy() {
            mIngredients.clear();
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
}
