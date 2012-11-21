package com.devon.infiniteworld;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.objects.Tree;
import com.devon.infiniteworld.tiles.Tile;

public class Chunk 
{
	public double[][] heightMap;
	int[][] objects;
	public float xPos;
	public float yPos;
	public int[][] tiles;
	public static final int SIZE = 128;
	
	public Chunk(int xPos, int yPos, int[][] tiles)
	{
		this.tiles = tiles;
		this.objects = new int[SIZE][SIZE];
		this.heightMap = MyNoiseMap.getMap(SIZE, SIZE, xPos, yPos);
	}
	
	public void draw(float x, float y, Player player, Level level, Graphics g)
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
			bottomRightIndices.y= 0;
		
		//only loop through tiles that are near the player rather than the ENTIRE map!
		for(int i = (int)topLeftIndices.x; i < (int)bottomRightIndices.x; i++)
		{
			for(int j = (int)topLeftIndices.y; j < (int)bottomRightIndices.y; j++)
			{
				//if tile is in the player's view frustram, then draw it
				int tileType = this.tiles[i][j];
				int objectType = this.objects[i][j];
				//double temperature = this.tempMap[i][j];
				
				if(tileType == Tile.grass.id)
				{		
					//check array to prevent ArrayOutOFBoundsException
					if(i != this.tiles.length && j != 0)
					{
						//if grass has a different type of tile to the left and below it, then draw a bottom left corner grass tile
						if(this.tiles[i][j - 1] != Tile.grass.id && this.tiles[i + 1][j] != Tile.grass.id)
						{
							Tile tile = level.getTile(i + 1, j); //get type of tile below the tile
							tile.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT))); //draw that tile first so that it fills in the rounded corners with whatever should be underneath the tile

							try {
								g.drawImage(new Image("assets/images/tiles/grass_bottom_left_corner_02.png"), (j * Tile.WIDTH), (float)(y + (i * Tile.HEIGHT)));
							} catch (SlickException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
							Tile.grass.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
					}
					else
						Tile.grass.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
					
					
					
				}
				else if(tileType == Tile.water.id)
				{
					//water
					Tile.water.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.snow.id)
				{
					//snow
					Tile.snow.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.lava.id)
				{
					//lava
					Tile.lava.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.cement.id)
				{
					Tile.cement.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.dirt.id)
				{
					Tile.dirt.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.mountain.id)
				{
					
					Tile.mountain.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}	
				else if(tileType == Tile.sand.id)
				{
					Tile.sand.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				
				if(objectType == Tree.basicTree.id)
				{	
					Tree.basicTree.draw((float)(this.xPos + (j * Tile.WIDTH)), (float)(this.yPos + (i * Tile.HEIGHT)));
				}
				
			}	
		}
	}
	
	public Vector2f getMapArrayIndices(Vector2f position)
	{
		int x = (int) (position.y / Tile.HEIGHT);
		int y = (int) (position.x / Tile.WIDTH);

		return new Vector2f(x, y);
	}
}
