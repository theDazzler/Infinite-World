package com.devon.infiniteworld;

import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.TileType;

/**
 * Generate chunks around the player as he moves
 * [][][]
 * []X []
 * [][][]
 * The chunks(brackets) around the player(X) are generated
 * @author Devon Guinane
 *
 */
public class ChunkGenerator 
{
	static Player player;
	
	public ChunkGenerator(Player player1)
	{
		player = player1;
	}
	
	/**
	 * Generate WorldMapChunks near player
	 * player is on chunk 4
	 * [0][1][2]
	 * [3][4][5]
	 * [6][7][8]
	 * @throws SlickException 
	 */
	public void generateWorldMapChunks() throws SlickException
	{
		//get top left coordinates of the chunk the player is currently on
		Vector2f currentWorldMapChunkPosition = player.getWorldMapChunkPosition();
		
		String xKey = "x"; //used for hashmap key(x32y32)
		String yKey = "y"; //used for hashmap key(x32y32)
		
		float startPosX = currentWorldMapChunkPosition.x - GameSettings.CHUNK_PIXEL_WIDTH; //start one WorldMapChunk above and to the left of player
		float startPosY = currentWorldMapChunkPosition.y - GameSettings.CHUNK_PIXEL_HEIGHT;
		
		
		//for each worldMapChunk in 3x3 section around player
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				String key = xKey + Integer.toString((int)startPosX) + yKey + Integer.toString((int)startPosY);
				System.out.println("STARTX: " + startPosX + "STARTY: " + startPosY);
				
				//place WorldMapChunk into hashmap
				WorldMap.map.put(key, new WorldMapChunk(new Vector2f(startPosX, startPosY), key));
				startPosX += GameSettings.CHUNK_PIXEL_WIDTH;
			}
			
