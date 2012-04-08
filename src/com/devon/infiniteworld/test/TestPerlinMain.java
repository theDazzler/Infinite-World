package com.devon.infiniteworld.test;

import com.devon.infiniteworld.ImprovedNoise;

public class TestPerlinMain
{
	public static void main(String[] args) 
	{
		int seed = 532434;
		
		//create first noise array
		double x = 0.0;    //x-coordinate
		double y = -768.0; //y-coordinate
		float z = 10.0f;
		int octaves = 4;
		double lacunarity = 7.0;
		double h = 2.0;
		
		double[][] test = new double[15][15];
		
		System.out.println("Noise Array 1: ");
		
		for(int i = 0; i < test.length; i++)
		{
			for(int j = 0; j < test[i].length; j++)
			{
				test[i][j] = ImprovedNoise.noise(x + (x * .472), y + (y * .472), .77);
				x += .314f;//************
				
				System.out.print(test[i][j] + " ");
			}
			y += .314f;//***********
			
		}
		System.out.println();
		
		//create 2nd noise array
		double x2 = -1024.0; //x coordinate
		double y2 = -768.0;  //y coordinate
		float z2 = 10.0f;    
		int octaves2 = 4;
		double lacunarity2 = 7.0;
		double h2 = 2.0;
		
		System.out.println();
		
		double[][] test2 = new double[15][15];
		
		System.out.println("Noise Array 2: ");
		
		for(int i = 0; i < test2.length; i++)
		{
			for(int j = 0; j < test2[i].length; j++)
			{
				test2[i][j] = ImprovedNoise.noise(x2 + (x2 * .472 ), y2 + (y2 * .472), .77);
				x2 += .314f;//*************
				
				System.out.print(test2[i][j] + " ");
			}
			y2 += .314f;//************
			
		}
		System.out.println();
	}
}
