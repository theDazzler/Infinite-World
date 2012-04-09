package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Horse;
import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.objects.Cave;
import com.devon.infiniteworld.objects.Tree;
import com.devon.infiniteworld.objects.WorldObject;
import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.Tile;
import com.devon.infiniteworld.tiles.WaterTile;

/**
 * Creates an actual screen sized chunk that the player plays on(fills screen with grass, forests, etc)
 * A chunk is a section of the map made up of tiles that is the size of the screen
 * new chunks are created near the player as the player moves.
 * These chunks are based off the WorldMapCHunk the player is on.
 * If a player is on land, the GameScreenChunk will generate tiles on the screen that can be placed on land(forests, snow, dungeons, etc.)
 * @author Devon Guinane
 *
 */
public abstract class GameScreenChunk implements Renderable
{
	final int NUM_TILES_X = GameSettings.CHUNK_WIDTH;  //number of tiles in horizontal direction
	final int NUM_TILES_Y = GameSettings.CHUNK_HEIGHT; //number of tiles in vertical direction
	final int WIDTH = GameSettings.CHUNK_PIXEL_WIDTH;   
	final int HEIGHT = GameSettings.CHUNK_PIXEL_HEIGHT;
	Vector2f position; //top left coordinates of the chunk
	public Vector2f worldMapPosition; //position of chunk on the WorldMap
	Player player;
	
	Vector2f parentWorldMapChunkPosition; //get coordinates of the WorldMapChunk the GameScreenChunk is in
	
	public int[][] tileLayer; //holds tile data for each GameScreenChunk(which tile should be placed)
	public int[][] objectLayer; //holds WorldObject data for the chunk(trees, buildings, caves etc.)
	public ArrayList<WorldObject> worldObjects; //holds WorldObjects
	
	/**
	 * Each value from the WorldMap is taken and a GameScreenCHunk is generated from that value
	 * Ex. If value from WorldMap is 0.8(GRASS), then a GameScreenChunk is generated from that value(a forest, jungle, trees, grass, etc.)
	 * The WorldMap is like a minimap. It tells whether a GameScreenChunk should be water, land, etc. Then the tileLayer is filled with terrain relating to that value(trees, grass, etc.)
	 * 
	 */	
	public GameScreenChunk(Vector2f position) throws SlickException
	{
		this.position = position;	
		this.worldMapPosition = new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		this.tileLayer = new int[this.NUM_TILES_Y][this.NUM_TILES_X];
		this.objectLayer = new int[this.NUM_TILES_Y][this.NUM_TILES_X];
		this.worldObjects = new ArrayList<WorldObject>();
		this.parentWorldMapChunkPosition = this.getParentWorldMapChunkPosition();
		
		//this.initObjectNoise();
		
		this.createChunk();
	}
	
	public abstract void generateObjectLayer();
	public abstract void generateTileLayer();
	public abstract void drawTileLayer();
		
	//generate terrain data for the chunk
	public void createChunk() 
	{	
		//generate initial tiles
		generateTileLayer(); 
		generateObjectLayer();
	}	
	
	/**
	 * Adds a WorldObject to the GameScreenChunk
	 * @param object
	 */
	public void addWorldObject(WorldObject object) 
	{
		this.worldObjects.add(object);
		
	}

	//gets parent WorldMapChunk position
	public Vector2f getParentWorldMapChunkPosition()
	{
		Vector2f coordinates = new Vector2f();
		float x = 0;
		float y = 0;
		
		x = (float) (Math.floor(this.getWorldMapPosition().x / GameSettings.CHUNK_PIXEL_WIDTH) * GameSettings.CHUNK_PIXEL_WIDTH);
		
		y = (float) (Math.floor(this.getWorldMapPosition().y / GameSettings.CHUNK_PIXEL_HEIGHT) * GameSettings.CHUNK_PIXEL_HEIGHT);
		
		coordinates.set(x, y);
		
		return coordinates;
	}
	
	/**
	 * returns array index of the chunk from WorldMapChunk
	 * @return Vector2f WorldMap Indices
	 */
	
	public Vector2f getWorldMapIndices()
	{
		float x = (float)(Math.floor((this.getWorldMapPosition().y % 768) / Tile.HEIGHT)) + GameSettings.CHUNK_HEIGHT;
		float y = (float)(Math.floor((this.getWorldMapPosition().x % 1024) / Tile.WIDTH)) + GameSettings.CHUNK_WIDTH;
		y = y % this.NUM_TILES_X;
		x = x % this.NUM_TILES_Y;
		
		return new Vector2f(x, y);
	}

	/**
	 * returns position of the chunk on the WorldMap
	 * @return Vector2f WorldMap position
	 */
	private Vector2f getWorldMapPosition() 
	{
		return this.worldMapPosition;
	}

	//get top left x coordinate of the chunk
	public float getX()
	{
		return this.position.getX();
	}
	
	//get top left y coordinate of the chunk
	public float getY()
	{
		return this.position.getY();
	}

	@Override
	public void draw(float x, float y) 
	{
		drawTileLayer();
		
		//render objects
		drawObjectLayer();
		
	}

	private void drawObjectLayer() 
	{
		for(int i = 0; i < this.worldObjects.size(); i++)
		{
			WorldObject obj = this.worldObjects.get(i);

			//draw WorldObjects
			obj.draw();

		}		
	}

	
}
