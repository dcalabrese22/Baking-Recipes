package com.example.dan.baking_app;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.helpers.Constants;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

/**
 * Fragment for displaying a single recipe step
 */
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

    private String TAG = "Step Fragment";

    private static final String USER_AGENT = "ua";
    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mDescTextView = (TextView) rootview.findViewById(R.id.textview_step_description);
        mProgressbar = (ProgressBar) rootview.findViewById(R.id.progress_bar);

        mForward = (ImageButton) rootview.findViewById(R.id.imagebutton_next_step);
        mBack = (ImageButton) rootview.findViewById(R.id.imagebutton_prev_step);

        mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
        mPlayerView.setControllerShowTimeoutMs(1000);


        if (savedInstanceState != null) {
            mVideoUrl = savedInstanceState.getString(Constants.STATE_URL);
            mId = savedInstanceState.getInt(Constants.STATE_ID);
            mDescription = savedInstanceState.getString(Constants.STATE_DESCRIPTION);
            mSteps = savedInstanceState.getParcelableArrayList(Constants.STATE_STEPS);
            mVideoPosition = savedInstanceState.getLong(Constants.STATE_VIDEO_POSITION);
            initializePlayer(mVideoUrl, mVideoPosition);

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
            initializePlayer(mVideoUrl, mVideoPosition);

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
        mDescription = getArguments().getString(Constants.DESC_STEP_EXTRA);
        mVideoUrl = getArguments().getString(Constants.URL_STEP_EXTRA);
        mId = getArguments().getInt(Constants.ID_STEP_EXTRA);
        mTwoPane = getArguments().getBoolean(Constants.TWO_PANE_EXTRA);
    }

    /**
     * Initializes an ExoPlayer with a media source
     *
     * @param url String url source
     */
    public void initializePlayer(String url, long videoPosition) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mExoPlayer.addListener(new MyExoPlayerStateListener());
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);
            if (!url.equals("")) {
                Uri mediaUri = Uri.parse(url);
                mExoPlayer.prepare(buildMediaSource(mediaUri), true, true);
                mExoPlayer.seekTo(videoPosition);
            } else {
                mPlayerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Builds a media source for ExoPlayer
     *
     * @param uri Uri of source
     * @return MediaSource
     */
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory(USER_AGENT),
                new DefaultExtractorsFactory(), null, null);
    }

    /**
     * Gets a Step's movie if there is on. Used for the forward and back buttons when cycling
     * through steps
     */
    public void getMovie() {
        mVideoUrl = mSteps.get(mId).getVideoUrl();
        if (!mVideoUrl.equals("")) {
            mProgressbar.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            Uri mediaUri = Uri.parse(mVideoUrl);
            mExoPlayer.prepare(buildMediaSource(mediaUri), true, true);
        } else {
            mProgressbar.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called " + Long.toString(mVideoPosition));
        initializePlayer(mVideoUrl, mVideoPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
        releasePlayer();
    }

    public void releasePlayer() {
        mVideoPosition = mExoPlayer.getCurrentPosition();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mVideoPosition = mExoPlayer.getCurrentPosition();
        outState.putParcelableArrayList(Constants.STATE_STEPS, mSteps);
        outState.putInt(Constants.STATE_ID, mId);
        outState.putString(Constants.STATE_DESCRIPTION, mDescription);
        outState.putString(Constants.STATE_URL, mVideoUrl);
        outState.putLong(Constants.STATE_VIDEO_POSITION, mVideoPosition);
        Log.d("saved instance state", outState.toString());
    }

    /**
     * Custom ExoPlayer Eventlistener for determining when to show the progress bar
     */
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
