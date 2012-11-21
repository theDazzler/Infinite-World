package com.devon.infiniteworld;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.newdawn.slick.geom.Vector2f;

public class TempMap
{
	private static final Random random = new Random();
	public double[] values;
	public int w, h;
	public double[][] noiseData;
	public Vector2f origin;
	
	public TempMap(int w, int h, int featureSize)
	{
		this.w = w;
		this.h = h;
		this.noiseData = new double[w][h];
		
		this.values = new double[w * h];
		
		for(int y = 0; y < w; y += featureSize)
		{
			for(int x = 0; x < w; x += featureSize)
			{
				setSample(x, y, random.nextFloat() * 2 - 1);
			}
		}
		
		
		int stepSize = featureSize;
		double scale = 1.0 / w;
		double scaleMod = 1;
		do
		{
			int halfStep = stepSize / 2;
			
			for(int y = 0; y < w; y += stepSize)
			{
				for(int x = 0; x < w; x += stepSize)
				{
					double a = sample(x, y);
					double b = sample(x + stepSize, y);
					double c = sample(x, y + stepSize);
					double d = sample(x + stepSize, y + stepSize);
					
					double e = (a + b + c + d) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale;
					setSample(x + halfStep, y + halfStep, e);
				}
			}
			for(int y = 0; y < w; y += stepSize)
			{
				for(int x = 0; x < w; x += stepSize)
				{
					double a = sample(x, y);
					double b = sample(x + stepSize, y);
					double c = sample(x, y + stepSize);
					double d = sample(x + halfStep, y + halfStep);
					double e = sample(x + halfStep, y - halfStep);
					double f = sample(x - halfStep, y + halfStep);
					
					double H = (a + b + d + e) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
					double g = (a + c + d + f) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
					setSample(x + halfStep, y, H);
					setSample(x, y + halfStep, g);
				}
			}
			stepSize /= 2;
			scale *= (scaleMod + 1);
			scaleMod *= 0.3;
		}
		while(stepSize > 1);
	}
	
	public void setOrigin(float xPos, float yPos)
	{
		this.origin = new Vector2f(xPos, yPos);
	}
	
	
	private double sample(int x, int y)
	{
		return this.values[(x & (w - 1)) + (y & (h - 1)) * w];
	}
	
	private void setSample(int x, int y, double value)
	{
		this.values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
	}
	
	public static double[][] getMap(int w, int h)
	{		
		double [][] map = new double[w][h];
		MyNoiseMap noise1 = new MyNoiseMap(w, h, w / 4, 0, 0);
		MyNoiseMap noise2 = new MyNoiseMap(w, h, w / 8, 0, 0);
		MyNoiseMap noise3 = new MyNoiseMap(w, h, w / 8, 0, 0);
		
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				int i = x + y * w;
				
				//double val = Math.abs(noise1.values[i] - noise2.values[i]) * 4 - 1.4;
				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 2 - 1.6;
				//val = Math.abs(val - noise3.values[i]) * 3 - 2;
				
				double xd = x / (w - 1.0) * 2 - 1;
				double yd = y / (h - 1.0) * 2 - 1;
				if(xd < 0) xd = -xd;
				if(yd < 0) yd = -yd;
				double dist = xd >= yd ? xd : yd;
				
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				val = val + 1 - dist * 20;
				
				
				/*
				int br = val < 0 ? 0 : 255;
				if(val < 0) map[y][x] = Tile.water.id; //water
				else if(val < 0.4) map[y][x] = Tile.mountain.id; //beach sand
				else if(val < 6.5) map[y][x] = Tile.grass.id; //grass
				else if(val > 6.5) map[y][x] = Tile.mountain.id; //mountain
				else
					map[y][x] = Tile.grass.id; //grass
					//*/
				map[y][x] = val;
					
				//System.out.println("index: " + i + " NOISE VAL : " + val);
			}
		}
		
		return map;
	}
	
	public static void main(String[] args)
	{
		for(int j = 0; j < 5; j++)
		{
			
			int w = 512;
			int h = 512;
			
			//NoiseMap noiseMap = new NoiseMap(w, h, w/4);
			double[][] testM = TempMap.getMap(w, h);
			
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			double max = 0;
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
					
					//System.out.println(testM[y][x]);
					if(testM[y][x] > max)
						max = testM[y][x];
					//System.out.println(max);
					
					
					
					if(testM[y][x] < -0.3)pixels[i] = 0x2B05FF;//freezing
					//else if(testM[y][x] < 0.2)pixels[i] = 0x05E6FF;//cold
					//else if(testM[y][x] < 0.6)pixels[i] = 0x00AD00;//mild
					else if(testM[y][x] < 0.4)pixels[i] = 0x66FF00;//average
					else if(testM[y][x] < 1.2)pixels[i] = 0xFFFF00;//hot
					else if(testM[y][x] > 1.2)pixels[i] = 0xFF0000;//hottest
					//else if(testM[y][x] == 3)pixels[i] = 0xa77939;//semi-mountain
					//else if(testM[y][x] < 3)pixels[i] = 0x404040; //dirt
						

				}
			}
			img.setRGB(0, 0, w, h, pixels, 0, w);
			JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img));
		}
	}
	
}
