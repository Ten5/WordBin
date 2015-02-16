package com.wordbin.utility;

public class SingleRow {
	String word;
	String meaning;
	private Boolean bookmark;
	
	public SingleRow(String w, String m, Boolean b) {
		word = w;
		meaning = m;
		bookmark = b;
	}
	
	public void setBookmark(Boolean bookmark) {
		this.bookmark = bookmark;
	}
	
	public Boolean getBookmark() {
		return bookmark;
	}
}