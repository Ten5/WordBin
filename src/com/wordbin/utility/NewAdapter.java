package com.wordbin.utility;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fordox.wordbin.Edit;
import com.fordox.wordbin.R;

public class NewAdapter extends BaseAdapter implements Filterable {

	Context context;
	ArrayList<SingleRow> data;
	ArrayList<SingleRow> filteredData;
	CheckBox bookmark;
	String user;
	Filter dataFilter;

	public NewAdapter(Context c, String user) {
		context = c;
		data = new ArrayList<SingleRow>();
		filteredData = new ArrayList<SingleRow>();
		String word, meaning; boolean bookmark;
		
		SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
		Cursor cursor = null;
		
		try {
			cursor = wordTable.rawQuery("select * from wordTable order by word", null);
		}
		catch(Exception e) {
			Toast.makeText(context, "No words found.", Toast.LENGTH_LONG).show();
			Log.e("NewAdapterConstructor", "Word Table not found.");
		}
		if(cursor != null) { 
			int i=0;
			while(cursor.moveToNext()) {
				word = cursor.getString(0);
				meaning = cursor.getString(1);
				bookmark = cursor.getInt(2) > 0? true : false;
				data.add(new SingleRow(word, meaning, bookmark));
				filteredData.add(data.get(i++));
			}
			cursor.close();			
		}		
		wordTable.close();
		
		/*for(int i=0; i<array.length; i++) {
			data.add(new SingleRow(array[i], meaning[i], bookmarks[i]>0?true:false));
			filteredData.add(data.get(i));
		}*/
		this.user = user;
	}

	@Override
	public int getCount() {	
		return filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredData.get(position);
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
		temp = filteredData.get(position);
		holder.word.setText(temp.word);
		holder.meaning.setText(temp.meaning);	
		holder.bookmark.setChecked(temp.getBookmark(context, temp.word));
		
		holder.word.setOnTouchListener(new OnSwipeTouchListener(context) {
			
			@Override
		    public void onSwipeRight() {
		        holder.meaning.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_in));		        
		        holder.word.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_out));
		        holder.bookmark.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_out));
		        holder.word.setVisibility(View.INVISIBLE);
				holder.meaning.setVisibility(View.VISIBLE);
				holder.bookmark.setVisibility(View.INVISIBLE);
		    }
			
			@Override
		    public void onSwipeLeft() {
		    	holder.meaning.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_in));  
		        holder.word.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_out));
		        holder.bookmark.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_out));
		        holder.word.setVisibility(View.INVISIBLE);
				holder.meaning.setVisibility(View.VISIBLE);
				holder.bookmark.setVisibility(View.INVISIBLE);
		    }
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {				
				return gestureDetector.onTouchEvent(event);
			}
			
			@Override
			public void onSingleTap() {
				Intent i = new Intent(context, Edit.class);
				SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
				Cursor c = wordTable.rawQuery("select sentence from wordTable where word = '"+holder.word.getText().toString().trim()+"'", null);
				c.moveToNext();
				i.putExtra("word", holder.word.getText().toString().trim());
				i.putExtra("meaning", holder.meaning.getText().toString().trim());
				i.putExtra("sentence", c.getString(0));
				i.putExtra("Username", user);
				context.startActivity(i);
			}
		});
		
		holder.meaning.setOnTouchListener(new OnSwipeTouchListener(context) {
			
			@Override
		    public void onSwipeRight() {
		        holder.meaning.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_out));		        
		        holder.word.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_in));
		        holder.bookmark.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_in));
		        holder.word.setVisibility(View.VISIBLE);
				holder.meaning.setVisibility(View.INVISIBLE);
				holder.bookmark.setVisibility(View.VISIBLE);
		    }
			
			@Override
		    public void onSwipeLeft() {
		    	holder.meaning.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_out));  
		        holder.word.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_in));
		        holder.bookmark.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_in));
		        holder.word.setVisibility(View.VISIBLE);
				holder.meaning.setVisibility(View.INVISIBLE);
				holder.bookmark.setVisibility(View.VISIBLE);
		    }
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {				
				return gestureDetector.onTouchEvent(event);
			}
			
			@Override
			public void onSingleTap() {
				Intent i = new Intent(context, Edit.class);
				SQLiteDatabase wordTable = context.openOrCreateDatabase("Words", 0, null);
				Cursor c = wordTable.rawQuery("select sentence from wordTable where word = '"+holder.word.getText().toString().trim()+"'", null);
				c.moveToNext();
				i.putExtra("word", holder.word.getText().toString().trim());
				i.putExtra("meaning", holder.meaning.getText().toString().trim());
				i.putExtra("sentence", c.getString(0));
				i.putExtra("Username", user);
				context.startActivity(i);
			}
		});

	    holder.bookmark.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	       @Override
	       public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
        	   if(!isChecked)
        		   temp.setBookmark(false, context, holder.word.getText().toString());
        	   else
        		   temp.setBookmark(true, context, holder.word.getText().toString());
	       }
	    });
		return convertView;
	}

	/*public Boolean[] getBookmarks() {
		Boolean bookmarks[] = new Boolean[data.size()];
		for(int i=0; i<filteredData.size(); i++)
			bookmarks[i] = filteredData.get(i).getBookmark(context);
		return bookmarks;
	}*/
	
	@Override
	public Filter getFilter() {
		dataFilter = new MyFilter();
	    return dataFilter;
	}	
	
	class MyFilter extends Filter {
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
		    // We implement here the filter logic
		    if (constraint == null || constraint.length() == 0) {
		        // No filter implemented we return all the list
		        results.values = data;
		        results.count = data.size();
		    }
		    else {
		        // We perform filtering operation
		    	ArrayList<SingleRow> dataList = new ArrayList<SingleRow>();
		         
		        for (SingleRow s : data) {
		            if (s.word.toUpperCase().startsWith(constraint.toString().toUpperCase()))
		                dataList.add(s);
		        }
		         
		        results.values = dataList;
		        results.count = dataList.size();
		    }
		    return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraints, FilterResults results) {
			// Now we have to inform the adapter about the new list filtered
			
			if (results.count == 0)
		        notifyDataSetInvalidated();
		    else {
		        filteredData = (ArrayList<SingleRow>) results.values;
		        notifyDataSetChanged();
		    }
		}
	}
	
}

class MyViewHolder {
	  TextView word;
	  TextView meaning;
	  CheckBox bookmark;
	  RelativeLayout back;
	  int position;
}