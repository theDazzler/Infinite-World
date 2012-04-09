package com.devon.infiniteworld;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;

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
	
	public static HashMap<String, WorldMapChunk> map = new HashMap<String, WorldMapChunk>();
	
	private ChunkGenerator chunkGenerator;
	Player player;
	
	private WorldMap(Player player) throws SlickException
	{
		this.player = player;
		this.chunkGenerator = new ChunkGenerator(player);
		
		this.chunkGenerator.generateWorldMapChunks();
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
