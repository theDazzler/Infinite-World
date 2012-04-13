package com.devon.infiniteworld;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class City
{

	public static final int MAX_WIDTH = 480;
	public static final int MAX_HEIGHT = 384;
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT= 16;

	
	public static final int FLOOR = 0; //used for cellular automata
	public static final int WALL = 1;  //used for cellular automata
	public static final int WATER = 2;
	public static final int GRASS = 3;
	public static final int BUILDING = 4;
	
	public int[][] map;
	public int numCols;
	public int numRows;
	public int buildingDensity; //0-100
	
	private Random rand = new Random();
	
	private static final long serialVersionUID = 8111554517572474109L;
	
	public City(int buildingDensity)
	{
		this.numCols = MAX_WIDTH / TILE_WIDTH;
		this.numRows = MAX_HEIGHT / TILE_HEIGHT;
		this.map = new int[this.numRows][this.numCols];
		this.buildingDensity = buildingDensity;
		this.createCave();
		this.generateWalls();
		this.addWater();
		this.addCoast();
		this.addBuildings();
		
	}
	
	public void createCave()
	{
		//Random rand = new Random();
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				//40% floor fill rate to begin with
				if(rand.nextInt(10) < 4)
				{
					this.map[i][j] = WALL;
				}
				else
				{
					this.map[i][j] = FLOOR;
				}
			}
		}	
	}
	
	private void addBuildings()
	{
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{	
				int tileValue = this.map[i][j];

				if(tileValue == WALL)
				{
					if(rand.nextInt(100) < this.buildingDensity)
						this.map[i][j] = BUILDING;
							
				}
			}
		}
	}
	
	private void addCoast()
	{
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{	
				int tileValue = this.map[i][j];

				if(tileValue == FLOOR)
				{
					if(isTouching(i, j, WATER))
					{
						this.map[i][j] = GRASS;
					}
				}
			}
		}
	}
	
	private boolean isTouching(int xIndex, int yIndex, int tileType) 
	{
		if(checkNorth(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkNorthEast(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkEast(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkSouthEast(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkSouth(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkSouthWest(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkWest(xIndex, yIndex, 1, tileType) > 0)
			return true;
		if(checkNorthWest(xIndex, yIndex, 1, tileType) > 0)
			return true;
		return false;
	}

	private void addWater()
	{
		int checkDistance = 4; //how far to check for all squares being water
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{	
				int tileValue = this.map[i][j];
				
				//dont check edge tiles
				if(i < checkDistance || i > (this.numRows - 1) - checkDistance || j < checkDistance || j > (this.numCols - 1) - checkDistance)
				{
					continue;
				}
				
				if(tileValue == WALL)
				{
					if(isSurroundedBy(i, j, checkDistance, WALL))
					{
						System.out.println("i: " + i + " j: " + j);
						floodFill(i, j, WALL, WATER);
					}
				}
			}
		}
	}

	private void floodFill(int i, int j, int targetColor, int replacementColor)
	{
		if(i < this.numRows && i >=0 && j < this.numCols && j >= 0)
		{
			if(this.map[i][j] != targetColor)
				return;
			this.map[i][j] = replacementColor;
			floodFill(i, j - 1, targetColor, replacementColor);
			floodFill(i, j + 1, targetColor, replacementColor);
			floodFill(i - 1, j, targetColor, replacementColor);
			floodFill(i + 1, j, targetColor, replacementColor);
			return;
		}
		
		
	}

	private boolean isSurroundedBy(int xIndex, int yIndex, int checkDistance, int tileType) 
	{
		
		int x = xIndex;
		int y = yIndex;

		//check northeast
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[--x][++y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check southeast
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[++x][++y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check southwest
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[++x][--y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check northwest
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[--x][--y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check north
		for(int i = 0; i < checkDistance; i++)
		{
			
			if(this.map[++x][y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check east
		for(int i = 0; i < checkDistance; i++)
		{
			if(this.map[x][++y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check south
		for(int i = 0; i < checkDistance; i++)
		{
			if(this.map[--x][y] != tileType)
				return false;
		}
		
		x = xIndex;
		y = yIndex;
		//check west
		for(int i = 0; i < checkDistance; i++)
		{
			if(this.map[x][--y] != tileType)
				return false;
		}
		
		return true;
	}

	private int[][] populateNextGrid(int r1Cutoff, int r2Cutoff) 
	{
		int[][] tempGrid = new int[this.numRows][this.numCols];
		
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				int numCells = checkSurroundings(i, j, 1, WALL);
				
				if(numCells >= r1Cutoff)
				{
					tempGrid[i][j] = WALL;
				}
				
				int numCellsTwo = checkSurroundings(i, j, 2, WALL);
				
				if(numCellsTwo <= r2Cutoff)
				{
					tempGrid[i][j] = WALL;
				}	
			}
		}
		
		return tempGrid;
	}

	private void generateWalls() 
	{
		int[][] nextGrid;
		int oneR1 = 7;
		int oneR2 = 0;
		int twoR1 = 6;
		int twoR2 = 1;
		
		for(int i = 0; i < 4; i++)
		{
			//6 3
			nextGrid = populateNextGrid(oneR1, oneR2);
			this.map = nextGrid;
		}
		
		for(int i = 0; i < 5; i++)
		{
			// 6 -1
			nextGrid = populateNextGrid(twoR1, twoR2);
			this.map = nextGrid;
		}
	}
		

	//counts number of surrounding walls in neighboring cells
	private int checkSurroundings(int xIndex, int yIndex, int amount, int tileType) 
	{
		int count = 0;
		count += checkNorth(xIndex, yIndex, amount, tileType);
		count += checkNorthEast(xIndex, yIndex, amount, tileType);
		count += checkEast(xIndex, yIndex, amount, tileType);
		count += checkSouthEast(xIndex, yIndex, amount, tileType);
		count += checkSouth(xIndex, yIndex, amount, tileType);
		count += checkSouthWest(xIndex, yIndex, amount, tileType);
		count += checkWest(xIndex, yIndex, amount, tileType);
		count += checkNorthWest(xIndex, yIndex, amount, tileType);
		count += this.map[xIndex][yIndex];
		
		return count;
	}

	//amount = how many blocks away to check(ex. 2 = look at blocks 2 away, it wont look at 1 away)
	private int checkNorthWest(int xIndex, int yIndex, int amount, int tileType)
	{
		if(xIndex <= amount - 1 || yIndex <= amount - 1)
			return 0;
		if(this.map[xIndex - amount][yIndex - amount] == tileType)
			return 1;
		return 0;
	}

	private int checkWest(int xIndex, int yIndex, int amount, int tileType) 
	{
		if(yIndex <= amount - 1)
			return 0;
		if(this.map[xIndex][yIndex - amount] == tileType)
			return 1;
		return 0;
	}

	private int checkSouthWest(int xIndex, int yIndex, int amount, int tileType) 
	{
		if(xIndex >= this.numRows - amount || yIndex <= amount - 1)
			return 0;
		if(this.map[xIndex + amount][yIndex - amount] == tileType)
			return 1;
		return 0;
	}

	private int checkSouth(int xIndex, int yIndex, int amount, int tileType) 
	{
		if(xIndex >= this.numRows - amount)
			return 0;
		if(this.map[xIndex + amount][yIndex] == tileType)
			return 1;
		return 0;
	}

	private int checkSouthEast(int xIndex, int yIndex, int amount, int tileType) 
	{
		if(xIndex >= this.numRows - amount || yIndex >= this.numCols - amount)
			return 0;
		if(this.map[xIndex + amount][yIndex + amount] == tileType)
			return 1;
		return 0;
	}

	private int checkEast(int xIndex, int yIndex, int amount, int tileType) 
	{
		if(yIndex >= this.numCols - amount)
			return 0;
		if(this.map[xIndex][yIndex + amount] == tileType)
			return 1;
		return 0;
	}

	private int checkNorthEast(int xIndex, int yIndex, int amount, int tileType)
	{
		if(xIndex <= 1 || yIndex >= this.numCols - amount)
			return 0;
		if(this.map[xIndex - amount][yIndex + amount] == tileType)
			return 1;
		return 0;
	}

	private int checkNorth(int xIndex, int yIndex, int amount, int tileType) 
	{
		if(xIndex <= 1)
			return 0;
		if(this.map[xIndex - amount][yIndex] == tileType)
			return 1;
		return 0;
	}

	
}
