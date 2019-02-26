package com.adingx.chao.rangerplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

public class VideoPlayerActivity extends Activity {
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();

    private SimpleExoPlayerView exoPlayerView;
    private static SimpleExoPlayer exoPlayer;
    private long contentPosition = 0;
    private static final String PLAYING_POS = "PlayingPosition";

    void initPlayer()   {
        if(exoPlayer != null) {
            return ;
        }
        try {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());

            Intent intent = getIntent();
            String message = intent.getStringExtra(SecondActivity.EXTRA_MESSAGE);

            //String uri="asset:///output.mp4";
            //String uri="asset:///mkv-test.mkv";
            String uri = "asset:///" + message;
            DataSpec dataSpec = new DataSpec(Uri.parse(uri));
            final AssetDataSource fileDataSource = new AssetDataSource(this);
            try {
                fileDataSource.open(dataSpec);
            } catch (AssetDataSource.AssetDataSourceException e) {
                Log.e(TAG,"exoplayer error0 "+ e.toString());
                e.printStackTrace();
            }
            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return fileDataSource;
                }
            };
            Log.d(TAG, "exoplayer local folder: " + this.getFilesDir() + "/");

            MediaSource mediaSource = new ExtractorMediaSource(
                    fileDataSource.getUri(),
                    factory,
                    new DefaultExtractorsFactory(),
                    null, null
            );

            Log.d(TAG, "exoplayer seek pos: " + contentPosition);
            exoPlayer.seekTo(contentPosition);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }catch (Exception e){
            Log.e(TAG," exoplayer error "+ e.toString());
        }
    }

    private void updatePlayerState() {
        if (exoPlayer != null) {
            contentPosition = Math.max(0, exoPlayer.getContentPosition());
            Log.d(TAG, "updatePlayerState seek pos: " + contentPosition);
        }
    }

    private void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        exoPlayerView = findViewById(R.id.exo_player_view);
        if (savedInstanceState != null) {
            contentPosition = savedInstanceState.getLong(PLAYING_POS);
            Log.i(TAG, "onCreate restore pos: " + contentPosition);
        }
        Log.i(TAG, "onCreate ~~~");
        initPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayerView.setPlayer(exoPlayer);
        Log.i(TAG, "onResume ~~~");
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerView.setPlayer(null);
        Log.i(TAG, "onPause ~~~");
    }

    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
        Log.i(TAG, "onDestroy ~~~");
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        updatePlayerState();
        outState.putLong(PLAYING_POS, contentPosition);
        Log.i(TAG, "onSaveInstanceState ~~~");
    }
}
