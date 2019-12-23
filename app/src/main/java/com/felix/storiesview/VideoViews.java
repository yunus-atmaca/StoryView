package com.felix.storiesview;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felix.storiesview.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoViews extends Fragment implements Player.EventListener {
    private static final String TAG = "VideoViews";
    private View view;

    private PlayerView playerView;
    private SimpleExoPlayer player;

    private String videoUri;

    private VideoListener mListener;

    public VideoViews() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoUri = getArguments().getString("videoUri");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_views, container, false);

        playerView = view.findViewById(R.id.player_view);
        return view;
    }

    public void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
            playerView.setPlayer(player);
            preparePlayer();
        }
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
            playerView.setPlayer(null);
        }
    }

    private void preparePlayer(){
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "story_view"));
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUri));
        player.addListener(this);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_ENDED:
                mListener.onEnd();
                break;
            case Player.STATE_READY:
                mListener.onReady(player.getDuration());
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    public void setVideoListener(VideoListener listener){
        mListener = listener;
    }
    public interface VideoListener{
        void onEnd();
        void onReady(long duration);
    }
}
