package com.devon.infiniteworld.test;

public class NewEntity
{
	public int x, y;
	public boolean removed;
	public Level level;
	
	public void render()
	{
		
	}
	
	public void remove()
	{
		this.removed = true;
	}

	public final void init(Level level) 
	{
		this.level = level;
		
	}
}
