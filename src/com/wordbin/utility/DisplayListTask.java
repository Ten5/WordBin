package com.wordbin.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fordox.wordbin.DisplayBin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DisplayListTask extends AsyncTask<String, Void, String> {

	private Context context;
	protected ArrayList<CharSequence> word_List = new ArrayList<CharSequence>(), 
			meaning_List = new ArrayList<CharSequence>(), 
			sentence_List = new ArrayList<CharSequence>();
	SQLiteDatabase word;
String user;
	public DisplayListTask(Context context, String user) {
		this.context = context;
		this.user =user;
	}

	@Override
	protected String doInBackground(String... args) { 
		try {
			String link="http://dbtest.webuda.com/wordlist.php";
			URL url = new URL(link);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while((line = reader.readLine()) != null) {
				sb.append(line);
				break;
			}
			return sb.toString();
		}catch(Exception e) {
			Log.e("------DISPLAY-LIST-TASK FAILED--------", e.getMessage());
			return new String("There is a problem with the network connection.\nPlease try again.");
		}
	}

	@Override
	protected void onPostExecute(String result) {
		try {			
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_object = jArray.getJSONObject(i);                                                        
				word_List.add(json_object.getString("word"));				
				meaning_List.add(json_object.getString("meaning"));
				sentence_List.add(json_object.getString("sentence"));	
			}
		} catch (Exception e)	{
			Log.e("Error", "" + e);
			Toast.makeText(context,"Error" +e.getMessage(), Toast.LENGTH_LONG).show();
		}
		Intent i = new Intent(context, DisplayBin.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putCharSequenceArrayListExtra("arraylist", word_List);
		i.putExtra("Username", user);
		context.startActivity(i);
	}
}
