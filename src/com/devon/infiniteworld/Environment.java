package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Entity;
import com.devon.infiniteworld.entities.Player;

public abstract class Environment implements Renderable
{
	Player player;
	MiniMap miniMap;
	public ChunkManager chunkManager;
	public List<Entity> entities = new ArrayList<Entity>(); //holds all entities in the game
	public HashMap<String, GameScreenChunk> visibleChunks;
	public HashMap<String, GameScreenChunk> generatedChunks;
	
	public Environment()
	{
		this.chunkManager = new ChunkManager();
		this.visibleChunks = new HashMap<String, GameScreenChunk>();
		this.generatedChunks = new HashMap<String, GameScreenChunk>();
	}
	
	public Environment(Player player)
	{
		this.player = player;
		this.chunkManager = new ChunkManager();
		this.visibleChunks = new HashMap<String, GameScreenChunk>();
		this.generatedChunks = new HashMap<String, GameScreenChunk>();
	}
	
	public abstract void render(GameContainer gc, Graphics g);
	
	public void update(GameContainer container, int delta) 
	{
		//update player
		for(int i = delta; i > 0; i--)
		{
			player.update(container, delta);
		}
				
		//update entities
		for(int i = 0; i < this.entities.size(); i++)
		{
			this.entities.get(i).update(container, delta);
		}
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public void addEntity(Entity entity)
	{ 
		this.entities.add(entity);
	}
	
	@Override
	public void draw(float x, float y)
	{
		//draw entities
		for(int i = 0; i < this.entities.size(); i++)
		{
			this.entities.get(i).draw(x, y);
		}
	}

	//draw player
	public void drawPlayer()
	{
		//display player
		//player.image.draw(player.getX(), player.getY());
		//player.arm.draw(player.getX() + 100, player.getY());
		player.draw(player.getX(), player.getY());
		
	}
		
	//draw position information
	public void drawDebugInformation(Graphics g)
	{
		g.drawString("WorldMap X:" + player.getWorldMapPosition().x + " Y:" + player.getWorldMapPosition().y, player.getX() - 380, player.getY() - 342);
		g.drawString("Position X:" + player.getX() + " Y:" + player.getY(), player.getX() - 380, player.getY() - 322);
		g.drawString("Screen X:" + player.getCurrentGameScreenChunkTopLeftPosition().x + " Y:" + player.getCurrentGameScreenChunkTopLeftPosition().y, player.getX() - 380, player.getY() - 302);
		g.drawString("Chunk X:" + player.getWorldMapChunkPosition().x+ " Y:" + player.getWorldMapChunkPosition().y, player.getX() - 380, player.getY() - 282);
		//g.drawString("Biome:" + player.getCurrentBiomeType(), player.getX() - 380, player.getY() - 262);
	
	}

	
}
