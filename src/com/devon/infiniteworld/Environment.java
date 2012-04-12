package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Renderable;

import com.devon.infiniteworld.entities.Entity;

public abstract class Environment
{
	public HashMap<String, GameScreenChunk> visibleChunks = new HashMap<String, GameScreenChunk>(); //holds chunks that need to be rendered(3x3 section surrounding player)
	public HashMap<String, GameScreenChunk> generatedChunks = new HashMap<String, GameScreenChunk>(); //holds chunks that are currently generated
	
	public List<Entity> entities = new ArrayList<Entity>(); //holds all entities in the environment
	
	abstract void update(GameContainer container, int delta);
	abstract void draw();
	
	public void addEntity(Entity entity)
	{ 
		entities.add(entity);
	}
	
	
}
