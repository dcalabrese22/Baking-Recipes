package com.example.dan.baking_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyRequestQueue {

    private static MyRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;


    private MyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Gets the instance of the requestqueue
     *
     * @param context
     * @return
     */
    public static synchronized MyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyRequestQueue(context);
        }
        return mInstance;
    }

    /**
     * Get the requestqueue
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Add a network request to the queueu
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}