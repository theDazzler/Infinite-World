package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import com.devon.infiniteworld.entities.Entity;
import com.devon.infiniteworld.entities.Player;

public class WorldManager
{
	public Player player;
	public static List<Entity> entities = new ArrayList<Entity>(); //holds all entities in the game
	 
	public WorldManager()
	{	
		
	}
	 
	public static void addEntity(Entity entity)
	{ 
		entities.add(entity);
	}
}
