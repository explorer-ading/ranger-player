package com.adingx.chao.rangerplayer;

import android.app.Activity;
import android.content.Context;
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
import java.util.HashMap;
import java.util.List;

public class ExplorerActivity extends Activity {

    private static final String TAG = ExplorerActivity.class.getSimpleName();
    private ListView listView;

    // Defined Array values to show in ListView
    private ArrayList<String> listValues = new ArrayList<>();
    private String currentPath ;

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /* refer: https://www.vogella.com/tutorials/AndroidListView/article.html */
    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

    /* http://custom-android-dn.blogspot.com/2013/01/create-simple-file-explore-in-android.html */
    private void fillFileList(String path) {
        listValues.clear();
        listValues.add("..");
        if (isExternalStorageReadable())    {
            // path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
            Log.i(TAG, "fillFileList path: " + path);
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
            currentPath = path;
        }
    }

    private void listOnClick(int pos)  {
        File file;

        if(pos == 0) { // pos=0 mean that go up the folder
            file = new File(currentPath);
            if( file.getParentFile().exists() ) {
                fillFileList( file.getParentFile().getAbsolutePath() );
            } else {
                Log.i(TAG, "No parent path. ");
            }
        } else {
            File filesInDirectory[] = (new File(currentPath)).listFiles();
            file = filesInDirectory[pos - 1];
            Log.i(TAG, "listOnClick getAbsolutePath: " + file.getAbsolutePath());
            Log.i(TAG, "listOnClick getAbsoluteFile: " + file.getAbsoluteFile());

            if(file.isDirectory()) {
                Log.i(TAG, "it's a directory. ");
                fillFileList(file.getAbsolutePath());
            }else if(file.isFile()) {
                // FIXME
                Log.i(TAG, "it's a file. ");
                Log.i(TAG, "listOnClick file: " + file.getAbsoluteFile());
            }else {
                Log.i(TAG, "what's that... ");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        Log.i(TAG, "onCreate ~~");
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
        //String path = "/sdcard/";
        
        Log.i(TAG, "Download path: " + path);
        fillFileList(path);

        listView = findViewById(R.id.listView2);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, listValues);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // ListView Clicked item index
                final int itemPosition = position;

                // ListView Clicked item value
                final String  itemValue    = (String) listView.getItemAtPosition(position);

                Log.d(TAG, "position: " + itemPosition + " ,Value: " + itemValue);
                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();

                view.animate().setDuration(1000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                listOnClick(itemPosition);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }
        });
    }
}
