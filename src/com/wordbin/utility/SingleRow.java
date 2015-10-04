package com.wordbin.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SingleRow {
	String word;
	String meaning;
	private boolean bookmark;
	
	public SingleRow(String w, String m, Boolean b) {
		word = w;
		meaning = m;
		bookmark = b;
	}
	
	public void setBookmark(Boolean bookmark, Context context, String word) {
		SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
		String updateWord="";
		if(bookmark)
			updateWord = "update wordTable set bookmark=1 where word='"+word+"';";
		else
			updateWord = "update wordTable set bookmark=0 where word='"+word+"';";
		try {
				wordTable.execSQL(updateWord);
				Log.i("UpdateBookmark", "Updated Successfully");
	    } catch (Exception e) {
	    	Log.e("UpdateBookmark", "Update Failed");
	    }
	    wordTable.close();
	    
		this.bookmark = bookmark;
	}
	
	public Boolean getBookmark(Context context, String word) {
		SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
		Cursor c = null;
		try {
			c= wordTable.rawQuery("select bookmark from wordTable where word = '"+word+"'", null);
		}
		catch(Exception e) {
			Log.e("GetBookmark", "Error in retrieving bookmark.");
		}
		if(c!=null) {
			c.moveToNext();
			if(c.getInt(0) > 0)
				bookmark = true;
			else
				bookmark = false;
			c.close();
		}
		wordTable.close();
		return bookmark;
	}
}