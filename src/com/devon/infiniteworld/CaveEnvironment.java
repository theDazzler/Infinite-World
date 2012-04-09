package com.devon.infiniteworld;

import java.awt.Color;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.tiles.Tile;

public class CaveEnvironment extends Environment
{
	CaveWorld caveWorld;
	
	public CaveEnvironment(float xPos, float yPos) 
	{
		super();
		this.caveWorld = new CaveWorld(xPos, yPos);
		
	}
	
	public CaveEnvironment(Player player, float xPos, float yPos) 
	{
		super(player);
		this.caveWorld = new CaveWorld(xPos, yPos);
		
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) 
	{
		drawCaveGameScreenChunks();
		drawDebugInformation(g);
		drawPlayer();
				
	}

	private void drawCaveGameScreenChunks()
	{
		//render 3x3 GameScreenChunks
		for (GameScreenChunk chunk : this.visibleChunks.values()) 
		{
			chunk.draw(chunk.getX(), chunk.getY());
		}
	}
}
