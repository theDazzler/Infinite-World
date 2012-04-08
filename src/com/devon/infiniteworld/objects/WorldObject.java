package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.GrassTile;
import com.devon.infiniteworld.tiles.Tile;

public abstract class WorldObject implements Renderable
{

	public static WorldObject[] objects = new WorldObject[256]; //holds all object types
	public static WorldObject cave = new Cave(1);
	public static WorldObject tree = new Tree(2);
	
	public final int id;
	public Image texture;
	public Vector2f position;
	public int rarity; //higher the number, the more rare the object is(ex. 100 means object will appear 1 in 100 times)
	
	public WorldObject(int id)
	{
		this.id = id;
	}
	
	public boolean isCollidable() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void draw(float x, float y)
	{
		this.texture.draw(x, y, 0.5f);
		
	}
	
}
