package com.adingx.chao.rangerplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate ~~");
    }

    public void launchSettings(View view) {
        Log.i(TAG, "launchSettings ~~");
    }

    public void launchFileWindow(View view) {
        Log.i(TAG, "launchFileWindow ~~");
    }

    public void launchServer(View view) {
        Log.i(TAG, "launchServer ~~");
    }
}
