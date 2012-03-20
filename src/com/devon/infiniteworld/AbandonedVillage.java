package com.devon.infiniteworld;

import java.util.Random;
import java.util.Scanner;

public class AbandonedVillage
{
	public static final String ROAD_DISPLAY = "R";
	public static final String BUILDING_DISPLAY = "X";
	public static final String TREASURE_DISPLAY = "*";
	
	public static final String[][] VERTICAL_ROAD = {{"1", "V", "3"}};
	public static final String[][] HORIZONTAL_ROAD = {{"H"},
													  {"H"},
													  {"H"}};
	
	public static final int MAX_VILLAGE_WIDTH = 128; //num of tiles wide
	public static final int MIN_VILLAGE_WIDTH = 32;
	public static final int MAX_VILLAGE_HEIGHT = 96; //num of tiles wide
	public static final int MIN_VILLAGE_HEIGHT = 24;
	
	String[][] terrain;
	
	public AbandonedVillage()
	{
		initTerrain();
		
		Random rand = new Random();

		int startX = MAX_VILLAGE_HEIGHT / 2;
		int startY = MAX_VILLAGE_WIDTH/ 2;
		
		buildRoads(32, startX, startY);
		displayVillage();
	}

	private void displayVillage() 
	{
		for(int i = 0; i < this.terrain.length; i++)
		{
			for(int j = 0; j < this.terrain[i].length; j++)
			{
				System.out.print(this.terrain[i][j]);
			}
			System.out.println();
		}
		
	}

	private void buildRoads(int iterations, int startX, int startY)
	{
		
			if(iterations == 0)
				return;
			Random rand = new Random();
			
			System.out.println("STARTX: " + startX);
			System.out.println("STARTY: " + startY);
			
			//build vertical road
			if(rand.nextInt(2) == 0)
			{
				//build road going up
				if(rand.nextInt(2) == 0)
				{
					//startY -= 2;
					//startX += 2;
					
					for(int i = 0; i < rand.nextInt(MAX_VILLAGE_HEIGHT - 4) + 4; i++)
					{
						//System.out.println("X: " + startX);
						//System.out.println("Y: " + startY);
						placeStructure(VERTICAL_ROAD, startX, startY);
						startX--;
					}
				}
				//build road going down
				else
				{
					//startY += 1;
					//startX -= 1;
					
					for(int i = 0; i < rand.nextInt(MAX_VILLAGE_HEIGHT - 4) + 4; i++)
					{
						System.out.println("X: " + startX);
						System.out.println("Y: " + startY);
						placeStructure(VERTICAL_ROAD, startX, startY);
						startX++;
					}
				}
			}
				
			//build horizontal road
			else
			{
				if(rand.nextInt(2) == 0)
				{
					//build road going right
					for(int i = 0; i < rand.nextInt(MAX_VILLAGE_WIDTH - 4) + 4; i++)
					{
						placeStructure(HORIZONTAL_ROAD, startX, startY);
						startY++;
					}
				}
				else
				{
					//build road going left
					for(int i = 0; i < rand.nextInt(MAX_VILLAGE_WIDTH - 4) + 4; i++)
					{
						placeStructure(HORIZONTAL_ROAD, startX, startY);
						startY--;
					}
				}
			}
			
			
			buildRoads(iterations - 1, startX, startY);
			
			
	}

	private void placeStructure(String[][] structure, int startX, int startY) 
	{
		int tempX = startX;
		int tempY = startY;
		
		if(startX >= 0 && startX < MAX_VILLAGE_HEIGHT && startY >= 0 && startY < MAX_VILLAGE_WIDTH)
		{
			//if tile is empty
			if(this.terrain[startX][startY] == "-")
			{
				//if there is enough width space to place structure
				if(startY + getMaxStructureWidth(structure) < getMaxStructureWidth(this.terrain))
				{
					//if there is enough vertical space to place structure
					if(startX + structure.length < this.terrain.length)
					{
						boolean placeable = true;
						
						for(int i = 0; i < structure.length; i++)
						{
							for(int j = 0; j < structure[i].length; j++)
							{
								if(this.terrain[startX][startY] != "-")
								{
									placeable = false;
									break;	
								}
								startY++;
							}
							
							startX++;
							startY = tempY;
						}
						
						if(placeable)
						{
							startX = tempX;
							startY = tempY;
							//place structure
							for(int i = 0; i < structure.length; i++)
							{
								for(int j = 0; j < structure[i].length; j++)
								{
									this.terrain[startX][startY] = structure[i][j];
									startY++;
								}
								
								startX++;
								startY = tempY;
							}
						}
					}
				}
			}
		}
		
	}
	
	//returns width of structure
	private int getMaxStructureWidth(String[][] structure)
	{
		int max = 0;
		for(int i = 0; i < structure.length; i++)
		{
			if(structure[i].length > max)
				max = structure[i].length;
		}
		
		return max;
	}

	private void initTerrain()
	{
		this.terrain = new String[MAX_VILLAGE_HEIGHT][MAX_VILLAGE_WIDTH];
		
		for(int i = 0; i < this.terrain.length; i++)
		{
			for(int j = 0; j < this.terrain[i].length; j++)
			{
				this.terrain[i][j] = "-";
			}
		}
	}
	
	
}
