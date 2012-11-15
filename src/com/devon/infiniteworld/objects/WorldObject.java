package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Rectangle;

import com.devon.infiniteworld.tiles.Tile;

public abstract class WorldObject implements Renderable
{
	public final int id;
	public Image texture;
	public Rectangle boundingBox;
	
	public abstract boolean isCollidable();
	
	public WorldObject(int id)
	{
		this.id = id;
	}
	
	@Override
	public void draw(float x, float y)
	{
		this.texture.draw(x, y);
	}
	
	//get collision rectangle
	public Rectangle getBoundingBox()
	{
		return this.boundingBox;
	}
	
	//set tile's boundingBox
	public void setBoundingBox(float x, float y, int width, int height)
	{
		this.boundingBox = new Rectangle(x, y, Tile.WIDTH, Tile.HEIGHT);
	}
	
	public Image getTexture()
	{
		return this.texture;
	}
	
	@Override
	public String toString()
	{
		String result = "Object ID: " + this.id;
		
		return result;
		
	}
	
	

}
