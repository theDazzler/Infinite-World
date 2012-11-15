package com.devon.infiniteworld;

import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.objects.OneByOneTree;
import com.devon.infiniteworld.objects.Tree;
import com.devon.infiniteworld.objects.WorldObject;
import com.devon.infiniteworld.tiles.Tile;

public class OutdoorLevel extends Level
{
	double[][] tempMap; //holds temperature data
	int[][] objects;
	static Random rand = new Random();
	OneByOneTree tree;
	
	
	
	public OutdoorLevel(float xPos, float yPos, int width, int height) 
	{
		super(xPos, yPos, width, height);
		this.heightMap = NoiseMap.getMap(width, height);
		this.tempMap = TempMap.getMap(width, height);
		
		this.tiles = new int[width][height];
		this.objects = new int[width][height];
		
		this.assignTileTypes();
		this.addObjects();
		
	}
	
	public void assignTileTypes()
	{
		for(int y = 0; y < this.heightMap.length; y++)
		{
			for(int x = 0; x < this.heightMap[y].length; x++)
			{
				//if water
				if(this.heightMap[y][x] < 0)
				{
					this.tiles[y][x] = Tile.water.id;//water
					
				}
				
				//if beach sand
				else if(this.heightMap[y][x] < 0.4)
				{

					this.tiles[y][x] = Tile.sand.id; //beach sand
				}
					
				
				//if grass
				else if(this.heightMap[y][x] < 6.5)
				{
					if(this.tempMap[y][x] > 1.2)
					{
						this.tiles[y][x] = Tile.sand.id; //desert sand
					}
					else
						this.tiles[y][x] = Tile.grass.id; //grass
				}
					
				//if mountain
				else if(this.heightMap[y][x] > 6.5)
				{
					this.tiles[y][x] = Tile.mountain.id;//mountain
				}
				//else if(testM[y][x] == 3)pixels[i] = 0xa77939;//semi-mountain
				//else if(testM[y][x] < 3)pixels[i] = 0x404040; //dirt
			}
		}
	}
	
	public void addObjects()
	{
		for(int i = 0; i < this.heightMap.length; i++)
		{
			for(int j = 0; j < this.heightMap[i].length; j++)
			{
				if(this.tiles[i][j] == Tile.grass.id)
				{
					if(this.tempMap[i][j] > 0.4 && this.tempMap[i][j] < 1.2)
					{
						if(rand.nextInt(3) == 0)
							this.objects[i][j] = Tree.basicTree.id; //tree	
					}
				}
			}
		}
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
			bottomRightIndices.y= 0;
		
		//only loop through tiles that are near the player rather than the ENTIRE map!
		for(int i = (int)topLeftIndices.x; i < (int)bottomRightIndices.x; i++)
		{
			for(int j = (int)topLeftIndices.y; j < (int)bottomRightIndices.y; j++)
			{
				//if tile is in the player's view frustram, then draw it
				int tileType = this.tiles[i][j];
				int objectType = this.objects[i][j];
				double temperature = this.tempMap[i][j];
				
				if(tileType == Tile.grass.id)
				{		
					//check array to prevent ArrayOutOFBoundsException
					if(i != this.tiles.length && j != 0)
					{
						//if grass has a different type of tile to the left and below it, then draw a bottom left corner grass tile
						if(this.tiles[i][j - 1] != Tile.grass.id && this.tiles[i + 1][j] != Tile.grass.id)
						{
							Tile tile = this.getTile(i + 1, j); //get type of tile below the tile
							tile.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT))); //draw that tile first so that it fills in the rounded corners with whatever should be underneath the tile

							try {
								g.drawImage(new Image("assets/images/tiles/grass_bottom_left_corner_02.png"), (j * Tile.WIDTH), (float)(y + (i * Tile.HEIGHT)));
							} catch (SlickException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
							Tile.grass.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					}
					else
						Tile.grass.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
					
					
					
				}
				else if(tileType == Tile.water.id)
				{
					//water
					Tile.water.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.snow.id)
				{
					//snow
					Tile.snow.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
				}
				else if(tileType == Tile.lava.id)
				{
					//lava
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
				else if(tileType == Tile.sand.id)
				{
					Tile.sand.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
				}
				
				if(objectType == Tree.basicTree.id)
				{	
					Tree.basicTree.draw((float)(x + (j * Tile.WIDTH)), (float)(y + (i * Tile.HEIGHT)));
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
