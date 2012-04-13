package com.devon.infiniteworld;

import java.io.Serializable;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.BiomeType;
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
	public static boolean oneCity = false;

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
	public int[][] cityData; //holds data for cities
	public int[][] gameScreenTypes; //holds data about which type of GameScreenChunk should be generated. (ex. if forest, build a GameScreenChunk with a forest. If road, build road. If vacant lot, build vacant lot)
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
		this.cityData = new int[this.HEIGHT][this.WIDTH];
		this.gameScreenTypes = new int[this.HEIGHT][this.WIDTH];

		generateTerrainNoise();

		//ChunkGenerator.cleanUpWorldChunkWater();
		
		generateTemperatureNoise();
		generateRainfallNoise();
		initBiomeTypes();

		//generateCities();
		
		System.out.println();
		System.out.println("DONE");
	}
	
	public void generateCities()
	{
		Random random = new Random();
		//try to create city ONLY if there isnt any city data in this chunk
		if(!containsCity())
		{
			for (int i = 0; i < this.biomeTypes.length; i++)
			{
				for(int j = 0; j < this.biomeTypes[i].length; j++)
				{
					int biomeType = this.biomeTypes[i][j];
					
					//place cities in snow biomes
					if(biomeType == BiomeType.SNOW)
					{
						if(random.nextInt(3) == 0)
						{
							//if there is NOT a city at this location
							if(this.cityData[i][j] != 1)
							{
								City city = new City(45);
							
								placeCity(city, i, j);
								
							}
						}
					}
				}
			}
		}
	}

	private boolean containsCity()
	{
		for(int i = 0; i < this.cityData.length; i++)
		{
			for(int j = 0; j < this.cityData[i].length; j++)
			{
				if(this.cityData[i][j] == 1)
					return true;
			}
		}
		return false;
	}

	private void placeCity(City city, int xIndex, int yIndex) 
	{
		if(canPlace(city, xIndex, yIndex))
		{
			oneCity = true;
			System.out.println("INDEXES : " + xIndex + " " + yIndex);
			
			System.out.println("ROWSCITY: " + city.numRows);
			System.out.println("COSLCITY: " + city.numCols);
			WorldMapChunk targetChunk = null;
			int xTargetIndex;
			int yTargetIndex;
			int targetChunkRow;
			int targetChunkCol;

			String key;
			
			System.out.println("CITY ROWS: " + city.numRows + " CITYCOLS: " + city.numCols);
			for(int i = 0; i < city.numRows; i++)
			{
				for(int j = 0; j < city.numCols; j++)
				{
					xTargetIndex = xIndex + i;
					yTargetIndex = yIndex + j;
					targetChunk = this;
					
					targetChunkRow = xTargetIndex / this.HEIGHT;
					targetChunkCol = yTargetIndex / this.WIDTH;
					
					
					System.out.println("xTARRRGET: " + xTargetIndex + " yTARRRGET: " + yTargetIndex);
					
					//if city needs to extend to WorldMapChunk below and to the right of this chunk
					if(xTargetIndex > this.HEIGHT - 1 && yTargetIndex > this.WIDTH - 1)
					{
						
						
						key = "x" + Integer.toString((int)this.getX()+ (GameSettings.CHUNK_PIXEL_WIDTH * targetChunkCol)) + "y" + Integer.toString((int)this.getY() + (GameSettings.CHUNK_PIXEL_HEIGHT * targetChunkRow));
						xTargetIndex = xTargetIndex % this.HEIGHT;
						yTargetIndex = yTargetIndex % this.WIDTH;
						if(WorldMap.map.containsKey(key))
						{
							//get WorldMapChunk below
							targetChunk = WorldMap.map.get(key);
							
							
						}
						else
						{
							continue;
						}
					}
					
					//if city needs to extend to WorldMapChunk below this chunk
					else if(xTargetIndex > this.HEIGHT - 1)
					{
						key = "x" + Integer.toString((int)this.getX()) + "y" + Integer.toString((int)this.getY() + (GameSettings.CHUNK_PIXEL_HEIGHT * targetChunkRow));
						xTargetIndex = xTargetIndex % this.HEIGHT;
						
						if(WorldMap.map.containsKey(key))
						{
							//get WorldMapChunk below
							targetChunk = WorldMap.map.get(key);
							
								
						}
						else
						{
							continue;
						}
					}
					
					//if city needs to extend into the WorldMapChunk to the right
					else if(yTargetIndex > this.WIDTH - 1)
					{
						yTargetIndex = yTargetIndex % this.WIDTH;

						key = "x" + Integer.toString((int)this.getX() + (GameSettings.CHUNK_PIXEL_WIDTH * targetChunkCol)) + "y" + Integer.toString((int)this.getY());
						
						if(WorldMap.map.containsKey(key))
						{
							//get WorldMapChunk below
							targetChunk = WorldMap.map.get(key);
							
							
						}
						else
						{
							continue;
						}
					}
					
					if(WorldMap.map.containsValue(targetChunk))
					{
						System.out.println("XTARGET: " + xTargetIndex + "YTarget: " + yTargetIndex);
						//place a 1 into cityData to indicate that a city GameScreenType is at this location
						//System.out.println("xTargetIndex: " + xTargetIndex + "yTargetIndex: " + yTargetIndex);
						targetChunk.cityData[xTargetIndex][yTargetIndex] = 1;
					
						
						//type of city GameScreen(ex. road, building, water, etc.)
						int cityScreenValue = city.map[i][j];
					
						//place road screen
						if(cityScreenValue == City.FLOOR)
						{
							targetChunk.gameScreenTypes[xTargetIndex][yTargetIndex] = GameScreenType.cityRoad.id;
						}
					
						//place building screen
						else if(cityScreenValue == City.BUILDING)
						{
							targetChunk.gameScreenTypes[xTargetIndex][yTargetIndex] = GameScreenType.cityBuilding.id;
						}
					
						//place water screen
						else if(cityScreenValue == City.WATER)
						{
							targetChunk.gameScreenTypes[xTargetIndex][yTargetIndex] = GameScreenType.cityWater.id;
						}
					
						//place coastline(beach)
						else if(cityScreenValue == City.GRASS)
						{
							targetChunk.gameScreenTypes[xTargetIndex][yTargetIndex] = GameScreenType.cityCoast.id;
						}	
						
						//place coastline(beach)
						else if(cityScreenValue == City.WALL)
						{
							targetChunk.gameScreenTypes[xTargetIndex][yTargetIndex] = GameScreenType.plain.id;
						}
						
					}	
				}
			}
			
		}
	}

	//checks to see if city can fit into specified area on the World map
	private boolean canPlace(City city, int xIndex, int yIndex)
	{
		
		WorldMapChunk targetChunk;
		int xTargetIndex;
		int yTargetIndex;
		String key;
		int targetChunkRow;
		int targetChunkCol;
		
		System.out.println("CHECKING...");
		for(int i = 0; i < city.numRows; i++)
		{
			for(int j = 0; j < city.numCols; j++)
			{
				xTargetIndex = xIndex + i;
				yTargetIndex = yIndex + j;
				
				targetChunk = this;
				
				targetChunkRow = xTargetIndex / this.HEIGHT;
				targetChunkCol = yTargetIndex / this.WIDTH;
				
				//if city needs to extend to WorldMapChunk below and to the right of this chunk
				if(xTargetIndex > this.HEIGHT - 1 && yTargetIndex > this.WIDTH - 1)
				{
					key = "x" + Integer.toString((int)this.getX()+ (GameSettings.CHUNK_PIXEL_WIDTH * targetChunkCol)) + "y" + Integer.toString((int)this.getY() + (GameSettings.CHUNK_PIXEL_HEIGHT * targetChunkRow));
					xTargetIndex = xTargetIndex % this.HEIGHT;
					yTargetIndex = yTargetIndex % this.WIDTH;
					if(WorldMap.map.containsKey(key))
					{
						//get WorldMapChunk below
						targetChunk = WorldMap.map.get(key);
						
						
					}
					else
					{
						continue;
					}
				}
				
				//if city needs to extend to WorldMapChunk below this chunk
				else if(xTargetIndex > this.HEIGHT - 1)
				{
					key = "x" + Integer.toString((int)this.getX()) + "y" + Integer.toString((int)this.getY() + (GameSettings.CHUNK_PIXEL_HEIGHT * targetChunkRow));
					xTargetIndex = xTargetIndex % this.HEIGHT;

					if(WorldMap.map.containsKey(key))
					{
						//get WorldMapChunk below
						targetChunk = WorldMap.map.get(key);
						
						
					}
					else
					{
						continue;
					}
				}
				
				//if city needs to extend into the WorldMapChunk to the right
				else if(yTargetIndex > this.WIDTH - 1)
				{
					yTargetIndex = yTargetIndex % this.WIDTH;
					key = "x" + Integer.toString((int)this.getX() + (GameSettings.CHUNK_PIXEL_WIDTH * targetChunkCol)) + "y" + Integer.toString((int)this.getY());
					if(WorldMap.map.containsKey(key))
					{
						//get WorldMapChunk to right
						targetChunk = WorldMap.map.get(key);
						
						
					}
					else
					{
						continue;
					}
				}
				
				if(WorldMap.map.containsKey(targetChunk))
				{					
					if(targetChunk.cityData[xTargetIndex][yTargetIndex] == 1)
					{
						return false;
					}
				}
			}
		}
		
		return true;
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
					this.gameScreenTypes[i][j] = GameScreenType.water.id;
				}
				
				//land
				else
				{
					//snow
					if(tempValue <= -0.5f)
					{
						this.biomeTypes[i][j] = BiomeType.SNOW;
						this.gameScreenTypes[i][j] = GameScreenType.snow.id;
					}
					
					//forest
					else if(tempValue < 0.2 && tempValue > 0 && rainValue > 0 && rainValue < 0.4)
					{
						this.biomeTypes[i][j] = BiomeType.FOREST;
						this.gameScreenTypes[i][j] = GameScreenType.forest.id;
					}
					
					//volcanic
					else if(tempValue > 0.6 && rainValue < 0)
					{
						this.biomeTypes[i][j] = BiomeType.VOLCANIC;
						this.gameScreenTypes[i][j] = GameScreenType.volcanic.id;
						
					}			
					
					//plain
					else
					{
						this.biomeTypes[i][j] = BiomeType.PLAIN;
						this.gameScreenTypes[i][j] = GameScreenType.plain.id;
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

