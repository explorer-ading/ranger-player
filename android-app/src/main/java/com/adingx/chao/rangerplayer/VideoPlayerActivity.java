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
    private static final String TAG = MainActivity.class.getSimpleName();

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        exoPlayerView = findViewById(R.id.exo_player_view);

        try {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
            exoPlayerView.setPlayer(exoPlayer);

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
                Log.e(TAG," exoplayer error0 "+ e.toString());
                e.printStackTrace();
            }
            DataSource.Factory factory = new DataSource.Factory() {
                @Override
                public DataSource createDataSource() {
                    return fileDataSource;
                }
            };
            Log.d(TAG, "exoplayer error " + this.getFilesDir() + "/");

            MediaSource mediaSource = new ExtractorMediaSource(
                    fileDataSource.getUri(),
                    factory,
                    new DefaultExtractorsFactory(),
                    null, null
            );

            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }catch (Exception e){
            Log.e(TAG," exoplayer error "+ e.toString());
        }
    }
}
