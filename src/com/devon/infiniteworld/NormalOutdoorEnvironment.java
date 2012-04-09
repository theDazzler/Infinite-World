package com.devon.infiniteworld;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;

public class NormalOutdoorEnvironment extends Environment
{
	ChunkGenerator chunkGenerator;
	
	public NormalOutdoorEnvironment()
	{
		super();	
		
		
	}
	
	public void create()
	{		
		chunkGenerator = new ChunkGenerator(player);
		
		try 
		{
			chunkGenerator.generateGameScreenChunks();
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public NormalOutdoorEnvironment(Player player)
	{
		super(player);	
				
		chunkGenerator = new ChunkGenerator(player);
		
		try 
		{
			chunkGenerator.generateGameScreenChunks();
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public void render(GameContainer gc, Graphics g)
	{
		drawGameScreenChunks();
		//drawMiniMap();
		drawDebugInformation(g);
		drawPlayer();
		
		//draw entities
		for(int i = 0; i < this.entities.size(); i++)
		{
			this.entities.get(i).draw();
		}
	}
	
	//render GameScreenChunks
	private void drawGameScreenChunks() 
	{
		//render 3x3 GameScreenChunks
		for (GameScreenChunk chunk : this.visibleChunks.values()) 
		{
			chunk.draw(chunk.getX(), chunk.getY());
		}
	}
}
