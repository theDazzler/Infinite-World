package com.devon.infiniteworld;

import org.newdawn.slick.GameContainer;

public class OutdoorEnvironment extends Environment
{	
	public void draw() 
	{
		drawGameScreenChunks();
		//drawMiniMap();		
	}

	@Override
	void update(GameContainer container, int delta)
	{
		//update entities
		for(int i = 0; i < this.entities.size(); i++)
		{
			this.entities.get(i).update(container, delta);
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
