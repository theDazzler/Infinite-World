package com.devon.infiniteworld.test;

public class NewRockTile extends NewTile 
{

	public NewRockTile(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public boolean  mayPass(Level level, int x, int y, NewEntity e)
	{
		return false;
	}

}
