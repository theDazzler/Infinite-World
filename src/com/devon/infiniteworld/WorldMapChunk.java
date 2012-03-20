package com.devon.infiniteworld;

import java.io.Serializable;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.TileType;
import com.devon.infiniteworld.tiles.VisibleTile;
import com.devon.infiniteworld.tiles.WaterTile;


/**
 * creates the actual world. Creates the minimap view of the world
 * @author Devon Guinane
 *
 */
public class WorldMapChunk implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -588622170029850906L;

	//private static Random rand = new Random(GameSettings.seed);
	//private static final long serialVersionUID = System.currentTimeMillis();
	private String key; //key used for lookup in WorldMap hashmap
	
	final int WIDTH = GameSettings.CHUNK_WIDTH;
	final int HEIGHT = GameSettings.CHUNK_HEIGHT;
	
	float xPos; //top left x coordinate of the chunk
	float yPos; //top left y coordinate of the chunk
	
	public double[][] terrain; //holds terrain noise data(which tile should be placed)
	public double[][] temperature; //temperature data for each tile(GameScreenChunk) on the worldMap. It can be a value from 0-100
	public double[][] rainfall; //rainfall data
	public int[][] biomeTypes; //holds biome data for each WorldMapChunk
	PerlinNoise terrainNoise;
	
	
	//Image water;
	//Image grass;
	//Image dirt;
	//Image deepWater;
	
	public WorldMapChunk(Vector2f position, String key) throws SlickException
	{
		this.xPos = position.x;
		this.yPos = position.y;
		this.key = key;
		//water = new Image("assets/images/tiles/water.png");
		//grass = new Image("assets/images/tiles/grass.png");
		//dirt = new Image("assets/images/tiles/dirt.png");
		//deepWater = new Image("assets/images/tiles/deep_water.png");

		this.terrain = new double[this.HEIGHT][this.WIDTH]; //[rows=height][cols=width]
		this.temperature = new double[this.HEIGHT][this.WIDTH];
		this.rainfall = new double[this.HEIGHT][this.WIDTH];
		this.terrainNoise = new PerlinNoise(GameSettings.seed);
		this.biomeTypes = new int[this.HEIGHT][this.WIDTH];

		generateTerrainNoise();

		//ChunkGenerator.cleanUpWorldChunkWater();
		
		generateTemperatureNoise();
		generateRainfallNoise();
		
		initBiomeTypes();
		
		System.out.println();
		System.out.println("DONE");
	}
	
	private void generateRainfallNoise() 
	{
		int rainSeed;
		
		if(GameSettings.seed > 0)
			rainSeed = GameSettings.seed - 2;
		else
		{
			rainSeed = GameSettings.seed + 2;
		}
		
		PerlinNoise rainfallNoise = new PerlinNoise(rainSeed);
		
		double x = this.getX();
		double y = this.getY();
		
		//for each row in the chunk
		for(int i = 0; i < this.rainfall.length; i++)
		{
			//for each column in the chunk
			for(int j = 0; j < this.rainfall[i].length; j++)
			{
				//place noise value into chunk array
				this.rainfall[i][j] =  rainfallNoise.noise(x  + (x * .472), y + (y * .472), .77);
				x += .314f; //arbitrary number

				System.out.print("RAIN" + this.rainfall[i][j] + " ");
				
			}
			y += .314f;	
		}
		
		
	}

	//generate noise data for temperature map
	private void generateTemperatureNoise()
	{
		int tempSeed;
		
		if(GameSettings.seed > 0)
			tempSeed = GameSettings.seed - 1;
		else
		{
			tempSeed = GameSettings.seed + 1;
		}
		
		PerlinNoise temperatureNoise = new PerlinNoise(tempSeed);
		
		double x = this.getX();
		double y = this.getY();
		
		//for each row in the chunk
		for(int i = 0; i < this.temperature.length; i++)
		{
			//for each column in the chunk
			for(int j = 0; j < this.temperature[i].length; j++)
			{
				//place noise value into chunk array
				this.temperature[i][j] =  temperatureNoise.noise(x  + (x * .472), y + (y * .472), .77);
				x += .314f; //arbitrary number

				System.out.print("QQQQ" + this.temperature[i][j] + " ");
				
			}
			y += .314f;	
		}
	}

	//determine what biome type each tile will be on the worldMap(ex. FOREST, VOLCANIC, PLAIN, etc)
	private void initBiomeTypes() 
	{
		for(int i = 0; i < this.terrain.length; i++)
		{
			for(int j = 0; j < this.terrain[i].length; j++)
			{
				double terrainValue = this.terrain[i][j];
				double tempValue = this.temperature[i][j];
				double rainValue = this.rainfall[i][j];
				
				//water
				if(terrainValue <= GameSettings.WATER_THRESHOLD)
				{
					this.biomeTypes[i][j] = BiomeType.OCEAN;
				}
				
				//land
				else
				{
					//snow
					if(tempValue <= -0.4f)
					{
						this.biomeTypes[i][j] = BiomeType.SNOW;
					}
					
					else if(tempValue < 0.2 && tempValue > 0 && rainValue > 0 && rainValue < 0.4)
					{
						this.biomeTypes[i][j] = BiomeType.FOREST;
					}
					
					else if(tempValue > 0.5 && rainValue < 0)
					{
						this.biomeTypes[i][j] = BiomeType.VOLCANIC;
						
					}
					
					else
					{
						this.biomeTypes[i][j] = BiomeType.PLAIN;
					}
					
				}
			}
		}
	}

	//generates noise data for the worldMap terrain
	private void generateTerrainNoise() 
	{
		double x = this.getX();
		double y = this.getY();
		
		//for each row in the chunk
		for(int i = 0; i < this.terrain.length; i++)
		{
			//for each column in the chunk
			for(int j = 0; j < this.terrain[i].length; j++)
			{
				//place noise value into chunk array
				this.terrain[i][j] = this.terrainNoise.noise(x  + (x * .472), y + (y * .472), .77);
				//this.terrain[i][j] = this.terrainNoise.fBm(x  + (x * .472), y + (y * .472), .77, GameSettings.FBM_OCTAVES, GameSettings.FBM_LACUNARITY, GameSettings.FBM_H);
				x += .314f; //arbitrary number

				System.out.print(this.terrain[i][j] + " ");
				
			}
			y += .314f;	
		}
	}

	//key used for lookup in WorldMap hashmap
	public String getKey()
	{
		return this.key;
	}
	
	//get top left x coordinate of the chunk
	public float getX()
	{
		return this.xPos;
	}
	
	//get top left y coordinate of the chunk
	public float getY()
	{
		return this.yPos;
	}

	/**
	@Override
	public void draw(float x, float y) 
	{
		for(int i = 0; i < this.terrain.length; i++)
		{
			for(int j = 0; j < this.terrain[i].length; j++)
			{
				double value = this.terrain[i][j];
				
				if(value <= GameSettings.WATER_THRESHOLD)
					water.draw((float)(this.getX() + (j * (GameSettings.TILE_WIDTH * 0.25f))), (float)(this.getY() + (i * (GameSettings.TILE_HEIGHT * 0.25f))), 0.25f);
				
				else
					grass.draw((float)(this.getX() + (j * (GameSettings.TILE_WIDTH * 0.25f))), (float)(this.getY() + (i * (GameSettings.TILE_HEIGHT * 0.25f))), 0.25f);
					
			}
		}
	}
	**/
	
}

