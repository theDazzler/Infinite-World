package com.devon.infiniteworld;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class City extends JPanel
{

	public static final int MAX_WIDTH = 512 * 64; //width of city in pixels
	public static final int MAX_HEIGHT = 512 * 64;//height of city in pixels
	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT= 64;


	public static final int FLOOR = 0; //used for cellular automata
	public static final int WALL = 1;  //used for cellular automata
	public static final int WATER = 2;
	public static final int GRASS = 3;
	public static final int BUILDING = 4;

	public int[][] map;
	public int numCols;
	public int numRows;
	private int buildingDensity; //0-100

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

	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				double tileValue = this.map[i][j];

				if(tileValue == FLOOR)
				{
					g.setColor(Color.GRAY);

				}
				else if(tileValue == WALL)
				{
					g.setColor(Color.BLACK);

				}
				else if(tileValue == WATER)
				{
					g.setColor(Color.BLUE);

				}
				else if(tileValue == GRASS)
				{
					g.setColor(Color.GREEN);

				}
				else if(tileValue == BUILDING)
				{
					g.setColor(new Color(109, 69, 19));

				}

				g.fillRect(i * TILE_WIDTH, j * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
			}
		}
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
				double tileValue = this.map[i][j];

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
				double tileValue = this.map[i][j];

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

	private boolean isTouching(int xIndex, int yIndex, double water2) 
	{
		if(checkNorth(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkNorthEast(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkEast(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkSouthEast(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkSouth(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkSouthWest(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkWest(xIndex, yIndex, 1, water2) > 0)
			return true;
		if(checkNorthWest(xIndex, yIndex, 1, water2) > 0)
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
				double tileValue = this.map[i][j];

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

	private void floodFill(int i, int j, double wall2, int water2)
	{
		if(i < this.numRows && i >=0 && j < this.numCols && j >= 0)
		{
			if(this.map[i][j] != wall2)
				return;
			this.map[i][j] = water2;
			floodFill(i, j - 1, wall2, water2);
			floodFill(i, j + 1, wall2, water2);
			floodFill(i - 1, j, wall2, water2);
			floodFill(i + 1, j, wall2, water2);
			return;
		}


	}

	private boolean isSurroundedBy(int xIndex, int yIndex, int checkDistance, double wall2) 
	{

		int x = xIndex;
		int y = yIndex;

		//check northeast
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[--x][++y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check southeast
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[++x][++y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check southwest
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[++x][--y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check northwest
		for(int i = 0; i < checkDistance / 2; i++)
		{
			if(this.map[--x][--y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check north
		for(int i = 0; i < checkDistance; i++)
		{

			if(this.map[++x][y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check east
		for(int i = 0; i < checkDistance; i++)
		{
			if(this.map[x][++y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check south
		for(int i = 0; i < checkDistance; i++)
		{
			if(this.map[--x][y] != wall2)
				return false;
		}

		x = xIndex;
		y = yIndex;
		//check west
		for(int i = 0; i < checkDistance; i++)
		{
			if(this.map[x][--y] != wall2)
				return false;
		}

		return true;
	}

	private int[][] populateNextGrid(double oneR1, double oneR2) 
	{
		int[][] tempGrid = new int[this.numRows][this.numCols];

		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				int numCells = checkSurroundings(i, j, 1, WALL);

				if(numCells >= oneR1)
				{
					tempGrid[i][j] = WALL;
				}

				int numCellsTwo = checkSurroundings(i, j, 2, WALL);

				if(numCellsTwo <= oneR2)
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
		double oneR1 = 6;
		double oneR2 = 1;
		double twoR1 = 6;
		double twoR2 = 1;

		for(int i = 0; i < 4; i++)
		{
			//6 3
			nextGrid = populateNextGrid(oneR1, oneR2);
			this.map = nextGrid;
		}

		for(int i = 0; i < 8; i++)
		{
			// 6 -1
			nextGrid = populateNextGrid(twoR1, twoR2);
			this.map = nextGrid;
		}
	}


	//counts number of surrounding walls in neighboring cells
	private int checkSurroundings(int xIndex, int yIndex, int amount, double wall2) 
	{
		int count = 0;
		count += checkNorth(xIndex, yIndex, amount, wall2);
		count += checkNorthEast(xIndex, yIndex, amount, wall2);
		count += checkEast(xIndex, yIndex, amount, wall2);
		count += checkSouthEast(xIndex, yIndex, amount, wall2);
		count += checkSouth(xIndex, yIndex, amount, wall2);
		count += checkSouthWest(xIndex, yIndex, amount, wall2);
		count += checkWest(xIndex, yIndex, amount, wall2);
		count += checkNorthWest(xIndex, yIndex, amount, wall2);
		count += this.map[xIndex][yIndex];

		return count;
	}

	//amount = how many blocks away to check(ex. 2 = look at blocks 2 away, it wont look at 1 away)
	private int checkNorthWest(int xIndex, int yIndex, int amount, double wall2)
	{
		if(xIndex <= amount - 1 || yIndex <= amount - 1)
			return 0;
		if(this.map[xIndex - amount][yIndex - amount] == wall2)
			return 1;
		return 0;
	}

	private int checkWest(int xIndex, int yIndex, int amount, double wall2) 
	{
		if(yIndex <= amount - 1)
			return 0;
		if(this.map[xIndex][yIndex - amount] == wall2)
			return 1;
		return 0;
	}

	private int checkSouthWest(int xIndex, int yIndex, int amount, double wall2) 
	{
		if(xIndex >= this.numRows - amount || yIndex <= amount - 1)
			return 0;
		if(this.map[xIndex + amount][yIndex - amount] == wall2)
			return 1;
		return 0;
	}

	private int checkSouth(int xIndex, int yIndex, int amount, double wall2) 
	{
		if(xIndex >= this.numRows - amount)
			return 0;
		if(this.map[xIndex + amount][yIndex] == wall2)
			return 1;
		return 0;
	}

	private int checkSouthEast(int xIndex, int yIndex, int amount, double wall2) 
	{
		if(xIndex >= this.numRows - amount || yIndex >= this.numCols - amount)
			return 0;
		if(this.map[xIndex + amount][yIndex + amount] == wall2)
			return 1;
		return 0;
	}

	private int checkEast(int xIndex, int yIndex, int amount, double wall2) 
	{
		if(yIndex >= this.numCols - amount)
			return 0;
		if(this.map[xIndex][yIndex + amount] == wall2)
			return 1;
		return 0;
	}

	private int checkNorthEast(int xIndex, int yIndex, int amount, double wall2)
	{
		if(xIndex <= 1 || yIndex >= this.numCols - amount)
			return 0;
		if(this.map[xIndex - amount][yIndex + amount] == wall2)
			return 1;
		return 0;
	}

	private int checkNorth(int xIndex, int yIndex, int amount, double water2) 
	{
		if(xIndex <= 1)
			return 0;
		if(this.map[xIndex - amount][yIndex] == water2)
			return 1;
		return 0;
	}


}