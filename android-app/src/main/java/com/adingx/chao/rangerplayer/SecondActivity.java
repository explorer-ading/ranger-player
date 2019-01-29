package com.adingx.chao.rangerplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SecondActivity extends Activity {
    public static final String EXTRA_MESSAGE = "com.adingx.chao.rangerplayer.extra.VideoFile";
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;

    // Defined Array values to show in ListView
    private String[] listValues = new String[] {
            "output.mp4",
            "mkv-test.mkv"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.i(TAG, "onCreate ~~");

        boolean bExtStorReadable = this.isExternalStorageReadable();
        Log.i(TAG, "bExtStorReadable: "  + bExtStorReadable);

        listView = (ListView)findViewById(R.id.listView);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listValues);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                Log.d(TAG, "position: " + itemPosition + " ,Value: " + itemValue);
                // Show Alert
                /*
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
                */
                Intent intent = new Intent(parent.getContext(), VideoPlayerActivity.class);
                intent.putExtra(EXTRA_MESSAGE, itemValue);
                startActivity(intent);
            }
        });
    }
    public void runVideoPlayer(int idx) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra(EXTRA_MESSAGE, listValues[idx]);
        startActivity(intent);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }
}
