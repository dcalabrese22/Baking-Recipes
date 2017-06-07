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
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}
