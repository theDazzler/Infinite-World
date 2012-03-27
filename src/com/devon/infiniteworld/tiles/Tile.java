package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.GameSettings;

/** 
 * SUper class that all other tiles inherit from
 * @author Devon Guinane
 *
 */
public abstract class Tile
{
	Vector2f position;     //top left corner of tile
	private int width;     //width of tile
	private int height;    //height of tile
	private Image texture; //tile image
	private Rectangle boundingBox; //box used for collision detection
	public boolean isCollidable; //true if tile is collidable
	
	public Tile(Vector2f position, int width, int height, Image texture, boolean isCollidable)
	{
		this.position = position;
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.boundingBox = new Rectangle(position.x, position.y, width, height);
		this.isCollidable = isCollidable;
	}
	
	public Image getTexture()
	{
		return this.texture;
	}
	
	public float getX()
	{
		return this.position.getX();
	}

	public float getY() 
	{
		return this.position.getY();
	}

	//get width of tile
	public float getWidth()
	{
		return this.width;
	}

	//get height of tile
	public float getHeight()
	{
		return this.height;
	}
	
	//get collision rectangle
	public Rectangle getBoundingBox()
	{
		return this.boundingBox;
	}
	
	//set tile's boundingBox
	public void setBoundingBox(float x, float y, int width, int height)
	{
		this.boundingBox = new Rectangle(x, y, width, height);
	}
	
	public Vector2f getWorldMapPosition()
	{
		return new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
	}
}
