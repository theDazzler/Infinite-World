package com.devon.infiniteworld;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.test.Cave;
import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.Tile;

public class CaveGameScreenChunk extends GameScreenChunk
{
	CaveWorld caveWorld;
	
	public CaveGameScreenChunk(Vector2f position) throws SlickException 
	{
		super(position);
		this.caveWorld = caveWorld;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateObjectLayer() 
	{
		
		
	}

	@Override
	public void generateTileLayer() 
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				int tileValue = 0;
				
				switch(this.caveWorld.map[i][j])
				{
					case Cave.FLOOR:
						tileValue = Tile.dirt.id;
						break;
						
					case Cave.WALL:
						tileValue = Tile.lava.id;
						break;
						
				}
				
				this.tileLayer[i][j] = tileValue;
			}
		}		
	}

	@Override
	public void drawTileLayer() 
	{
		for(int i = 0; i < this.tileLayer.length; i++)
		{
			for(int j = 0; j < this.tileLayer[i].length; j++)
			{
				switch(this.tileLayer[i][j])
				{
					case CaveWorld.FLOOR:
					{
						Tile.dirt.draw((float)(this.position.x + (j * Tile.WIDTH)), this.position.y + (i * Tile.HEIGHT));
						break;
					}
					
					case CaveWorld.WALL:
					{
						Tile.lava.draw((float)(this.position.x + (j * Tile.WIDTH)), this.position.y + (i * Tile.HEIGHT));
						break;
					}
				}
			}
		}
		
	}

}
