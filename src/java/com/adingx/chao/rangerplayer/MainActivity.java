package com.adingx.chao.rangerplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.adingx.chao.rangerplayer.extra.MESSAGE";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate ~~");
    }

    public void launchSettings(View view) {
        Log.i(TAG, "launchSettings ~~");
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "setting ready ? ");
        startActivity(intent);
    }

    public void launchFileWindow(View view) {
        Log.i(TAG, "launchFileWindow ~~");

        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void launchServer(View view) {
        Log.i(TAG, "launchServer ~~");
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);
    }
}
