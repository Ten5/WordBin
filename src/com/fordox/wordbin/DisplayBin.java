package com.fordox.wordbin;

import com.wordbin.utility.NewAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayBin extends Activity {
	
	ListView list;
	String arr[], meaning[];
	Boolean marks[];
	SQLiteDatabase word;
	NewAdapter adapter;
	String user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_layout);
		list = (ListView)findViewById(R.id.listView1);
		word = openOrCreateDatabase("Words", 0, null);	
		Cursor c=null;
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		user=b.getString("Username");
		
		try {
			c= word.rawQuery("select * from wordTable order by word", null);
		}
		catch(Exception e) {
			Toast.makeText(getApplicationContext(), "Please download words.", Toast.LENGTH_LONG).show();
			DisplayBin.this.finish();
		}
		if(c!=null) {
			arr = new String[c.getCount()];
			marks = new Boolean[c.getCount()];
			meaning = new String[c.getCount()];
			int i=0;
			while(c.moveToNext()) {
				arr[i] = c.getString(0);
				meaning[i++] = c.getString(1);
			}
			c.close();			
		}
		
		word.close();		
		getActionBar().setDisplayHomeAsUpEnabled(true);		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	   super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.display_list_menu, menu);
       MenuItem item = menu.findItem(R.id.count);
       if(adapter != null)
    	   item.setTitle("Total Words:\t" + (adapter.getCount()));
       else
    	   item.setTitle("Total Words:\t" + (marks.length));
       return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
	
	public void credits() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(DisplayBin.this);
    	builder.setTitle("Credits")
    	       .setMessage("Word Bin" + "\nVersion 2.0" +
    	    		   "\n\nThis is a free app and has been built solely for the purpose of entertainment.\n"
    	       		+ "Please do not reproduce this app for any monetory gains."
    	    		+ "\n\n©FordoX"
    	    		+ "\nContact us at: ten5dox@gmail.com");
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   dialog.cancel();
    	           }
    	       });
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp =  getSharedPreferences("Bookmarks", MODE_PRIVATE);
		for(int i=0; i < arr.length; i++)
			marks[i] = sp.getBoolean("bookmark"+i, false);
		
		adapter = new NewAdapter(DisplayBin.this, arr, meaning, marks, user);
		list.setAdapter(adapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences sp = getSharedPreferences("Bookmarks", MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		
		Boolean bookmarks[] = adapter.getBookmarks();
		for(int i=0; i<bookmarks.length; i++)
			ed.putBoolean("bookmark"+i, bookmarks[i]);
		ed.commit();
    }
}