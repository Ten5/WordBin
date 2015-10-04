package com.fordox.wordbin;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayBinHelp extends Activity {

	TextView t1, t2, t3, t4;
	RelativeLayout back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaybin_help);
		
		t1 = (TextView)findViewById(R.id.help1);
		t2 = (TextView)findViewById(R.id.help2);
		t3 = (TextView)findViewById(R.id.help3);
		t4 = (TextView)findViewById(R.id.help4);
		back = (RelativeLayout)findViewById(R.id.RelativeLayout1);
		back.setBackgroundColor((Color.parseColor("#ffffff")));
		
		Typeface faceTrack = Typeface.createFromAsset(getAssets(), "fonts/Track.ttf");
		t1.setTypeface(faceTrack);
		t2.setTypeface(faceTrack);
		t3.setTypeface(faceTrack);
		t4.setTypeface(faceTrack);
		t1.setTextColor(Color.BLACK);
		t2.setTextColor(Color.BLACK);
		t3.setTextColor(Color.BLACK);
		t4.setTextColor(Color.BLACK);
	}
}
