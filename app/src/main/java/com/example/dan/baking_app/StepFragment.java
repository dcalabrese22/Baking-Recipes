package com.example.dan.baking_app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.objects.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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
    TextView mDescTextView;
    ImageButton mForward;
    ImageButton mBack;
    ProgressBar mProgressbar;
    long mVideoPosition;
    boolean mTwoPane;

    private static final String STATE_STEPS = "steps";
    private static final String STATE_ID = "id";
    private static final String STATE_URL = "url";
    private static final String STATE_DESCRIPTION = "description";
    private static final String STATE_VIDEO_POSITION = "video_position";

    public static final String UPDATE_WIDGET_BUTTON
            = "com.example.dan.baking_app.ACTION_UPDATE_WIDGET_FROM_BUTTON";

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mDescTextView = (TextView) rootview.findViewById(R.id.textview_step_description);
        mProgressbar = (ProgressBar) rootview.findViewById(R.id.progress_bar);

        mForward = (ImageButton) rootview.findViewById(R.id.imagebutton_next_step);
        mBack = (ImageButton) rootview.findViewById(R.id.imagebutton_prev_step);

        mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
        mPlayerView.setControllerShowTimeoutMs(1000);


        if (savedInstanceState != null) {

            mVideoUrl = savedInstanceState.getString(STATE_URL);
            mId = savedInstanceState.getInt(STATE_ID);
            mDescription = savedInstanceState.getString(STATE_DESCRIPTION);
            mSteps = savedInstanceState.getParcelableArrayList(STATE_STEPS);
            mVideoPosition = savedInstanceState.getLong(STATE_VIDEO_POSITION);
            initializePlayer(mVideoUrl);
            mExoPlayer.addListener(new MyExoPlayerStateListener());
            mExoPlayer.seekTo(mVideoPosition);
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT || mTwoPane) {
                mDescTextView.setVisibility(View.VISIBLE);

                mDescTextView.setText(mDescription);
                mForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mId < mSteps.size() - 1) {
                            mId++;
                            mDescTextView.setText(mSteps.get(mId).getDescription());
                            getMovie();
                        }
                    }
                });

                mBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mId > 0) {
                            mId--;
                            mDescTextView.setText(mSteps.get(mId).getDescription());
                            getMovie();
                        }
                    }
                });

            } else {
                mDescTextView.setVisibility(View.GONE);
                mForward.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            }
        } else {
            initializePlayer(mVideoUrl);

            mExoPlayer.addListener(new MyExoPlayerStateListener());

            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT || mTwoPane) {

                mDescTextView.setText(mDescription);

                mForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mId < mSteps.size() - 1) {
                            mId++;
                            mDescTextView.setText(mSteps.get(mId).getDescription());
                            getMovie();
                        }
                    }
                });

                mBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mId > 0) {
                            mId--;
                            mDescTextView.setText(mSteps.get(mId).getDescription());
                            getMovie();
                        }
                    }
                });
            } else {
                mForward.setVisibility(View.GONE);
                mBack.setVisibility(View.GONE);
                mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            }
        }
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
        mTwoPane = getArguments().getBoolean(RecipeDetailActivity.TWO_PANE_EXTRA);
    }

    public void initializePlayer(String url) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);
            if (!url.equals("")) {
                Uri mediaUri = Uri.parse(url);
                mExoPlayer.prepare(buildMediaSource(mediaUri), true, true);
            } else {
                mPlayerView.setVisibility(View.GONE);
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    public void getMovie() {
        mVideoUrl = mSteps.get(mId).getVideoUrl();
        if (!mVideoUrl.equals("")) {
            Uri mediaUri = Uri.parse(mVideoUrl);
            mExoPlayer.prepare(buildMediaSource(mediaUri), true, true);
        } else {
            mPlayerView.setVisibility(View.GONE);
        }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        long position = mExoPlayer.getCurrentPosition();
        outState.putParcelableArrayList(STATE_STEPS, mSteps);
        outState.putInt(STATE_ID, mId);
        outState.putString(STATE_DESCRIPTION, mDescription);
        outState.putString(STATE_URL, mVideoUrl);
        outState.putLong(STATE_VIDEO_POSITION, position);
        super.onSaveInstanceState(outState);

    }

    private class MyExoPlayerStateListener implements ExoPlayer.EventListener {
        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {}

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

        @Override
        public void onLoadingChanged(boolean isLoading) {}

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == ExoPlayer.STATE_BUFFERING) {
                mProgressbar.setVisibility(View.VISIBLE);
            } else if (playbackState == ExoPlayer.STATE_READY) {
                mProgressbar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {}

        @Override
        public void onPositionDiscontinuity() {}
    }
}
