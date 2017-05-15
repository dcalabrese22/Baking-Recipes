package com.example.dan.baking_app.objects;

/**
 * Created by dcalabrese on 5/15/2017.
 */

public class Step {

    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;

    public Step(String shortDesc, String desc, String url) {
        mShortDescription = shortDesc;
        mDescription = desc;
        mVideoUrl = url;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }
}
