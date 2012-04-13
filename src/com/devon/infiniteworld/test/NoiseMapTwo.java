package com.devon.infiniteworld.test;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.devon.infiniteworld.tiles.Tile;

public class NoiseMapTwo
{
	private static final Random random = new Random();
	public double[] values;
	private int w, h;
	
	public NoiseMapTwo(int w, int h, int featureSize)
	{
		this.w = w;
		this.h = h;
		
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
	
	private double sample(int x, int y)
	{
		return this.values[(x & (w - 1)) + (y & (h - 1)) * w];
	}
	
	private void setSample(int x, int y, double value)
	{
		this.values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
	}
	
	public static int[] getMap(int w, int h)
	{
		NoiseMapTwo noise1 = new NoiseMapTwo(w, h, w / 4);
		NoiseMapTwo noise2 = new NoiseMapTwo(w, h, w / 4);
		NoiseMapTwo noise3 = new NoiseMapTwo(w, h, w / 4);
		
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int[] map = new int[w * h];
		for(int y = 0; y < h; y++)
		{
			for(int x = 0; x < w; x++)
			{
				int i = x + y * w;
				
				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 4 - 1.6;
				//val = Math.abs(val - noise3.values[i]) * 3 - 2;
				
				double xd = x / (w - 1.0) * 2 - 1;
				double yd = y / (h - 1.0) * 2 - 1;
				if(xd < 0) xd = -xd;
				if(yd < 0) yd = -yd;
				double dist = xd >= yd ? xd : yd;
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;

				val = val + 1 - dist * 20;
				
				int br = val < 0 ? 0 : 255;
				if(val < 0)
				{
					map[i] = 0;//water
				}
				else if(val > 3)
					map[i] = 1; //mountain
				else if(val > 2.5)
					map[i] = 2; //semi-mountain
				else if(val < .3)
				{
					map[i] = 3;//dirt
				}
				else
				{
					map[i] = 4;//grass
				}
			}
		}
		return map;
	}
	
	public static void main(String[] args)
	{
		for(int j = 0; j < 10; j++)
		{
			
			int w = 512;
			int h = 512;
			
			int[] map = NoiseMapTwo.getMap(w, h);
			
			
			
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			int[] pixels = new int[w * h];
			for(int y = 0; y < h; y++)
			{
				for(int x = 0; x < w; x++)
				{
					int i = x + y * w;
					
					if(map[i] == 0)pixels[i] = 0x000080;//water
					if(map[i] == 1)pixels[i] = 0xe5c08c;//mountain
					if(map[i] == 2)pixels[i] = 0xa77939;//semi-mountain
					if(map[i] == 3)pixels[i] = 0x404040; //dirt
					if(map[i] == 4)pixels[i] = 0x208020; //grass

				}
			}
			img.setRGB(0, 0, w, h, pixels, 0, w);
			JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon(img));
		}
	}
	
}
