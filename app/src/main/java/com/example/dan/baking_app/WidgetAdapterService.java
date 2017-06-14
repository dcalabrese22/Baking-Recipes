package com.example.dan.baking_app;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Class for creating widget remote views
 */
public class WidgetAdapterService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}
