package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.HashMap;

import com.devon.infiniteworld.tiles.Tile;

public class CollisionManager 
{
	/**
	 * keeps track of collidableTiles that are currently in the rendered GameScreenChunks
	 */
	public static HashMap<String, Tile> collidableTiles = new HashMap<String, Tile>(); 
	
	/**
	 * Adds a collision tile to the collidableTiles ArrayList
	 * @param tile
	 */
	public static void addCollisionTile(String key, Tile tile)
	{
		collidableTiles.put(key, tile);
	}
	
	public static void removeCollisionTiles()
	{
		
	}
}
