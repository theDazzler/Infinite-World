package com.devon.infiniteworld;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Writes WorldChunks that are two world chunks away from the player to a binary file
 * @author Devon Guinane
 *
 */
public class WorldChunkWriter implements Runnable
{
	private static String filePath = "world_chunks/";
	private static String fileType = ".dat";
	private HashMap<String, WorldMapChunk> chunks; //holds WorldMapChunks that need to be written and removed
	
	public WorldChunkWriter(HashMap<String, WorldMapChunk> chunks)
	{
		this.chunks = chunks;
	}
	
	@Override
	public void run() 
	{
		writeWorldChunks();
		removeWorldChunks();
		WorldMap.dump();
	}

	private void removeWorldChunks()
	{
		for (String key : chunks.keySet()) 
		{
			WorldMap.map.remove(key);
		}
		
	}

	//write WorldMapChunks objects to file
	public void writeWorldChunks()
	{
		ObjectOutputStream oStream = null;
		try
		{
			for(Map.Entry<String, WorldMapChunk> entry: this.chunks.entrySet())
			{
				String filename = entry.getKey();
				WorldMapChunk chunk = entry.getValue();
				
				oStream = new ObjectOutputStream(new FileOutputStream(filePath + filename + fileType));
				
				//write WorldMapChunk to the file
				oStream.writeObject(chunk);
				System.out.println("WROTE: " + filename);
				
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File Not Found");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.out.println("CRAZY");
			System.out.println("\n" + e.getMessage());
		}
		finally
		{
			try
			{
				oStream.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static String getFilePath()
	{
		return filePath;
	}
	
	public static String getFileType()
	{
		return fileType;
	}

	
	
}
