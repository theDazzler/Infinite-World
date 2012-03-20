package com.devon.infiniteworld;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class ChunkManager 
{
	public static HashMap<String, GameScreenChunk> visibleChunks = new HashMap<String, GameScreenChunk>(); //holds chunks that need to be rendered(3x3 section surrounding player)

	//adds a column of GameScreenChunksto to be rendered to the right or left of the player's current GameScreenChunk
	//String side should be "left" to add left column or "right" to add right column
	public static void addRenderColumn(String side, Player player, Vector2f playerPreviousGameScreenChunkPosition)
	{
		float startX = 0;
		
		if(side == "left")
			startX  = playerPreviousGameScreenChunkPosition.x - (2 * GameSettings.CHUNK_PIXEL_WIDTH);
		else if(side == "right")
			startX = playerPreviousGameScreenChunkPosition.x + (2 * GameSettings.CHUNK_PIXEL_WIDTH);
		
		float startY = playerPreviousGameScreenChunkPosition.y - (GameSettings.CHUNK_PIXEL_HEIGHT);
		
		//generate one column
		for(int i = 0; i < 3; i++)
		{
			String key = "x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY);
			if(!visibleChunks.containsKey(key))
			{
				//add GameScreenChunks to ChunkManager's hashmap to get rendered
				try
				{
					visibleChunks.put(key, new GameScreenChunk(new Vector2f(startX, startY)));
				} 
				catch (SlickException e)
				{
					System.out.println("Couldn't create GameScreenChunk " + e.getMessage());
				}
			}

			startY += GameSettings.CHUNK_PIXEL_HEIGHT;
		}
		
		if(side == "left")
			removeRenderColumn("right", playerPreviousGameScreenChunkPosition);
		else if(side == "right")
			removeRenderColumn("left", playerPreviousGameScreenChunkPosition);
		
		System.out.println("RENDER SIZE: " + visibleChunks.size());
		System.out.println("WorldChunkSize: " + WorldMap.map.size());
		
	}

	//removes a column of GameScreenChunkstoso it won't be rendered anymore
	private static void removeRenderColumn(String side, Vector2f playerPreviousGameScreenChunkPosition) 
	{
		float startX = 0;
		
		if(side == "left")
			startX = playerPreviousGameScreenChunkPosition.x - GameSettings.CHUNK_PIXEL_WIDTH;
		else if(side == "right")
			startX = playerPreviousGameScreenChunkPosition.x + GameSettings.CHUNK_PIXEL_WIDTH;
		
		float startY = playerPreviousGameScreenChunkPosition.y - (GameSettings.CHUNK_PIXEL_HEIGHT);
		
		//remove column
		for(int i = 0; i < 3; i++)
		{
			//remove column so it wont get rendered
			visibleChunks.remove("x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY));
			startY += GameSettings.CHUNK_PIXEL_HEIGHT;
		}
	}	
	
	//adds a row of GameScreenChunksto be rendered, "top" or "bottom"
	public static void addRenderRow(String side, Player player, Vector2f playerPreviousGameScreenChunkPosition)
	{
		float startY = 0;
		
		if(side == "top")
			startY  = playerPreviousGameScreenChunkPosition.y - (2 * GameSettings.CHUNK_PIXEL_HEIGHT);
		else if(side == "bottom")
			startY = playerPreviousGameScreenChunkPosition.y + (2 * GameSettings.CHUNK_PIXEL_HEIGHT);
		
		float startX = playerPreviousGameScreenChunkPosition.x - GameSettings.CHUNK_PIXEL_WIDTH;
		
		//generate one row
		for(int i = 0; i < 3; i++)
		{
			String key = "x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY);
			if(!visibleChunks.containsKey(key))
			{
				//add GameScreenChunks to ChunkManager's hashmap to get rendered
				try 
				{
					visibleChunks.put(key, new GameScreenChunk(new Vector2f(startX, startY)));
				} 
				catch (SlickException e) 
				{
					// TODO Auto-generated catch block
					System.out.println("Couldn't create GameScreenChunk " + e.getMessage());
				}
			}

			startX += GameSettings.CHUNK_PIXEL_WIDTH;
		}
		
		if(side == "top")
			removeRenderRow("bottom", playerPreviousGameScreenChunkPosition);
		else if(side == "bottom")
			removeRenderRow("top", playerPreviousGameScreenChunkPosition);
		
		System.out.println("RENDER SIZE: " + visibleChunks.size());
		System.out.println("WorldChunkSize: " + WorldMap.map.size());
	}

	//removes a row of GameScreenChunkstoso it wont be rendered anymore
	private static void removeRenderRow(String side, Vector2f playerPreviousGameScreenChunkPosition)
	{
		float startY = 0;
		
		if(side == "top")
			startY = playerPreviousGameScreenChunkPosition.y - GameSettings.CHUNK_PIXEL_HEIGHT;
		else if(side == "bottom")
			startY = playerPreviousGameScreenChunkPosition.y + GameSettings.CHUNK_PIXEL_HEIGHT;
		
		float startX = playerPreviousGameScreenChunkPosition.x - (GameSettings.CHUNK_PIXEL_WIDTH);
		
		//remove row
		for(int i = 0; i < 3; i++)
		{
			//remove row so it wont get rendered
			visibleChunks.remove("x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY));
			startX += GameSettings.CHUNK_PIXEL_WIDTH;
		}
		
	}
	
	public static void addWorldChunkRow(String side, Player player, Vector2f playerCurrentGameScreenChunkPosition)
	{
		float startY = 0;
		
		//generate new row *********************************************************************************************
		if(side == "top")
		{
			startY  = playerCurrentGameScreenChunkPosition.y - GameSettings.CHUNK_PIXEL_HEIGHT;
		}
		else if(side == "bottom")
		{
			startY  = playerCurrentGameScreenChunkPosition.y + GameSettings.CHUNK_PIXEL_HEIGHT;
		}
		
		float startX = playerCurrentGameScreenChunkPosition.x - GameSettings.CHUNK_PIXEL_WIDTH;
		
		//generate one row
		for(int i = 0; i < 3; i++)
		{
			String key = "x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY);
			
			if(!WorldMap.map.containsKey(key))
			{
				//check if WorldMapChunk file already exists
				File file = new File(WorldChunkWriter.getFilePath() + key + WorldChunkWriter.getFileType());
				
				//if WorldMapChunk doesn't exist in file, generate new WorldMapChunk
				if(!file.exists())
				{
					//add WorldMapChunk to WorldMap Hashmap
					try 
					{
						WorldMap.map.put(key, new WorldMapChunk(new Vector2f(startX, startY), key));
						System.out.println("ROW ADDED to HASHY" + key);
					} 
					catch (SlickException e) 
					{
						// TODO Auto-generated catch block
						System.out.println("Couldn't create WorldMapChunk at " + startX + " " + startY + e.getMessage());
					}
				}
				//read WorldChunk from file and add it to WorldMap
				else
				{
					System.out.println("FILE EXISTS...READING FILE");
					WorldMapChunk worldChunk = WorldChunkReader.readWorldChunk(key);

					WorldMap.map.put(key, worldChunk);
					
				}
			}
			
			startX += GameSettings.CHUNK_PIXEL_WIDTH;			
		}
		
		//***************************************************************************************************************
		
		//write far row to file
		if(side == "top")
		{
			//write bottom row to file
			removeWorldMapChunkRow("bottom", playerCurrentGameScreenChunkPosition);
		}
		else if(side == "bottom")
		{
			removeWorldMapChunkRow("top", playerCurrentGameScreenChunkPosition);
		}
		
		//WorldMap.dump();
	}

	//writes each WorldMapChunk in a row to a text file so they can later be retrieved, then removes them from the WorldMapHashMap
	private static void removeWorldMapChunkRow(String side, Vector2f playerCurrentWorldMapChunkPosition)
	{
		HashMap<String, WorldMapChunk> chunks = new HashMap<String, WorldMapChunk>();

		float startY = 0;
		
		if(side == "top")
			startY = playerCurrentWorldMapChunkPosition.y - (2 * GameSettings.CHUNK_PIXEL_HEIGHT);
		else if(side == "bottom")
			startY = playerCurrentWorldMapChunkPosition.y + (2 * GameSettings.CHUNK_PIXEL_HEIGHT);
		
		float startX = playerCurrentWorldMapChunkPosition.x - (GameSettings.CHUNK_PIXEL_WIDTH);
		
		//write the row to file before removing it
		for(int i = 0; i < 3; i++)
		{
			String key = "x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY);
						
			WorldMapChunk chunk = WorldMap.map.get(key);
			
			chunks.put(key, chunk);
			//write the chunk to file
			//WorldChunkWriter.writeWorldChunk(chunk, key);
			
			//remove chunk from worldMap
			//WorldMap.map.remove(key);
			
			startX += GameSettings.CHUNK_PIXEL_WIDTH;
		}
		
		new Thread(new WorldChunkWriter(chunks)).start();
		
		System.out.println("WorldChunkSize: " + WorldMap.map.size());
	}

	public static void addWorldChunkColumn(String side, Player player,Vector2f playerCurrentWorldMapChunkPosition) 
	{
		float startX = 0;
		
		if(side == "left")
			startX  = playerCurrentWorldMapChunkPosition.x - GameSettings.CHUNK_PIXEL_WIDTH;
		else if(side == "right")
			startX = playerCurrentWorldMapChunkPosition.x + GameSettings.CHUNK_PIXEL_WIDTH;
		
		float startY = playerCurrentWorldMapChunkPosition.y - (GameSettings.CHUNK_PIXEL_HEIGHT);
		
		//generate one column
		for(int i = 0; i < 3; i++)
		{
			String key = "x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY);
			
			//if chunk isn't already in the WorldMap
			if(!WorldMap.map.containsKey(key))
			{
				//check if WorldMapChunk file already exists
				File file = new File(WorldChunkWriter.getFilePath() + key + WorldChunkWriter.getFileType());
				
				//if WorldMapChunk doesn't exist in file, generate new WorldMapChunk
				if(!file.exists())
				{
					System.out.println("FILE DOESNT EXIST - Generating new WorldMapChunk...");
					//add WorldMapChunks to WorldMap's hashmap
					try
					{
						WorldMap.map.put(key, new WorldMapChunk(new Vector2f(startX, startY), key));
					} 
					catch (SlickException e)
					{
						System.out.println("Couldn't create GameScreenChunk " + e.getMessage());
					}
				}
				//if file exists, read file and add it to WorldMap hashmap
				else
				{
					System.out.println("FILE EXISTS...READING FILE");
					WorldMapChunk worldChunk = WorldChunkReader.readWorldChunk(key);

					WorldMap.map.put(key, worldChunk);
					
				}
			}

			startY += GameSettings.CHUNK_PIXEL_HEIGHT;
		}
		
		if(side == "left")
			removeWorldMapChunkColumn("right", playerCurrentWorldMapChunkPosition);
		else if(side == "right")
			removeWorldMapChunkColumn("left", playerCurrentWorldMapChunkPosition);
		
		//System.out.println("WorldChunkSize: " + WorldMap.map.size());	
	}

	private static void removeWorldMapChunkColumn(String side, Vector2f playerCurrentWorldMapChunkPosition)
	{
		HashMap<String, WorldMapChunk> chunks = new HashMap<String, WorldMapChunk>();
		
		float startX = 0;
		
		if(side == "left")
			startX = playerCurrentWorldMapChunkPosition.x - (2 * GameSettings.CHUNK_PIXEL_WIDTH);
		else if(side == "right")
			startX = playerCurrentWorldMapChunkPosition.x + (2 * GameSettings.CHUNK_PIXEL_WIDTH);
		
		float startY = playerCurrentWorldMapChunkPosition.y - (GameSettings.CHUNK_PIXEL_HEIGHT);
		
		//write the column to file before removing it
		for(int i = 0; i < 3; i++)
		{
			String key = "x" + Integer.toString((int)startX) + "y" + Integer.toString((int)startY);
			WorldMapChunk chunk = WorldMap.map.get(key);

			chunks.put(key, chunk);

			startY += GameSettings.CHUNK_PIXEL_HEIGHT;
		}
		
		new Thread(new WorldChunkWriter(chunks)).start();
	}
}
