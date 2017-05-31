package com.example.dan.baking_app;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetAdapterService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactoryReceiver(getApplicationContext(), intent);
    }
}
