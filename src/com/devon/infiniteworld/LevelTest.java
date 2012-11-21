package com.devon.infiniteworld;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class LevelTest 
{
	double[][] heightMap;
	double[][] temperatureMap;
	int width;
	int height;
	static Random rand = new Random();
	
	public LevelTest(int width, int height)
	{
		this.heightMap = MyNoiseMap.getMap(width, height, 0, 0);
		this.temperatureMap = TempMap.getMap(width, height);
		this.width = width;
		this.height = height;
	}
	
	public static void main(String[] args)
	{
		
		
		for(int j = 0; j < 10; j++)
		{
			LevelTest level = new LevelTest(512, 512);
			
			int w = level.width;
			int h = level.height;
			
			//NoiseMap noiseMap = new NoiseMap(w, h, w/4);
			
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			int[] pixels = new int[w * h];
			for(int y = 0; y < h; y++)
			{
				for(int x = 0; x < w; x++)
				{
					int i = x + y * w;
					
					/*
					if(testM[y][x] == 1)pixels[i] = 0x000080;//water
					else if(testM[y][x] == 2)pixels[i] = 0xe5c08c;//mountain
					//else if(testM[y][x] == 3)pixels[i] = 0xa77939;//semi-mountain
					else if(testM[y][x] == 3)pixels[i] = 0x404040; //dirt
					else 
						pixels[i] = 0x208020; //grass
						/*/

					//if water
					if(level.heightMap[y][x] < 0)
					{
						
						pixels[i] = 0x000080;//water
					}
					
					//if beach sand
					else if(level.heightMap[y][x] < 0.4)
					{

						pixels[i] = 0xFFEEAB; //beach sand
					}
						
					
					//if grass
					else if(level.heightMap[y][x] < 6.5)
					{
						if(level.temperatureMap[y][x] > 0.4 && level.temperatureMap[y][x] < 1.2)
						{
							if(rand.nextInt(3) == 0)
								pixels[i] = 0x402200; //tree
							else
								pixels[i] = 0x067A00; //grass	
						}
						//hottest
						else if(level.temperatureMap[y][x] > 1.2)
						{
							if(rand.nextInt(8) == 0)
								pixels[i] = 0x04820E; //cactus
							else
								pixels[i] = 0xFFE8A3; //desert sand
						}
						else
							pixels[i] = 0x067A00; //grass
					}
						
					//if mountain
					else if(level.heightMap[y][x] > 6.5)
					{
	
						pixels[i] = 0xFFF563;//mountain
					}
					//else if(testM[y][x] == 3)pixels[i] = 0xa77939;//semi-mountain
					//else if(testM[y][x] < 3)pixels[i] = 0x404040; //dirt
						

				}
			}
			img.setRGB(0, 0, w, h, pixels, 0, w);
			JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img));
		}
	}
	
}
