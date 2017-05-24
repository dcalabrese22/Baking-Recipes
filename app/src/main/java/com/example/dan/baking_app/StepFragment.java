package com.example.dan.baking_app;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dan.baking_app.Interfaces.PassRecipeDataHandler;
import com.example.dan.baking_app.Interfaces.StepClickHandler;
import com.example.dan.baking_app.objects.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
    TextView mDescTextView;
    ImageButton mForward;
    ImageButton mBack;
    ProgressBar mProgressbar;
    long videoPosition;

    private static final String STATE_STEPS = "steps";
    private static final String STATE_ID = "id";
    private static final String STATE_URL = "url";
    private static final String STATE_DESCRIPTION = "description";
    private static final String STATE_VIDEO_POSITION = "video_position";

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_step_detail, container, false);
        if (savedInstanceState != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mProgressbar = (ProgressBar) rootview.findViewById(R.id.progress_bar);
                mDescTextView = (TextView) rootview.findViewById(R.id.textview_step_description);
                mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
                mPlayerView.setControllerShowTimeoutMs(1000);
                mVideoUrl = savedInstanceState.getString(STATE_URL);
                mId = savedInstanceState.getInt(STATE_ID);
                mDescription = savedInstanceState.getString(STATE_DESCRIPTION);
                mDescTextView.setText(mDescription);
                mSteps = savedInstanceState.getParcelableArrayList(STATE_STEPS);
                long position = savedInstanceState.getLong(STATE_VIDEO_POSITION);
                initializePlayer(mVideoUrl);
                mExoPlayer.seekTo(position);
            } else {
                mProgressbar = (ProgressBar) rootview.findViewById(R.id.progress_bar);
                mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
                mPlayerView.setControllerShowTimeoutMs(1000);
                mVideoUrl = savedInstanceState.getString(STATE_URL);
                mId = savedInstanceState.getInt(STATE_ID);
                long position = savedInstanceState.getLong(STATE_VIDEO_POSITION);
                initializePlayer(mVideoUrl);
                mExoPlayer.seekTo(position);
            }
        } else {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mProgressbar = (ProgressBar) rootview.findViewById(R.id.progress_bar);
                mDescTextView = (TextView) rootview.findViewById(R.id.textview_step_description);
                mDescTextView.setText(mDescription);

                mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
                mPlayerView.setControllerShowTimeoutMs(1000);
                initializePlayer(mVideoUrl);

                mExoPlayer.addListener(new ExoPlayer.EventListener() {
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
                            Log.d("STATE", "BUFFERING");
                        } else if (playbackState == ExoPlayer.STATE_READY) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Log.d("STATE", "READY");
                        }
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {}

                    @Override
                    public void onPositionDiscontinuity() {}
                });

                mForward = (ImageButton) rootview.findViewById(R.id.imagebutton_next_step);
                mBack = (ImageButton) rootview.findViewById(R.id.imagebutton_prev_step);

                mForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mId < mSteps.size() - 1) {
                            Log.d("NEXT", Integer.toString(mId));
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
                            Log.d("PREV", Integer.toString(mId));
                            mId--;
                            mDescTextView.setText(mSteps.get(mId).getDescription());
                            getMovie();
                        }
                    }
                });
            } else {
                mProgressbar = (ProgressBar) rootview.findViewById(R.id.progress_bar);

                mPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.media_player);
                mPlayerView.setControllerShowTimeoutMs(1000);
                initializePlayer(mVideoUrl);

                mExoPlayer.addListener(new ExoPlayer.EventListener() {
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
                            Log.d("STATE", "BUFFERING");
                        } else if (playbackState == ExoPlayer.STATE_READY) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Log.d("STATE", "READY");
                        }
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {}

                    @Override
                    public void onPositionDiscontinuity() {}
                });
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
    }

    public void initializePlayer(String url) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector(null);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "baking_app");
            if (!url.equals("")) {
                Uri mediaUri = Uri.parse(url);
                new LoadVideoTask().execute(mediaUri);
            }
        }
    }

    public void getMovie() {
        mVideoUrl = mSteps.get(mId).getVideoUrl();
        if (mVideoUrl.equals("")) {
            return;
        } else {
            Uri mediaUri = Uri.parse(mVideoUrl);
//            String userAgent = Util.getUserAgent(getContext(), "baking_app");
//            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
//                    new DefaultDataSourceFactory(getContext(), userAgent),
//                    new DefaultExtractorsFactory(), null, null);
            new LoadVideoTask().execute(mediaUri);
//            mExoPlayer.setPlayWhenReady(true);
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

    private class LoadVideoTask extends AsyncTask<Uri, Void, MediaSource> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MediaSource doInBackground(Uri... params) {
            String userAgent = Util.getUserAgent(getContext(), "baking_app");
            MediaSource mediaSource = new ExtractorMediaSource(params[0],
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            return  mediaSource;
        }

        @Override
        protected void onPostExecute(MediaSource mediaSource) {
            super.onPostExecute(mediaSource);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
}
