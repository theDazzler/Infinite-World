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
public class NormalOutdoorGameScreenChunk extends GameScreenChunk
{
	int worldMapBiomeValue; //value of biome from worldMap, if worldMap biome is BiomeType.OCEAN, then GameScreenChunk will use this value to fill screen with forest tiles and forest objects
	
	/**
	 * Each value from the WorldMap is taken and a GameScreenCHunk is generated from that value
	 * Ex. If value from WorldMap is 0.8(GRASS), then a GameScreenChunk is generated from that value(a forest, jungle, trees, grass, etc.)
	 * The WorldMap is like a minimap. It tells whether a GameScreenChunk should be water, land, etc. Then the tileLayer is filled with terrain relating to that value(trees, grass, etc.)
	 * 
	 */	
	public NormalOutdoorGameScreenChunk(Vector2f position) throws SlickException
	{
		super(position);
		
		//this.initObjectNoise();
		
		this.createChunk();
	}
		
	//generate terrain data for the chunk
	@Override
	public void createChunk() 
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
	
	public void generateObjectLayer() 
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
					if(this.tileLayer[i][j] != Tile.water.id)
					{
						//place tree
						if(rand.nextInt(5) == 0)
						{
							this.objectLayer[i][j] = WorldObject.treeId;
							this.addWorldObject(new Tree(this.position.x + (i * GameSettings.TILE_WIDTH), this.position.y + (j * GameSettings.TILE_HEIGHT)));
						}
					}

					//add horses to forests
					if(rand.nextInt(200) == 0)
					{
						Horse horse = new Horse(new Vector2f(this.position.x + (i * GameSettings.TILE_WIDTH), this.position.y + (j * GameSettings.TILE_HEIGHT)));
						WorldManager.currentEnvironment.addEntity(horse);
					}
					
				}
				
				//dont place caves on water tiles
				if(this.tileLayer[i][j] != Tile.water.id)
				{
					//add caves
					if(rand.nextInt(Cave.rarity) == 0)
					{
						this.objectLayer[i][j] = WorldObject.caveId;
						this.addWorldObject(new Cave(this.position.x + (i * GameSettings.TILE_WIDTH), this.position.y + (j * GameSettings.TILE_HEIGHT)));
					}
				}
				
			}
		}	
	}

	//generate tiles for the chunk
	public void generateTileLayer()
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
		
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				this.tileLayer[i][j] = tileValue;
			}
		}
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

	public void drawTileLayer()
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
