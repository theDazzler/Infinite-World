package com.devon.infiniteworld;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;

import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.Tile;

public class MiniMap implements Renderable
{
	float xPos;
	float yPos;
	Image background;
	Image water;
	Image grass;
	Image snow;
	Image lava;
	Image tree;
	Image dirt;
	Image cement;
	
	float tileScaleFactor = 0.125f;
	int width = (int) ((GameSettings.TILE_WIDTH * tileScaleFactor) * GameSettings.CHUNK_WIDTH);
	int height = (int) ((GameSettings.TILE_HEIGHT * tileScaleFactor) * GameSettings.CHUNK_HEIGHT);
	
	public MiniMap(float xPos, float yPos) throws SlickException
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.water = new Image("assets/images/tiles/water.png");
		this.grass = new Image("assets/images/tiles/grass.png");
		this.snow = new Image("assets/images/tiles/snow.png");
		this.lava = new Image("assets/images/tiles/lava.png");
		this.tree = new Image("assets/images/tiles/tree.png");
		this.dirt = new Image("assets/images/tiles/dirt.png");
		this.cement = new Image("assets/images/tiles/cement.png");
		
		/**
		water = water.getScaledCopy(tileScaleFactor);
		grass = grass.getScaledCopy(tileScaleFactor);
		tree = tree.getScaledCopy(tileScaleFactor);
		lava = lava.getScaledCopy(tileScaleFactor);
		snow = snow.getScaledCopy(tileScaleFactor);
		**/
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
					int screenType = chunk.gameScreenTypes[i][j];
					
					if(screenType == GameScreenType.water.id)
					{
						water.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.plain.id)
					{
						grass.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.forest.id)
					{
						tree.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.snow.id)
					{
						snow.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.volcanic.id)
					{
						lava.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityRoad.id)
					{
						cement.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityBuilding.id)
					{
						dirt.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityCoast.id)
					{
						grass.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityWater.id)
					{
						water.draw((float)(startX + (j * (Tile.WIDTH * (tileScaleFactor * 2)))), (float)(startY + (i * (Tile.HEIGHT * (tileScaleFactor * 2)))), tileScaleFactor);
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
