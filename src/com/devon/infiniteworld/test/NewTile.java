package com.devon.infiniteworld.test;

public class NewTile 
{
	public static NewTile[] tiles = new NewTile[256];
	public static NewTile grass = new NewGrassTile(0);
	public static NewTile rock = new NewRockTile(1);
	
	public final byte id;
	
	public NewTile(int id)
	{
		this.id = (byte)id;
		tiles[id] = this;
	}
	
	public void render()
	{
		
	}
	
	public boolean  mayPass(Level level, int x, int y, NewEntity e)
	{
		return true;
	}
}
