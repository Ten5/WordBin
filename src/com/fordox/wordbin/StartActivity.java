package com.fordox.wordbin;

import com.wordbin.utility.SignInOrUpTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {
		private EditText usernameField,passwordField;
		StartActivity sa;
		Button signIn, signUp;
		CheckBox rememberMe;
		
	    @Override 
	    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_start);
	       usernameField = (EditText)findViewById(R.id.editText1);
	       passwordField = (EditText)findViewById(R.id.editText2);
	       TextView tv1 = (TextView) findViewById(R.id.textView1);
	       TextView tv2 = (TextView) findViewById(R.id.textView2);
	       TextView tv3 = (TextView) findViewById(R.id.textView3);
	       TextView tv4 = (TextView) findViewById(R.id.textView4);
	       TextView tv5 = (TextView) findViewById(R.id.textView5);
	       signIn = (Button)findViewById(R.id.button1);
	       signUp = (Button)findViewById(R.id.button2);
	       rememberMe = (CheckBox)findViewById(R.id.checkBox1);
	       Typeface faceTrack = Typeface.createFromAsset(getAssets(), "fonts/Track.ttf");
	       Typeface faceHomestead = Typeface.createFromAsset(getAssets(), "fonts/Homestead-Regular.ttf");
	       tv1.setTypeface(faceHomestead);
	       tv2.setTypeface(faceHomestead);
	       tv3.setTypeface(faceTrack);
	       tv4.setTypeface(faceTrack);
	       tv5.setTypeface(faceTrack); 
	       signIn.setTypeface(faceHomestead);
	       signUp.setTypeface(faceHomestead);
	       rememberMe.setTypeface(faceTrack);
	       sa=this;
	       
	       rememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
				}
			});
	    }
	    
	    @Override
		protected void onStart() {
			super.onStart();
			SharedPreferences sp =  getSharedPreferences("StartPreferences", MODE_PRIVATE);
			rememberMe.setChecked(sp.getBoolean("remember", false));
			if(rememberMe.isChecked()) {
				usernameField.setText(sp.getString("username", ""));
				passwordField.setText(sp.getString("password", ""));
			}
		}
	    
	    @Override
		protected void onStop() {
			super.onStop();
		}
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       getMenuInflater().inflate(R.menu.main, menu);
	       return true;
	    }
	    
	    public boolean onOptionsItemSelected(MenuItem item) {
		
			if(item.getItemId()==R.id.About)
				Toast.makeText(StartActivity.this, "Version 2.0\nDesigned by FordoX.\nAn app to help learn new words in an interesting way.",Toast.LENGTH_LONG).show();
			
			return super.onOptionsItemSelected(item);
		}
	   
	    public void signIn(View view) {
	       String username = usernameField.getText().toString().trim();
	       String password = ""+passwordField.getText().toString().trim().hashCode();
	       SignInOrUpTask sign = new SignInOrUpTask(StartActivity.this, 1, sa);
	       sign.execute(username,password);
	    }
	    
	   
	    public void signUp(View view) {
	       String username = usernameField.getText().toString().trim();
	       String password = ""+passwordField.getText().toString().trim().hashCode();
	       if(username.equals("")|| password.equals(""))
	    	   Toast.makeText(getApplicationContext(),"Please fill up the fields.", Toast.LENGTH_LONG).show();
	       else
	       new SignInOrUpTask(StartActivity.this, 0,sa).execute(username,password);
	    }
	   
	    public void finishActivity() {
	    	SharedPreferences sp = getSharedPreferences("StartPreferences", MODE_PRIVATE);
			SharedPreferences.Editor ed = sp.edit();
			if(rememberMe.isChecked()) {
				ed.putBoolean("remember", true);
				ed.putString("username", usernameField.getText().toString().trim());
				ed.putString("password", passwordField.getText().toString().trim());
			}
			else {
				ed.remove("remember");
				ed.remove("username");
				ed.remove("password");
			}
			ed.commit();
		    StartActivity.this.finish();
	    }
}
