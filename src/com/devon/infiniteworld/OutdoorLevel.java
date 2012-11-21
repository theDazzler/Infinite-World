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
	static Random rand = new Random();
	OneByOneTree tree;
	
	
	
	public OutdoorLevel(float xPos, float yPos, int width, int height) 
	{
		super(xPos, yPos, width, height);
		//this.heightMap = NoiseMap.getMap(width, height, 0, 0);
		this.tempMap = TempMap.getMap(width, height);
		
		this.tiles = new int[width][height];
		
		this.addChunk(new Chunk((int)xPos, (int)yPos, this.tiles));
		
		this.assignTileTypes();
		this.addObjects();
		
	}
	
	public void assignTileTypes()
	{
		//for each chunk
		for(int i = 0; i < this.getChunks().size(); i++)
		{
			Chunk chunk = this.getChunks().get(i);
			for(int y = 0; y < chunk.heightMap.length; y++)
			{
				for(int x = 0; x < chunk.heightMap[y].length; x++)
				{
					//if water
					if(chunk.heightMap[y][x] < 0)
					{
						this.tiles[y][x] = Tile.water.id;//water
						
					}
					
					//if beach sand
					else if(chunk.heightMap[y][x] < 0.4)
					{
	
						this.tiles[y][x] = Tile.sand.id; //beach sand
					}
						
					
					//if grass
					else if(chunk.heightMap[y][x] < 6.5)
					{
						if(this.tempMap[y][x] > 1.2)
						{
							this.tiles[y][x] = Tile.sand.id; //desert sand
						}
						else
							this.tiles[y][x] = Tile.grass.id; //grass
					}
						
					//if mountain
					else if(chunk.heightMap[y][x] > 6.5)
					{
						this.tiles[y][x] = Tile.mountain.id;//mountain
					}
					//else if(testM[y][x] == 3)pixels[i] = 0xa77939;//semi-mountain
					//else if(testM[y][x] < 3)pixels[i] = 0x404040; //dirt
				}
			}
		}
	}
	
	public void addObjects()
	{
		//for each chunk
		for(int c = 0; c < this.getChunks().size(); c++)
		{
			Chunk chunk = this.getChunks().get(c);
			for(int i = 0; i < chunk.heightMap.length; i++)
			{
				for(int j = 0; j < chunk.heightMap[i].length; j++)
				{
					if(this.tiles[i][j] == Tile.grass.id)
					{
						if(this.tempMap[i][j] > 0.4 && this.tempMap[i][j] < 1.2)
						{
							if(rand.nextInt(3) == 0)
								chunk.objects[i][j] = Tree.basicTree.id; //tree	
						}
					}
				}
			}
		}
	}
		
	
	@Override
	public void draw(float x, float y, Player player, Graphics g)
	{		
		for(Chunk chunk: this.getChunks())
			chunk.draw(x, y, player, this, g);
	}

	@Override
	public void draw(float arg0, float arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
