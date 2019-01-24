package com.adingx.chao.rangerplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ServerActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Log.i(TAG, "onCreate ~~");
        TextView textView = (TextView) findViewById(R.id.text_message);
        textView.setText("http://127.0.0.1:8089");
    }
}
