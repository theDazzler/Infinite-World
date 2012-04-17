package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	final int NUM_TILES_X = GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH;  //number of tiles in horizontal direction
	final int NUM_TILES_Y = GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT; //number of tiles in vertical direction
	final int WIDTH = GameSettings.CHUNK_PIXEL_WIDTH;   
	final int HEIGHT = GameSettings.CHUNK_PIXEL_HEIGHT;
	Vector2f position; //top left coordinates of the chunk
	private Vector2f worldMapPosition; //position of chunk on the WorldMap
	Player player;
	int worldMapBiomeValue; //value of biome from worldMap, if worldMap biome is BiomeType.FOREST, then GameScreenChunk will use this value to fill screen with forest tiles and forest objects
	int gameScreenType;
	Vector2f parentWorldMapChunkPosition; //get coordinates of the WorldMapChunk the GameScreenChunk is in
	public List<WorldObject> worldObjects;
	
	public int[][] tileLayer; //holds tile data for each GameScreenChunk(which tile should be placed)
	public int[][] objectLayer; //holds object data for the chunk(trees, buildings, etc.)
	private Random random;
	
	/**
	 * Each value from the WorldMap is taken and a GameScreenCHunk is generated from that value
	 * Ex. If value from WorldMap is 0.8(GRASS), then a GameScreenChunk is generated from that value(a forest, jungle, trees, grass, etc.)
	 * The WorldMap is like a minimap. It tells whether a GameScreenChunk should be water, land, etc. Then the tileLayer is filled with terrain relating to that value(trees, grass, etc.)
	 * 
	 */	
	public GameScreenChunk(Vector2f position, Player player) throws SlickException
	{
		this.position = position;	
		this.player = player;
		this.worldMapPosition = new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		this.tileLayer = new int[this.NUM_TILES_Y][this.NUM_TILES_X];
		this.objectLayer = new int[this.NUM_TILES_Y][this.NUM_TILES_X];
		this.parentWorldMapChunkPosition = this.getParentWorldMapChunkPosition();
		this.worldObjects = new ArrayList<WorldObject>();
		this.random = new Random((long) (GameSettings.seed + ((this.getX() + this.getY()) / 100)));
		
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
		WorldMapChunk parentChunk = WorldMap.map.get(key);
		System.out.println("OOOO: " + this.getX() + " " +  this.getY());
		System.out.println(parentChunk.xPos + " " + parentChunk.yPos + " " + this.getX() + " " +  this.getY());
		this.worldMapBiomeValue = parentChunk.biomeTypes[(int)this.getWorldMapIndices().x][(int)this.getWorldMapIndices().y];
		this.gameScreenType = parentChunk.gameScreenTypes[(int)this.getWorldMapIndices().x][(int)this.getWorldMapIndices().y];
		//System.out.println("POO: " + key + "screen pos: "+ this.getX() + ", " +  this.getY() + " " + (int)this.getWorldMapIndices().x + ", " + (int)this.getWorldMapIndices().y);
		
		//generate initial tiles
		generateTileLayer(); 
		
		//modifyTileLayer(); //makes GameScreenChunks less square (some water tiles will go into forest chunks etc.)
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
		//for each row in the chunk
		for(int i = 0; i < this.objectLayer.length; i++)
		{
			//for each column in the chunk
			for(int j = 0; j < this.objectLayer[i].length; j++)
			{
				if(this.gameScreenType == GameScreenType.forest.id)
				{
					//dont place trees on water tiles
					if(this.tileLayer[i][j] == Tile.grass.id)
					{
						//place tree
						if(this.random.nextInt(5) == 0)
						{
							this.objectLayer[i][j] = WorldObject.treeId;
							this.addWorldObject(new Tree(this.position.x + (i * GameSettings.TILE_WIDTH), this.position.y + (j * GameSettings.TILE_HEIGHT)));
						}
						
						//add caves
						if(this.random.nextInt(Cave.rarity) == 0)
						{
							this.objectLayer[i][j] = WorldObject.caveId;
							this.worldObjects.add(new Cave(this.position.x + (i * GameSettings.TILE_WIDTH), this.position.y + (j * GameSettings.TILE_HEIGHT)));
						}
					}

					
					Random rand = new Random();
					//add horses to forests
					if(rand.nextInt(50) == 0)
					{
						Horse horse = new Horse(new Vector2f(this.position.x + (i * 64), this.position.y + (j * 64)));
						player.currentEnvironment.addEntity(horse);
					}
					
				}				
			}
		}	
	}

	private void addWorldObject(WorldObject obj)
	{
		this.worldObjects.add(obj);
		
	}

	//generate tiles for the chunk
	private void generateTileLayer()
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				int tileValue = 0;
								
				if(this.gameScreenType == GameScreenType.water.id)
					tileValue = Tile.water.id;
				else if(this.gameScreenType == GameScreenType.plain.id)
					tileValue = Tile.grass.id;
				else if(this.gameScreenType == GameScreenType.forest.id)
					tileValue = Tile.grass.id;
				else if(this.gameScreenType == GameScreenType.snow.id)
					tileValue = Tile.snow.id;
				else if(this.gameScreenType == GameScreenType.volcanic.id)
					tileValue = Tile.lava.id;
				else if(this.gameScreenType == GameScreenType.cityRoad.id)
					tileValue = Tile.cement.id;
				else if(this.gameScreenType == GameScreenType.cityBuilding.id)
					tileValue = Tile.dirt.id;
				else if(this.gameScreenType == GameScreenType.cityCoast.id)
					tileValue = Tile.grass.id;
				else if(this.gameScreenType == GameScreenType.cityWater.id)
					tileValue = Tile.water.id;
				else if(this.gameScreenType == GameScreenType.dirt.id)
					tileValue = Tile.dirt.id;
				
				this.tileLayer[i][j] = tileValue;
			}
		}
	}

	//gets parent WorldMapChunk position
	private Vector2f getParentWorldMapChunkPosition()
	{		
		for (WorldMapChunk chunk : WorldMap.map.values()) 
		{
			System.out.println("CHUNKYY: " + chunk.getKey());
		}
		
		int xIndex = (int) Math.abs((this.getY() / GameSettings.CHUNK_PIXEL_HEIGHT));
		int yIndex = (int) Math.abs((this.getX() / GameSettings.CHUNK_PIXEL_WIDTH));
		
		if(xIndex < 0)
			xIndex += GameSettings.WORLDMAP_CHUNK_WIDTH;
		if(yIndex < 0)
			yIndex += GameSettings.WORLDMAP_CHUNK_HEIGHT;
		
		xIndex %= GameSettings.WORLDMAP_CHUNK_WIDTH;
		yIndex %= GameSettings.WORLDMAP_CHUNK_HEIGHT;
		
		float parentX = this.getX() - (yIndex * GameSettings.CHUNK_PIXEL_WIDTH);
		float parentY = this.getY() - (xIndex * GameSettings.CHUNK_PIXEL_HEIGHT);

		System.out.println(parentX +"::" + parentY);
		return new Vector2f(parentX, parentY);
					
	}
	
	/**
	 * returns array index of the chunk from WorldMapChunk
	 * @return Vector2f WorldMap Indices
	 */
	
	public Vector2f getWorldMapIndices()
	{
		float x = Math.abs(this.getY() / GameSettings.CHUNK_PIXEL_HEIGHT);
		float y = Math.abs(this.getX() / GameSettings.CHUNK_PIXEL_WIDTH);
		
		x = x % GameSettings.WORLDMAP_CHUNK_WIDTH;
		y = y % GameSettings.WORLDMAP_CHUNK_HEIGHT;
		
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
		for(int i = 0; i < this.worldObjects.size(); i++)
		{
			WorldObject obj = this.worldObjects.get(i);

			//draw WorldObjects
			obj.draw();

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
				
				else if(tileType == Tile.cement.id)
				{
					//draw lava
					Tile.cement.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
				}	
				
				else if(tileType == Tile.dirt.id)
				{
					//draw lava
					Tile.dirt.draw((float)(this.getX() + (j * Tile.WIDTH)), (float)(this.getY() + (i * Tile.HEIGHT)));
				}	
				
			}
		}
	}
}
