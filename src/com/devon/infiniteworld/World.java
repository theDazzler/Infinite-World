package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

public class World
{
	public Player player;
	public List<Entity> entities = new ArrayList<Entity>(); //holds all entities in the game
	 
	public World()
	{	
		
	}
	 
	public void add(Entity entity)
	{ 
		this.entities.add(entity);
	}
}
