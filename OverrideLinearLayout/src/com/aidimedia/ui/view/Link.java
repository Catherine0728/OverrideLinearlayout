package com.aidimedia.ui.view;

import android.graphics.Bitmap;

public class Link {
	private String title;
	
	private String url;
	
	private int textColor = -1;
	private int bgColor	  = -1;
	
	private Object obj;
	
	private int type = -1;

	public Link(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		if(obj instanceof Bitmap){
			Bitmap bmp = (Bitmap) obj;
			if(!bmp.isRecycled()){
				bmp.recycle();
			}
			bmp = null;
		}
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Title: " + title + ", Url: " + url;
	}
	
	
	
}
