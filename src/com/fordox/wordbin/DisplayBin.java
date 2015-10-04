package com.fordox.wordbin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.wordbin.utility.NewAdapter;

public class DisplayBin extends Activity {
	
	ListView list;
	String arr[], meaning[];
	int marks[];
	SQLiteDatabase word;
	NewAdapter adapter;
	String user;
	int connection;
	private static final String LIST_STATE = "listState";
	private Parcelable mListState = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.display_layout);
		list = (ListView)findViewById(R.id.listView1);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		user=b.getString("Username");
		connection = b.getInt("Connection");
		
		/*word = openOrCreateDatabase("Words", 0, null);		
		Cursor c=null;		
		
		try {
			c= word.rawQuery("select * from wordTable order by word", null);
		}
		catch(Exception e) {
			Toast.makeText(getApplicationContext(), "No words found.", Toast.LENGTH_LONG).show();
			DisplayBin.this.finish();
		}
		if(c!=null) {
			arr = new String[c.getCount()];
			marks = new int[c.getCount()];
			meaning = new String[c.getCount()];
			int i=0;
			while(c.moveToNext()) {
				arr[i] = c.getString(0);
				meaning[i] = c.getString(1);
				marks[i++] = c.getInt(2);
			}
			c.close();			
		}		
		word.close();*/
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		checkFirstRun();
		if(connection == 1) {
			AdView mAdView = (AdView) findViewById(R.id.adViewDisplay);
			AdRequest adRequest = new AdRequest.Builder().build();
				//.addTestDevice(com.google.ads.AdRequest.TEST_EMULATOR)
				
			if(mAdView==null)
				Toast.makeText(DisplayBin.this, "View Null", Toast.LENGTH_LONG).show();
			if(adRequest==null)
				Toast.makeText(DisplayBin.this, "Request Null", Toast.LENGTH_LONG).show();
			mAdView.loadAd(adRequest);
			onSetContentView();
		}
	}
	
	protected void onSetContentView() {
		AdView adView = (AdView) findViewById(R.id.adViewDisplay);
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(com.google.ads.AdRequest.TEST_EMULATOR)
			.build();
		adView.loadAd(adRequest);	   
		adView.setVisibility(View.VISIBLE);
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
       
       
       //Search Function
       
       SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

       searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       //searchView.setIconifiedByDefault(false);  

       SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextChange(String newText) {
               // this is your adapter that will be filtered
               adapter.getFilter().filter(newText);
               return true;
           }
           
           @Override
           public boolean onQueryTextSubmit(String query) {
               // this is your adapter that will be filtered
               adapter.getFilter().filter(query);
               return true;
           }
       };
       searchView.setOnQueryTextListener(textChangeListener);       
       return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		if(item.getItemId()==R.id.help)
			startActivity(new Intent(DisplayBin.this, DisplayBinHelp.class));
		return super.onOptionsItemSelected(item);
	}
	
	public void credits() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(DisplayBin.this);
    	builder.setTitle("Credits")
    	       .setMessage("Word Bin" + "\nVersion 3.0" +
    	    		   "\n\nThis is a free app and has been built solely for the purpose of entertainment.\n"
    	       		+ "Please do not reproduce this app for any monetory gain."
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
		/*word = openOrCreateDatabase("Words", 0, null);
		Cursor c = null;
		try {
			c= word.rawQuery("select bookmark from wordTable order by word", null);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			DisplayBin.this.finish();
		}
		if(c!=null) {
			int i=0;
			while(c.moveToNext())
				marks[i++] = c.getInt(0);
			c.close();			
		}
		word.close();*/
		
		adapter = new NewAdapter(DisplayBin.this, user);
		list.setAdapter(adapter);
		if (mListState != null)
	        list.onRestoreInstanceState(mListState);
	    mListState = null;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		/*word = openOrCreateDatabase("Words", 0, null);
		Boolean bookmarks[] = adapter.getBookmarks();
		for(int i=0; i<bookmarks.length; i++) {
			String updateWord="";
			if(bookmarks[i])
				updateWord = "update wordTable set bookmark=1 where word='"+arr[i]+"';";
			else
				updateWord = "update wordTable set bookmark=0 where word='"+arr[i]+"';";
			try {
				word.execSQL(updateWord);
				System.out.println("Updated Successfully");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		word.close();*/
    }
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);
	    mListState = state.getParcelable(LIST_STATE);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    mListState = list.onSaveInstanceState();
	    state.putParcelable(LIST_STATE, mListState);
	}
	
	//Checking whether first run of application and display help.
	private void checkFirstRun() {

	    final String PREFS_NAME = "MyPrefsFile";
	    final String PREF_VERSION_CODE_KEY = "version_code";
	    final int DOESNT_EXIST = -1;


	    // Get current version code
	    int currentVersionCode = 0;
	    try {
	        currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
	    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
	        // handle exception
	        Log.e("PackageMissing", "Package Not Found");
	        return;
	    }

	    // Get saved version code
	    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
	    int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

	    // Check for first run or upgrade
	    if (currentVersionCode == savedVersionCode) {
	    	// This is just a normal run
	        return;
	    } else {
	    	startActivity(new Intent(DisplayBin.this, DisplayBinHelp.class));
	    }
	    // Update the shared preferences with the current version code
	    prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();
	}
}