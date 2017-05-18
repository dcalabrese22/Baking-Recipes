package com.example.dan.baking_app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StepFragment extends Fragment {

    String mDescription;
    String mVideoUrl;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_step_detail, container, false);
        TextView descTextview = (TextView) rootview.findViewById(R.id.textview_step_description);
        descTextview.setText(mDescription);
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDescription = getArguments().getString(RecipeDetailActivity.DESC_STEP_EXTRA);
        mVideoUrl = getArguments().getString(RecipeDetailActivity.URL_STEP_EXTRA);
    }
}
