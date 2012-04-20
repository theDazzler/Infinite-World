package com.devon.infiniteworld;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.tiles.Tile;

public class OutdoorLevel extends Level
{
	public OutdoorLevel(float xPos, float yPos, int width, int height) 
	{
		super(xPos, yPos, width, height);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void draw(float x, float y, Player player)
	{
		for(int i = 0; i < this.map.length; i++)
		{
			for(int j = 0; j < this.map[i].length; j++)
			{
				//if tile is in the player's view frustram, then draw it
				if(player.inFrustram(x + (j* Tile.WIDTH), y + (i * Tile.HEIGHT)))
				{
					byte tileType = this.map[i][j];
					if(tileType == Tile.grass.id)
					{
						Tile.grass.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else if(tileType == Tile.water.id)
					{
						Tile.water.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else if(tileType == Tile.snow.id)
					{
						Tile.snow.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else if(tileType == Tile.lava.id)
					{
						Tile.lava.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else if(tileType == Tile.cement.id)
					{
						Tile.cement.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else if(tileType == Tile.dirt.id)
					{
						Tile.dirt.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else if(tileType == Tile.mountain.id)
					{
						Tile.mountain.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
				}
			}
		}
	}

	@Override
	public void draw(float arg0, float arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
