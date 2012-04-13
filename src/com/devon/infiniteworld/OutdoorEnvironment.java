package com.devon.infiniteworld;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.newdawn.slick.GameContainer;

import com.devon.infiniteworld.test.NoiseMap;

public class OutdoorEnvironment extends Environment
{	
	public static HashMap<String, NoiseMap> noiseMaps = new HashMap<String, NoiseMap>();//holds noise data for the outdoor world. key is the upper left position. all worldMapChunks in its area will use the noise map for its terrain data
	
	public void addNoiseMap(float xPos, float yPos)
	{
		NoiseMap noiseMap = new NoiseMap(64, 64, 64 / 4);
		noiseMap.createMap(64, 64);
		noiseMap.setOrigin(xPos, yPos);
		
		String key = "x" + Integer.toString((int)xPos) + "y" + Integer.toString((int)yPos);
		
		noiseMaps.put(key, noiseMap);
	}
	
	public void addNoiseMap(float xPos, float yPos, NoiseMap map)
	{
		map.setOrigin(xPos, yPos);
		String key = "x" + Integer.toString((int)xPos) + "y" + Integer.toString((int)yPos);
		
		noiseMaps.put(key, map);
		
		
	}
	
	public void draw() 
	{
		drawGameScreenChunks();
		//drawMiniMap();		
	}

	@Override
	void update(GameContainer container, int delta)
	{
		//update entities
		for(int i = 0; i < this.entities.size(); i++)
		{
			this.entities.get(i).update(container, delta);
		}
	}
	
	//render GameScreenChunks
	private void drawGameScreenChunks() 
	{
		//render 3x3 GameScreenChunks
		for (GameScreenChunk chunk : this.visibleChunks.values()) 
		{
			chunk.draw(chunk.getX(), chunk.getY());
		}
	}

}
