package com.fordox.wordbin;

import com.wordbin.utility.UpdateTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends Activity {

	EditText word, meaning, sentence;
	Button edit, save, report;
	SQLiteDatabase db;
	String user,time;
	int report_word=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		word = (EditText)findViewById(R.id.word);
		meaning = (EditText)findViewById(R.id.meaning);
		sentence = (EditText)findViewById(R.id.sentence);
		edit = (Button)findViewById(R.id.editButton);
		save = (Button)findViewById(R.id.saveButton);
		report = (Button)findViewById(R.id.report);
		Typeface faceHomestead = Typeface.createFromAsset(getAssets(), "fonts/Homestead-Regular.ttf");
		edit.setTypeface(faceHomestead);
		save.setTypeface(faceHomestead);
		report.setTypeface(faceHomestead);


		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		time=today.monthDay+ "/" +(today.month+1)+"/"+today.year;

		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		user=b.getString("Username");


		word.setText(b.getString("word"));
		meaning.setText(b.getString("meaning"));
		sentence.setText(b.getString("sentence"));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit.setEnabled(false);
				save.setEnabled(true);
				save.clearFocus();
				sentence.setEnabled(true);
				sentence.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(sentence, InputMethodManager.SHOW_IMPLICIT);
			}					
		});
		report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				report_word=1;
			}					
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit.setEnabled(true);
				save.setEnabled(false);
				sentence.setEnabled(false);
				db = openOrCreateDatabase("Words", 0, null);
				String wordString = word.getText().toString().trim();
				String meaningString = meaning.getText().toString().trim();
				String sentenceString = sentence.getText().toString().trim();
				String replaceMeaning = meaningString.replace("'", "''");
				String replaceSentence = sentenceString.replace("'", "''");
				String sql_update = "update wordTable set"
						+ " word='"+wordString+"',"
						+ " meaning='"+replaceMeaning+"',"
						+ " sentence='"+replaceSentence+"'"
						+ " where word = '"+wordString+"';";
				try {
					db.execSQL(sql_update);
					Toast.makeText(Edit.this, "Successfully Edited!", Toast.LENGTH_SHORT).show();
				} catch(Exception e) {
					Toast.makeText(Edit.this, "Edit Failed!\n", Toast.LENGTH_SHORT).show();
					Log.e("------EDIT FAILED--------", e.getMessage());
				}
				db.close();
				new UpdateTask(Edit.this, sentence,report_word).execute(wordString, meaningString, sentenceString,user,time);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			Edit.this.finish();
		if(item.getItemId()==R.id.About)
			credits();			
		return super.onOptionsItemSelected(item);
	}

	public void credits() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Credits")
		.setMessage("Word Bin" + "\nVersion 2.0" + "\n\nThis is a free app and has been built solely for the purpose of entertainment.\n"
				+ "Please do not reproduce this app for any monetory gains."
				+ "\n\n�FordoX"
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
