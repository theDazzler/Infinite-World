package com.devon.infiniteworld;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;

import com.devon.infiniteworld.tiles.TileType;

public class MiniMap implements Renderable
{
	float xPos;
	float yPos;
	Image background;
	Image water;
	Image grass;
	Image snow;
	Image volcanic;
	
	float tileScaleFactor = 0.25f;
	int width = (int) ((GameSettings.TILE_WIDTH * tileScaleFactor) * GameSettings.CHUNK_WIDTH);
	int height = (int) ((GameSettings.TILE_HEIGHT * tileScaleFactor) * GameSettings.CHUNK_HEIGHT);
	
	public MiniMap(float xPos, float yPos) throws SlickException
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.water = new Image("assets/images/tiles/water.png");
		this.grass = new Image("assets/images/tiles/grass.png");
		this.snow = new Image("assets/images/tiles/snow.png");
		this.volcanic = new Image("assets/images/tiles/volcanic.png");
	}

	@Override
	public void draw(float x, float y) 
	{
		int start = 0;
		float startX = 200;
		float startY = 50;
		
		//render 3x3 GameScreenChunks
		for (WorldMapChunk chunk : WorldMap.map.values())
		{	
			for(int i = 0; i < chunk.terrain.length; i++)
			{
				for(int j = 0; j < chunk.terrain[i].length; j++)
				{
					int tileType = chunk.tileTypes[i][j];
					
					switch(tileType)
					{
						case TileType.WATER:
							water.draw((float)(startX + (j * (GameSettings.TILE_WIDTH * tileScaleFactor))), (float)(startY + (i * (GameSettings.TILE_HEIGHT * tileScaleFactor))), tileScaleFactor);
							break;
							
						case TileType.GRASS:
							grass.draw((float)(startX + (j * (GameSettings.TILE_WIDTH * tileScaleFactor))), (float)(startY + (i * (GameSettings.TILE_HEIGHT * tileScaleFactor))), tileScaleFactor);
							break;
							
						case TileType.SNOW:
							snow.draw((float)(startX + (j * (GameSettings.TILE_WIDTH * tileScaleFactor))), (float)(startY + (i * (GameSettings.TILE_HEIGHT * tileScaleFactor))), tileScaleFactor);
							break;
							
						case TileType.VOLCANIC:
							volcanic.draw((float)(startX + (j * (GameSettings.TILE_WIDTH * tileScaleFactor))), (float)(startY + (i * (GameSettings.TILE_HEIGHT * tileScaleFactor))), tileScaleFactor);
							break;
					}
					//chunk.draw(player.getX() + 300, player.getY() - 300);
				}
			}
			startX += 256;
			
			if(startX > 700)
			{
				startX = 200;
				startY += 192;
			}	
		}
	}
}
