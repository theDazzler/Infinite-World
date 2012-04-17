package com.devon.infiniteworld;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.test.NoiseMap;

/**
 * Singleton class
 * Large Map of the entire world
 * It is made up of WorldMapChunks
 * Each tile in the map represents one GameScreenChunk
 * @author Devon Guinane
 *
 */
public class WorldMap
{
	private static WorldMap ref;
	
	//public WorldMapChunk[][] map;
	public static LinkedHashMap<String, WorldMapChunk> map = new LinkedHashMap<String, WorldMapChunk>();
	
	private ChunkGenerator chunkGenerator;
	Player player;
	
	private WorldMap(Player player) throws SlickException
	{
		this.player = player;
		this.chunkGenerator = new ChunkGenerator(player);
		initNoiseMap();
		this.chunkGenerator.generateWorldMapChunks();
		this.chunkGenerator.generateGameScreenChunks();
	}
	
	private void initNoiseMap()
	{
		NoiseMap noiseMap = new NoiseMap(128, 128, 128 / 4);
		noiseMap.createMap(128, 128);

		int noiseMapRows = 128 / 16; 
		int noiseMapCols = 128 / 16;
		
		//get top left coordinates of the chunk the player is currently on
		Vector2f currentWorldMapChunkPosition = this.player.getWorldMapChunkPosition();
		
		float startPosX = currentWorldMapChunkPosition.x - (GameSettings.CHUNK_PIXEL_WIDTH * (noiseMapCols / 2));
		float startPosY = currentWorldMapChunkPosition.y - (GameSettings.CHUNK_PIXEL_HEIGHT * (noiseMapRows / 2));
		
		System.out.println("player chunk at " + currentWorldMapChunkPosition.x + ", " + currentWorldMapChunkPosition.y + "created noise map at: " + startPosX + ", " + startPosY);
		
		player.currentEnvironment.addNoiseMap(startPosX, startPosY, noiseMap);
		
	}

	//returns array index values from the WorldMap of a tile (tile is at (64, 64), array values will be 1,1)
	public Vector2f getWorldMapArrayIndices(float xPos, float yPos)
	{
		Vector2f indices = new Vector2f();
		indices.set(Math.abs((float)(Math.floor(yPos / GameSettings.CHUNK_PIXEL_HEIGHT))), Math.abs((float)(Math.floor(xPos / GameSettings.CHUNK_PIXEL_WIDTH))));
		
		return indices;
	}

	public static WorldMap getWorldMap(Player player) throws SlickException
	{	
		if (ref == null)
			ref = new WorldMap(player);		
	    return ref;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException(); 
	}
	
	public static void dump()
	{
		String result = "";
		
		for (String key : map.keySet()) 
		{
		    result += key + "\n";
		}
		
		System.out.println(result);
		
	}
}
