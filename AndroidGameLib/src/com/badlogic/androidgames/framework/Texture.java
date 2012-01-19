package com.badlogic.androidgames.framework;

public interface Texture {
	
	public void reload();
	public void setFilters(int minFilter, int magFilter);
	public void bind();
	public void dispose();
	public int getWidth();
	public int getHeight();
}
