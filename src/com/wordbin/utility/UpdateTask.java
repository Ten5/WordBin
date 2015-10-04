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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateTask  extends AsyncTask<String,Void,String> {

	private Context context;
	private EditText sentence;

	public UpdateTask(Context context, EditText sentence) {
		this.context = context;
		this.sentence = sentence;
	}

	@Override
	protected String doInBackground(String... arg0) {     
		try {
			String word = (String)arg0[0];
			String sentence = (String)arg0[2];
			String user=(String)arg0[3].trim();
			String time=(String)arg0[4].trim();
			String link="http://dbtest.webuda.com/wordupdate.php";
			String data  = URLEncoder.encode("word", "UTF-8") + "=" + URLEncoder.encode(word, "UTF-8");
			data += "&" + URLEncoder.encode("sentence", "UTF-8") + "=" + URLEncoder.encode(sentence, "UTF-8");
			data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
			data += "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
			
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
			Log.e("------UPDATE-TASK FAILED--------", e.getMessage());
			return new String("There is a problem with the network connection.\nPlease try again.");
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();		
		if(result.equalsIgnoreCase("Word Updated Successfully") || result.equalsIgnoreCase("Success")) {
			sentence.setEnabled(false);
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(sentence.getWindowToken(), 0);
		}
	}
}