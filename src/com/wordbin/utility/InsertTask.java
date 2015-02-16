package com.wordbin.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class InsertTask  extends AsyncTask<String,Void,String> {

	private Context context;
	private EditText word, meaning, sentence;
	String user;

	public InsertTask(Context context, EditText word, EditText meaning, EditText sentence,String user) {
		this.context = context;
		this.word = word;
		this.meaning = meaning;
		this.sentence = sentence;
		this.user=user;
	}

	@Override
	protected String doInBackground(String... arg0) {     
		try {
			String word = (String)arg0[0];
			String meaning = (String)arg0[1];
			String sentence = (String)arg0[2];
			
			
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			String time=today.monthDay+ "/" +(today.month+1)+"/"+today.year;
			
			
			Log.d("User Time",user + " : " + time);
			String link="http://dbtest.webuda.com/word.php";
			String data  = URLEncoder.encode("word", "UTF-8") + "=" + URLEncoder.encode(word, "UTF-8");
			data += "&" + URLEncoder.encode("meaning", "UTF-8") + "=" + URLEncoder.encode(meaning, "UTF-8");
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
		} catch(Exception e) {
			Log.e("--INSERT-TASK--", e.getMessage());
			return new String("There is a problem with the network connection.\nPlease try again.");
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();		
		if(result.equalsIgnoreCase("Word Inserted Successfully") || result.equalsIgnoreCase("Success")) {
			word.setText("");
			meaning.setText("");
			sentence.setText("");
			word.requestFocus();
			word.setFocusableInTouchMode(true);
		}
	}
}