			startPosX -= GameSettings.CHUNK_PIXEL_WIDTH * 3;
			startPosY += GameSettings.CHUNK_PIXEL_HEIGHT;
		}
	}
	
	//get rid of some water islands
	public static void cleanUpWorldChunkWater()
	{
		for (WorldMapChunk chunk : WorldMap.map.values())
		{	
			cleanMainSection(chunk);
			//cleanCorners(chunk);
			
		}
	}
	
	private static void cleanCorners(WorldMapChunk chunk)
	{
		//top left corner
		
		//get top left diagonal chunk 
		String key = "x" + Integer.toString((int)(chunk.xPos - GameSettings.CHUNK_PIXEL_WIDTH)) + "y" + Integer.toString((int)(chunk.yPos - GameSettings.CHUNK_PIXEL_HEIGHT));
		WorldMapChunk topLeftChunk = WorldMap.map.get(key);

		//bottom right corner of top left diagonal chunk(top left diagonal of the tile)
		if(topLeftChunk.terrain[GameSettings.CHUNK_HEIGHT][GameSettings.CHUNK_WIDTH] > GameSettings.WATER_THRESHOLD)
		{
			//get above chunk 
			String key2 = "x" + Integer.toString((int)(chunk.xPos)) + "y" + Integer.toString((int)(chunk.yPos - GameSettings.CHUNK_PIXEL_HEIGHT));
			WorldMapChunk topChunk = WorldMap.map.get(key2);

			if(topChunk.terrain[GameSettings.CHUNK_HEIGHT][0] > GameSettings.WATER_THRESHOLD)
			{
				//check top right diagonal tile
				if(topChunk.terrain[GameSettings.CHUNK_HEIGHT][1] > GameSettings.WATER_THRESHOLD)
				{
					//check left tile
					String key3 = "x" + Integer.toString((int)(chunk.xPos - GameSettings.CHUNK_PIXEL_WIDTH)) + "y" + Integer.toString((int)(chunk.yPos));
					WorldMapChunk leftChunk = WorldMap.map.get(key3);

					if(leftChunk.terrain[1][GameSettings.CHUNK_WIDTH] > GameSettings.WATER_THRESHOLD)
					{
						
							//check right tile
							
					}
				}
			}	
		}

	}

	//checks every tile except the edges
	private static void cleanMainSection(WorldMapChunk chunk) 
	{
		Random rand = new Random();
		
		//check everything except the edges of each chunk
		for(int i = 1; i < chunk.terrain.length - 1; i++)
		{
			for(int j = 1; j < chunk.terrain[i].length - 1; j++)
			{
				double value = chunk.terrain[i][j];
				
				if(value <= GameSettings.WATER_THRESHOLD)
				{
					if(surroundedByLand(chunk, i, j))
					{
						int randVal = rand.nextInt(100);
						if(randVal < 80)
							chunk.terrain[i][j] = TileType.GRASS;
					}
				}
			}
		}
		
	}

	
	
	private static boolean surroundedByLand(WorldMapChunk chunk, int x, int y) 
	{
		int xStartIndex = x - 1;
		int yStartIndex = y - 1;
		//if not in first or bottom row of the map
		if(x > 0 && x < chunk.terrain.length - 1)
		{
			//if not in leftmost or right most column
			if(y > 0 && y < chunk.terrain[x].length - 1)
			{
				for(int i = xStartIndex; i < xStartIndex + 3; i++)
				{
					for(int j = yStartIndex; j < yStartIndex + 3; j++)
					{
						//skip the tile we are checking(middle tile)
						if(i == x && j == y)
							continue;
	
						//if a surrounding tile is water, then return false
						if(chunk.terrain[i][j] <= GameSettings.WATER_THRESHOLD)
							return false;
					}
				}
				return true;
			}
		}
		//we are in top or bottom row
		else
		{
			//if top row
			if(x == 0)
			{
				//if in leftmost column, we are in top left corner of the WorldMapChunk
				if(y == 0)
				{
					//get top left diagonal chunk 
					String key = "x" + Integer.toString((int)(chunk.xPos - GameSettings.CHUNK_PIXEL_WIDTH)) + "y" + Integer.toString((int)(chunk.yPos - GameSettings.CHUNK_PIXEL_HEIGHT));
					WorldMapChunk topLeftChunk = WorldMap.map.get(key);
					
					//bottom right corner of top left diagonal chunk(top left diagonal of the tile)
					if(topLeftChunk.terrain[15][11] <= GameSettings.WATER_THRESHOLD)
						return false;
					
				}
				//in right most column, we are in top right corner
				else if(y == chunk.terrain[x].length - 1)
				{
					//get top right diagonal chunk 
					String key = "x" + Integer.toString((int)(chunk.xPos + GameSettings.CHUNK_PIXEL_WIDTH)) + "y" + Integer.toString((int)(chunk.yPos - GameSettings.CHUNK_PIXEL_HEIGHT));
					
					WorldMapChunk topRightChunk = WorldMap.map.get(key);
					
					//bottom left corner of top right diagonal chunk(top right diagonal of the tile)
					if(topRightChunk.terrain[15][0] <= GameSettings.WATER_THRESHOLD)
						return false;
				}
			}
			//bottom row
			else
			{
				//in leftmost column, bottom left corner
				if(y == 0)
				{
					//get bottom left diagonal chunk 
					String key = "x" + Integer.toString((int)(chunk.xPos - GameSettings.CHUNK_PIXEL_WIDTH)) + "y" + Integer.toString((int)(chunk.yPos + GameSettings.CHUNK_PIXEL_HEIGHT));
					WorldMapChunk bottomLeftChunk = WorldMap.map.get(key);
					
					//top right corner of bottom left diagonal chunk(bottom left diagonal of the tile)
					if(bottomLeftChunk.terrain[0][11] <= GameSettings.WATER_THRESHOLD)
						return false;
				}
				
			}
		}
		
		return false;
		
	}

	public void generateWorldMapChunkRow()
	{
		
	}
	
	//generate initial 3x3 section of GameScreenChunks around the player
	public void generateGameScreenChunks() throws SlickException
	{		
		Vector2f currentChunk = player.getCurrentGameScreenChunkTopLeftPosition();
		
		//start adding chunk from top left diagonal to be rendered
		float startX = currentChunk.getX() - GameSettings.CHUNK_PIXEL_WIDTH;
		float startY = currentChunk.getY() - GameSettings.CHUNK_PIXEL_HEIGHT;
		
		//for each GameScreenChunk in 3x3 section around player
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				//place GameScreenChunk into hashmap to be rendered
				GameScreenChunk chunk = new GameScreenChunk(new Vector2f(startX, startY));
				ChunkManager.visibleChunks.put("x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY), chunk);
				ChunkManager.generatedChunks.put("x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY), chunk);
				startX += GameSettings.CHUNK_PIXEL_WIDTH;
			}
			
			startX -= GameSettings.CHUNK_PIXEL_WIDTH * 3;
			startY += GameSettings.CHUNK_PIXEL_HEIGHT;
		}		
	}
}
