package com.wordbin.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.fordox.wordbin.SelectActivity;
import com.fordox.wordbin.StartActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SignInOrUpTask extends AsyncTask<String,Void,String> {

	   private Context context;
	   String username;
	   private int byInOrUp = 0; 
	   int condition=0;
	   StartActivity sa;
	   
	   public SignInOrUpTask(Context context, int flag, StartActivity sa) {
		   this.context = context;	      
		   byInOrUp = flag;
		   this.sa=sa;
	   }
	   
	   @Override
	   protected String doInBackground(String... arg0) {
		   username = (String)arg0[0];
           String password = (String)arg0[1];
		   if(byInOrUp == 0) {
	         try {	        	
	            String link="http://dbtest.webuda.com/Signup.php";
	            StringBuilder sb = new StringBuilder();
	            sb.append(signInOrUp(link, username, password));
	            return sb.toString();
	         } catch(Exception e) {
	        	 Log.e("------SIGN-IN-OR-UP-TASK FAILED--------", e.getMessage());
	 			return new String("There is a problem with the network connection.\nPlease try again.");
	         }
	       }
	       else{
	         try{
	            String link="http://dbtest.webuda.com/Signin.php";
	            StringBuilder sb = new StringBuilder();
	            sb.append(signInOrUp(link, username, password));
	            return sb.toString();	           
	         } catch(Exception e) {
	        	 return new String("There is a problem with the network connection.\nPlease try again.");
	         }
	       }
	   }
	   
	   @Override
	   protected void onPostExecute(String result) {
	      Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
	      if(result.equalsIgnoreCase("Signed Up Successfully. Logging you in..") || result.equalsIgnoreCase("Success")) {
	    	 condition=1;
	    	 Intent i=new Intent(context, SelectActivity.class);	    	 
	    	 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	 i.putExtra("Username", username);
	    	 context.startActivity(i);
	    	 sa.finishActivity();
	      }
	   }
	   
	   public String signInOrUp(String link, String username, String password) {
		   try {		
	   			String data  = URLEncoder.encode("username", "UTF-8") 
	            + "=" + URLEncoder.encode(username, "UTF-8");
	            data += "&" + URLEncoder.encode("password", "UTF-8") 
	            + "=" + URLEncoder.encode(password, "UTF-8");
	            URL url = new URL(link);
	            URLConnection conn = url.openConnection(); 
	            conn.setDoOutput(true); 
	            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
	            wr.write( data ); 
	            wr.flush(); 
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            
	            // Read Server Response
	            while((line = reader.readLine()) != null) {
	               sb.append(line);
	               break;
	            }
	            return sb.toString();
	       } catch(Exception e) {
	            return new String("Exception: " + e.getMessage());
	       }
	   }
	}