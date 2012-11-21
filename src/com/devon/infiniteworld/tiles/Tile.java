package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Renderable;

import com.devon.infiniteworld.objects.WorldObject;

/** 
 * Super class that all other tiles inherit from
 * @author Devon Guinane
 *
 */
public abstract class Tile extends WorldObject implements Renderable
{
	public static final int WIDTH = 64;
	public static final int HEIGHT= 64;
	
	public static Tile[] tiles = new Tile[256]; //holds all tile types
	public static Tile grass = new GrassTile(1);
	public static Tile water = new WaterTile(2);
	public static Tile snow = new SnowTile(3);
	public static Tile lava = new LavaTile(4);
	public static Tile cement = new CementTile(5);
	public static Tile dirt = new DirtTile(6);
	public static Tile mountain = new MountainTile(7);
	public static Tile ice = new IceTile(8);
	public static Tile sand = new SandTile(9);
	
	
	public Tile(int id)
	{
		super(id);
		//this.texture = texture;
		//this.boundingBox = new Rectangle(position.x, position.y, width, height);
	}
	
	@Override
	public void draw(float x, float y) 
	{
		this.getTexture().draw(x, y);
	}
	
	public void draw(float x, float y, float scaleFactor) 
	{
		this.getTexture().draw(x, y, scaleFactor);
	}
	
	
	
	/*
	public float getX()
	{
		return this.position.getX();
	}
	*/

	/*
	public float getY() 
	{
		return this.position.getY();
	}
	*/
	
	
	/*
	public Vector2f getWorldMapPosition()
	{
		return new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
	}
	*/
	
	
}
