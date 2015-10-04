package com.fordox.wordbin;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Play extends Activity {
	LinearLayout linear1, linear2, back; 
	TextView word_header, meaning_header, word, meaning;
	Button next, prev;
	String[] headers={"Let's try this one!","Do you know the meaning of this word?","Get this right, it is very easy.","What about this one?", "You must be knowing this!", "Here's the next one:"};
	String[] colors={"#e67e22","#8e44ad","#2c3e50","#27ae60", "#99494f", "#4f4234"};
	String[] meanings={"Got it?","Here is the meaning:","Wasn't it easy?","It means:","This was simple!", "Check out the meaning:"};
	SQLiteDatabase db;
	Cursor c;
	int randomNum;
	Random rand = new Random();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.play);
		back = (LinearLayout)findViewById(R.id.back);
		linear1=(LinearLayout)findViewById(R.id.linear1);
		linear2=(LinearLayout)findViewById(R.id.linear2);
		word_header=(TextView)findViewById(R.id.tv_city1);
		meaning_header=(TextView)findViewById(R.id.tv_city2);
		word=(TextView)findViewById(R.id.tv_word);
		meaning=(TextView)findViewById(R.id.tv_meaning);
		next = (Button)findViewById(R.id.Button_next);
		prev = (Button)findViewById(R.id.Button_previous);
		Typeface faceTrack = Typeface.createFromAsset(getAssets(), "fonts/Track.ttf");
	    word_header.setTypeface(faceTrack);
	    meaning_header.setTypeface(faceTrack);
	    next.setTypeface(faceTrack);
	    prev.setTypeface(faceTrack);
	    randomNum = 0;
		
	    getActionBar().setDisplayHomeAsUpEnabled(true);

		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);

				vf.setAnimation(AnimationUtils.loadAnimation(view.getContext(),	R.anim.right_to_left));
				back.setBackground(linear1.getBackground());
				
				int num=randomNum;
				while(num == randomNum)
					num = rand.nextInt(6);
				randomNum = num;
				meaning_header.setText(meanings[randomNum]);
				meaning.setText("Meaning:\n"+c.getString(1)+"\n\nSentence:\n"+c.getString(2));
				linear2.setBackgroundColor(Color.parseColor(colors[randomNum]));
				vf.showNext();
			}
		});

		prev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ViewFlipper vf = (ViewFlipper)findViewById(R.id.details);
				vf.setAnimation(AnimationUtils.loadAnimation(view.getContext(),	R.anim.left_to_right));
				back.setBackground(linear2.getBackground());
				if(!c.moveToNext()) {
					Toast.makeText(Play.this, "That was it!\nThank You for Playing.\nPlease add more words to make it more interesting!", Toast.LENGTH_LONG).show();
					new Thread() {
						public void run() {
							try {
								sleep(200);
							} catch (InterruptedException e) {
								
							}
							Play.this.finish();
						};
					}.start();
				}
				
				int num=randomNum;
				while(num == randomNum)
					num = rand.nextInt(6);
				randomNum = num;
				word_header.setText(headers[randomNum]);
				word.setText(c.getString(0));
				linear1.setBackgroundColor(Color.parseColor(colors[randomNum]));
				vf.showPrevious();
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		c.close();
		db.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		db = openOrCreateDatabase("Words", 0, null);
		c = db.rawQuery("select * FROM wordTable order by random()", null);
		c.moveToNext();
		word.setText(c.getString(0));
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		if(item.getItemId()==R.id.About)
			credits();			
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	   // Inflate the menu; this adds items to the action bar if it is present.
	   getMenuInflater().inflate(R.menu.main, menu);
	   return true;
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
