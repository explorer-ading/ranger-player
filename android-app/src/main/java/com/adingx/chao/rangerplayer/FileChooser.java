package com.adingx.chao.rangerplayer;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.List;
import java.text.DateFormat; 
import android.os.Bundle; 
import android.app.ListActivity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FileChooser extends ListActivity {
	private static final String TAG = FileChooser.class.getSimpleName();
	private File currentDir;
    private FileArrayAdapter adapter;

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if( isExternalStorageReadable() ) {
            currentDir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS);
			//currentDir = new File("/sdcard/");
			//currentDir = new File("/storage/");
			fill(currentDir);
		}else {
			Toast.makeText(getApplicationContext(), "No permission to access Storage Directory.", Toast.LENGTH_LONG)
					.show();
		}
    }

    private void fill(File f)
    {
    	File[]dirs = f.listFiles(); 
		 this.setTitle("Current Dir: "+f.getName());
		 List<Item>dir = new ArrayList<>();
		 List<Item>fls = new ArrayList<>();
		 try{
			 for(File ff: dirs)  {
				Date lastModDate = new Date(ff.lastModified()); 
				DateFormat formater = DateFormat.getDateTimeInstance();
				String date_modify = formater.format(lastModDate);

				if(ff.isDirectory()){
					File[] fbuf = ff.listFiles(); 
					int buf = 0;
					if(fbuf != null){ 
						buf = fbuf.length;
					} else
						buf = 0;

					String num_item = String.valueOf(buf);
					if(buf == 0)
						num_item = num_item + " item";
					else
						num_item = num_item + " items";
					
					//String formated = lastModDate.toString();
					dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon")); 
				}
				else {
					fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
				}
			 }
		 }catch(Exception e) {
			 Log.e(TAG, "fill - " + e.getLocalizedMessage());
		 }

		 Collections.sort(dir);
		 Collections.sort(fls);
		 dir.addAll(fls);

		 if(!f.getName().equalsIgnoreCase("sdcard"))
			 dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));

		 adapter = new FileArrayAdapter(FileChooser.this,R.layout.activity_explorer,dir);
		 this.setListAdapter(adapter); 
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Item o = adapter.getItem(position);
		if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")) {
			currentDir = new File(o.getPath());
			fill(currentDir);
		}else {
			onFileClick(o);
		}
	}

	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

    private void onFileClick(Item o)  {
		String fileExt = getFileExt(o.getName());

		if( fileExt.equalsIgnoreCase("mp4") ||
				fileExt.equalsIgnoreCase("mp3") ||
				fileExt.equalsIgnoreCase("mkv") ) {
			Intent intent = new Intent(this, VideoPlayerActivity.class);
			intent.putExtra(VideoPlayerActivity.VIDEO_TYPE, "file");
			intent.putExtra(VideoPlayerActivity.LOCAL_FILE, currentDir.toString() + File.separator + o.getName());
			startActivity(intent);
		} else {
			Toast.makeText(this, "UNSUPPORT FORMAT: [" + fileExt + "] - " + o.getName(), Toast.LENGTH_SHORT).show();
		}
	}
}
