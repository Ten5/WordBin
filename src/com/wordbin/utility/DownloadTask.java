package com.wordbin.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<String, Integer, String> {

	private Context context;
	protected SQLiteDatabase db;
	ProgressDialog progress;
	private int totalCount;
	
	public DownloadTask(Context context, SQLiteDatabase db) {
		this.context = context;
		this.db = db;
		progress = new ProgressDialog(this.context);
	}
	
	@Override
	protected void onPreExecute() {		
		progress.setMessage("Please Wait.\nDownloading Words....");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setMax(100);
        progress.setIndeterminate(false);
        progress.show();
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
		} catch(Exception e) {
			Log.e("------DOWNLOAD-TASK FAILED--------", e.getMessage());
			return new String("There is a problem with the network connection.\nPlease try again.");
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		try {			
			JSONArray jArray = new JSONArray(result);
			totalCount = jArray.length()-1;			
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_object = jArray.getJSONObject(i);
				String replaceMeaning = json_object.getString("meaning").replace("'", "''");
				String replaceSentence = json_object.getString("sentence").replace("'", "''");
				String sql_insert = "insert into wordTable values('"+json_object.getString("word")+"','"
						+replaceMeaning+"','"
						+replaceSentence+"', 0);";
				progress.setProgress((int) ( (double) i / totalCount) * 100);
				try {
					db.execSQL(sql_insert);
				} catch (Exception e)	{
					
				}
			}
			if(progress.getProgress() >= progress.getMax()) {
				progress.dismiss();
				Toast.makeText(context, "Downloading Finished!", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e)	{
			Log.e("Error", "" + e);
			Toast.makeText(context, "Updating the list.", Toast.LENGTH_SHORT).show();
		}		
	}
}
