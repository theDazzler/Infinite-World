package com.devon.infiniteworld;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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
public class GameScreenChunk implements Renderable
{
	final int NUM_TILES_X = GameSettings.CHUNK_WIDTH;  //number of tiles in horizontal direction
	final int NUM_TILES_Y = GameSettings.CHUNK_HEIGHT; //number of tiles in vertical direction
	final int WIDTH = GameSettings.CHUNK_PIXEL_WIDTH;   
	final int HEIGHT = GameSettings.CHUNK_PIXEL_HEIGHT;
	Vector2f position; //top left coordinates of the chunk
	private Vector2f worldMapPosition; //position of chunk on the WorldMap
	Player player;
	WorldMap worldMap;
	int worldMapBiomeValue; //value of biome from worldMap, if worldMap biome is BiomeType.OCEAN, then GameScreenChunk will use this value to fill screen with forest tiles and forest objects
	Vector2f parentWorldMapChunkPosition; //get coordinates of the WorldMapChunk the GameScreenChunk is in
	
	public int[][] tileLayer; //holds tile data for each GameScreenChunk(which tile should be placed)
	public int[][] objectLayer; //holds object data for the chunk(trees, buildings, etc.)
	
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
		this.parentWorldMapChunkPosition = this.getParentWorldMapChunkPosition();
		
		//this.initObjectNoise();
		
		this.createChunk();
	}
		
	//generate terrain data for the chunk
	private void createChunk() 
	{	
		String key = "x" + Integer.toString((int)this.parentWorldMapChunkPosition.x) + "y" + Integer.toString((int)this.parentWorldMapChunkPosition.y);
		System.out.println("WORLD X: " + this.worldMapPosition.x + "WORLD Y: " + this.worldMapPosition.y);
		System.out.println("INDICES X: " + (int)this.getWorldMapIndices().x + " INDICES Y: " + (int)this.getWorldMapIndices().y);
		//get tile terrain value
		this.worldMapBiomeValue = WorldMap.map.get(key).biomeTypes[(int)this.getWorldMapIndices().x][(int)this.getWorldMapIndices().y];
		

		//generate initial tiles
		generateTileLayer(); 
		
		modifyTileLayer(); //makes GameScreenChunks less square (some water tiles will go into forest chunks etc.)
		generateObjectLayer();
		
		/**
		try 
		{
			addWaterTiles();
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
			
	}
	
	/*
	private void addWaterTiles() throws SlickException
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				switch(this.worldMapBiomeValue)
				{
					case BiomeType.OCEAN:
						WaterTile waterTile = new WaterTile(new Vector2f((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT))), water);
						String key = "x" + Integer.toString((int)waterTile.getWorldMapPosition().x) + "y" + Integer.toString((int)waterTile.getWorldMapPosition().y);
						CollisionManager.collidableTiles.put(key, waterTile);
						break;
				}
			}
		}
		
	}
	*/

	//connect oceans that are diagonal to each other by placing tiles on the land chunks next to them
	private void modifyTileLayer()
	{	
		//modify the chunk if it isn't water
		if(this.worldMapBiomeValue != BiomeType.OCEAN)
		{
			new Thread(new GameScreenChunkModifier(this)).start();
		}
	}
	
	private void generateObjectLayer() 
	{
		//generate new random seed for each gameScreenChunk so objects will be placed in different spots in each GameScreenChunk
		Random rand = new Random((long) (GameSettings.seed + ((this.getX() + this.getY()) / 100)));
		
		//for each row in the chunk
		for(int i = 0; i < this.objectLayer.length; i++)
		{
			//for each column in the chunk
			for(int j = 0; j < this.objectLayer[i].length; j++)
			{
				if(this.worldMapBiomeValue == BiomeType.FOREST)
				{
					//dont place trees on water tiles
					if(this.objectLayer[i][j] != Tile.water.id)
					{
						//place tree
						if(rand.nextInt(10) == 1)
						{
							this.objectLayer[i][j] = Tile.tree.id;
						}
					}
				}
			}
		}	
	}

	//generate tiles for the chunk
	private void generateTileLayer()
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				int tileValue = 0;
				
				switch(this.worldMapBiomeValue)
				{
					case BiomeType.OCEAN:
						tileValue = Tile.water.id;
						break;
					case BiomeType.PLAIN:
						tileValue = Tile.grass.id;
						break;
					case BiomeType.FOREST:
						tileValue = Tile.grass.id;
						break;
					case BiomeType.SNOW:
						tileValue = Tile.snow.id;
						break;
					case BiomeType.VOLCANIC:
						tileValue = Tile.lava.id;
						break;
				}
				
				this.tileLayer[i][j] = tileValue;
			}
		}
	}

	//gets parent WorldMapChunk position
	private Vector2f getParentWorldMapChunkPosition()
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
		//render tiles
		try 
		{
			drawTileLayer();
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//render objects
		drawObjectLayer();
		
	}

	private void drawObjectLayer() 
	{
		for(int i = 0; i < this.objectLayer.length; i++)
		{
			for(int j = 0; j < this.objectLayer[i].length; j++)
			{
				if(this.objectLayer[i][j] == Tile.tree.id)
				{
					//draw tree
					Tile.tree.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
					break;
				}
			}
		}
		
	}

	private void drawTileLayer() throws SlickException
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				int tileType = this.tileLayer[i][j];
				
				if(tileType == Tile.water.id)
				{
					//draw water
					Tile.water.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
					//waterTile.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
				}
				else if(tileType == Tile.grass.id)
				{
					//draw grass
					Tile.grass.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.snow.id)
				{
					//draw grass
					Tile.snow.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.lava.id)
				{
					//draw lava
					Tile.lava.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
				}				
			}
		}
	}
}
