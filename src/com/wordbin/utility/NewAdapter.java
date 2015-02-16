package com.wordbin.utility;

import java.util.ArrayList;

import com.fordox.wordbin.Edit;
import com.fordox.wordbin.R;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewAdapter extends BaseAdapter  {

	Context context;
	ArrayList<SingleRow> data;	
	TextView word, meaning;
	CheckBox bookmark;
	String user;

	public NewAdapter(Context c, String[] array, String[] meaning, Boolean[] bookmarks, String user) {
		context = c;
		data = new ArrayList<SingleRow>();
		for(int i=0; i<array.length; i++) {
			data.add(new SingleRow(array[i], meaning[i], bookmarks[i]));
		}
		this.user = user;
	}

	@Override
	public int getCount() {		
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
	    return getCount();
	}

	@Override
	public int getItemViewType(int position) {
	    return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final MyViewHolder holder;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.list_view_layout, parent, false);
			holder = new MyViewHolder();
			holder.word = (TextView)convertView.findViewById(R.id.text1);
			holder.meaning = (TextView)convertView.findViewById(R.id.text2);
			holder.bookmark = (CheckBox)convertView.findViewById(R.id.check1);
			holder.back = (RelativeLayout)convertView.findViewById(R.id.back);
			holder.position = position;
			convertView.setTag(holder);
		}
		else
			holder = (MyViewHolder)convertView.getTag();
		
		
		
		Typeface faceHomestead = Typeface.createFromAsset(context.getAssets(), "fonts/Homestead-Regular.ttf");
		holder.word.setTypeface(faceHomestead);
		holder.meaning.setTextColor(Color.WHITE);
		holder.meaning.setTypeface(faceHomestead);
		holder.back.setBackgroundColor((Color.parseColor("#e87e04")));        
		holder.word.setTextColor(Color.WHITE);

		final SingleRow temp;
		temp = data.get(position);
		holder.word.setText(temp.word);
		holder.meaning.setText(temp.meaning);	
		holder.bookmark.setChecked(temp.getBookmark());
		
		holder.word.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.word.setVisibility(View.INVISIBLE);
				holder.meaning.setVisibility(View.VISIBLE);
				holder.bookmark.setVisibility(View.INVISIBLE);
			}
		});
		
		holder.word.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent i = new Intent(context, Edit.class);
				SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
				Cursor c = wordTable.rawQuery("select sentence from wordTable where word = '"+holder.word.getText().toString().trim()+"'", null);
				c.moveToNext();
				i.putExtra("word", holder.word.getText().toString().trim());
				i.putExtra("meaning", holder.meaning.getText().toString().trim());
				i.putExtra("sentence", c.getString(0));
				i.putExtra("Username", user);
				context.startActivity(i);
				return false;
			}
		});
		
		holder.meaning.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.word.setVisibility(View.VISIBLE);
				holder.meaning.setVisibility(View.INVISIBLE);
				holder.bookmark.setVisibility(View.VISIBLE);
			}
		});
		
		holder.meaning.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent i = new Intent(context, Edit.class);
				SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
				Cursor c = wordTable.rawQuery("select sentence from wordTable where word = '"+holder.word.getText().toString().trim()+"'", null);
				c.moveToNext();
				i.putExtra("word", holder.word.getText().toString().trim());
				i.putExtra("meaning", holder.meaning.getText().toString().trim());
				i.putExtra("sentence", c.getString(0));
				i.putExtra("Username", user);
				context.startActivity(i);
				return false;
			}						
		});

	    holder.bookmark.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	       @Override
	       public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {	    	   
            	   if(!isChecked)
            		   temp.setBookmark(false);
            	   else
            		   temp.setBookmark(true);
	       }
	    });
		return convertView;
	}

	public Boolean[] getBookmarks() {
		Boolean bookmarks[] = new Boolean[data.size()];
		for(int i=0; i<data.size(); i++)
			bookmarks[i] = data.get(i).getBookmark();
		return bookmarks;
	}
}

class MyViewHolder {
	  TextView word;
	  TextView meaning;
	  CheckBox bookmark;
	  RelativeLayout back;
	  int position;
}