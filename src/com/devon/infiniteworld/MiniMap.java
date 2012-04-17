package com.devon.infiniteworld;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.test.NoiseMap;
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
	int tileWidth;
	int tileHeight;
	Player player;
	NoiseMap noise;
	
	float tileScaleFactor = 0.03125f;
	int width = (int) ((GameSettings.TILE_WIDTH * tileScaleFactor) * GameSettings.WORLDMAP_CHUNK_WIDTH);
	int height = (int) ((GameSettings.TILE_HEIGHT * tileScaleFactor) * GameSettings.WORLDMAP_CHUNK_HEIGHT);
	
	public MiniMap(float xPos, float yPos, Player player) throws SlickException
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.player = player;
		this.tileWidth = 8;
		this.tileHeight = 8;
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
		int chunkNumber = 0;
		float startX = x;
		float startY = y;
		
		//render 3x3 GameScreenChunks
		for (WorldMapChunk chunk : WorldMap.map.values())
		{		
			if(chunkNumber %3 == 0)
			{
				startX = x;
				startY += this.tileHeight * chunk.cityData.length;
			}
			
			for(int i = 0; i < chunk.gameScreenTypes.length; i++)
			{
				for(int j = 0; j < chunk.gameScreenTypes[i].length; j++)
				{
					int screenType = chunk.gameScreenTypes[i][j];		
					
					if(screenType == GameScreenType.water.id)
					{
						water.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.plain.id)
					{
						grass.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.forest.id)
					{
						tree.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.snow.id)
					{
						snow.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.volcanic.id)
					{
						lava.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityRoad.id)
					{
						cement.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityBuilding.id)
					{
						dirt.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.dirt.id)
					{
						dirt.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityCoast.id)
					{
						grass.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					else if(screenType == GameScreenType.cityWater.id)
					{
						water.draw(startX + (j * this.tileWidth), startY + (i * this.tileHeight), this.tileScaleFactor);
					}
					//chunk.draw(player.getX() + 300, player.getY() - 300);
					
					

				}
			}
			startX += this.tileWidth * chunk.cityData[0].length;
			chunkNumber++;
		}
	}
	
	public void drawNoise(float x, float y)
	{
		int chunkNumber = 0;
		float startX = x;
		float startY = y;
		
		
		for(int b = 0; b < 128; b++)
		{
			for(int a = 0; a < 128; a++)
			{
				for (String key : OutdoorEnvironment.noiseMaps.keySet())
				{
					double[][] map = OutdoorEnvironment.noiseMaps.get(key).noiseData;
					double screenType = map[a][b];
						if(screenType < 0)
						{
							water.draw(x + (a * this.tileWidth / 2), y + (b * this.tileHeight / 2), this.tileScaleFactor);
						}
						else if(screenType < 0.5)
						{
							dirt.draw(x + (a * this.tileWidth / 2), y + (b * this.tileHeight / 2), this.tileScaleFactor);
						}
						else if(screenType > 0.85 && screenType < 1)
						{
							snow.draw(x + (a * this.tileWidth / 2), y + (b * this.tileHeight / 2), this.tileScaleFactor);
						}
						else
						{
							grass.draw(x + (a * this.tileWidth / 2), y + (b * this.tileHeight / 2), this.tileScaleFactor);
						}
						
						//chunk.draw(player.getX() + 300, player.getY() - 300);
						
				}
			}
		}
		//System.out.println(player.getWorldMapPosition().x);
		lava.draw(this.xPos + (player.getWorldMapPosition().x * tileScaleFactor), this.yPos + (player.getWorldMapPosition().y *  tileScaleFactor), tileScaleFactor);
	}
	
	public void update(int delta)
	{
		this.xPos = player.getX() - 100;
		this.yPos = player.getY() - 350;
	}
}
