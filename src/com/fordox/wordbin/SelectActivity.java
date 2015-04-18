package com.fordox.wordbin;

import java.io.File;

import com.wordbin.utility.DisplayListTask;
import com.wordbin.utility.DownloadTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectActivity extends Activity {

	Button play, enter, display, download;
	ProgressDialog progress;
	TextView tv;
	SQLiteDatabase db;
	String user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_activity);
		play = (Button)findViewById(R.id.play);
		enter = (Button)findViewById(R.id.enter_word);
		display = (Button)findViewById(R.id.display_list);
		download = (Button)findViewById(R.id.download);
		tv=(TextView)findViewById(R.id.textView1);
		Typeface faceTrack = Typeface.createFromAsset(getAssets(), "fonts/Track.ttf");
	    Typeface faceHomestead = Typeface.createFromAsset(getAssets(), "fonts/Homestead-Regular.ttf");
	    play.setTypeface(faceHomestead);
	    display.setTypeface(faceHomestead);
	    enter.setTypeface(faceHomestead);
	    download.setTypeface(faceHomestead);
	    
		
		progress = new ProgressDialog(SelectActivity.this);
		db = openOrCreateDatabase("Words", 0, null);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		user=b.getString("Username");
		tv.setText("Welcome\n" + b.getString("Username"));
		tv.setTypeface(faceTrack);
		enter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SelectActivity.this, WordActivity.class);
				i.putExtra("Username", user);
				startActivity(i);
			}
		});
		
		display.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String dataPath = getFilesDir().getPath();
				dataPath = dataPath.substring(0, dataPath.lastIndexOf("/")) + "/databases";
				File databases = new File(dataPath);
				if(!databases.exists())	{
					Toast.makeText(SelectActivity.this, "Please Download Database to begin!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Cursor c=null;
				try {
					c= db.rawQuery("select * from wordTable order by word", null);
					if(c.getCount()==0) {
						Toast.makeText(SelectActivity.this, "Please Download Database to begin!", Toast.LENGTH_SHORT).show();
						return;
					}						
				}
				catch(Exception e) {
					Toast.makeText(getApplicationContext(), "Please Download Database to begin..", Toast.LENGTH_LONG).show();
					return;
				}

				DisplayListTask dlt = new DisplayListTask(SelectActivity.this,user);
				dlt.execute("");
			}
		});
		
		download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				   
			       String sql_create = "create table wordTable ( word varchar(30) primary key, meaning varchar(60), sentence varchar(200) );";
			       try {
			    	   db.execSQL(sql_create);
			    	   Toast.makeText(SelectActivity.this, "Preparing words...", Toast.LENGTH_SHORT).show();
			       } catch(Exception e) {
			    	   Toast.makeText(SelectActivity.this, "Updating the list...", Toast.LENGTH_SHORT).show();
			       }
				   new DownloadTask(SelectActivity.this, db).execute("");
				}
		});
		
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String dataPath = getFilesDir().getPath();
				
				dataPath = dataPath.substring(0, dataPath.lastIndexOf("/")) + "/databases";
				File databases = new File(dataPath);
				if(!databases.exists())	{
					Toast.makeText(SelectActivity.this, "Please Download Database to begin!", Toast.LENGTH_SHORT).show();
					return;
				}
				@SuppressWarnings("unused")
				Cursor c=null;
				try {
					c= db.rawQuery("select * from wordTable order by word", null);
				}
				catch(Exception e) {
					Toast.makeText(getApplicationContext(), "Please download words.", Toast.LENGTH_LONG).show();
					return;
				}


				Intent i = new Intent(SelectActivity.this, Play.class);
				startActivity(i);
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			AlertDialog.Builder builder=new AlertDialog.Builder(SelectActivity.this);
			builder.setTitle("Exit Message");
			builder.setMessage("Are you sure to exit?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setNegativeButton("No",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.create().show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu; this adds items to the action bar if it is present.	
       getMenuInflater().inflate(R.menu.select_activity_menu, menu);
       return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		if(item.getItemId()==R.id.About)
			credits();
		if(item.getItemId()==R.id.Clear)
			clearDatabase();
		return super.onOptionsItemSelected(item);
	}
	
	public void credits() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Credits")
    	       .setMessage("Word Bin" + "\nVersion 2.0" + "\n\nThis is a free app and has been built solely for the purpose of entertainment.\n"
    	       		+ "Please do not reproduce this app for any monetory gains."
    	    		+ "\n\n©FordoX"
    	    		+ "\nContact us at: ten5dox@gmail.com");
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	               // User clicked OK button
    	        	   dialog.cancel();
    	           }
    	       });
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
	
	private void clearDatabase() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Confirm Clear")
    	       .setMessage("Are you sure you want to clear the bin?");
    	builder.setPositiveButton("Don't Clear", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   dialog.cancel();
    	           }
    	       });
    	builder.setNegativeButton("Clear Bin", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String sql_delete = "delete from wordTable";
			    try {
		    	   db.execSQL(sql_delete);
		    	   Toast.makeText(SelectActivity.this, "Bin Cleared", Toast.LENGTH_SHORT).show();
		       } catch(Exception e) {
		    	   Toast.makeText(SelectActivity.this, "Failed to clear the bin", Toast.LENGTH_SHORT).show();
		       }
			}
		});
    	AlertDialog dialog = builder.create();
    	dialog.show();	
	}
}
