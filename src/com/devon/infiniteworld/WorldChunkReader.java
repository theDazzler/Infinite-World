package com.devon.infiniteworld;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class WorldChunkReader
{
	private static String filePath = "world_chunks/";
	private static String fileType = ".dat";
	
	//filename is the key of the WorldChunk used to look it up in the WorldMap Hashmap
	public static WorldMapChunk readWorldChunk(String filename)
	{
		ObjectInputStream iStream = null;
		WorldMapChunk chunk = null;
		
		try 
		{
			iStream = new ObjectInputStream(new FileInputStream(filePath + filename + fileType));
			
			//read the WorldMapChunk object from the file
			chunk = (WorldMapChunk)iStream.readObject();
			//System.out.println("INDI" + chunk.getKey());
			
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				iStream.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return chunk;	
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
