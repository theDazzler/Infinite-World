package com.devon.infiniteworld;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.TileType;

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
	
	Image water;
	Image grass;
	Image dirt;
	Image deepWater;
	Image tree;
	Image snow;
	Image lava;
	
	public GameScreenChunk(Vector2f position) throws SlickException
	{
		this.position = position;

		water = new Image("assets/images/tiles/water.png");
		grass = new Image("assets/images/tiles/grass.png");
		dirt = new Image("assets/images/tiles/dirt.png");
		deepWater = new Image("assets/images/tiles/deep_water.png");
		tree = new Image("assets/images/tiles/tree.png");
		snow = new Image("assets/images/tiles/snow.png");
		lava = new Image("assets/images/tiles/lava.png");
		
		water = water.getScaledCopy(0.5f);
		grass = grass.getScaledCopy(0.5f);
		deepWater = deepWater.getScaledCopy(0.5f);
		dirt = dirt.getScaledCopy(0.5f);
		tree = tree.getScaledCopy(0.5f);
		snow = snow.getScaledCopy(0.5f);
		lava = lava.getScaledCopy(0.5f);
		
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
		System.out.println("INDICES X: " + (int)this.getWorldMapIndices().x + " INDICES Y: " + (int)this.getWorldMapIndices().y);
		//get tile terrain value
		this.worldMapBiomeValue = WorldMap.map.get(key).biomeTypes[(int)this.getWorldMapIndices().x][(int)this.getWorldMapIndices().y];
		
		generateTileLayer();
		generateObjectLayer();
			
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
					//place tree
					if(rand.nextInt(10) == 1)
					{
						this.objectLayer[i][j] = TileType.TREE;
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
						tileValue = TileType.WATER;
						break;
					case BiomeType.PLAIN:
						tileValue = TileType.GRASS;
						break;
					case BiomeType.FOREST:
						tileValue = TileType.GRASS;
						break;
					case BiomeType.SNOW:
						tileValue = TileType.SNOW;
						break;
					case BiomeType.VOLCANIC:
						tileValue = TileType.LAVA;
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
	
	//returns array index of the chunk from WorldMapChunk
	public Vector2f getWorldMapIndices()
	{
		float x = Math.abs((float)(Math.floor(this.getWorldMapPosition().y / GameSettings.TILE_HEIGHT)));
		float y = Math.abs((float)(Math.floor(this.getWorldMapPosition().x / GameSettings.TILE_WIDTH)));
		y = y % this.NUM_TILES_X;
		x = x % this.NUM_TILES_Y;
		
		return new Vector2f(x, y);
	}

	//returns position of the chunk on the WorldMap
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
		drawObjectLayer();
		
	}

	private void drawObjectLayer() 
	{
		for(int i = 0; i < this.objectLayer.length; i++)
		{
			for(int j = 0; j < this.objectLayer[i].length; j++)
			{
				switch(this.objectLayer[i][j])
				{
					//draw tree
					case TileType.TREE:
						tree.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
						break;
				}
			}
		}
		
	}

	private void drawTileLayer()
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				switch(this.tileLayer[i][j])
				{
					//draw deep water
					case TileType.DEEP_WATER:
						deepWater.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
						break;
					
					//draw water
					case TileType.WATER:
						water.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
						break;
					
					//draw grass
					case TileType.GRASS:
						grass.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
						break;
						
					//draw snow
					case TileType.SNOW:
						snow.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
						break;
						
					//draw lava
					case TileType.LAVA:
						lava.draw((float)(this.getX() + (j * GameSettings.TILE_WIDTH)), (float)(this.getY() + (i * GameSettings.TILE_HEIGHT)));
						break;
						
				}
				
			}
		}
		
	}
}
