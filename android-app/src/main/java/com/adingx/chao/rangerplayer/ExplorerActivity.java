package com.adingx.chao.rangerplayer;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExplorerActivity extends Activity {

    private static final String TAG = ExplorerActivity.class.getSimpleName();
    private ListView listView;

    // Defined Array values to show in ListView
    private ArrayList<String> listValues = new ArrayList<String>();

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /*
    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //inFiles.addAll(getListFiles(file));
                inFiles.add(file);
            } else {
                //if(file.getName().endsWith(".csv")){
                    inFiles.add(file);
                //}
            }
        }
        return inFiles;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        Log.i(TAG, "onCreate ~~");
        String path;
        listValues.add("..");
        if (isExternalStorageReadable())    {
            path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
            Log.i(TAG, "Download path: " + path);
            File file = new File(path);
            File filesInDirectory[] = file.listFiles();
            if (filesInDirectory != null) {
                Log.i(TAG, "files numbs: " + filesInDirectory.length);
                for (int i = 0; i<filesInDirectory.length;i++) {
                    listValues.add(filesInDirectory[i].getName());
                    Log.i(TAG, "file: " + filesInDirectory[i].getName());
                }
            }else {
                Log.i(TAG, "no files in path: " + path);
            }
        }

        listView = (ListView)findViewById(R.id.listView2);
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
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


}
