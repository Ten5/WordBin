package com.fordox.wordbin;

import java.util.Locale;

import com.wordbin.utility.InsertTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WordActivity extends Activity {

	private EditText word, meaning, sentence;
	String user;
	Button insert;

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_activity);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		user=b.getString("Username");
		word = (EditText)findViewById(R.id.word);
		meaning = (EditText)findViewById(R.id.meaning);
		sentence = (EditText)findViewById(R.id.sentence);
		insert = (Button)findViewById(R.id.insert);
		Typeface faceHomestead = Typeface.createFromAsset(getAssets(), "fonts/Homestead-Regular.ttf");
		insert.setTypeface(faceHomestead);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.insword, menu);
		return true;
	}

	public void insertWord(View view) {
		String wordString = word.getText().toString().trim().toLowerCase(Locale.US);
		String meaningString = meaning.getText().toString().trim();
		String sentenceString = sentence.getText().toString().trim();
		if(sentenceString.endsWith(".") || sentenceString.endsWith("!") || sentenceString.endsWith("?"))
			sentenceString += ".";
		if(wordString.compareTo("")==0 || meaningString.compareTo("") == 0) {
			Toast.makeText(WordActivity.this, "Please fill the word and meaning fields!", Toast.LENGTH_SHORT).show();
			return;
		}
		new InsertTask(WordActivity.this, word, meaning, sentence,user).execute(wordString, meaningString, sentenceString);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		if(item.getItemId()==R.id.aboutUs)
			credits();
		if(item.getItemId()==R.id.ins) {
			String wordString = word.getText().toString().trim();
			String meaningString = meaning.getText().toString().trim();
			String sentenceString = sentence.getText().toString().trim();
			if(wordString.compareTo("")==0 || meaningString.compareTo("") == 0) {
				Toast.makeText(WordActivity.this, "Please fill the word and meaning fields!", Toast.LENGTH_SHORT).show();
				return false;
			}
			new InsertTask(WordActivity.this, word, meaning, sentence,user).execute(wordString, meaningString, sentenceString);
		}
		return super.onOptionsItemSelected(item);
	}

	public void credits() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Credits")
		.setMessage("Word Bin" + "\nVersion 3.0" + "\n\nThis is a free app and has been built solely for the purpose of entertainment.\n"
				+ "Please do not reproduce this app for any monetory gain."
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
}
