package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.GrassTile;
import com.devon.infiniteworld.tiles.Tile;

public abstract class WorldObject implements Renderable
{

	public static WorldObject[] objects = new WorldObject[256]; //holds all object types
	public static final int caveId = 1;
	public static final int treeId = 2;
	
	public int id;
	public Image texture;
	public float width;
	public float height;
	public Rectangle boundingBox;
	public Vector2f position;
	
	public WorldObject()
	{
		
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
	
	
	public void draw()
	{
		this.texture.draw(this.position.x, this.position.y, 0.5f);
		
	}
	
}
