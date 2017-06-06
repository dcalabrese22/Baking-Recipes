package com.example.dan.baking_app;

import android.app.Service;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;

import java.util.ArrayList;

public class WidgetAdapterService extends RemoteViewsService {

    String mRecipeName;
    ArrayList<Ingredient> mIngredients;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(getApplicationContext(), intent,
                mRecipeName, mIngredients);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRecipeName = intent.getStringExtra(Constants.RECIPE_NAME_EXTRA);
        mIngredients = intent.getParcelableArrayListExtra(Constants.INGREDIENT_EXTRA);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mRecipeName = null;
        mIngredients.clear();
        stopSelf();
        super.onDestroy();
    }
}
