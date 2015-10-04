package com.wordbin.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ReportTask  extends AsyncTask<String,Void,String> {

	private Context context;
	int report_word;

	public ReportTask(Context context, int report_word) {
		this.context = context;
		this.report_word = report_word;
	}

	@Override
	protected String doInBackground(String... arg0) {     
		try {
			String word = (String)arg0[0];
			String link="http://dbtest.webuda.com/wordreport.php";
			String data  = URLEncoder.encode("word", "UTF-8") + "=" + URLEncoder.encode(word, "UTF-8");
			data += "&" + URLEncoder.encode("report", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(report_word), "UTF-8");
			
			URL url = new URL(link);
			URLConnection conn = url.openConnection(); 
			conn.setDoOutput(true); 
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
			wr.write( data ); 
			wr.flush(); 
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
			Log.e("------REPORT-TASK FAILED--------", e.getMessage());
			return new String("There is a problem with the network connection.\nPlease try again.");
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();		
		if(result.equalsIgnoreCase("Word Reported Successfully") || result.equalsIgnoreCase("Success"))
			Toast.makeText(context, "Reported Succesfully.\nPlease update the bin in a few days.\nThank you for your report.", Toast.LENGTH_SHORT).show();
	}
}