package com.example.dan.baking_app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class StepFragment extends Fragment {

    String mDescription;
    String mVideoUrl;
    int mId;
    ArrayList<Step> mSteps;
    PassRecipeDataHandler mHandler;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mExoPlayer;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_step_detail, container, false);
        final TextView descTextview = (TextView) rootview.findViewById(R.id.textview_step_description);
        descTextview.setText(mDescription);

        mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
        initializePlayer(mVideoUrl);


        ImageButton forward = (ImageButton) rootview.findViewById(R.id.imagebutton_next_step);
        ImageButton back = (ImageButton) rootview.findViewById(R.id.imagebutton_prev_step);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mId < mSteps.size() - 1) {
                    Log.d("NEXT", Integer.toString(mId));
                    mId++;
                    descTextview.setText(mSteps.get(mId).getDescription());
                    getMovie();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mId > 0) {
                    Log.d("PREV", Integer.toString(mId));
                    mId--;
                    descTextview.setText(mSteps.get(mId).getDescription());
                    getMovie();
                }
            }
        });


        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler = (PassRecipeDataHandler) context;
        mSteps = mHandler.passSteps();
        mDescription = getArguments().getString(RecipeDetailActivity.DESC_STEP_EXTRA);
        mVideoUrl = getArguments().getString(RecipeDetailActivity.URL_STEP_EXTRA);
        mId = getArguments().getInt(RecipeDetailActivity.ID_STEP_EXTRA);
    }

    public void initializePlayer(String url) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector(null);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "baking_app");
            Uri mediaUri = Uri.parse(url);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    public void getMovie() {
        mVideoUrl = mSteps.get(mId).getVideoUrl();
        Uri mediaUri = Uri.parse(mVideoUrl);
        String userAgent = Util.getUserAgent(getContext(), "baking_app");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    public void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
}
