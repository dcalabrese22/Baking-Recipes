package com.example.dan.baking_app;

import android.app.Service;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.example.dan.baking_app.helpers.Constants;
import com.example.dan.baking_app.objects.Ingredient;

import java.util.ArrayList;

public class WidgetAdapterService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("RemoteViewsFacotry", "called");
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        mRecipeName = intent.getStringExtra(Constants.RECIPE_NAME_EXTRA);
//        mIngredients = intent.getParcelableArrayListExtra(Constants.INGREDIENT_EXTRA);
//        Log.d("Widget service", "mRecipeName = " + mRecipeName);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        if (mRecipeName != null) {
//            mRecipeName = null;
//        }
//        if (mIngredients != null) {
//            mIngredients.clear();
//        }
//    }

}
