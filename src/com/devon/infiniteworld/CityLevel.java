package com.devon.infiniteworld;

import java.awt.Color;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.tiles.Tile;

public class CityLevel extends Level
{
	City city;

	public CityLevel(float xPos, float yPos, int width, int height) 
	{
		super(xPos, yPos, width, height);
		city = new City(37);
		this.tiles = city.map;
	}

	@Override
	public void draw(float x, float y) 
	{
		
	}

	@Override
	public void draw(float x, float y, Player player, Graphics g) 
	{
		Vector2f topLeftIndices = this.getMapArrayIndices(new Vector2f(player.position.x - (Game.SCREEN_WIDTH / 2), player.position.y - (Game.SCREEN_HEIGHT / 2)));
		Vector2f bottomRightIndices = this.getMapArrayIndices(new Vector2f(player.position.x + (Game.SCREEN_WIDTH / 2) + player.getWidth() * 2, player.position.y + (Game.SCREEN_HEIGHT / 2) + player.getHeight() * 2));
		
		if(topLeftIndices.x < 0)
			topLeftIndices.x = 0;
		if(topLeftIndices.y < 0)
			topLeftIndices.y = 0;
		if(bottomRightIndices.x < 0)
			bottomRightIndices.x = 0;
		if(bottomRightIndices.y < 0)
			bottomRightIndices.y = 0;
		
		for(int i = (int)topLeftIndices.x; i < (int)bottomRightIndices.x; i++)
		{
			for(int j = (int)topLeftIndices.y; j < (int)bottomRightIndices.y; j++)
			{
				int tileValue = this.tiles[i][j];

				if(tileValue == City.FLOOR)
				{
					Tile.cement.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));

				}
				else if(tileValue == City.WALL)
				{
					Tile.grass.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));

				}
				else if(tileValue == City.WATER)
				{
					Tile.water.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));

				}
				else if(tileValue == City.GRASS)
				{
					Tile.grass.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));

				}
				else if(tileValue == City.BUILDING)
				{
					Tile.dirt.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));

				}
					
			}
		}
		
		
	}
	
	

}
