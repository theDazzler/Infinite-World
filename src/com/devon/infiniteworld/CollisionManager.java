package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.HashMap;

import com.devon.infiniteworld.tiles.VisibleTile;

public class CollisionManager 
{
	/**
	 * keeps track of collidableTiles that are currently in the rendered GameScreenChunks
	 */
	public static HashMap<String, VisibleTile> collidableTiles = new HashMap<String, VisibleTile>(); 
	
	/**
	 * Adds a collision tile to the collidableTiles ArrayList
	 * @param tile
	 */
	public static void addCollisionTile(String key, VisibleTile tile)
	{
		collidableTiles.put(key, tile);
	}
	
	public static void removeCollisionTiles()
	{
		
	}
}
