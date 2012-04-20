package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Renderable;
import com.devon.infiniteworld.entities.Entity;
import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.tiles.Tile;

public abstract class Level implements Renderable
{
	public byte[][] map;
	protected int width;
	protected int height;
	protected float xPos; //top left x coordinate of the level
	protected float yPos; //top left y coordinate of the level
	public List<Entity> entities = new ArrayList<Entity>();
	
	public Level(float xPos, float yPos, int width, int height)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		
		this.map = NoiseMap.getMap(width, height);
	}
	
	public abstract void draw(float x, float y, Player player);
	
	public void update(GameContainer gc, int delta)
	{

	}
	
	public Tile getTile(int x, int y)
	{
		byte tileValue = this.map[x][y];
		
		if(tileValue == Tile.water.id) return Tile.water;
		else if(tileValue == Tile.grass.id) return Tile.grass;
		else if(tileValue == Tile.snow.id) return Tile.snow;
		else if(tileValue == Tile.lava.id) return Tile.lava;
		else if(tileValue == Tile.cement.id) return Tile.cement;
		else if(tileValue == Tile.dirt.id) return Tile.dirt;
		else if(tileValue == Tile.mountain.id) return Tile.mountain;
		
		else
			return null;
	}
	
	
	
	public void addEntity(Entity e)
	{
		this.entities.add(e);
	}

	/**
	 * get width of level
	 * @return
	 */
	public int getWidth() 
	{
		return width;
	}

	/**
	 * get height if level
	 * @return
	 */
	public int getHeight() 
	{
		return height;
	}
	
	/**
	 * get top left x coordinate of the level
	 * @return
	 */
	public float getX() 
	{
		return xPos;
	}

	/**
	 * get top left y coordinate of the level
	 * @return
	 */
	public float getY() {
		return yPos;
	}
}
